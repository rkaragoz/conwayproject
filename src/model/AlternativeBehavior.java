package model;

// Dit is een strategie-klasse voor de alternatieve spelregels (Strategy Pattern)
public class AlternativeBehavior implements CellBehavior {

    // Deze methode controleert of een levende cel mag blijven leven
    @Override
    public boolean shouldSurvive(int total) {
        // Een cel overleeft als hij tussen de 2 en 4 buren heeft (inclusief 2 en 4)
        return total >= 2 && total <= 4;
    }

    // Deze methode controleert of er op een leeg vakje een nieuwe cel geboren wordt
    @Override
    public boolean shouldBeBorn(int typeN) {
        // Er wordt alleen een cel geboren als er exact 4 buren van dit type omheen staan
        return typeN == 4;
    }

    // Dit is een getter die het type van deze regels teruggeeft
    @Override
    public CellType getType() {
        // Laat de buitenwereld weten dat dit de ALTERNATIVE variant is
        return CellType.ALTERNATIVE;
    }
}