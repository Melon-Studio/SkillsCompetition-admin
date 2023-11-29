package top.dooper.utils;

import io.jsonwebtoken.*;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "546EAA91531BD19CED89E23E7EAE4";
    private static final long EXPIRATION_TIME = 864_000_000; // 过期时间（10天）

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
