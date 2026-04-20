package PlaneManagement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import StateEnum.ParkState;


public class Parking {

    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private int parkingId;
    private Plane plane;
    private ParkState state;

    private static final List<Parking> register = new ArrayList<>();

    public Parking() {
    }

    public Parking(int parkingId, LocalDateTime dateFrom, LocalDateTime dateTo, Plane plane) {
        this.parkingId = parkingId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.plane = plane;
        this.state = ParkState.available;
    }

    // -------------------- Getters & Setters --------------------

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public int getParkingId() {
        return parkingId;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    // -------------------- Business Methods --------------------

    public boolean overDostupnost() {
        if (dateFrom == null || dateTo == null) {
            throw new IllegalStateException("Datumy parkovania nesmu byt prazdne.");
        }
        if (dateFrom.isAfter(dateTo)) {
            throw new IllegalStateException("Datum zaciatku musi byt pred datumom konca.");
        }
        for (Parking p : register) {
            if (p.parkingId == this.parkingId
                    && p.dateFrom.isBefore(this.dateTo)
                    && p.dateTo.isAfter(this.dateFrom)) {
                return false;
            }
        }
        return true;
    }

    public void ulozRezervaciu() {
        boolean is_available = overDostupnost();

        if (is_available) {
            this.state = ParkState.reserved;
            register.add(this);
            this.plane.ulozRezervaciu();
        }
    }

    // -------------------- Object --------------------

    @Override
    public String toString() {
        return "Parking{" +
                "parkingId=" + parkingId +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", plane=" + plane +
                ", state=" + state +
                '}';
    }
}
