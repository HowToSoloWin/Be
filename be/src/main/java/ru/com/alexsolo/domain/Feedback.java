package ru.com.alexsolo.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(optional = false,cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rate")
    private byte rate;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "text")
    private String text;

    @OneToOne(optional = false, cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public Feedback() {
    }

    public Feedback(User user, byte rate, Timestamp date, String text, Ticket ticket) {
        this.user = user;
        this.rate = rate;
        this.date = date;
        this.text = text;
        this.ticket = ticket;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public byte getRate() {
        return rate;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
