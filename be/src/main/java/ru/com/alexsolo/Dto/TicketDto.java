package ru.com.alexsolo.Dto;

import ru.com.alexsolo.Enum.Action;
import ru.com.alexsolo.Enum.Urgency;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

public class TicketDto {

    private long id;
    @Pattern(regexp = "[a-z0-9~.\"(),:;< >@\\[\\]!#$%&'*+-\\/=?^_`{|}]{0,100}")
    private String name;
    @Pattern(regexp = "[a-zA-Z0-9~.\"(),:;< >@\\[\\]!#$%&'*+-\\/=?^_`{|}]{0,500}")
    private String description;
    private String createOn;
    private String desiredResolutionDate;
    private String assignee;
    private String approver;
    private String owner;
    @NotNull
    private String state;
    @NotNull
    private Urgency urgency;
    @NotNull
    private String category;
    private String comment;
    private List<Action> actions;
    private List<AttacmentDto> attacmentDto;

    public TicketDto() {
    }

    public TicketDto(long id, String name, String desiredResolutionDate, String state, Urgency urgency) {
        this.id = id;
        this.name = name;
        this.desiredResolutionDate = desiredResolutionDate;
        this.state = state;
        this.urgency = urgency;
    }

    public TicketDto(long id, String name, String description, String createOn, String desiredResolutionDate,
                     String assignee, String owner, String state, Urgency urgency, String category, String approver) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createOn = createOn;
        this.desiredResolutionDate = desiredResolutionDate;
        this.assignee = assignee;
        this.owner = owner;
        this.state = state;
        this.urgency = urgency;
        this.category = category;
        this.approver = approver;
    }

    public TicketDto(long id, String name, String description, String createOn, String desiredResolutionDate,
                     String assignee, String approver, String owner, String state, Urgency urgency, String category, List<Action> actions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createOn = createOn;
        this.desiredResolutionDate = desiredResolutionDate;
        this.assignee = assignee;
        this.approver = approver;
        this.owner = owner;
        this.state = state;
        this.urgency = urgency;
        this.category = category;
        this.actions = actions;
    }


    public TicketDto(long id, String name, String description, String createOn, String desiredResolutionDate, String assignee,
                     String owner, String state,Urgency urgency, String category, String approver,
                      List<AttacmentDto> attacmentDto) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createOn = createOn;
        this.desiredResolutionDate = desiredResolutionDate;
        this.assignee = assignee;
        this.owner = owner;
        this.state = state;
        this.urgency = urgency;
        this.category = category;
        this.approver = approver;
        this.attacmentDto = attacmentDto;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public String getDesiredResolutionDate() {
        return desiredResolutionDate;
    }

    public void setDesiredResolutionDate(String desiredResolutionDate) {
        this.desiredResolutionDate = desiredResolutionDate;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<AttacmentDto> getAttacmentDto() {
        return attacmentDto;
    }

    public void setAttacmentDto(List<AttacmentDto> attacmentDto) {
        this.attacmentDto = attacmentDto;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}