package io.github.imunsmart;

import io.github.imunsmart.items.ItemType;
import io.github.imunsmart.items.Product;
import io.github.imunsmart.items.ProductionRecord;
import io.github.imunsmart.items.audio.AudioPlayer;
import io.github.imunsmart.items.visual.MonitorType;
import io.github.imunsmart.items.visual.MoviePlayer;
import io.github.imunsmart.items.visual.Screen;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * The main controller class for our JavaFX Production application Currently handles H2 database,
 * SQL and events from our UI.
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
  private TableView<Product> tblvwExistingProducts;

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

  private ObservableList<Product> productLine;
  private ArrayList<ProductionRecord> productionRun;

  /**
   * Event when add product button is pressed, inserts values from UI into H2 database using SQL.
   *
   * @param event ActionEvent to handle, the parameter gives an unused warning but it really should
   *              be there
   */
  @FXML
  public void addProduct(@SuppressWarnings("unused") ActionEvent event) {
    // Get data from UI fields
    String productName = tfProductName.getText();
    String manufacturer = tfManufacturer.getText();
    ItemType itemType = ItemType.valueOf(chbItemType.getValue());

    Product product = determineProduct(productName, manufacturer, itemType);

    if (product != null) {
      try {
        // SQL to insert a product into the DB
        String sql = "INSERT INTO Product(type, manufacturer, name) VALUES ( ?, ?, ? )";

        // Create a prepared statement from connection and set values to UI field values
        PreparedStatement prst = conn.prepareStatement(sql);
        // This is the only way to remove the FindBugs magic number bug
        final int itemTypeIndex = 1;
        final int manufacturerIndex = 2;
        final int productNameIndex = 3;
        prst.setString(itemTypeIndex, itemType.name());
        prst.setString(manufacturerIndex, manufacturer);
        prst.setString(productNameIndex, productName);

        // Execute and close the statement
        prst.execute();
        prst.close();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    }

    loadProductList();
  }

  /**
   * Event when record production button is pressed, inserts values from UI into H2 database using
   * SQL.
   *
   * @param event ActionEvent to handle, the parameter gives an unused warning but it really should
   *              be there
   */
  @FXML
  public void recordProduction(@SuppressWarnings("unused") ActionEvent event) {
    Product product = productLine.get(lstvwChooseProduct.getSelectionModel().getSelectedIndex());
    final int numDigits = 5;
    final int manufacturerIndex = 3;
    String serialNumber =
        product.getManufacturer().substring(0, manufacturerIndex) + product.getType().getCode()
            + String.format("%0" + numDigits + "d", product.getId());
    ProductionRecord productionRecord = new ProductionRecord(productionRun.size(), product.getId(),
        serialNumber, Timestamp.from(Instant.now()));

    try {
      // SQL to insert a product into the DB
      String sql =
          "INSERT INTO productionrecord(production_num, product_id, serial_num, date_produced) "
              + "VALUES ( ?, ?, ?, ? )";

      //This is to get rid of the find bugs error
      final int prodNumberIndex = 1;
      final int prodIdIndex = 2;
      final int serialNumIndex = 3;
      final int prodDateIndex = 4;
      // Create a prepared statement from connection and set values to UI field values
      PreparedStatement prst = conn.prepareStatement(sql);
      prst.setInt(prodNumberIndex, productionRecord.getProductionNumber());
      prst.setInt(prodIdIndex, productionRecord.getProductId());
      prst.setString(serialNumIndex, productionRecord.getSerialNumber());
      prst.setDate(prodDateIndex, new java.sql.Date(productionRecord.getProduced().getTime()));

      // Execute and close the statement
      prst.execute();
      prst.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    loadProductionLog();
  }

  /**
   * Main initialization method.
   */
  public void initialize() {
    setupProductLineTable();
    connectToDatabase();
    loadProductList();
    loadProductionLog();

    // Product Line - Init the item type choice box,
    // populates with 4 different possible item types and places default
    for (ItemType it : ItemType.values()) {
      chbItemType.getItems().add(it.name());
    }
    chbItemType.getSelectionModel().selectLast();

    // Produce - Init the choose quantity combo box,
    // populates with 1-10 and allows editing and places default
    final int numItems = 10;
    for (int i = 1; i <= numItems; i++) {
      cmbxChooseQuantity.getItems().add(i);
    }
    cmbxChooseQuantity.setEditable(true);
    cmbxChooseQuantity.getSelectionModel().selectFirst();
  }

  /**
   * Initializes the product line table, links it to the observable list.
   */
  private void setupProductLineTable() {
    productLine = FXCollections.observableArrayList();

    TableColumn<Product, Integer> colId = new TableColumn<>("ID");
    colId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));

    TableColumn<Product, String> colName = new TableColumn<>("Name");
    colName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

    TableColumn<Product, String> colManufacturer = new TableColumn<>("Manufacturer");
    colManufacturer.setCellValueFactory(new PropertyValueFactory<Product, String>("manufacturer"));
    // to fix the findbugs error
    final int manufacturerWidth = 100;
    colManufacturer.setPrefWidth(manufacturerWidth);

    TableColumn<Product, ItemType> colType = new TableColumn<>("Type");
    colType.setCellValueFactory(new PropertyValueFactory<Product, ItemType>("type"));

    tblvwExistingProducts.getColumns().add(colId);
    tblvwExistingProducts.getColumns().add(colName);
    tblvwExistingProducts.getColumns().add(colManufacturer);
    tblvwExistingProducts.getColumns().add(colType);
    tblvwExistingProducts.setItems(productLine);
  }

  /**
   * Loads the products already inserted into the database at initialization. Also initializes the
   * product ListView.
   */
  private void loadProductList() {
    productLine.clear();
    lstvwChooseProduct.getItems().clear();

    try {
      String sql = "SELECT * FROM PRODUCT";

      Statement statement = conn.createStatement();
      ResultSet result = statement.executeQuery(sql);

      while (result.next()) {
        int id = result.getInt("ID");
        String name = result.getString("NAME");
        String manufacturer = result.getString("MANUFACTURER");
        ItemType type = ItemType.valueOf(result.getString("TYPE"));

        Product product = determineProduct(name, manufacturer, type);
        product.setId(id);

        productLine.add(product);
        lstvwChooseProduct.getItems().add(product.getName());
      }

      statement.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Loads the production log from the database.
   */
  private void loadProductionLog() {
    if (productionRun == null) {
      productionRun = new ArrayList<>();
    } else {
      productionRun.clear();
    }
    taProductionLog.clear();
    try {
      String sql = "SELECT * FROM PRODUCTIONRECORD";

      Statement statement = conn.createStatement();
      ResultSet result = statement.executeQuery(sql);

      while (result.next()) {
        int productionNumber = result.getInt("PRODUCTION_NUM");
        int productId = result.getInt("PRODUCT_ID");
        String serialNumber = result.getString("SERIAL_NUM");
        Date dateProduced = result.getDate("DATE_PRODUCED");

        ProductionRecord productionRecord = new ProductionRecord(productionNumber, productId,
            serialNumber, dateProduced);
        productionRun.add(productionRecord);
        taProductionLog.appendText(productionRecord.toString() + "\n");
      }

      statement.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * This is a helper method to determine some values for certain products, not quite sure what the
   * real way is to do it yet.
   *
   * @param name         Name of the product
   * @param manufacturer Manufacturer of the product
   * @param type         Type of the product, used to determine certain values
   * @return a Product of type MoviePlayer or AudioPlayer
   */
  private Product determineProduct(String name, String manufacturer, ItemType type) {
    Product product = null;
    if (type == ItemType.AUDIO || type == ItemType.AUDIOMOBILE) {
      product = new AudioPlayer(name, manufacturer,
          "DSD/FLAC/ALAC/WAV/AIFF/MQA/Ogg-Vorbis/MP3/AAC", "M3U/PLS/WPL");
    } else if (type == ItemType.VISUAL || type == ItemType.VISUALMOBILE) {
      // to fix the findbugs error
      final int refreshRate = 40;
      final int responseTime = 22;
      product = new MoviePlayer(name, manufacturer,
          new Screen("720x480", refreshRate, responseTime),
          MonitorType.LCD);
    }
    return product;
  }

  /**
   * Initializes JDBC driver and connects to H2 database, initializes the connection field with URL,
   * USERNAME and PASSWORD.
   *
   * @return void
   */
  private void connectToDatabase() {
    // I had to change all of these to get 0 bugs on findbugs
    final String jdbcDriver = "org.h2.Driver";
    final String dbUrl = "jdbc:h2:./res/ProductionDB";

    //  Database credentials
    final String user = "";
    final String pass = "";

    try {
      // Initialize the driver
      Class.forName(jdbcDriver);

      // Connect to the database using credentials
      conn = DriverManager.getConnection(dbUrl, user, pass);
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
