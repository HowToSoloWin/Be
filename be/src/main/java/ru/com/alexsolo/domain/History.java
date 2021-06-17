package ru.com.alexsolo.domain;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(optional = false,cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "action")
    private String action;
    
    @ManyToOne(optional = false,cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description")
    private String description;

    public History(Ticket ticket, Timestamp date, String action, User user, String description) {
        this.ticket = ticket;
        this.date = date;
        this.action = action;
        this.user = user;
        this.description = description;
    }

    public History() {
    }

    public long getId() {
        return id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getAction() {
        return action;
    }

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }
}
