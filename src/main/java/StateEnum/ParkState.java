package StateEnum;

public enum ParkState {
    available("AVAILABLE"),
    reserved("RESERVED"),
    occupied("OCCUPIED"),
    outOfService("OUT_OF_SERVICE");

    private String state;
    
    ParkState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

}
