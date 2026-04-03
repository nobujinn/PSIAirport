package model;

import enums.PlaneType;
import java.util.ArrayList;
import java.util.List;

public class Plane {

    private int capacity;
    private int planeId;
    private PlaneType type;

    private static final List<Plane> register = new ArrayList<>();

    public Plane() {
    }

    public Plane(int planeId, PlaneType type, int capacity) {
        this.planeId = planeId;
        this.type = type;
        this.capacity = capacity;
    }

    // -------------------- Getters & Setters --------------------

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPlaneId() {
        return planeId;
    }

    public void setPlaneId(int planeId) {
        this.planeId = planeId;
    }

    public PlaneType getType() {
        return type;
    }

    public void setType(PlaneType type) {
        this.type = type;
    }

    // -------------------- Business Methods --------------------

    public static Plane najdiLietadlo(int index) {
        return register.get(index);
    }

    public void odstranLietadlo() {
        register.remove(this);
    }

    public void ulozLietadlo() {
        register.add(this);
    }

    public void overUdaje() {
        if (capacity <= 0) {
            throw new IllegalStateException("Kapacita lietadla musí byť kladná.");
        }
        if (type == null) {
            throw new IllegalStateException("Typ lietadla nesmie byť prázdny.");
        }
    }

    public void zadajTechnickeUdaje() {
        overUdaje();
        ulozLietadlo();
    }

    public void ulozRezervaciu() {
        if (!register.contains(this)) {
            register.add(this);
        }
    }

    // -------------------- Object --------------------

    @Override
    public String toString() {
        return "Plane{" +
                "planeId=" + planeId +
                ", type=" + type +
                ", capacity=" + capacity +
                '}';
    }
}
