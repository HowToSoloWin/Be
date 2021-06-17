package ru.com.alexsolo.Dto;

public class ApplicationUser {
    private String name;
    private String password;

    public ApplicationUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public ApplicationUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
