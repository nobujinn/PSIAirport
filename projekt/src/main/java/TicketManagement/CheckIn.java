package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TicketManagement::Check-in
 *
 * Agregácia: CheckIn 1 ---◇ 1 Ticket
 */
public class CheckIn {

    private int checkinId;
    private LocalDateTime date;

    public CheckIn() {
    }

    public CheckIn(int checkinId, LocalDateTime date) {
        this.checkinId = checkinId;
        this.date = date;
    }

    // -------------------- Getters & Setters --------------------

    public int getCheckinId() {
        return checkinId;
    }

    public void setCheckinId(int checkinId) {
        this.checkinId = checkinId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // -------------------- Business Methods --------------------

    public List<Ticket> najdiListky() {
        List<Ticket> result = Ticket.najdiListky(null, null, true, null, null);
        return result;
    }

    public void overUdaje() {
        if (date == null) {
            throw new IllegalStateException("Dátum check-inu nesmie byť prázdny.");
        }
    }

    public void ulozDoLetenky(Ticket t) {
        overUdaje();
        t.setCheckin(this);
    }

    // -------------------- Object --------------------

    @Override
    public String toString() {
        return "CheckIn{" +
                "checkinId=" + checkinId +
                ", date=" + date +
                '}';
    }
}
