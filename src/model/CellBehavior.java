package model;

// Dit is de interface (het contract) voor de spelregels van een cel
public interface CellBehavior {

    // Dit dwingt af dat elk gedrag moet kunnen berekenen of een cel overleeft
    // Input: het totale aantal levende buren rondom de cel
    boolean shouldSurvive(int totalNeighbors);

    // Dit dwingt af dat elk gedrag moet kunnen berekenen of een cel geboren wordt
    // Input: het aantal buren van DIT specifieke type cel
    boolean shouldBeBorn(int typeNeighbors);

    // Dit dwingt af dat elk gedrag zijn eigen CellType (enum) moet kunnen teruggeven
    CellType getType();
}