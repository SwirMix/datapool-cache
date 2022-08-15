package org.datapool.core.jwt.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.datapool.core.jwt.TokenObject;

import java.util.*;

public class JwtService{

    private String secret;

    public JwtService() {
    }
    public JwtService(String secret){
        this.secret = secret;
    }

    public TokenObject decryptToken(String token) {
        JWTVerifier verified = JWT.require(Algorithm.HMAC512(secret)).build();
        DecodedJWT jwt = verified.verify(token);

        String email = jwt.getClaim("email").asString();
        String login = jwt.getClaim("login").asString();
        long createDate = jwt.getClaim("createDate").asLong();
        long validDateEnd = jwt.getClaim("validDateEnd").asLong();
        long userId = jwt.getClaim("userId").asLong();

        TokenObject tokenObject = new TokenObject();
        tokenObject.setLogin(login);
        tokenObject.setEmail(email);
        tokenObject.setCreateDate(createDate);
        tokenObject.setValidDateEnd(validDateEnd);
        tokenObject.setUserId(userId);
        return tokenObject;
    }

    public Map<String, Object> decryptRemoteToken(String token) {
        JWTVerifier verified = JWT.require(Algorithm.HMAC512(secret)).build();
        DecodedJWT jwt = verified.verify(token);
        List projects = Arrays.asList(jwt.getClaim("projects").asArray(String.class));

        String email = jwt.getClaim("email").asString();
        String login = jwt.getClaim("login").asString();
        long createDate = jwt.getClaim("createDate").asLong();
        long validDateEnd = jwt.getClaim("validDateEnd").asLong();
        long userId = jwt.getClaim("userId").asLong();

        TokenObject tokenObject = new TokenObject();
        tokenObject.setLogin(login);
        tokenObject.setEmail(email);
        tokenObject.setCreateDate(createDate);
        tokenObject.setValidDateEnd(validDateEnd);
        tokenObject.setUserId(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("projects", projects);
        data.put("user", tokenObject);
        return data;
    }

    public String createToken(TokenObject data) {
        String signed = JWT.create()
                .withClaim("login", data.getLogin())
                .withClaim("email",data.getEmail())
                .withClaim("createDate", data.getCreateDate())
                .withClaim("validDateEnd", data.getValidDateEnd())
                .withClaim("type", "rest")
                .withClaim("userId", data.getUserId())
                .sign(Algorithm.HMAC512(secret));
        return signed;
    }

    public String remoteTokenBuilder(TokenObject data, String[] projects){
        String remoteToken = JWT.create()
                .withClaim("login", data.getLogin())
                .withClaim("email",data.getEmail())
                .withClaim("createDate", data.getCreateDate())
                .withClaim("validDateEnd", data.getValidDateEnd())
                .withArrayClaim("projects", projects)
                .withClaim("userId", data.getUserId())
                .withClaim("type", "datapool")
                .sign(Algorithm.HMAC512(secret));
        return remoteToken;
    }
}
