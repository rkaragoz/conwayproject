package model;

// Dit is de interface (de luisteraar) voor het Observer Pattern
public interface TickListener {

    // Elke klasse die naar de klok wil luisteren, MOET deze methode implementeren
    // Input: 'tickNumber' (de huidige ronde die de klok meestuurt)
    void onTick(long tickNumber);
}