package ru.com.alexsolo.Enum;

public enum State {
        Draft("Draft"),New("New"),Approved("Approved"),Declined("Declined"),In_Progress("In Progress"),Done("Done"),Canceled("Canceled");


    public final String label;

    private State(String label) {
        this.label = label;
    }
}
