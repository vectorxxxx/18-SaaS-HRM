package com.ihrm.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-04 10:31:20
 */
public class ParseJwtTest
{
    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4OCIsInN1YiI6IuWwj" +
                "-eZvSIsImlhdCI6MTcxNzQ2ODE3NiwiY29tcGFueSI6IjEyMzQ1NiIsImNvbXBhbnlOYW1lIjoi5rGf6IuP5Lyg5pm65pKt5a6i5pWZ6IKy6IKh5Lu95pyJ6ZmQ5YWs5Y-4In0" +
                "._g8FAr7Jb71ZaHAjcS7jYHssmcaR-uSkx4C89OPFw6w";
        final Claims claims = Jwts
                .parser()
                .setSigningKey("ihrm")
                .parseClaimsJws(token)
                .getBody();

        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());

        final String companyId = (String) claims.get("companyId");
        final String companyName = (String) claims.get("companyName");
        System.out.println(companyId + "---" + companyName);
    }
}
