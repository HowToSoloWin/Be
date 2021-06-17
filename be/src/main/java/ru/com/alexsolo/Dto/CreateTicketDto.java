package ru.com.alexsolo.Dto;

import ru.com.alexsolo.Enum.State;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class CreateTicketDto {
    @NotNull
    private String category;
    private String comment;
    @Pattern(regexp = "[a-zA-Z0-9~.\"(),:;< >@\\[\\]!#$%&'*+-\\/=?^_`{|}]{0,500}")
    private String description;
    private String desiredResolutionDate;
    @Pattern(regexp = "[a-z0-9~.\"(),:;< >@\\[\\]!#$%&'*+-\\/=?^_`{|}]{0,100}")
    private String name;
    @NotNull
    private String urgency;
    @NotNull
    private String username;
    @NotNull
    private State state;

    public CreateTicketDto() {
    }

    public String getCategory() {
        return category;
    }

    public String getComment() {
        return comment;
    }

    public String getDescription() {
        return description;
    }

    public String getDesiredResolutionDate() {
        return desiredResolutionDate;
    }

    public String getName() {
        return name;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getUsername() {
        return username;
    }

    public State getState() {
        return state;
    }

}
