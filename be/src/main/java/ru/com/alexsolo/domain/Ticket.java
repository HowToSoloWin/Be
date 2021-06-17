package ru.com.alexsolo.domain;

import ru.com.alexsolo.Enum.State;
import ru.com.alexsolo.Enum.Urgency;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 500)
    private  String description;

    @Column(name = "created_on")
    private Timestamp createdOn;

    @Column(name = "desired_resolution_date")
    private Timestamp resolutionDate;

    @ManyToOne( cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "assignee_id" )
    private User assignee;

    @ManyToOne(optional = false, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state_id")
    private State state;

    @ManyToOne(optional = false, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "urgency_id")
    private Urgency urgency;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "approver_id")
    private User approved;

    public Ticket() {
    }

    public Ticket(String name, String description, Timestamp createdOn, Timestamp resolutionDate, User assignee, User owner, State state, Category category, Urgency urgency, User approved) {
        this.name = name;
        this.description = description;
        this.createdOn = createdOn;
        this.resolutionDate = resolutionDate;
        this.assignee = assignee;
        this.owner = owner;
        this.state = state;
        this.category = category;
        this.urgency = urgency;
        this.approved = approved;
    }

    public Ticket(String name, String description, Timestamp createdOn, Timestamp resolutionDate, User owner, State state, Category category, Urgency urgency) {
        this.name = name;
        this.description = description;
        this.createdOn = createdOn;
        this.resolutionDate = resolutionDate;
        this.owner = owner;
        this.state = state;
        this.category = category;
        this.urgency = urgency;
    }

    public Ticket(String name, String description, Timestamp createdOn, Timestamp resolutionDate, User owner, State state, Category category, Urgency urgency, User approved) {
        this.name = name;
        this.description = description;
        this.createdOn = createdOn;
        this.resolutionDate = resolutionDate;
        this.owner = owner;
        this.state = state;
        this.category = category;
        this.urgency = urgency;
        this.approved = approved;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public Timestamp getResolutionDate() {
        return resolutionDate;
    }

    public User getAssignee() {
        return assignee;
    }

    public User getOwner() {
        return owner;
    }

    public State getState() {
        return state;
    }

    public Category getCategory() {
        return category;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public User getApproved() {
        return approved;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public void setResolutionDate(Timestamp resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public void setApproved(User approved) {
        this.approved = approved;
    }
}
