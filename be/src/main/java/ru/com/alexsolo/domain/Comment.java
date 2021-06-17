package ru.com.alexsolo.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "text",length = 500)
    private String text;

    @Column(name = "date")
    private Timestamp date;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public Comment(User user, String text, Timestamp date, Ticket ticket) {
        this.user = user;
        this.text = text;
        this.date = date;
        this.ticket = ticket;
    }

    public Comment() {
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public Timestamp getDate() {
        return date;
    }

    public Ticket getTicket() {
        return ticket;
    }

}
