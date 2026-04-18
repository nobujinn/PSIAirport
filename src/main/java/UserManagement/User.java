package UserManagement;

import TicketManagement.Ticket;
import java.util.ArrayList;
import java.util.List;

/**
 * UserManagement::User
 *
 * Asociacia: User 1 --- 0..* Ticket
 */
public class User {

    private String email;
    private String name;
    private List<Ticket> tickets;
    private UserType type;
    private int userId;

    public User() {
        this.tickets = new ArrayList<>();
    }

    public User(int userId, String name, String email, UserType type) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.type = type;
        this.tickets = new ArrayList<>();
    }

    // -------------------- Getters & Setters --------------------

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // -------------------- Business Methods --------------------

    public void nakupLetenku(int ticketId, double price) {
        Ticket ticket = new Ticket(ticketId, price);
        this.addTicket(ticket);
    }

    public void overUdaje() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Meno pouzivatela nesmie byt prazdne.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalStateException("E-mail pouzivatela nesmie byt prazdny.");
        }
    }

    // -------------------- Helper Methods --------------------

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    // -------------------- Object --------------------

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", tickets=" + tickets.size() +
                '}';
    }
}
