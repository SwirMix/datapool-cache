package org.datapool.dto;

public class Token {
    private String token;
    private String login;
    private String email;

    public String getLogin() {
        return login;
    }

    public Token setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Token setEmail(String email) {
        this.email = email;
        return this;
    }

    public Token(){

    }

    public Token(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Token setToken(String token) {
        this.token = token;
        return this;
    }
}
