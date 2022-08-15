package org.datapool.core.jwt;

public class TokenObject {
    /** используется почта*/
    private String email;
    private String login;
    private long createDate;
    private long validDateEnd;
    private long userId;

    public long getUserId() {
        return userId;
    }

    public TokenObject setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public TokenObject setLogin(String login) {
        this.login = login;
        return this;
    }

    public TokenObject(String email, long lifeTime){
        this.createDate = System.currentTimeMillis();
        this.validDateEnd = createDate + lifeTime;
        this.email = email;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getValidDateEnd() {
        return validDateEnd;
    }

    public void setValidDateEnd(long validDateEnd) {
        this.validDateEnd = validDateEnd;
    }

    public TokenObject(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
