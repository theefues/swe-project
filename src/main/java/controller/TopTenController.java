package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import queen.results.GameResult;
import queen.results.GameResultDao;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
/**
 * Controller class for result screen.
 */
public class TopTenController {

    @FXML
    private TableView<GameResult> toptenTable;

    @FXML
    private TableColumn<GameResult, String> player;

    @FXML
    private TableColumn<GameResult, String> otherPlayer;

    @FXML
    private TableColumn<GameResult, String> winnerPlayer;

    @FXML
    private TableColumn<GameResult, Integer> steps;

    @FXML
    private TableColumn<GameResult, Duration> duration;

    @FXML
    private TableColumn<GameResult, ZonedDateTime> created;

    private GameResultDao gameResultDao;

    /**
     * Go back to the main menu.
     * @param actionEvent Event of the action
     * @throws IOException Error if file not found
     */
    public void back(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/launch.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        log.info("Loading launch scene.");
    }

    /**
     * Initialize all the variables and store them in a property for display.
      */
    @FXML
    public void initialize() {
        gameResultDao = GameResultDao.getInstance();

        List<GameResult> toptenList = gameResultDao.findBest(10);

        //Set cell informations from GameResult class
        player.setCellValueFactory(new PropertyValueFactory<>("player"));
        otherPlayer.setCellValueFactory(new PropertyValueFactory<>("otherPlayer"));
        winnerPlayer.setCellValueFactory(new PropertyValueFactory<>("winnerPlayer"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        created.setCellValueFactory(new PropertyValueFactory<>("created"));


        duration.setCellFactory(column -> {
            TableCell<GameResult, Duration> cell = new TableCell<GameResult, Duration>() {

                @Override
                protected void updateItem(Duration item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(DurationFormatUtils.formatDuration(item.toMillis(),"H:mm:ss"));
                    }
                }
            };

            return cell;
        });

        created.setCellFactory(column -> {
            TableCell<GameResult, ZonedDateTime> cell = new TableCell<GameResult, ZonedDateTime>() {
                private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss Z");

                @Override
                protected void updateItem(ZonedDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(item.format(formatter));
                    }
                }
            };

            return cell;
        });

        ObservableList<GameResult> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(toptenList);

        toptenTable.setItems(observableResult);
    }

}
