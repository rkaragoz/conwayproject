package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// Dit is de klok van het spel die de 'ticks' regelt (de Observable in het Observer Pattern)
public class GameClock {
    // Houdt bij de hoeveelste ronde (tick) het spel nu is
    private long tickNumber = 0;

    // De lijst met alle 'abonnees' (zoals het GameBoard) die een seintje willen als de klok tikt
    private final List<TickListener> subscribers = new ArrayList<>();

    // De Java Timer die op de achtergrond de herhaling uitvoert
    private Timer timer;

    // Statusvariabele om bij te houden of het spel loopt of op pauze staat
    private boolean isRunning = false;

    // De snelheid van de klok in milliseconden (300 ms per stap)
    private long delay = 300;

    // Methode waarmee objecten (luisteraars) zich kunnen aanmelden voor de klok-tik
    public void subscribe(TickListener l) { subscribers.add(l); }

    // Start de klok. 'synchronized' zorgt dat er geen twee klokken tegelijk starten bij dubbelklikken
    public synchronized void start() {
        // Als het spel al loopt, hoeven we niks te doen
        if (isRunning) return;
        isRunning = true;

        // Maak een nieuwe achtergrond-timer aan
        timer = new Timer(true);
        // Start een taak die zich telkens herhaalt met de ingestelde snelheid ('delay')
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Verhoog de rondeteller met 1
                tickNumber++;
                // Loop door alle abonnees heen en geef ze de opdracht: voer je 'onTick' actie uit!
                for (TickListener s : subscribers) s.onTick(tickNumber);
            }
        }, 0, delay);
    }

    // Zet het spel op pauze
    public synchronized void pause() {
        isRunning = false;
        // Stop de achtergrond-timer als die bestaat
        if (timer != null) timer.cancel();
    }

    // Zet de klok volledig terug naar het begin
    public void reset() {
        pause(); // Eerst pauzeren
        this.tickNumber = 0; // Rondeteller terug naar 0
    }

    // Past de snelheid van het spel aan (bijv. met een schuifbalk in de GUI)
    public void setSpeed(long delay) {
        // Zorg dat de klok nooit sneller gaat dan 50ms om crashes te voorkomen
        this.delay = Math.max(50, delay);
        // Als het spel al liep, herstart de klok direct met de nieuwe snelheid
        if (isRunning) { pause(); start(); }
    }

    // Getter om te zien bij welke ronde we zijn
    public long getTickNumber() { return tickNumber; }

    // Getter om te checken of het spel momenteel loopt
    public boolean isRunning() { return isRunning; }
}