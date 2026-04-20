package StateEnum;

public enum PlaneState {
    registered("REGISTERED"),
    parked("PARKED"),
    inFlight("IN_FLIGHT"),
    maintenance("MAINTENANCE"),
    deleted("DELETED");

    private String state;

    PlaneState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
