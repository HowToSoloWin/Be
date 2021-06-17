package ru.com.alexsolo.Dto;

public class CommentDto {

    private String name;
    private String comment;
    private int ticketId;
    private String data;

    public CommentDto() {
    }

    public CommentDto(String data,String name,String comment){
        this.data = data;
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getData() {
        return data;
    }
}
