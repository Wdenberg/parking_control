package com.aurora.parking.interfaces.user;


import com.aurora.parking.role.domain.RoleCode;
import com.aurora.parking.security.application.CurrentOperator;
import com.aurora.parking.user.application.dto.*;
import com.aurora.parking.user.application.usecase.*;
import com.aurora.parking.user.domain.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final CreateUserUseCase createUseCase;
    private final UpdateUserUseCase updateUseCase;
    private final ChangeUserRoleUseCase changeRoleUseCase;
    private final SetUserActiveUseCase setActiveUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final GetUserUseCase getUseCase;
    private final ListUsersUseCase listUseCase;

    public UserController(CreateUserUseCase createUseCase,
                          UpdateUserUseCase updateUseCase,
                          ChangeUserRoleUseCase changeRoleUseCase,
                          SetUserActiveUseCase setActiveUseCase,
                          ChangePasswordUseCase changePasswordUseCase,
                          GetUserUseCase getUseCase,
                          ListUsersUseCase listUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.changeRoleUseCase = changeRoleUseCase;
        this.setActiveUseCase = setActiveUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.getUseCase = getUseCase;
        this.listUseCase = listUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        User created = createUseCase.execute(
                request.username(), request.password(), request.fullName(), request.email(), request.role());
        UserResponse body = UserResponse.from(created);
        return ResponseEntity.created(URI.create("/api/v1/users/" + body.id())).body(body);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<UserResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponse.from(getUseCase.execute(id)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<Page<UserResponse>> list(
            @RequestParam(required = false) RoleCode role,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        Page<UserResponse> page = listUseCase.execute(pageable, role, active).map(UserResponse::from);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id,
                                               @Valid @RequestBody UpdateUserRequest request) {
        User updated = updateUseCase.execute(id, request.fullName(), request.email());
        return ResponseEntity.ok(UserResponse.from(updated));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<UserResponse> changeRole(@PathVariable UUID id,
                                                   @Valid @RequestBody ChangeRoleRequest request) {
        User updated = changeRoleUseCase.execute(id, request.role());
        return ResponseEntity.ok(UserResponse.from(updated));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<UserResponse> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponse.from(setActiveUseCase.execute(id, false)));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<UserResponse> activate(@PathVariable UUID id) {
        return ResponseEntity.ok(UserResponse.from(setActiveUseCase.execute(id, true)));
    }

    /**
     * Troca de senha do PRÓPRIO usuário logado — não exige USER_WRITE,
     * exige apenas estar autenticado, e nunca aceita o id de outro
     * usuário (comparação com CurrentOperator, não confia em path arbitrário
     * para autoatendimento).
     */
    @PatchMapping("/me/password")
    public ResponseEntity<Void> changeOwnPassword(@AuthenticationPrincipal CurrentOperator operator,
                                                  @Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(operator.userId(), request.newPassword());
        return ResponseEntity.noContent().build();
    }

    /** Admin resetando a senha de outro usuário — exige USER_WRITE. */
    @PatchMapping("/{id}/password")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<Void> resetPassword(@PathVariable UUID id,
                                              @Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(id, request.newPassword());
        return ResponseEntity.noContent().build();
    }
}