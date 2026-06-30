package model;

// Dit is de strategie-klasse voor de originele Conway-spelregels (Strategy Pattern)
public class ConwayBehavior implements CellBehavior {

    // Deze methode controleert of een levende Conway-cel mag blijven leven
    @Override
    public boolean shouldSurvive(int total) {
        // Een Conway-cel overleeft ALLEEN als hij exact 2 OF exact 3 buren heeft
        return total == 2 || total == 3;
    }

    // Deze methode controleert of er op een leeg vakje een nieuwe Conway-cel geboren wordt
    @Override
    public boolean shouldBeBorn(int typeN) {
        // Er wordt alleen een cel geboren als er exact 3 Conway-buren omheen staan
        return typeN == 3;
    }

    // Dit is de getter die het type van deze regels teruggeeft
    @Override
    public CellType getType() {
        // Laat de buitenwereld weten dat dit de CONWAY variant is
        return CellType.CONWAY;
    }
}