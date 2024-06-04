package com.ihrm.demo;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-04 10:34:43
 */
public class CreateJwtTest2
{
    public static void main(String[] args) {
        final long now = System.currentTimeMillis();
        final long exp = now + 1000 * 60;       // 60s后过期
        JwtBuilder builder = Jwts
                .builder()
                .setId("888")
                .setSubject("小白")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "itcast")
                .setExpiration(new Date(exp))
                .claim("roles", "admin") //自定义claims存储数据
                .claim("logo", "logo.png");
        System.out.println(builder.compact());
    }
}
