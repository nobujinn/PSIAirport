# Dnipro Airport Management System - Implementačná dokumentácia

## 1. Prehľad systému

Systém Dnipro Airport Management System je aplikácia v jazyku Java na správu leteckej prevádzky, plánovania letov, rezervácií lístkov a súvisiacich služieb pre letisko. Systém dodržiava zásady objektovo orientovaného programovania so zreteľným oddelením zodpovedností medzi dátovými modelmi, enumeráciami a obchodnou logikou.

### Architektonické komponenty
- **Balík enums**: Definuje typy enumerácií pre klasifikáciu v systéme.
- **Balík model**: Obsahuje hlavné obchodné entity s gettermi, settermi a logikou.
- **Dizajnový vzor**: Približný MVC s modelmi obsahujúcimi metódy pre obchodné pravidlá.

---

## 2. Základné modely a vzťahy

### 2.1 Správa používateľov
**Trieda: User**
- **Účel**: Reprezentuje cestujúcich a používateľov systému.
- **Kľúčové atribúty**: userId, name, email, tickets (List), userType.
- **Vzťahy**:
  - 1 User → 0..* Ticket (Asociácia)
- **Implementovaná metóda**: `nakupLetenku(int ticketId, double price)`
  - Vytvorí nový lístok a priradí ho užívateľovi.
  - Podporuje nákup letenky cez systém.

### 2.2 Správa letov
**Triedy: Flight, Schedule, Plane**

#### Flight
- **Účel**: Reprezentuje jednotlivý let s trasou a časom.
- **Kľúčové atribúty**: flightId, origin, destination, departureTime, arrivalTime, plane, tickets.
- **Implementovaná metóda**: `najdiLety(String origin, String destination, LocalDateTime date)`
  - Vyhľadáva lety podľa prieletu, destinácie a dátumu.
  - Vráti zodpovedajúce lety pre rezerváciu.
  - Podporuje null parametre pre flexibilné filtrovanie.

#### Schedule
- **Účel**: Zoskupuje lety podľa dátumu alebo časového slotu.
- **Kľúčové atribúty**: scheduleId, date, flights (List).
- **Metódy**:
  - `najdiLety(String origin, String destination)`
  - `skontrolujDostupnost()`

#### Plane
- **Účel**: Reprezentuje lietadlo s kapacitou a typom.
- **Kľúčové atribúty**: planeId, type (PlaneType), capacity.
- **PlaneType enum**: PRIVATE, COMMERCIAL, MILITARY.
- **Metódy**:
  - `zadajTechnickeUdaje()`
  - `ulozRezervaciu()`
- **Registry**: statický `List<Plane>` uchováva lietadlá v pamäti.

### 2.3 Systém správy lístkov
**Triedy: Ticket, Seat, CheckIn, Baggage, Meal**

#### Ticket (centrálne jadro)
- **Účel**: Kombinuje let, sedadlo, check-in, batožinu a stravu.
- **Kľúčové atribúty**: ticketId, price, seat, checkin, baggage (List), meals (List).
- **Vzťahy**:
  - 1 Ticket ↔ 1 Seat
  - 1 Ticket ↔ 1 CheckIn
  - 1 Ticket ↔ 0..* Baggage
  - 1 Ticket ↔ 0..* Meal
- **Metódy**:
  - `spracujPlatbu()`
  - `ulozDoLetenky()`
  - `najdiListky(...)`

#### Seat
- **Účel**: Spravuje rezerváciu sedadla a cenu.
- **Atribúty**: seatId, available (boolean), type (SeatType).
- **SeatType enum**: ECONOMY, BUSINESS, PREMIUM.
- **Metóda**: `zistiCenu()`.

#### CheckIn
- **Účel**: Zaznamenáva stav odbavenia.
- **Atribúty**: checkinId, date.
- **Metóda**: `ulozDoLetenky(Ticket ticket)`.

#### Baggage / Meal
- **BaggageType**: BACKPACK, SUITCASE, TROLLEY.
- **MealType**: BEEF_AND_RICE, PORK_AND_EGGS, CHICKEN_AND_NOODLES.

### 2.4 Správa parkovania
**Trieda: Parking**
- **Účel**: Spravuje rezervácie parkovania lietadiel.
- **Atribúty**: parkingId, dateFrom, dateTo, plane.
- **Metóda**: `overDostupnost()`.
- **Registry**: statický zoznam zabraňuje konfliktom.

---

## 3. Implementované funkcie a spracovanie obchodnej logiky

### Vzor validácie údajov
Všetky modely používajú metódy typu `overUdaje()`:
- kontrola null pre povinné polia
- rozsahové kontrolky (kladné IDs, hmotnosti, ceny)
- kontrola enumerácií

### Príklad obchodných tokov

**Tok nákupu lístka**
1. Používateľ volá `nakupLetenku(...)` na entite `User`
2. Rezervuje sedadlo cez `seat.ulozRezervaciu()`
3. Pridáva batožinu cez `baggage.ulozBatozinu()`
4. Pridáva stravu cez `meal.ulozDoLetenky()`
5. Zrealizuje platbu cez `ticket.spracujPlatbu()`

**Tok vyhľadávania letov**
1. Používateľ volá `schedule.najdiLety(origin, destination)`
2. Filtruje výsledky podľa dátumu cez `flight.najdiLety(...)`
3. Overí kapacitu a dostupnosť cez `schedule.skontrolujDostupnost()`

### Metódy filtrovania
- null-safe filtrovanie pre flexibilitu používateľského vyhľadávania
- viackriteriálne dotazy v `Ticket.najdiListky(...)`

---

## 4. Kľúčové návrhové rozhodnutia

- statické registry = jednoduché pre vývojovú fázu
- agregácia entít = Ticket spravuje životný cyklus súvisiacich komponentov
- enumy = zjednodušený typový systém a kontrola hodnôt
- jednotný navrhovací vzor pre validáciu
- architektúra podobná MVC so silným modelom

---


### 5.2 Vzťah k architektúre
- Ticket a User sú definované v modeli, čo spĺňa architektúru s jasnou separáciou entít.
- Použitie statického registry v `Ticket` je jednoduchý forma perzistencie v rámci modelovej vrstvy.
- Logiku platby/rezervácie držíme v modeli (Domain Model) a volania chodu aplikácie môžu pôsobiť z kontrolleru/servisu.

### 5.3 Prípad použitia
Prípad použitia: **Rezervovať lístok**
1. Používateľ potvrdí parametre (lét, sedadlo, cena)
2. Aplikačná vrstva vytvorí objekt `Ticket`
3. Volá metódu `spracujPlatbu()` → rezervuje sedadlo a uloží lístok
4. Ak platba prebehne, `User.nakupLetenku()` pridá lístok do používateľa
5. Systém vypíše úspešné potvrdenie alebo chybu


---

## 6. Overenie plnenia hodnotenia

- Kód je spustiteľný v IntelliJ IDEA 2025.3.2 (Java 17+)
- Dokumentácia je v slovenčine a obsahuje vysvetlenie vzťahu k architektúre a prípadom použitia
- Zachované sú zásady OOP, separácia zodpovedností a testovateľnosť

## 7. Možné vylepšenia
1. Premiestnenie databázy: statické registre → JPA/Hibernate (H2 alebo PostgreSQL)
2. Vytvorenie služby `TicketService` pre transakčný model
3. Pridanie REST API vrstvy (Spring Boot)
4. Implementácia overovania používateľa/rolí (bezpečnosť)
5. Pridanie testov (JUnit 5) pre Ticket/User prípad použitia
