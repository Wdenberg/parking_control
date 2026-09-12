package com.aurora.parking.infrastructure.persistence.security;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String accessSecret;
    private String refreshSecret;
    private long accessExpiresMinutes = 15;
    private long refreshExpiresDays = 7;
    private String issuer = "parking-control";

    public String getAccessSecret() { return accessSecret; }
    public void setAccessSecret(String accessSecret) { this.accessSecret = accessSecret; }
    public String getRefreshSecret() { return refreshSecret; }
    public void setRefreshSecret(String refreshSecret) { this.refreshSecret = refreshSecret; }
    public long getAccessExpiresMinutes() { return accessExpiresMinutes; }
    public void setAccessExpiresMinutes(long accessExpiresMinutes) { this.accessExpiresMinutes = accessExpiresMinutes; }
    public long getRefreshExpiresDays() { return refreshExpiresDays; }
    public void setRefreshExpiresDays(long refreshExpiresDays) { this.refreshExpiresDays = refreshExpiresDays; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
}