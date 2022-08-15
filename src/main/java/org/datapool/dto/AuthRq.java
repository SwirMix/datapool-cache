package org.datapool.dto;

public class AuthRq {
    private String login;
    private String pass;

    public String getLogin() {
        return login;
    }

    public AuthRq setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPass() {
        return pass;
    }

    public AuthRq setPass(String pass) {
        this.pass = pass;
        return this;
    }
}
