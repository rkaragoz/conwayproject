package view;

import model.GameBoard;

import javax.swing.*;
import java.awt.*;

// Dit is het hoofdvenster van de applicatie (JFrame) waarin alle knoppen en het bord samenkomen
public class MainView extends JFrame {
    // We maken het tekenveld (GridView) aan als onderdeel van dit venster
    public GridView gridView;

    // De knoppen om het spel mee te besturen
    public JButton btnStart = new JButton("Start");
    public JButton btnPause = new JButton("Pause");
    public JButton btnReset = new JButton("Reset");
    public JButton btnFaster = new JButton("Sneller");
    public JButton btnSlower = new JButton("Langzamer");

    // De tekstlabels om de statistieken op het scherm te tonen
    public JLabel lblTicks = new JLabel("Ticks: 0  ");
    public JLabel lblConway = new JLabel("Conway: 0  ");
    public JLabel lblAlt = new JLabel("Alternatief: 0  ");

    // De constructor: bouwt het hele venster op
    public MainView(GameBoard board) {
        setTitle("Conway's Game of Life++"); // De titel bovenin de balk
        setSize(800, 700); // Het venster is 800 pixels breed en 700 pixels hoog
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Zorgt dat het programma écht stopt als je op het kruisje drukt
        setLocationRelativeTo(null); // Dit zorgt ervoor dat het venster netjes in het midden van je beeldscherm opstart

        // Maak het tekenveld aan en geef het bord (model) mee
        gridView = new GridView(board);

        // We gebruiken een BorderLayout (een lay-out met een Noord, Zuid, Oost, West en Centrum)
        setLayout(new BorderLayout());
        // Het grote speelveld stoppen we in het midden (dit vult automatisch alle overgebleven ruimte op)
        add(gridView, BorderLayout.CENTER);

        // Maak een apart paneel aan voor de knoppen (bovenin)
        JPanel controlPanel = new JPanel();
        controlPanel.add(btnStart); controlPanel.add(btnPause); controlPanel.add(btnReset);
        controlPanel.add(btnSlower); controlPanel.add(btnFaster);
        // Voeg het knoppenpaneel toe aan de bovenkant (North)
        add(controlPanel, BorderLayout.NORTH);

        // Maak een apart paneel aan voor de statistieken en de handleiding (onderin)
        JPanel statusPanel = new JPanel();
        statusPanel.add(lblTicks); statusPanel.add(lblConway); statusPanel.add(lblAlt);
        statusPanel.add(new JLabel("| Pijltjes = Scrollen. Klik = Conway, Rechtsklik = Alt, Ctrl+Klik = Wissen"));
        // Voeg het statuspaneel toe aan de onderkant (South)
        add(statusPanel, BorderLayout.SOUTH);
    }

    // Deze methode wordt vanuit de Controller aangeroepen om de cijfers op het scherm bij te werken
    public void updateStats(long ticks, long conway, long alt) {
        // SwingUtilities.invokeLater zorgt ervoor dat het updaten veilig gebeurt op de hoofd-thread van de GUI
        SwingUtilities.invokeLater(() -> {
            lblTicks.setText("Ticks: " + ticks + "  ");
            lblConway.setText("Conway: " + conway + "  ");
            lblAlt.setText("Alternatief: " + alt + "  ");
            // Teken direct ook het grid opnieuw met de nieuwste cellen
            gridView.repaint();
        });
    }
}