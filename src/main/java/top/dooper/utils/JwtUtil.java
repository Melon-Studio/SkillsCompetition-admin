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

    public static String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            // Invalid signature
        } catch (MalformedJwtException e) {
            // Invalid token
        } catch (ExpiredJwtException e) {
            // Expired token
        } catch (UnsupportedJwtException e) {
            // Unsupported token
        } catch (IllegalArgumentException e) {
            // Token claims string is empty
        }
        return false;
    }
}
