package ru.com.alexsolo.Dto;

public class HistoryDto {
    private String data;
    private String username;
    private String action;
    private String description;

    public HistoryDto(String data, String username, String action, String description) {
        this.data = data;
        this.username = username;
        this.action = action;
        this.description = description;
    }

    public HistoryDto() {
    }

    public String getData() {
        return data;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }
}
