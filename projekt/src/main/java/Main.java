import PlaneManagement.Plane;
import PlaneManagement.Parking;
import PlaneManagement.PlaneType;
import FlightSchedule.Flight;
import TicketManagement.Ticket;
import TicketManagement.Baggage;
import TicketManagement.BaggageType;
import TicketManagement.Seat;
import TicketManagement.SeatType;

import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        uc01_registraciaLietadla();
        uc02_nakupLetenky();
        uc03_rezervaciaParkovaciehoMiesta();
        uc04_registraciaSpecialnejBatoziny();
        uc05_vyberSedadla();
    }

    // UC:01 - Registracia lietadla
    static void uc01_registraciaLietadla() {
        Plane plane = new Plane();
        plane.setPlaneId(1);

        // First attempt: invalid data — simulate user error
        plane.setCapacity(-1);
        plane.setType(null);

        // --- LOOP: Pokial su udaje nespravne ---
        boolean dataValid = false;
        while (!dataValid) {
            try {
                plane.overUdaje();
                dataValid = true;
            } catch (IllegalStateException e) {
                System.out.println("[UC01] Nespravne udaje lietadla: " + e.getMessage() + " — opravujem...");
                plane.setCapacity(180);
                plane.setType(PlaneType.PRIVATE);
            }
        }
        // --- END LOOP ---

        plane.ulozLietadlo();
        System.out.println("[UC01] Lietadlo registrovane: " + plane);
    }

    // UC:02 - Nakup letenky
    static void uc02_nakupLetenky() {
        Plane plane = new Plane(10, PlaneType.COMMERCIAL, 200);
        new Flight(101, LocalDateTime.of(2026, 5, 1, 10, 0), LocalDateTime.of(2026, 5, 1, 12, 30),
                "Dnipro", "Warsaw", plane).ulozLet();
        new Flight(102, LocalDateTime.of(2026, 5, 1, 15, 0), LocalDateTime.of(2026, 5, 1, 17, 45),
                "Dnipro", "Vienna", plane).ulozLet();

        List<Flight> found = Ticket.vyhladajLetyPodlaFiltrov("Dnipro", "Warsaw",
                LocalDateTime.of(2026, 5, 1, 0, 0));

        // opt: ref UC:05 Vyber sedadla
        // opt: ref UC:06 Objednanie stravy
        // opt: ref UC:04 Registracia specialnej batoziny
        // opt: ref UC:07 Online check-in

        Ticket ticket = new Ticket(201, 199.99);
        ticket.spracujPlatbu();
        System.out.println("[UC02] Letenka zakupena: id=" + ticket.getTicketId()
                + ", cena=" + ticket.getPrice() + " EUR, lety=" + found.size());
    }

    // UC:03 - Rezervacia parkovacieho miesta pre lietadlo
    static void uc03_rezervaciaParkovaciehoMiesta() {
        Plane plane = new Plane(2, PlaneType.PRIVATE, 8);

        // Pre-seed a conflicting reservation so the loop fires at least once
        new Parking(1, LocalDateTime.of(2026, 5, 10, 10, 0),
                LocalDateTime.of(2026, 5, 10, 14, 0), plane).ulozRezervaciu();

        // Candidate time windows — first overlaps, second is free
        LocalDateTime[][] attempts = {
            { LocalDateTime.of(2026, 5, 10, 12, 0), LocalDateTime.of(2026, 5, 10, 16, 0) },
            { LocalDateTime.of(2026, 5, 10, 15, 0), LocalDateTime.of(2026, 5, 10, 19, 0) }
        };

        // --- LOOP: Pokial miesto nie je k dispozicii ---
        Parking reservation = null;
        int attempt = 0;
        boolean available = false;
        while (!available) {
            reservation = new Parking(1, attempts[attempt][0], attempts[attempt][1], plane);
            available = reservation.overDostupnost();
            if (!available) {
                System.out.println("[UC03] Miesto nie je dostupne pre " + attempts[attempt][0] + " — skusam iny cas...");
                attempt++;
            }
        }
        // --- END LOOP ---

        reservation.ulozRezervaciu();
        System.out.println("[UC03] Rezervacia ulozena: " + reservation);
    }

    // UC:04 - Registracia specialnej batoziny
    static void uc04_registraciaSpecialnejBatoziny() {
        List<Ticket> existingTickets = Ticket.najdiListky(null, null, null, null, null);

        // --- ALT: Ak nie su listky ---
        if (existingTickets.isEmpty()) {
            System.out.println("[UC04] Ziadne letenky nenajdene — vytvoram demo letenku...");
            Ticket seeded = new Ticket(301, 149.99);
            seeded.spracujPlatbu();
            existingTickets.add(seeded);
        }
        // --- END ALT ---

        Ticket selectedTicket = existingTickets.get(0);
        Baggage baggage = new Baggage();

        // Start with invalid data so the loop fires at least once
        baggage.setBaggageId(0);
        baggage.setType(null);
        baggage.setWeight(-5.0);

        // --- LOOP: Pokial udaje nie su spravne ---
        boolean dataValid = false;
        while (!dataValid) {
            try {
                baggage.overovanie();
                dataValid = true;
            } catch (IllegalStateException e) {
                System.out.println("[UC04] Nespravne udaje batoziny: " + e.getMessage() + " — opravujem...");
                baggage.setBaggageId(401);
                baggage.setType(BaggageType.SUITCASE);
                baggage.setWeight(23.5);
            }
        }
        // --- END LOOP ---

        baggage.ulozDoLetenky(selectedTicket);
        System.out.println("[UC04] Batozina registrovana: " + baggage
                + ", letenka ma " + selectedTicket.getBaggage().size() + " batozin");
    }

    // UC:05 - Vyber sedadla
    static void uc05_vyberSedadla() {
        Ticket ticket = new Ticket(501, 199.99);
        ticket.spracujPlatbu();

        // First seat is occupied, second is free
        Seat[] candidates = {
            new Seat(10, false, SeatType.ECONOMY),
            new Seat(11, true,  SeatType.BUSINESS)
        };
        int attempt = 0;

        // --- LOOP: Pokial je miesto obsadene ---
        Seat chosen = null;
        while (chosen == null) {
            try {
                candidates[attempt].skontrolujMiesto();
                chosen = candidates[attempt];
            } catch (IllegalStateException e) {
                System.out.println("[UC05] Sedadlo obsadene: " + e.getMessage() + " — skusam ine...");
                attempt++;
            }
        }
        // --- END LOOP ---

        float price = chosen.zistiCenu();
        chosen.ulozRezervaciu(ticket);
        System.out.println("[UC05] Sedadlo rezervovane: id=" + ticket.getSeat().getSeatId()
                + ", typ=" + ticket.getSeat().getType() + ", cena=" + price + " EUR");
    }
}
