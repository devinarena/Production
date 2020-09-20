package io.github.imunsmart;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.sql.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


/**
 * The main controller class for our JavaFX Production application Currently handles H2 database,
 * SQL and events from our UI
 *
 * @author Devin
 * @version 1.0
 * @since 9/19/2020
 */
public class Controller {

  // In order for inspect code to show no errors i had to implement all of these fields

  // Product Line Tab
  @FXML
  private Tab tabProductLine;
  @FXML
  private TextField tfProductName;
  @FXML
  private TextField tfManufacturer;
  @FXML
  private ChoiceBox<String> chbItemType;
  @FXML
  private Button btnAddProduct;
  @FXML
  private TableView<String> tblvwExistingProducts;

  // Produce Tab
  @FXML
  private Tab tabProduce;
  @FXML
  private ComboBox<Integer> cmbxChooseQuantity;
  @FXML
  private ListView<String> lstvwChooseProduct;
  @FXML
  private Button btnRecordProduction;

  // Production Log Tab
  @FXML
  private Tab tabProductionLog;
  @FXML
  private TextArea taProductionLog;

  private Connection conn;

  /**
   * Event when add product button is pressed, inserts values from UI into H2 database using SQL.
   *
   * @param event ActionEvent to handle, the parameter gives an unused warning but it really should be there
   */
  @FXML
  public void addProduct(@SuppressWarnings("unused") ActionEvent event) {
    // Get data from UI fields
    String productName = tfProductName.getText();
    String manufacturer = tfManufacturer.getText();
    String itemType = chbItemType.getValue();

    try {
      // SQL to insert a product into the DB
      String sql = "INSERT INTO Product(type, manufacturer, name) VALUES ( ?, ?, ? )";

      // Create a prepared statement from connection and set values to UI field values
      PreparedStatement prst = conn.prepareStatement(sql);
      // This is the only way to remove the FindBugs magic number bug
      final int itemTypeIndex = 1;
      final int manufacturerIndex = 2;
      final int productNameIndex = 3;
      prst.setString(itemTypeIndex, itemType);
      prst.setString(manufacturerIndex, manufacturer);
      prst.setString(productNameIndex, productName);

      // Execute and close the statement
      prst.execute();
      prst.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    try {
      // Get every product from the Product tab
      String sql = "SELECT * FROM Product";
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);

      // Print them into the console
      while (rs.next()) {
        String type = rs.getString("TYPE");
        String manufact = rs.getString("MANUFACTURER");
        String name = rs.getString("NAME");

        System.out.println(type + " - " + manufact + " - " + name);
      }

      // Close the statement
      stmt.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Event when record production button is pressed, inserts values from UI into H2 database using
   * SQL.
   *
   * @param event ActionEvent to handle, the parameter gives an unused warning but it really should be there
   */
  @FXML
  public void recordProduction(@SuppressWarnings("unused") ActionEvent event) {
    System.out.println("Record Production button pressed!");
  }

  /**
   * Main initialization method
   */
  public void initialize() {
    connectToDB();

    // Product Line - Init the item type choice box, populates with 4 different possible item types and places default
    chbItemType.getItems().addAll("VIDEO", "AUDIO", "MECHANICAL", "MISC");
    chbItemType.getSelectionModel().selectLast();

    // Produce - Init the choose quantity combo box, populates with 1-10 and allows editing and places default
    final int numItems = 10;
    for (int i = 1; i <= numItems; i++) {
      cmbxChooseQuantity.getItems().add(i);
    }
    cmbxChooseQuantity.setEditable(true);
    cmbxChooseQuantity.getSelectionModel().selectFirst();
  }

  /**
   * Initializes JDBC driver and connects to H2 database, initializes the connection field with URL,
   * USERNAME and PASSWORD
   */
  private void connectToDB() {
    // I had to change all of these to get 0 bugs on findbugs
    final String jdbcDriver = "org.h2.Driver";
    final String dbURL = "jdbc:h2:./res/ProductionDB";

    //  Database credentials
    final String user = "";
    final String pass = "";

    try {
      // Initialize the driver
      Class.forName(jdbcDriver);

      // Connect to the database using credentials
      conn = DriverManager.getConnection(dbURL, user, pass);
    } catch (ClassNotFoundException | SQLException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Closes the connection when the program is exited.
   */
  public void disconnect() {
    try {
      conn.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
