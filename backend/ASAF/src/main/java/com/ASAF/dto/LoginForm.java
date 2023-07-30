// 사용자가 로그인 시 제공하는 사용자 이름과 비밀번호를 저장할 수 있는 DTO(Data Transfer Object)입니다.
package com.ASAF.dto;

public class LoginForm {

    private String username;
    private String password;

    public LoginForm() {
    }

    public LoginForm(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
