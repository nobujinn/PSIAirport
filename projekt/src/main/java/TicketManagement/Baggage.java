package TicketManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * TicketManagement::Baggage
 *
 * Agregacia: Baggage 0..* ---◇ 0..1 Ticket
 */
public class Baggage {

    private int baggageId;
    private BaggageType type;
    private double weight;
    private Ticket ticket;

    public Baggage() {
    }

    public Baggage(int baggageId, BaggageType type, double weight) {
        this.baggageId = baggageId;
        this.type = type;
        this.weight = weight;
    }

    // -------------------- Getters & Setters --------------------

    public int getBaggageId() {
        return baggageId;
    }

    public void setBaggageId(int baggageId) {
        this.baggageId = baggageId;
    }

    public BaggageType getType() {
        return type;
    }

    public void setType(BaggageType type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    // -------------------- Business Methods --------------------

    public List<Ticket> najdiListky() {
        List<Ticket> result = Ticket.najdiListky(null, null, null, this.getType(), null);

        return result;
    }

    public void overUdaje() {
        overovanie();
    }

    public void overovanie() {
        if (baggageId <= 0) {
            throw new IllegalStateException("ID batoziny musi byt kladne.");
        }
        if (type == null) {
            throw new IllegalStateException("Typ batoziny nesmie byt prazdny.");
        }
        if (weight <= 0) {
            throw new IllegalStateException("Hmotnost batoziny musi byt kladna.");
        }
    }

    public void ulozDoLetenky(Ticket t) {
        overovanie();
        t.ukladanie(this);
    }

    public void ulozBatozinu() {
        overovanie();
        this.ticket.addBaggage(this);
    }

    // -------------------- Object --------------------

    @Override
    public String toString() {
        return "Baggage{" +
                "baggageId=" + baggageId +
                ", type=" + type +
                ", weight=" + weight +
                '}';
    }
}
