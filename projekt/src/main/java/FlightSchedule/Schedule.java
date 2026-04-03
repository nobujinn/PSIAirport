package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * FlightSchedule::Schedule
 *
 * Agregácia: Flight 1..* ---◇ 1 Schedule
 */
public class Schedule {

    private LocalDateTime date;
    private List<Flight> flights;
    private int scheduleId;

    public Schedule() {
        this.flights = new ArrayList<>();
    }

    public Schedule(int scheduleId, LocalDateTime date) {
        this.scheduleId = scheduleId;
        this.date = date;
        this.flights = new ArrayList<>();
    }

    // -------------------- Getters & Setters --------------------

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    // -------------------- Business Methods --------------------

    public void overUdaje() {
        if (date == null) {
            throw new IllegalStateException("Dátum rozvrhu nesmie byť prázdny.");
        }
    }

    public List<Flight> najdiLety(String origin, String destination) {
        List<Flight> result = new ArrayList<>();
        for (Flight f : flights) {
            if ((origin == null || f.getOrigin().equals(origin)) &&
                (destination == null || f.getDestination().equals(destination))) {
                result.add(f);
            }
        }
        return result;
    }

    public void skontrolujDostupnost() {
        overUdaje();
        for (Flight f : flights) {
            if (f.getPlane().getCapacity() <= 0) {
                throw new IllegalStateException("Lietadlo id=" + f.getPlane().getPlaneId() + " má neplatnú kapacitu.");
            }
        }
    }

    // -------------------- Helper Methods --------------------

    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    // -------------------- Object --------------------

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", date=" + date +
                ", flights=" + flights.size() +
                '}';
    }
}
