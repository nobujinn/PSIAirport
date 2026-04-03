package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * FlightSchedule::Flight
 *
 * Asociácie:
 *   - Ticket 1..* --- 1 Flight
 *   - Plane 1 --- 1 Flight
 *
 * Agregácia: Flight 1..* ---◇ 1 Schedule
 */
public class Flight {

    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private String destination;
    private int flightId;
    private String origin;
    private Plane plane;
    private List<Ticket> tickets;

    public Flight() {
        this.tickets = new ArrayList<>();
    }

    public Flight(int flightId, LocalDateTime departureTime, LocalDateTime arrivalTime,
                  String origin, String destination, Plane plane) {
        this.flightId = flightId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.origin = origin;
        this.destination = destination;
        this.plane = plane;
        this.tickets = new ArrayList<>();
    }

    // -------------------- Getters & Setters --------------------

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    // -------------------- Business Methods --------------------

    public List<Flight> najdiLety(String origin, String destination, LocalDateTime date) {
        List<Flight> result = new ArrayList<>();
        if ((origin == null || this.origin.equals(origin)) &&
            (destination == null || this.destination.equals(destination)) &&
            (date == null || this.departureTime.toLocalDate().equals(date.toLocalDate()))) {
            result.add(this);
        }
        return result;
    }

    // -------------------- Helper Methods --------------------

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    // -------------------- Object --------------------

    @Override
    public String toString() {
        return "Flight{" +
                "flightId=" + flightId +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", plane=" + plane +
                ", tickets=" + tickets.size() +
                '}';
    }
}
