package org.example.bankmanagementsystem.services;

import java.util.Date;

public interface TokenBlacklistService {
    void addTokenToBlacklist(String token, Date expirationTime);
    boolean isTokenBlacklisted(String token);

}
