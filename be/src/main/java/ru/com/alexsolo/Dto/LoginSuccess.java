package ru.com.alexsolo.Dto;

public class LoginSuccess {

    private boolean login;

    public LoginSuccess(boolean login) {
        this.login = login;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
