package model;

// Dit is de klasse voor een individuele cel op het speelbord
public class Cell {
    // De specifieke spelregels (strategie) die deze cel gebruikt (Conway of Alternatief)
    private final CellBehavior behavior;

    // Statusvariabelen: leeft de cel NU, en leeft de cel in de VOLGENDE ronde?
    private boolean isAlive;
    private boolean nextAlive;

    // DE CONSTRUCTOR: Maakt een nieuwe cel aan met een bepaald gedrag en startstatus
    public Cell(CellBehavior behavior, boolean isAlive) {
        this.behavior = behavior;
        this.isAlive = isAlive;
    }

    // GETTER: Geeft het gedrag/de spelregels van deze cel terug
    public CellBehavior getBehavior() {
        return behavior;
    }

    // GETTER (Boolean): Controleert of de cel op dit moment leeft
    public boolean isAlive() {
        return isAlive;
    }

    // ACTIE-METHODE (Void met parameters): Berekent de volgende status op basis van de buren
    public void prepareTransition(int totalNeighbors, boolean cellTypeBorn) {
        if (isAlive) {
            // Als de cel nu leeft, vragen we aan de strategie of hij overleeft
            this.nextAlive = behavior.shouldSurvive(totalNeighbors);
        } else {
            // Als de cel nu dood is, horen we van het GameBoard of er een geboorte plaatsvindt
            this.nextAlive = cellTypeBorn;
        }
    }

    // ACTIE-METHODE (Of State-Updater): Voert de daadwerkelijke statuswijziging door
    public void commitTransition() {
        // Nu pas overschrijven we de huidige status met de vooraf berekende status
        this.isAlive = this.nextAlive;
    }
}