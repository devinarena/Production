package io.github.imunsmart;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {

    @FXML
    public void addProduct(ActionEvent event) {
        System.out.println("Add Product button pressed!");
    }

    @FXML
    public void recordProduction(ActionEvent event) {
        System.out.println("Record Production button pressed!");
    }

}
