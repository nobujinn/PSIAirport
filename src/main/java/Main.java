import FlightSchedule.Schedule;
import PlaneManagement.Plane;
import PlaneManagement.Parking;
import PlaneManagement.PlaneType;
import FlightSchedule.Flight;
import TicketManagement.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // UC:01.1 - Registracia lietadla (contains ref: UC:03)
        uc01_registraciaLietadla();

        // UC:02 - Nakup letenky (contains ref: UC:05, opt: UC:06, UC:04, UC:07)
        uc02_nakupLetenky();

        // UC:08 - Prehlad letoveho poriadku
        uc08_prehadLetovehoPoriadku();

        // UC:01.2 - Vymazanie lietadla
        uc09_vymazanieLietadla();
    }

    // UC:01 - Registracia lietadla
    static void uc01_registraciaLietadla() {
        // vyber registraciu lietadla() — user opens the registration screen
        // GUI self: zobraz formular() — form is shown to user
        System.out.println("[UC01] Formular na registraciu lietadla zobrazeny.");

        Plane plane = new Plane();
        plane.setPlaneId(1);

        // Simulate submissions: first invalid, then valid
        int[][] techData = {
                { -1, 0 },   // attempt 1: invalid capacity, missing type
                { 180, 1 }   // attempt 2: valid
        };
        int attempt = 0;

        // --- LOOP: Pokial su udaje nespravne ---
        boolean dataValid = false;
        while (!dataValid) {
            // zadaj technicke udaje() — user submits technical data via form
            plane.setCapacity(techData[attempt][0]);
            plane.setType(techData[attempt][1] == 0 ? null : PlaneType.PRIVATE);
            System.out.println("[UC01] Zadane technicke udaje: kapacita="
                    + plane.getCapacity() + ", typ=" + plane.getType());

            // over udaje() — GUI calls Plane.overUdaje(); Plane self-validates
            try {
                plane.overUdaje();
                dataValid = true;
            } catch (IllegalStateException e) {
                // GUI self: zobraz chybu() — error shown to user, loop continues
                System.out.println("[UC01] Chyba validacie: " + e.getMessage()
                        + " — zobrazujem chybu, prosim opravit.");
                attempt++;
            }
        }
        // --- END LOOP ---

        // nahraj dokumenty() — user uploads supporting documents (simulated)
        System.out.println("[UC01] Dokumenty nahrane.");

        // uloz lietadlo() — GUI → Plane.ulozLietadlo(); Plane saves itself to register
        plane.ulozLietadlo();

        // GUI self: zobraz potvrdenie o registracii()
        System.out.println("[UC01] Potvrdenie: lietadlo uspesne registrovane: " + plane);

        // ref: UC:03 Rezervacia parkovacieho miesta pre lietadlo
        uc03_rezervaciaParkovaciehoMiesta();

    }

    // UC:02 - Nakup letenky
    static void uc02_nakupLetenky() {
        // vyber kupit letenku() — user opens ticket purchase screen
        // GUI self: zobraz filtre()
        System.out.println("[UC02] Zobrazujem filtre na vyhladavanie letov.");

        // Seed available flights
        Plane plane = new Plane(10, PlaneType.COMMERCIAL, 200);
        new Flight(101, LocalDateTime.of(2026, 5, 1, 10, 0), LocalDateTime.of(2026, 5, 1, 12, 30),
                "Dnipro", "Warsaw", plane).ulozLet();
        new Flight(102, LocalDateTime.of(2026, 5, 1, 15, 0), LocalDateTime.of(2026, 5, 1, 17, 45),
                "Dnipro", "Vienna", plane).ulozLet();

        // zadaj filtre() — user submits filter criteria
        System.out.println("[UC02] Cestujuci zadal filtre: origin=Dnipro, destination=Warsaw, datum=2026-05-01.");

        // vyhladaj lety podla filtrov() — GUI -> Ticket.vyhladajLetyPodlaFiltrov()
        // Ticket self: najdi lety() — delegates to Flight.najdiLety()
        List<Flight> found = Ticket.vyhladajLetyPodlaFiltrov("Dnipro", "Warsaw",
                LocalDateTime.of(2026, 5, 1, 0, 0));

        // GUI self: zobraz zoznam letov()
        System.out.println("[UC02] Zobrazujem zoznam letov (" + found.size() + " najdenych).");

        // vyber let() — user selects a flight (simulated: first result)
        System.out.println("[UC02] Cestujuci vybral let: " + found.get(0));

        // ref: UC:05 Sequence Diagram Vyber sedadla
        uc05_vyberSedadla();

        // opt Objednanie stravy — ref: UC:06
        System.out.println("[UC02][opt] Cestujuci si objednava stravu.");
        uc06_objednanieStravy();

        // opt RegistraciaBatoziny — ref: UC:04
        System.out.println("[UC02][opt] Cestujuci registruje specialnu batozinu.");
        uc04_registraciaSpecialnejBatoziny();

        // opt Online-CheckIn — ref: UC:07
        System.out.println("[UC02][opt] Cestujuci vykonava online check-in.");
        uc07_onlineCheckIn();

        // GUI self: zobraz obrazovku platby()
        System.out.println("[UC02] Zobrazujem obrazovku platby.");

        // zaplat() — user confirms payment
        // GUI -> Ticket.spracujPlatbu() -> Ticket self: ulozStavPlatbyALetenku()
        Ticket ticket = new Ticket(201, 199.99);
        ticket.spracujPlatbu();

        // GUI self: zobraz QR kod letenky()
        System.out.println("[UC02] Platba uspesna. Zobrazujem QR kod letenky: id="
                + ticket.getTicketId() + ", cena=" + ticket.getPrice() + " EUR.");
    }

    // UC:03 - Rezervacia parkovacieho miesta pre lietadlo
    static void uc03_rezervaciaParkovaciehoMiesta() {
        System.out.println("[UC03] Otvaram FXML formular rezervacie parkovacieho miesta.");

        ParkingReservationResult result = ParkingReservationDialog.showAndWait();

        if (result == null) {
            System.out.println("[UC03] Rezervacia bola zrusena pouzivatelom.");
            return;
        }

        System.out.println("[UC03] Vstupy z formulara:");
        System.out.println("[UC03]  - lietadlo: " + result.plane());
        System.out.println("[UC03]  - datum od: " + result.dateFrom());
        System.out.println("[UC03]  - datum do: " + result.dateTo());
        System.out.println("[UC03]  - parkovacie miesto: " + result.parkingId());

        System.out.println("[UC03] Potvrdenie: rezervacia ulozena. Lietadlo=" + result.plane()
            + ", miesto=" + result.parkingId()
            + ", od=" + result.dateFrom()
            + ", do=" + result.dateTo());
    }

    // UC:04 - Registracia specialnej batoziny
    static void uc04_registraciaSpecialnejBatoziny() {
        // vyber pridanie batoziny() — user opens baggage registration
        System.out.println("[UC04] Cestujuci vybral pridanie batoziny.");

        // over existenciu listkov() — GUI -> Baggage.najdiListky() -> Ticket.najdiListky()
        // Ticket self: najdi listky()
        List<Ticket> existingTickets = Ticket.najdiListky(null, null, null, null, null);

        // --- ALT: Ak nie su listky ---
        if (existingTickets.isEmpty()) {
            // GUI self: zobraz chybu()
            System.out.println("[UC04] Ziadne letenky nenajdene — zobrazujem chybu.");
            return;
        }
        // --- END ALT ---

        // GUI self: zobraz zoznam listkov()
        System.out.println("[UC04] Zobrazujem zoznam listkov (" + existingTickets.size() + " najdenych).");

        // vyber listok() — user selects a ticket (simulated: first available)
        Ticket selectedTicket = existingTickets.get(0);
        System.out.println("[UC04] Cestujuci vybral listok: id=" + selectedTicket.getTicketId());

        // GUI self: zobraz formular batoziny()
        System.out.println("[UC04] Zobrazujem formular batoziny.");

        // Prepare baggage with invalid data so the loop fires at least once
        Baggage baggage = new Baggage();
        baggage.setBaggageId(0);
        baggage.setType(null);
        baggage.setWeight(-5.0);

        // --- LOOP: Pokial udaje nie su spravne ---
        boolean dataValid = false;
        while (!dataValid) {
            // zadaj udaje o batozine() — user fills in baggage details
            System.out.println("[UC04] Zadane udaje: id=" + baggage.getBaggageId()
                    + ", typ=" + baggage.getType() + ", hmotnost=" + baggage.getWeight());

            // over udaje batoziny() — GUI -> Baggage.overUdaje() -> Baggage self: overovanie()
            try {
                baggage.overovanie();
                dataValid = true;
            } catch (IllegalStateException e) {
                // GUI self: zobraz upozornenie()
                System.out.println("[UC04] Upozornenie: " + e.getMessage() + " — prosim opravit.");
                baggage.setBaggageId(401);
                baggage.setType(BaggageType.SUITCASE);
                baggage.setWeight(23.5);
            }
        }
        // --- END LOOP ---

        // uloz batozinu() — user confirms; GUI -> Baggage.ulozDoLetenky()
        //   -> Baggage -> Ticket.ulozDoLetenky (ukladanie()) -> Ticket self: ukladanie()
        baggage.ulozDoLetenky(selectedTicket);

        // GUI self: zobraz potvrdenie()
        System.out.println("[UC04] Potvrdenie: batozina registrovana: " + baggage
                + ", letenka id=" + selectedTicket.getTicketId()
                + " ma " + selectedTicket.getBaggage().size() + " batozin.");
    }

    // UC:05 - Vyber sedadla
    static void uc05_vyberSedadla() {
        // Prepare ticket and candidate seats (first occupied, second free)
        Ticket ticket = new Ticket(501, 199.99);
        ticket.spracujPlatbu();

        Seat[] candidates = {
                new Seat(10, false, SeatType.ECONOMY),   // obsadene
                new Seat(11, true,  SeatType.BUSINESS)   // volne
        };
        int attempt = 0;

        // --- LOOP: Pokial je miesto obsadene ---
        Seat chosen = null;
        while (chosen == null) {
            // vyber miesto() — user picks a seat
            System.out.println("[UC05] Cestujuci vybral sedadlo id=" + candidates[attempt].getSeatId()
                    + " (typ=" + candidates[attempt].getType() + ").");

            // over dostupnost miesta() — GUI -> Seat.overDostupnost() -> Seat self: skontroluj miesto()
            try {
                candidates[attempt].overDostupnost();
                chosen = candidates[attempt];
            } catch (IllegalStateException e) {
                // GUI self: Spytaj sa na inu miestenku() — prompt user to pick another seat
                System.out.println("[UC05] Miesto obsadene: " + e.getMessage()
                        + " — Spytaj sa na inu miestenku.");
                attempt++;
            }
        }
        // --- END LOOP ---

        // zisti cenu miesta() — GUI -> Seat.zistiCenu() -> Seat self: zisti cenu miesta()
        float price = chosen.zistiCenu();

        // GUI self: zobraz cenu miesta()
        System.out.println("[UC05] Cena sedadla id=" + chosen.getSeatId()
                + " (typ=" + chosen.getType() + "): " + price + " EUR.");

        // potvrď rezervaciu() — user confirms
        System.out.println("[UC05] Cestujuci potvrdil rezervaciu sedadla.");

        // uloz rezervaciu() — GUI -> Seat.ulozRezervaciu(ticket)
        //   -> Seat -> Ticket.uloz do letenky (ukladanie(Seat)) -> Ticket self: ukladanie()
        chosen.ulozRezervaciu(ticket);

        // GUI self: potvrd ulozenie()
        System.out.println("[UC05] Potvrdenie ulozenia: sedadlo id=" + ticket.getSeat().getSeatId()
                + ", typ=" + ticket.getSeat().getType() + ", cena=" + price + " EUR.");
    }

    static void uc06_objednanieStravy() {
        // Seed the meal register with available meals and their allergens
        Meal beef  = new Meal(601, 12.50, MealType.BEEF_AND_RICE);
        Meal pork  = new Meal(602, 10.00, MealType.PORK_AND_EGGS);
        Meal chick = new Meal(603, 11.00, MealType.CHICKEN_AND_NOODLES);
        beef.ulozAlergie(Arrays.asList("gluten"));
        pork.ulozAlergie(Arrays.asList("pork"));
        chick.ulozAlergie(List.of());
        beef.ulozJedlo();
        pork.ulozJedlo();
        chick.ulozJedlo();

        // over existenciu listkov() — GUI asks Meal, Meal delegates to Ticket.najdiListky()
        List<Ticket> existingTickets = Ticket.najdiListky(null, null, null, null, null);

        // --- ALT: Ak nie su listky ---
        if (existingTickets.isEmpty()) {
            System.out.println("[UC06] Ziadne letenky nenajdene — zobrazujem chybu.");
            return;
        }
        // --- END ALT ---

        // zobraz zoznam listkov — user selects first available ticket
        Ticket selectedTicket = existingTickets.get(0);
        System.out.println("[UC06] Zobrazujem zoznam listkov, vybrany: id=" + selectedTicket.getTicketId());

        // vyber let() — user picks their flight (simulated)
        System.out.println("[UC06] Cestujuci vybral let.");

        // spytaj sa na alergie() — GUI prompts user for allergies
        System.out.println("[UC06] Cestujuci opytany na alergie.");

        // --- OPT: Cestujuci zadava alergie ---
        List<String> userAlergie = Arrays.asList("pork");   // simulated: user is allergic to pork
        System.out.println("[UC06] Cestujuci zadal alergie: " + userAlergie);
        System.out.println("[UC06] Alergie ulozene.");
        // --- END OPT ---

        // zisti vhodne jedla() — GUI → Meal.najdiJedla() → Meal self: filters by allergies
        List<Meal> vhodneJedla = Meal.najdiJedla(userAlergie);
        System.out.println("[UC06] Zobrazujem zoznam jedal (" + vhodneJedla.size() + " vhodnych).");

        // vyber jedlo() — user picks a meal (first suitable one simulated)
        Meal chosen = vhodneJedla.get(0);
        System.out.println("[UC06] Cestujuci vybral jedlo: " + chosen);

        // uloz objednavku() — GUI → Meal.ulozDoLetenky() → Ticket.setMeals() → Ticket self: ukladanie()
        chosen.ulozDoLetenky(Arrays.asList(chosen), selectedTicket);

        // zobraz potvrdenie objednavky()
        System.out.println("[UC06] Objednavka potvrdena: letenka id=" + selectedTicket.getTicketId()
                + " ma " + selectedTicket.getMeals().size() + " jedlo/jedal, prve: "
                + selectedTicket.getMeals().get(0));
    }

    // UC:07 - Online check-in cestujuceho
    static void uc07_onlineCheckIn() {
        // vyber online check-in() — user opens online check-in screen
        System.out.println("[UC07] Cestujuci vybral online check-in.");

        // over existenciu listkov() — GUI -> CheckIn.najdiListky()
        //   -> CheckIn -> Ticket.najdiListky() -> Ticket self: najdi listky()
        List<Ticket> existingTickets = Ticket.najdiListky(null, null, null, null, null);

        // --- ALT: Ak nie su listky ---
        if (existingTickets.isEmpty()) {
            // GUI self: zobraz chybu()
            System.out.println("[UC07] Ziadne letenky nenajdene — zobrazujem chybu.");
            return;
        }
        // --- END ALT ---

        // GUI self: zobraz zoznam listkov()
        System.out.println("[UC07] Zobrazujem zoznam listkov (" + existingTickets.size() + " najdenych).");

        // vyber let() — user selects a flight/ticket (simulated: first available)
        Ticket selectedTicket = existingTickets.get(0);
        System.out.println("[UC07] Cestujuci vybral let pre listok id=" + selectedTicket.getTicketId() + ".");

        // GUI self: zobraz formular online check-in()
        System.out.println("[UC07] Zobrazujem formular online check-in.");

        // Prepare CheckIn with invalid data so the loop fires at least once
        CheckIn checkin = new CheckIn();
        checkin.setCheckinId(701);
        checkin.setDate(null);   // invalid — date missing

        // --- LOOP: Pokial udaje nie su spravne ---
        boolean dataValid = false;
        while (!dataValid) {
            // zadaj udaje do formulara() — user fills in check-in form
            System.out.println("[UC07] Zadane udaje: checkinId=" + checkin.getCheckinId()
                    + ", datum=" + checkin.getDate());

            // over udaje() — GUI -> CheckIn.overUdaje() -> CheckIn self: overovanie (date check)
            try {
                checkin.overUdaje();
                dataValid = true;
            } catch (IllegalStateException e) {
                // GUI self: vyziada opravu udajov()
                System.out.println("[UC07] Neplatne udaje: " + e.getMessage()
                        + " — vyziada opravu udajov.");
                checkin.setDate(LocalDateTime.of(2026, 5, 1, 8, 0));  // user corrects date
            }
        }
        // --- END LOOP ---

        // uloz online check-in() — user submits; GUI -> CheckIn.ulozDoLetenky()
        //   -> CheckIn -> Ticket.setCheckin() (ukladanie) -> Ticket self: ukladanie()
        checkin.ulozDoLetenky(selectedTicket);

        // GUI self: zobraz potvrdenie online check-in()
        System.out.println("[UC07] Potvrdenie: online check-in ulozeny pre listok id="
                + selectedTicket.getTicketId()
                + ", datum=" + selectedTicket.getCheckin().getDate() + ".");
    }

    // UC:08 - Prehlad letoveho poriadku
    static void uc08_prehadLetovehoPoriadku() {
        // vyber zobrazit rozvrh() — user opens flight schedule screen
        // GUI self: vyziada datum a cas() — prompts user to enter date/time
        System.out.println("[UC08] Vyziada datum a cas rozvrhu.");

        // Simulate schedule attempts: first has null date (invalid), second is valid
        LocalDateTime[] dateAttempts = {
                null,                                   // attempt 1: invalid
                LocalDateTime.of(2026, 5, 1, 0, 0)     // attempt 2: valid
        };
        int attempt = 0;

        Schedule schedule = new Schedule(801, null);

        // --- LOOP: Pokial udaje nie su spravne ---
        boolean dataValid = false;
        while (!dataValid) {
            // zadaj datum a cas() — user submits date/time
            schedule.setDate(dateAttempts[attempt]);
            System.out.println("[UC08] Zadany datum a cas: " + schedule.getDate());

            // over udaje() — GUI -> Schedule.overUdaje() -> Schedule self: over udaje()
            try {
                schedule.overUdaje();
                dataValid = true;
            } catch (IllegalStateException e) {
                // GUI self: vyziada zadanie datumu a casu znova()
                System.out.println("[UC08] Neplatne udaje: " + e.getMessage()
                        + " — vyziada zadanie datumu a casu znova.");
                attempt++;
            }
        }
        // --- END LOOP ---

        // Seed flights into schedule for the valid date
        Plane plane = new Plane(20, PlaneType.COMMERCIAL, 180);
        Flight f1 = new Flight(801, LocalDateTime.of(2026, 5, 1, 7, 30),
                LocalDateTime.of(2026, 5, 1, 9, 45), "Bratislava", "Prague", plane);
        Flight f2 = new Flight(802, LocalDateTime.of(2026, 5, 1, 12, 0),
                LocalDateTime.of(2026, 5, 1, 14, 20), "Bratislava", "Vienna", plane);
        schedule.addFlight(f1);
        schedule.addFlight(f2);

        // skontroluj dostupnost() — GUI -> Schedule.skontrolujDostupnost()
        //   -> Schedule -> Flight: najdi lety() -> Flight self: najdi lety()
        List<Flight> availableFlights;
        try {
            schedule.skontrolujDostupnost();
            availableFlights = schedule.najdiLety(null, null);
        } catch (IllegalStateException e) {
            availableFlights = List.of();
        }

        // --- ALT: Ak informacie nie su k dispozicii ---
        if (availableFlights.isEmpty()) {
            // GUI self: zobraz chybu()
            System.out.println("[UC08] Informacie nie su k dispozicii — zobrazujem chybu.");
            return;
        }
        // --- END ALT ---

        // GUI self: zobraz rozvrh odletov a prletov()
        System.out.println("[UC08] Rozvrh odletov a priletov pre datum "
                + schedule.getDate().toLocalDate() + " (" + availableFlights.size() + " letov):");
        for (Flight f : availableFlights) {
            System.out.println("[UC08]   " + f.getOrigin() + " -> " + f.getDestination()
                    + " | odlet: " + f.getDepartureTime().toLocalTime()
                    + " | prilet: " + f.getArrivalTime().toLocalTime());
        }
    }

    // UC:09 - Vymazanie lietadla
    static void uc09_vymazanieLietadla() {
        // vyber odstranenie lietadla() — user opens plane removal screen
        System.out.println("[UC09] Pouzivatel vybral odstranenie lietadla.");

        // over existenciu lietadiel() — GUI -> Plane.najdiLietadlo()
        // Plane self: najdi lietadla() — check register for any plane
        Plane found = null;
        try {
            found = Plane.najdiLietadlo(0);   // attempt to find first registered plane
        } catch (IndexOutOfBoundsException e) {
            found = null;
        }

        // --- ALT: ziadne lietadla ---
        if (found == null) {
            // GUI self: zobraz chybu()
            System.out.println("[UC09] Ziadne lietadla nenajdene — zobrazujem chybu.");
            return;
        }
        // --- END ALT ---

        // GUI self: zobraz zoznam lietadiel()
        System.out.println("[UC09] Zobrazujem zoznam lietadiel, najdene: " + found);

        // vyber lietadlo() — user selects a plane to delete (simulated: first found)
        Plane selected = found;
        System.out.println("[UC09] Pouzivatel vybral lietadlo: " + selected);

        // GUI self: vyzvi na potvrdenie odstranenia()
        System.out.println("[UC09] Vyzvanie na potvrdenie odstranenia lietadla id="
                + selected.getPlaneId() + ".");

        // potvrd odstranenie() — user confirms deletion
        System.out.println("[UC09] Pouzivatel potvrdil odstranenie.");

        // odstran lietadlo() — GUI -> Plane.odstranLietadlo() -> Plane self: odstran lietadlo()
        selected.odstranLietadlo();

        // GUI self: zobraz potvrdenie()
        System.out.println("[UC09] Potvrdenie: lietadlo id=" + selected.getPlaneId()
                + " bolo uspesne odstranene.");
    }
}
