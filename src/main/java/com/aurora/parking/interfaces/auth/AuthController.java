package com.aurora.parking.interfaces.auth;


import com.aurora.parking.security.application.CurrentOperator;
import com.aurora.parking.security.application.auth.dto.CurrentOperatorResponse;
import com.aurora.parking.security.application.auth.dto.LoginRequest;
import com.aurora.parking.security.application.auth.dto.RefreshRequest;
import com.aurora.parking.security.application.auth.dto.TokenPairResponse;
import com.aurora.parking.security.application.auth.usecase.LoginUseCase;
import com.aurora.parking.security.application.auth.usecase.LogoutUseCase;
import com.aurora.parking.security.application.auth.usecase.RefreshTokenUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;

    public AuthController(LoginUseCase loginUseCase,
                          RefreshTokenUseCase refreshTokenUseCase,
                          LogoutUseCase logoutUseCase) {
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.logoutUseCase = logoutUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginUseCase.execute(request.username(), request.password()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPairResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(refreshTokenUseCase.execute(request.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest request) {
        logoutUseCase.execute(request.refreshToken());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentOperatorResponse> me(@AuthenticationPrincipal CurrentOperator operator) {
        return ResponseEntity.ok(new CurrentOperatorResponse(
                operator.userId(), operator.username(), operator.role(), operator.permissions()));
    }
}