package ru.com.alexsolo.domain;

import javax.persistence.*;
import java.sql.Blob;

@Entity
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Lob
    @Column(name = "blob")
    private Blob blob;

    @ManyToOne(optional = false,cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(name = "name")
    private String name;

    public Attachment() {
    }

    public Attachment(Blob blob, Ticket ticket, String name) {
        this.blob = blob;
        this.ticket = ticket;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public Blob getBlob() {
        return blob;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getName() {
        return name;
    }
}
