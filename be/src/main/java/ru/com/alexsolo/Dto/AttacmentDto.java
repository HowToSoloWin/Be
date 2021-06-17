package ru.com.alexsolo.Dto;

public class AttacmentDto {

    private long ticketId;
    private String name;

    public AttacmentDto() {
    }

    public AttacmentDto(long ticketId, String name) {
        this.ticketId = ticketId;
        this.name = name;
    }

    public long getTicketID() {
        return ticketId;
    }

    public String getName() {
        return name;
    }
}
