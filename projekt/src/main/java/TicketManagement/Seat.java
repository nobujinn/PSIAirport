package model;

import enums.SeatType;

/**
 * TicketManagement::Seat
 *
 * Agregácia: Seat 1 ---◇ 1 Ticket
 */
public class Seat {

    private boolean available;
    private int seatId;
    private SeatType type;

    private Ticket ticket;

    public Seat() {
    }

    public Seat(int seatId, boolean available, SeatType type) {
        this.seatId = seatId;
        this.available = available;
        this.type = type;
    }

    // -------------------- Getters & Setters --------------------

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public SeatType getType() {
        return type;
    }

    public void setType(SeatType type) {
        this.type = type;
    }

    // -------------------- Business Methods --------------------

    public void overDostupnost() {
        if (!available) {
            throw new IllegalStateException("Sedadlo id=" + seatId + " nie je dostupné.");
        }
    }

    public void ulozRezervaciu() {
        overDostupnost();

        this.ticket.setSeat(this);
    }

    public float zistiCenu() {
        switch (type) {
            case ECONOMY:  return 50.0f;
            case BUSINESS: return 150.0f;
            case PREMIUM:  return 250.0f;
            default:       return 0.0f;
        }
    }

    // -------------------- Object --------------------

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", available=" + available +
                ", type=" + type +
                '}';
    }
}
