package com.retrobased.market.support.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private static final String SECRET = "425a4053752e244c216a4447752f2b67595623642b26794451273d2730676f48293b54682a7e4d2b572a2e32654f5c49663948635b7b524b456b2226215b61";

    public String extractUserID(String token) {
        return null;
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getBody();
    }

    private SecretKey getSignInKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
