package StateEnum;

public enum BaggageState {
    registered("REGISTERED"),
    canceled("CANCELED"),
    loaded("LOADED"),
    delivered("DELIVERED"),
    lost("LOST");

    private String state;

    BaggageState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
