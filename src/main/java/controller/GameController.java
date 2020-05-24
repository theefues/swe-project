package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import queen.results.GameResult;
import queen.results.GameResultDao;
import queen.state.Direction;
import queen.state.Table;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;


@Slf4j
/**
 * Controller class for the game.
 */
public class GameController {

    private Table gameTable;
    private String userName;
    private String otherUserName;
    private int stepCount;
    private Image blueQueenImage;
    private Image redQueenImage;
    private Image blankImage;
    private Image availableImage;
    private Instant beginGame;
    private int availableSteps;
    private boolean hasFinished;

    private GameResultDao gameResultDao;

    @FXML
    private Label usernameLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label stepLabel;

    @FXML
    private Label solvedLabel;

    @FXML
    private Button doneButton;

    /**
     * Drawing the game's table's base. Put to a white square to every field in a NxN grid.
     * Default value is 8.
     */
    private void drawGameState() {
        stepLabel.setText(String.valueOf(stepCount));
        availableSteps = 0;
        int n = 8;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ImageView view = (ImageView) gameGrid.getChildren().get(i * n + j);
                int index = gameTable.get(i, j);
                if (index != 0) {
                    view.setImage(index == 1 ? blueQueenImage : redQueenImage);
                } else {
                    if (!gameTable.isSolved() && gameTable.canMoveQueenTo(i, j, gameTable.getCurrentIndex()) != Direction.NONE) {
                        view.setImage(availableImage);
                        availableSteps++;
                    } else {
                        view.setImage(blankImage);
                    }
                }
            }
        }

        if (availableSteps == 0 && stepCount > 0) {
            log.warn("No avaliable steps, finishing game...");
            showFinish();
        }
    }

    /**
     * Initialize the usernames with the given strings.
     * @param userName Name of the first player
     * @param otherUserName Name of the second player
     */
    public void initdata(String userName, String otherUserName) {
        this.userName = userName;
        this.otherUserName = otherUserName;
        usernameLabel.setText("Current user: " + this.userName + " vs " + this.otherUserName);
    }

    /**
     * Get the images for blank square, red and blue queen pieces, green square.
     */
    @FXML
    public void initialize() {
        gameResultDao = GameResultDao.getInstance();
        blueQueenImage = new Image(getClass().getResource("/pictures/blue.png").toExternalForm());
        redQueenImage = new Image(getClass().getResource("/pictures/red.png").toExternalForm());
        blankImage = new Image(getClass().getResource("/pictures/blank.png").toExternalForm());
        availableImage = new Image(getClass().getResource("/pictures/available.png").toExternalForm());

        resetGame(null);
    }

    /**
     * What to do when mouse is clicked.
     * @param mouseEvent Event of the mouse
     */
    public void cubeClick(MouseEvent mouseEvent) {
        int clickedColumn = GridPane.getColumnIndex((Node)mouseEvent.getSource());
        int clickedRow = GridPane.getRowIndex((Node)mouseEvent.getSource());

        log.info("Try click to {}-{}", clickedRow, clickedColumn);
        if (gameTable.tryMoveQueenTo(clickedRow, clickedColumn, gameTable.getCurrentIndex())) {
            stepCount++;

            gameTable.setCurrent(gameTable.getCurrentIndex() == 1 ? 2 : 1); //Change users

            if (gameTable.isSolved()) {
                showFinish();
            }
        }

        drawGameState();
    }

    /**
     * Show winner message on screen and in log.
     */
    private void showFinish() {
        log.info("Player {} solved the game in {} steps.", userName, stepCount);
        solvedLabel.setText((gameTable.getWinnerIndex() == 1 ? userName : otherUserName) + " solved the puzzle!");
        doneButton.setText("Finish");
        hasFinished = true;
    }

    /**
     * Reset the game state.
     * @param actionEvent Event of the action
     */
    public void resetGame(ActionEvent actionEvent) {
        gameTable = new Table(8, 2);
        gameTable.setCurrent(1);
        stepCount = 0;
        hasFinished = false;
        solvedLabel.setText("");
        drawGameState();
        beginGame = Instant.now();
        log.info("Game reset.");
    }

    /**
     * Build the game results with Lombok.
     * @return Builded game result
     */
    private GameResult getResult() {
        GameResult result = GameResult.builder()
                                    .player(userName)
                                    .otherPlayer(otherUserName)
                                    .winnerPlayer(gameTable.getWinnerIndex() == 1 ? userName : otherUserName)
                                    .solved(hasFinished)
                                    .duration(Duration.between(beginGame, Instant.now()))
                                    .steps(stepCount)
                                    .build();
        return result;
    }

    /**
     * Finish the game and display the scoreboard when the Finish button pressed.
     * @param actionEvent Event of the action
     * @throws IOException Exception if file not found
     */
    public void finishGame(ActionEvent actionEvent) throws IOException {
        gameResultDao.persist(getResult());

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/topten.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        log.info("Finished game, loading Top Ten scene.");
    }
}
