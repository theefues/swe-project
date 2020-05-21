package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class LaunchController {

    @FXML
    private TextField usernameTextfield;

    @FXML
    private TextField otherUserText;

    @FXML
    private Label errorLabel;

    public void startAction(ActionEvent actionEvent) throws IOException {
        if (usernameTextfield.getText().isEmpty()) {
            errorLabel.setText("* User name is empty!");
        }else if (otherUserText.getText().isEmpty()) {
            errorLabel.setText("* Other user name is empty!");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().initdata(usernameTextfield.getText(), otherUserText.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            log.info("Username is set to {}, other user name is set to {}, loading game scene.", usernameTextfield.getText(), otherUserText.getText());
        }

    }
}
