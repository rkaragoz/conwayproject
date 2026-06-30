package model;

import java.util.HashMap;
import java.util.Map;

// Dit is het bord waarop het spel zich afspeelt. Het luistert naar de klok (TickListener)
public class GameBoard implements TickListener {
    // De afmetingen van het oneindige/grote speelveld (1000x1000)
    private final int width = 1000;
    private final int height = 1000;

    // De 'grid' waarin we ALLEEN de levende cellen opslaan met een unieke String-sleutel ("x,y")
    private Map<String, Cell> grid = new HashMap<>();

    // We maken de twee strategieën hier alvast één keer aan (hergebruik/efficiëntie)
    private final CellBehavior conway = new ConwayBehavior();
    private final CellBehavior alternative = new AlternativeBehavior();

    // Handige hulpmethode om van een X en Y coördinaat een String-sleutel te maken (bijv. "5,3")
    public String getKey(int x, int y) {
        return x + "," + y;
    }

    // Methode om handmatig een cel op het bord te plaatsen of te verwijderen (bijv. door te klikken)
    public void setCell(int x, int y, CellType type) {
        // Check of de klik wel binnen het bord valt, anders doen we niks
        if (x < 0 || x >= width || y < 0 || y >= height) return;

        if (type == null) {
            // Als het type null is, halen we de cel weg (leegmaken)
            grid.remove(getKey(x, y));
        } else {
            // Kijk welk type cel we moeten maken en koppel het juiste gedrag
            CellBehavior b = (type == CellType.CONWAY) ? conway : alternative;
            // Stop de nieuwe, levende cel in onze HashMap
            grid.put(getKey(x, y), new Cell(b, true));
        }
    }

    // Getter om een specifieke cel op te halen op basis van X en Y
    public Cell getCell(int x, int y) {
        return grid.get(getKey(x, y));
    }

    // HIER GEBEURT HET: Elke keer als de klok tikt, berekent deze methode de volgende ronde
    @Override
    public void onTick(long tickNumber) {
        // Drie hulp-kaarten om bij te houden hoeveel buren elk vakje om zich heen heeft
        Map<String, Integer> conwayCount = new HashMap<>();
        Map<String, Integer> altCount = new HashMap<>();
        Map<String, Integer> totalCount = new HashMap<>();

        // STAP 1: Loop door alle cellen die NU leven en geef hun 8 buren een 'plus 1'
        for (String key : grid.keySet()) {
            // Haal de X en Y weer uit de String-sleutel ("5,3" -> x=5, y=3)
            String[] p = key.split(",");
            int cx = Integer.parseInt(p[0]);
            int cy = Integer.parseInt(p[1]);
            Cell cell = grid.get(key);

            // Dubbele for-loop om langs alle 8 de vakjes rondom de cel te gaan (-1, 0, 1)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    // Sla de cel zelf over, we tellen alleen de buren rondom hem!
                    if (dx == 0 && dy == 0) continue;

                    int nx = cx + dx; int ny = cy + dy;
                    // Check of de buur niet buiten de randen van het bord valt
                    if (nx < 0 || nx >= width || ny < 0 || ny >= height) continue;

                    String nKey = getKey(nx, ny);
                    // Geef dit buurvakje +1 in de totale burenteller
                    totalCount.put(nKey, totalCount.getOrDefault(nKey, 0) + 1);

                    // Geef specifiek +1 aan het type buur (Conway of Alternatief) voor de geboorteregels
                    if (cell.getBehavior().getType() == CellType.CONWAY) {
                        conwayCount.put(nKey, conwayCount.getOrDefault(nKey, 0) + 1);
                    } else {
                        altCount.put(nKey, altCount.getOrDefault(nKey, 0) + 1);
                    }
                }
            }
        }

        // We maken een tijdelijk nieuw bord aan voor de volgende ronde
        Map<String, Cell> nextGrid = new HashMap<>();

        // STAP 2: Controleer alle cellen die NU al leven. Mogen zij overleven?
        for (Map.Entry<String, Cell> entry : grid.entrySet()) {
            String key = entry.getKey();
            Cell cell = entry.getValue();
            // Bereken de transitie op basis van het totale aantal buren
            cell.prepareTransition(totalCount.getOrDefault(key, 0), false);
            cell.commitTransition(); // Voer de statuswijziging nu écht door

            // Als de cel de ronde overleeft heeft, stoppen we hem in het nieuwe bord
            if (cell.isAlive()) nextGrid.put(key, cell);
        }

        // STAP 3: Controleer alle LEGE vakjes die buren hebben. Wordt hier een cel geboren?
        for (String key : totalCount.keySet()) {
            // Als er al een levende cel stond, hebben we die hierboven al gecheckt, dus overslaan
            if (grid.containsKey(key)) continue;

            // Vraag aan de strategieën of er een geboorte mag plaatsvinden
            boolean cBorn = conway.shouldBeBorn(conwayCount.getOrDefault(key, 0));
            boolean aBorn = alternative.shouldBeBorn(altCount.getOrDefault(key, 0));

            // Als er een Conway cel geboren wordt, zet hem op het nieuwe bord
            if (cBorn) nextGrid.put(key, new Cell(conway, true));
                // Als er een Alternatieve cel geboren wordt, zet hem op het nieuwe bord
            else if (aBorn) nextGrid.put(key, new Cell(alternative, true));
        }

        // STAP 4: Vervang het oude bord definitief door het gloednieuwe berekende bord
        this.grid = nextGrid;
    }

    // Methode om te tellen hoeveel cellen van een bepaald type nu op het bord staan (voor statistiek)
    public long countCells(CellType type) {
        // We streamen door alle cellen en filteren op het gevraagde type
        return grid.values().stream().filter(c -> c.getBehavior().getType() == type).count();
    }

    // Maakt het hele bord leeg (wist alle cellen uit de HashMap)
    public void reset() {
        grid.clear();
    }

    // Getters voor de breedte en hoogte van het bord
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}