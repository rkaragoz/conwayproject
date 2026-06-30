package controller;

import model.*;
import view.MainView;
import javax.swing.SwingUtilities;
import java.awt.event.*;

// Dit is de controller: de lijm tussen het Model en de View (MVC-patroon)
public class GameController {
    private final GameBoard board;
    private final GameClock clock;
    private final MainView view;

    // Houdt de huidige snelheid van de klok bij (standaard 300 milliseconden per tik)
    private long currentDelay = 300;

    // De constructor: koppelt model en view aan elkaar en stelt de 'luisteraars' in
    public GameController(GameBoard board, GameClock clock, MainView view) {
        this.board = board;
        this.clock = clock;
        this.view = view;

        // KNOPPEN BESTUREN: Wat moet er gebeuren als je op de knoppen klikt?
        view.btnStart.addActionListener(e -> clock.start()); // Start de klok
        view.btnPause.addActionListener(e -> clock.pause()); // Zet de klok op pauze

        view.btnReset.addActionListener(e -> {
            clock.reset(); // Zet de klok-teller op 0
            board.reset(); // Maak het speelbord helemaal leeg (grid leegmaken)
            updateAll();   // Werk direct de cijfers op het scherm bij
        });

        view.btnFaster.addActionListener(e -> {
            // Maak de pauze korter (minimaal 50ms) zodat het spel SNELLER gaat
            currentDelay = Math.max(50, currentDelay - 50);
            clock.setSpeed(currentDelay);
        });

        view.btnSlower.addActionListener(e -> {
            // Maak de pauze langer zodat het spel LANGZAMER gaat
            currentDelay += 50;
            clock.setSpeed(currentDelay);
        });

        // MUISKLIKKEN OP HET RASTER: Cellen tekenen of wissen
        view.gridView.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Reken de pixel-positie van de muisklik om naar X en Y coördinaten op het bord
                int gx = view.gridView.getGridX(e.getX());
                int gy = view.gridView.getGridY(e.getY());

                if (e.isControlDown()) {
                    // Als je Ctrl ingedrukt houdt tijdens het klikken -> wis de cel (null)
                    board.setCell(gx, gy, null);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // Rechtsklik -> plaats een ALTERNATIEVE cel (MAGENTA)
                    board.setCell(gx, gy, CellType.ALTERNATIVE);
                } else {
                    // Normale klik (linksklik) -> plaats een CONWAY cel (ORANJE)
                    board.setCell(gx, gy, CellType.CONWAY);
                }
                updateAll(); // Update direct het scherm zodat de nieuwe cel te zien is
            }
        });

        // TOETSENBORD BESTUREN: Camera verschuiven (scrollen)
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Verschuif de camera (offset) met 2 vakjes per toetsaanslag
                if (e.getKeyCode() == KeyEvent.VK_LEFT) view.gridView.moveOffset(-2, 0);
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) view.gridView.moveOffset(2, 0);
                if (e.getKeyCode() == KeyEvent.VK_UP) view.gridView.moveOffset(0, -2);
                if (e.getKeyCode() == KeyEvent.VK_DOWN) view.gridView.moveOffset(0, 2);
            }
        });
        // Zorg dat het venster naar het toetsenbord 'luistert' (focus krijgt)
        view.setFocusable(true);

        // DE HOOFDLUS (Observer Pattern): Wat gebeurt er als de klok een tik geeft?
        clock.subscribe(tick -> {
            board.onTick(tick); // 1. Laat het bord de volgende ronde berekenen
            updateAll();        // 2. Update direct alle statistieken en teken het scherm opnieuw
        });
    }

    // Hulpmethode om in één keer alle teksten en cellen op het scherm te verversen
    private void updateAll() {
        view.updateStats(
                clock.getTickNumber(),
                board.countCells(CellType.CONWAY),
                board.countCells(CellType.ALTERNATIVE)
        );
    }
}