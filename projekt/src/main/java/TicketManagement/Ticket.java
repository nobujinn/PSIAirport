package model;

import enums.BaggageType;
import enums.MealType;

import java.util.ArrayList;
import java.util.List;


public class Ticket {

    private List<Baggage> baggage;
    private CheckIn checkin;
    private List<Meal> meals;
    private double price;
    private Seat seat;
    private int ticketId;



    private static final List<Ticket> tickets = new ArrayList<>();

    public Ticket() {
        this.baggage = new ArrayList<>();
        this.meals = new ArrayList<>();
    }

    public Ticket(int ticketId, double price) {
        this.ticketId = ticketId;
        this.price = price;
        this.baggage = new ArrayList<>();
        this.meals = new ArrayList<>();
    }

    // -------------------- Getters & Setters --------------------

    public List<Baggage> getBaggage() {
        return baggage;
    }

    public void setBaggage(List<Baggage> baggage) {
        this.baggage = baggage;
    }

    public CheckIn getCheckin() {
        return checkin;
    }

    public void setCheckin(CheckIn checkin) {
        this.checkin = checkin;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    // -------------------- Business Methods --------------------

    public static List<Ticket> najdiListky(Double maxPrice, Integer seatId,
                                           Boolean hasCheckin, BaggageType baggageType,
                                           MealType mealType) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {

            if (maxPrice != null && t.price > maxPrice) continue;

            if (seatId != null && (t.seat == null || t.seat.getSeatId() != seatId)) continue;

            if (hasCheckin != null && (t.checkin != null) != hasCheckin) continue;

            if (baggageType != null) {
                boolean found = false;
                for (Baggage b : t.baggage) {
                    if (b.getType() == baggageType) { found = true; break; }
                }
                if (!found) continue;
            }

            if (mealType != null) {
                boolean found = false;
                for (Meal m : t.meals) {
                    if (m.getType() == mealType) { found = true; break; }
                }
                if (!found) continue;
            }

            result.add(t);
        }
        return result;
    }

    public void spracujPlatbu() {
        if (price <= 0) {
            throw new IllegalStateException("Cena lístka musí byť kladná.");
        }
        tickets.add(this);
    }

    public void ulozDoLetenky() {
        if (ticketId <= 0) {
            throw new IllegalStateException("ID lístka musí byť platné.");
        }
        tickets.add(this);
    }

    // -------------------- Helper Methods --------------------

    public void addMeal(Meal meal) {
        this.meals.add(meal);
    }

    public void addBaggage(Baggage bag) {
        this.baggage.add(bag);
    }

    // -------------------- Object --------------------

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", price=" + price +
                ", seat=" + seat +
                ", checkin=" + checkin +
                ", meals=" + meals.size() +
                ", baggage=" + baggage.size() +
                '}';
    }
}
