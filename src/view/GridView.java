package view;

import model.Cell;
import model.CellType;
import model.GameBoard;

import javax.swing.*;
import java.awt.*;

// Dit is het visuele paneel (JPanel) dat het raster en de cellen op het scherm tekent (de View)
public class GridView extends JPanel {
    // Koppeling naar de data (het bord) om te weten welke cellen leven
    private final GameBoard board;

    // De camera-verschuiving (bepaalt welk deel van het 1000x1000 bord je ziet)
    private int offsetX = 450;
    private int offsetY = 450;

    // Grootte van één vakje in pixels (15x15 pixels)
    private final int cellSize = 15;

    // De constructor: koppelt het bord en zet de achtergrond op wit
    public GridView(GameBoard board) {
        this.board = board;
        setBackground(Color.WHITE);
    }

    // De belangrijkste methode: deze tekent het hele scherm telkens opnieuw
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Zorgt dat Java eerst netjes de achtergrond wist

        // Bereken hoeveel kolommen en rijen er op het huidige scherm passen
        int cols = getWidth() / cellSize;
        int rows = getHeight() / cellSize;

        // STAP 1: Teken de grijze rasterlijnen (het grid)
        g.setColor(Color.LIGHT_GRAY);
        // Verticale lijnen
        for (int i = 0; i <= cols; i++) g.drawLine(i * cellSize, 0, i * cellSize, getHeight());
        // Horizontale lijnen
        for (int j = 0; j <= rows; j++) g.drawLine(0, j * cellSize, getWidth(), j * cellSize);

        // STAP 2: Loop door alle zichtbare vakjes op het scherm om cellen te tekenen
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                // Tel de camera-verschuiving (offset) op bij het scherm-coördinaat
                int gridX = offsetX + x;
                int gridY = offsetY + y;

                // Haal de cel op uit het model (GameBoard)
                Cell cell = board.getCell(gridX, gridY);

                // Als er een cel staat én hij leeft, gaan we hem inkleuren
                if (cell != null && cell.isAlive()) {
                    // Als het een Conway-cel is -> teken een BLAUW VIERKANT
                    if (cell.getBehavior().getType() == CellType.CONWAY) {
                        g.setColor(Color.ORANGE);
                        g.fillRect(x * cellSize + 1, y * cellSize + 1, cellSize - 1, cellSize - 1);
                    } else {
                        // Als het een Alternatieve cel is -> teken een RODE CIRKEL (Oval)
                        g.setColor(Color.MAGENTA);
                        g.fillOval(x * cellSize + 1, y * cellSize + 1, cellSize - 1, cellSize - 1);
                    }
                }
            }
        }
    }

    // Hulpmethode: rekent de X-positie van je muisklik om naar het echte X-coördinaat op het bord
    public int getGridX(int mouseX) { return offsetX + (mouseX / cellSize); }

    // Hulpmethode: rekent de Y-positie van je muisklik om naar het echte Y-coördinaat op het bord
    public int getGridY(int mouseY) { return offsetY + (mouseY / cellSize); }

    // Methode om de camera te verplaatsen (pijltjestoetsen of slepen) en grenzen te bewaken
    public void moveOffset(int dx, int dy) {
        // Zorg dat de camera nooit buiten het 1000x1000 bord kan schuiven
        this.offsetX = Math.max(0, Math.min(1000 - (getWidth()/cellSize), offsetX + dx));
        this.offsetY = Math.max(0, Math.min(1000 - (getHeight()/cellSize), offsetY + dy));

        // Geef Java een seintje dat het scherm direct opnieuw getekend moet worden (paintComponent)
        repaint();
    }
}