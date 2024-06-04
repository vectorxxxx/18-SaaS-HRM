package com.ihrm.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-04 10:31:20
 */
public class ParseJwtTest2
{
    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE3MTc0Njg2OTgsImV4cCI6MTcxNzQ2ODc1Nywicm9sZXMiOiJhZG1pbiIsImxvZ28iOiJsb2dvLnBuZyJ9" +
                ".XnUAGsHblXNii1nw1MQKZtVZ2xGuwfKZ2uhGKdE2H-U";
        final Claims claims = Jwts
                .parser()
                .setSigningKey("itcast")
                .parseClaimsJws(token)
                .getBody();

        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());

        final String companyId = (String) claims.get("roles");
        final String companyName = (String) claims.get("logo");
        System.out.println(companyId + "---" + companyName);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("签发时间:" + sdf.format(claims.getIssuedAt()));
        System.out.println("过期时间:" + sdf.format(claims.getExpiration()));
        System.out.println("当前时间:" + sdf.format(new Date()));
    }
}
