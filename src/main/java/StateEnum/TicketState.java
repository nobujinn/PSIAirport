package StateEnum;

public enum TicketState {
    created("CREATED"),
    canceled("CANCELED"),
    paid("PAID"),
    expired("EXPIRED"),
    used("USED");

    private String state;

    TicketState(String state) {
        this.state = state;
    }
    
    public String getState() {
        return state;
    }
}
