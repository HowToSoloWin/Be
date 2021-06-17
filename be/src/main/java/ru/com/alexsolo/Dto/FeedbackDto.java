package ru.com.alexsolo.Dto;

public class FeedbackDto {

    private String ticketName;
    private byte rate;
    private String text;
    private long ticketId;

    public FeedbackDto(byte rate, String text, long ticketId) {
        this.rate = rate;
        this.text = text;
        this.ticketId = ticketId;
    }

    public FeedbackDto(String ticketName, byte rate, String text, long ticketId) {
        this.ticketName = ticketName;
        this.rate = rate;
        this.text = text;
        this.ticketId = ticketId;
    }

    public FeedbackDto() {
    }

    public byte getRate() {
        return rate;
    }

    public String getText() {
        return text;
    }

    public long getTicketId() {
        return ticketId;
    }

    public String getTicketName() {
        return ticketName;
    }
}
