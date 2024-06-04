package com.ihrm.demo;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-04 10:27:53
 */
public class CreateJwtTest
{
    public static void main(String[] args) {
        final JwtBuilder jwtBuilder = Jwts
                .builder()
                .setId("88")
                .setSubject("小白")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "ihrm")
                .claim("company", "123456")
                .claim("companyName", "江苏传智播客教育股份有限公司");
        final String token = jwtBuilder.compact();
        System.out.println(token);
    }
}
