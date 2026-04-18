package TicketManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * TicketManagement::Meal
 *
 * Agregacia: Meal 0..* ---◇ 0..1 Ticket
 */
public class Meal {

    private int mealId;
    private double price;
    private MealType type;
    private static final List<Meal> register = new ArrayList<>();

    private List<String> alergie = new ArrayList<>();

    public Meal() {
    }

    public Meal(int mealId, double price, MealType type) {
        this.mealId = mealId;
        this.price = price;
        this.type = type;
    }

    // -------------------- Getters & Setters --------------------

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MealType getType() {
        return type;
    }

    public void setType(MealType type) {
        this.type = type;
    }

    // -------------------- Business Methods --------------------

    public static List<Meal> najdiJedla(List<String> userAlergie) {
        List<Meal> result = new ArrayList<>();
        for (Meal m : register) {
            boolean safe = true;
            for (String a : m.alergie) {
                if (userAlergie.contains(a)) {
                    safe = false;
                    break;
                }
            }
            if (safe) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Ticket> najdiListky() {
        List<Ticket> result = Ticket.najdiListky(null, null, null, null, this.type);
        return result;
    }

    public void ulozJedlo() {
        register.add(this);
    }

    public void ulozAlergie(List<String> alergie) {
        this.alergie = alergie;
    }

    public void ulozDoLetenky(List<Meal> meals, Ticket t) {
        t.setMeals(meals);
    }

    // -------------------- Object --------------------

    @Override
    public String toString() {
        return "Meal{" +
                "mealId=" + mealId +
                ", price=" + price +
                ", type=" + type +
                '}';
    }
}
