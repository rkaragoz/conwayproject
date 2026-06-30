import model.GameBoard;
import model.GameClock;
import view.MainView;
import controller.GameController;

public class Main {
    public static void main(String[] args) {
        // Instantieer alle MVC lagen
        GameBoard modelBoard = new GameBoard();
        GameClock modelClock = new GameClock();
        MainView view = new MainView(modelBoard);

        // De controller koppelt ze aan elkaar
        new GameController(modelBoard, modelClock, view);

        // Start het scherm
        view.setVisible(true);
    }
}