package ru.com.alexsolo.Enum;

public enum Action {
    Submit("Submit"),Cancel("Cancel"),Approve("Approve"),Decline("Decline"),Assign_to_Me("Assign to Me"),Done("Done");

    public final String label;

    private Action(String label) {
        this.label = label;
    }
    }
