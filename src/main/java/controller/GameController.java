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
import rollingcubes.results.GameResult;
import rollingcubes.results.GameResultDao;
import rollingcubes.state.RollingCubesState;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class GameController {

    private RollingCubesState gameState;
    private String userName;
    private int stepCount;
    private List<Image> cubeImages;
    private Instant beginGame;

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


    private void drawGameState() {
        stepLabel.setText(String.valueOf(stepCount));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ImageView view = (ImageView) gameGrid.getChildren().get(i * 3 + j);
                view.setImage(cubeImages.get(gameState.getTray()[i][j].getValue()));
            }
        }
    }

    public void initdata(String userName) {
        this.userName = userName;
        usernameLabel.setText("Current user: " + this.userName);
    }

    @FXML
    public void initialize() {

        gameResultDao = GameResultDao.getInstance();

        gameState = new RollingCubesState();

        stepCount = 0;

        beginGame = Instant.now();

        cubeImages = Arrays.asList(
                new Image(getClass().getResource("/pictures/cube0.png").toExternalForm()),
                new Image(getClass().getResource("/pictures/cube1.png").toExternalForm()),
                new Image(getClass().getResource("/pictures/cube2.png").toExternalForm()),
                new Image(getClass().getResource("/pictures/cube3.png").toExternalForm()),
                new Image(getClass().getResource("/pictures/cube4.png").toExternalForm()),
                new Image(getClass().getResource("/pictures/cube5.png").toExternalForm()),
                new Image(getClass().getResource("/pictures/cube6.png").toExternalForm())
        );

        drawGameState();
    }



    public void cubeClick(MouseEvent mouseEvent) {


        int clickedColumn = GridPane.getColumnIndex((Node)mouseEvent.getSource());
        int clickedRow = GridPane.getRowIndex((Node)mouseEvent.getSource());

        if (!gameState.isSolved() && gameState.canRollToEmptySpace(clickedRow, clickedColumn)) {
            stepCount++;
            gameState.rollToEmptySpace(clickedRow, clickedColumn);

            if (gameState.isSolved()) {
                log.info("Player {} solved the game in {} steps.", userName, stepCount);
                solvedLabel.setText("You solved the puzzle!");
                doneButton.setText("Finish");

                gameResultDao.persist(getResult());
            }
        }

        drawGameState();
    }

    public void resetGame(ActionEvent actionEvent) {
        gameState = new RollingCubesState();
        stepCount = 0;
        solvedLabel.setText("");
        drawGameState();
        beginGame = Instant.now();
        log.info("Game reset.");
    }

    private GameResult getResult() {

        GameResult result = GameResult.builder()
                                    .player(userName)
                                    .solved(gameState.isSolved())
                                    .duration(Duration.between(beginGame, Instant.now()))
                                    .steps(stepCount)
                                    .build();
        return result;
    }

    public void finishGame(ActionEvent actionEvent) throws IOException {
        if (!gameState.isSolved()) {
            gameResultDao.persist(getResult());
        }

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/topten.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        log.info("Finished game, loading Top Ten scene.");
    }
}
