package io.github.imunsmart;

import io.github.imunsmart.employee.Employee;
import io.github.imunsmart.items.ItemType;
import io.github.imunsmart.items.MultimediaControl;
import io.github.imunsmart.items.Product;
import io.github.imunsmart.items.ProductionRecord;
import io.github.imunsmart.items.audio.AudioPlayer;
import io.github.imunsmart.items.visual.MonitorType;
import io.github.imunsmart.items.visual.MoviePlayer;
import io.github.imunsmart.items.visual.Screen;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;


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
  private ComboBox<String> cmbxChooseQuantity;
  @FXML
  private ListView<String> lstvwChooseProduct;
  @FXML
  private Button btnRecordProduction;

  // Production Log Tab
  @FXML
  private Tab tabProductionLog;
  @FXML
  private TextArea taProductionLog;

  // Employee Tab
  @FXML
  private Tab tabEmployee;
  @FXML
  private TextField tfUsername;
  @FXML
  private PasswordField tfPassword;
  @FXML
  private TextField tfEmployeeName;
  @FXML
  private PasswordField tfInitialPassword;
  @FXML
  private TextArea taEmployeeInformation;
  @FXML
  private Button btnEmployeeRegister;
  @FXML
  private Button btnEmployeeLogin;

  private Stage primaryStage;

  private Connection conn;

  private Employee currentUser = null;
  private ObservableList<Product> productLine;
  private ArrayList<ProductionRecord> productionRun;
  private HashMap<ItemType, Integer> numCreated;

  /**
   * Event when add product button is pressed, inserts values from UI into H2 database using SQL,
   * assuming valid fields are entered.
   *
   * @param event ActionEvent to handle
   */
  @FXML
  public void addProduct(ActionEvent event) {
    // Get data from UI fields
    String productName = tfProductName.getText();
    String manufacturer = tfManufacturer.getText();
    ItemType itemType = ItemType.valueOf(chbItemType.getValue());

    if (productName.isEmpty()) {
      showDialog("Product Name Error!", "Please ensure product name is filled out.");
      tfProductName.requestFocus();
      return;
    } else if (manufacturer.isEmpty()) {
      showDialog("Product Manufacturer Error!",
          "Please ensure product manufacturer is filled out.");
      tfManufacturer.requestFocus();
      return;
    }

    Product product = new Product(productName, manufacturer, itemType);

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
      showDialog("Database Error!",
          "Failed to save product to database! Please make sure connection is valid and retry.");
      ex.printStackTrace();
    }

    loadProductList();
  }

  /**
   * Event when record production button is pressed, inserts values from UI into H2 database using
   * SQL.
   *
   * @param event ActionEvent to handle
   */
  @FXML
  public void recordProduction(ActionEvent event) {
    int numberToProduce = -1;
    if (lstvwChooseProduct.getSelectionModel().getSelectedIndex() < 0) {
      showDialog("Product Selection Error!",
          "Please select a product to record the production of.");
      lstvwChooseProduct.requestFocus();
      return;
    }
    Product product = productLine.get(lstvwChooseProduct.getSelectionModel().getSelectedIndex());
    try {
      numberToProduce = Integer.parseInt(cmbxChooseQuantity.getValue());
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (numberToProduce <= 0) {
      showDialog("Quantity Error!", "Please ensure quantity entered is an integer greater than 0.");
      cmbxChooseQuantity.requestFocus();
      return;
    }
    ArrayList<ProductionRecord> productionRecords = new ArrayList<>();
    for (int i = 0; i < numberToProduce; i++) {
      int numberOfCreated = numCreated.getOrDefault(product.getType(), 0) + i;
      ProductionRecord productionRecord = new ProductionRecord(product, numberOfCreated,
          currentUser.getName());
      productionRecord.setProductionNumber(productionRun.size());

      productionRun.add(productionRecord);
      productionRecords.add(productionRecord);
    }

    addToProductionDb(productionRecords);

    loadProductionLog();
  }

  /**
   * Gets information from text fields and attempts to register, fires when register button is
   * pressed.
   *
   * @param event ActionEvent
   */
  @FXML
  public void registerEmployee(ActionEvent event) {
    String name = tfEmployeeName.getText();
    String pass = tfInitialPassword.getText();
    if (name.isEmpty()) {
      showDialog("Register Error!", "Please enter your name.");
      tfEmployeeName.requestFocus();
      return;
    }
    if (pass.isEmpty()) {
      showDialog("Register Error!", "Please enter a password.");
      tfInitialPassword.requestFocus();
      return;
    }
    Employee employee = new Employee(tfEmployeeName.getText(), tfInitialPassword.getText());
    addEmployeeToDb(employee);
    taEmployeeInformation.clear();
    taEmployeeInformation.appendText(employee.toString());
    taEmployeeInformation.appendText("\nYou may now login!");
    tfEmployeeName.clear();
    tfInitialPassword.clear();
  }

  /**
   * Gets information from text fields and attempts to login, fires when login button is pressed.
   *
   * @param event ActionEvent
   */
  @FXML
  public void employeeLogin(ActionEvent event) {
    String name = tfUsername.getText();
    String pass = tfPassword.getText();
    if (name.isEmpty()) {
      showDialog("Login Error!", "Please enter your username.");
      tfUsername.requestFocus();
      return;
    }
    if (pass.isEmpty()) {
      showDialog("Login Error!", "Please enter your password.");
      tfPassword.requestFocus();
      return;
    }
    currentUser = attemptLogin(name, pass);
    if (currentUser == null) {
      showDialog("Login Failed!", "You entered an invalid username or password.");
      tfUsername.requestFocus();
    } else {
      showDialog("Login Success!", "You have signed in as: " + currentUser.getName() + "!");
      taEmployeeInformation.clear();
      taEmployeeInformation.appendText(currentUser.toString());
      tfUsername.clear();
      tfPassword.clear();
      if (tabProductLine.isDisabled()) {
        tabProductLine.setDisable(false);
      }
      if (tabProduce.isDisabled()) {
        tabProduce.setDisable(false);
      }
      if (tabProductionLog.isDisabled()) {
        tabProductionLog.setDisable(false);
      }
    }
  }

  /**
   * Adds an employee record to the database for future logins.
   *
   * @param employee the employee to add to the database.
   */
  private void addEmployeeToDb(Employee employee) {
    try {
      // SQL to insert a product into the DB
      String sql =
          "INSERT INTO employee(name, username, email, password) "
              + "VALUES ( ?, ?, ?, ? )";

      //This is to get rid of the find bugs error
      final int nameIndex = 1;
      final int usernameIndex = 2;
      final int emailIndex = 3;
      final int passwordIndex = 4;
      // Create a prepared statement from connection
      // and set values to employee field values
      PreparedStatement prst = conn.prepareStatement(sql);
      prst.setString(nameIndex, employee.getName());
      prst.setString(usernameIndex, employee.getUsername());
      prst.setString(emailIndex, employee.getEmail());
      prst.setString(passwordIndex, employee.getPassword());

      // Execute and close the statement
      prst.executeUpdate();
      prst.close();
    } catch (SQLException ex) {
      showDialog("Database Error!",
          "Failed to employee record into database."
              + " Ensure connection is valid and retry.");
      ex.printStackTrace();
    }
  }

  /**
   * Attempt to login to the system, retrieves an employee object from the database if login is
   * successful.
   *
   * @param username employee username
   * @param password employee password
   * @return employee object or null if login failed
   */
  private Employee attemptLogin(String username, String password) {
    Employee user = null;
    try {
      // SQL to insert a product into the DB
      String sql =
          "SELECT * FROM EMPLOYEE WHERE USERNAME=? AND PASSWORD=?";

      //This is to get rid of the find bugs error
      final int usernameIndex = 1;
      final int passwordIndex = 2;
      // Create a prepared statement from connection
      // and set values to ProductionRecord field values
      PreparedStatement prst = conn.prepareStatement(sql);
      prst.setString(usernameIndex, username);
      prst.setString(passwordIndex, password);

      // Execute and close the statement
      ResultSet rs = prst.executeQuery();
      while (rs.next()) {
        String name = rs.getString("NAME");
        String empUsername = rs.getString("USERNAME");
        String email = rs.getString("EMAIL");
        String empPassword = rs.getString("PASSWORD");

        user = new Employee(name, empUsername, email, empPassword);
      }
      prst.close();
    } catch (SQLException ex) {
      showDialog("Database Error!",
          "Failed retrieve employee record from database."
              + " Ensure connection is valid and retry.");
      ex.printStackTrace();
    }
    return user;
  }

  /**
   * Inserts the production records into the database.
   *
   * @param productionRecords to insert into the DB.
   */
  private void addToProductionDb(ArrayList<ProductionRecord> productionRecords) {
    try {
      // SQL to insert a product into the DB
      String sql =
          "INSERT INTO productionrecord(production_num, product_id, serial_num, "
              + "date_produced, employee) VALUES ( ?, ?, ?, ?, ? )";

      //This is to get rid of the find bugs error
      final int prodNumberIndex = 1;
      final int prodIdIndex = 2;
      final int serialNumIndex = 3;
      final int prodDateIndex = 4;
      final int employeeIndex = 5;
      for (ProductionRecord productionRecord : productionRecords) {
        // Create a prepared statement from connection
        // and set values to ProductionRecord field values
        PreparedStatement prst = conn.prepareStatement(sql);
        prst.setInt(prodNumberIndex, productionRecord.getProductionNumber());
        prst.setInt(prodIdIndex, productionRecord.getProductId());
        prst.setString(serialNumIndex, productionRecord.getSerialNumber());
        prst.setTimestamp(prodDateIndex, new Timestamp(productionRecord.getProduced().getTime()));
        prst.setString(employeeIndex, currentUser.getName());

        // Execute and close the statement
        prst.executeUpdate();
        prst.close();
      }
    } catch (SQLException ex) {
      showDialog("Database Error!",
          "Failed to insert production record into database."
              + " Ensure connection is valid and retry.");
      ex.printStackTrace();
      return;
    }

    showDialog("Production Recorded!",
        "Successfully recorded the production, check the production log for info.");
  }

  /**
   * Main initialization method.
   */
  public void initialize(Stage primaryStage) {
    this.primaryStage = primaryStage;
    numCreated = new HashMap<>();
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
      cmbxChooseQuantity.getItems().add(String.valueOf(i));
    }
    cmbxChooseQuantity.setEditable(true);
    cmbxChooseQuantity.getSelectionModel().selectFirst();

    taProductionLog.setEditable(false);
  }

  /**
   * Initializes the product line table, links it to the observable list.
   */
  private void setupProductLineTable() {
    productLine = FXCollections.observableArrayList();

    TableColumn<Product, Integer> colId = new TableColumn<>("ID");
    colId.setCellValueFactory(new PropertyValueFactory<>("id"));

    TableColumn<Product, String> colName = new TableColumn<>("Name");
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));

    TableColumn<Product, String> colManufacturer = new TableColumn<>("Manufacturer");
    colManufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
    // to fix the findbugs error
    final int manufacturerWidth = 150;
    colManufacturer.setPrefWidth(manufacturerWidth);

    TableColumn<Product, ItemType> colType = new TableColumn<>("Type");
    colType.setCellValueFactory(new PropertyValueFactory<>("type"));

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

        Product product = new Product(name, manufacturer, type);
        product.setId(id);

        productLine.add(product);
        lstvwChooseProduct.getItems().add(product.getName());
      }
      statement.close();
    } catch (SQLException ex) {
      showDialog("Database Error!",
          "Failed to load product list from database."
              + " Ensure connection is valid and retry.");
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
    numCreated.clear();
    try {
      String sql = "SELECT * FROM PRODUCTIONRECORD";

      Statement statement = conn.createStatement();
      ResultSet result = statement.executeQuery(sql);

      while (result.next()) {
        int productionNumber = result.getInt("PRODUCTION_NUM");
        int productId = result.getInt("PRODUCT_ID");
        String serialNumber = result.getString("SERIAL_NUM");
        Timestamp dateProduced = result.getTimestamp("DATE_PRODUCED");
        String employee = result.getString("EMPLOYEE");

        Product product = getProduct(productId);
        if (product != null) {
          numCreated.put(product.getType(), numCreated.getOrDefault(product.getType(), 0) + 1);
        }

        ProductionRecord productionRecord = new ProductionRecord(productionNumber, productId,
            serialNumber, dateProduced, employee);
        productionRecord.setProduct(product);
        productionRun.add(productionRecord);
      }

      statement.close();
    } catch (SQLException ex) {
      showDialog("Database Error!",
          "Failed to load production record from database."
              + " Ensure connection is valid and retry.");
      ex.printStackTrace();
    }

    showProduction();
  }

  /**
   * Logs all of the Production Records onto the Production Log text area.
   */
  private void showProduction() {
    taProductionLog.clear();
    for (ProductionRecord productionRecord : productionRun) {
      taProductionLog.appendText(productionRecord.toString() + "\n");
    }
  }

  /**
   * Tests the functionality of the multimedia interface.
   */
  public static void testMultimedia() {
    AudioPlayer newAudioProduct = new AudioPlayer("DP-X1A", "Onkyo",
        "DSD/FLAC/ALAC/WAV/AIFF/MQA/Ogg-Vorbis/MP3/AAC", "M3U/PLS/WPL");
    final int refreshRate = 40;
    final int responseTime = 22;
    Screen newScreen = new Screen("720x480", refreshRate, responseTime);
    MoviePlayer newMovieProduct = new MoviePlayer("DBPOWER MK101", "OracleProduction", newScreen,
        MonitorType.LCD);
    ArrayList<MultimediaControl> productList = new ArrayList<>();
    productList.add(newAudioProduct);
    productList.add(newMovieProduct);
    for (MultimediaControl p : productList) {
      System.out.println(p);
      p.play();
      p.stop();
      p.next();
      p.previous();
    }
  }

  /**
   * Gets a product based on its ID.
   *
   * @param productId the product's ID
   * @return the product with the corresponding ID.
   */
  private Product getProduct(int productId) {
    for (Product product : productLine) {
      if (product.getId() == productId) {
        return product;
      }
    }
    return null;
  }

  /**
   * Reverses a string recursively (used for basic file encryption).
   *
   * @param pw the password string to reverse
   * @return the reversed pw string
   */
  public String reverseString(String pw) {
    if (pw.length() < 1) {
      return "";
    }
    return pw.charAt(pw.length() - 1) + reverseString(pw.substring(0, pw.length() - 1));
  }

  /**
   * Creates a new popup dialog with the specified title and message. The popup forces application
   * modality so users must close the popup before continuing.
   *
   * @param title   the title of the dialog box
   * @param message the message of the dialog box
   */
  public void showDialog(String title, String message) {
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("popup.fxml"));
    Scene scene = null;
    try {
      Parent root = loader.load();
      scene = new Scene(root);
    } catch (IOException e) {
      System.err.println("Fatal error: failed to load popup dialog fxml file.");
      e.printStackTrace();
    }
    if (scene == null) {
      return;
    }

    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(primaryStage);
    dialog.setTitle(title);
    dialog.setScene(scene);

    PopupController controller = loader.getController();
    controller.initialize(dialog);

    controller.setText(message);
    dialog.show();
  }

  /**
   * Initializes JDBC driver and connects to H2 database, initializes the connection field with URL,
   * USERNAME and PASSWORD.
   */
  private void connectToDatabase() {
    final String jdbcDriver = "org.h2.Driver";
    final String dbUrl = "jdbc:h2:./res/ProductionDB";

    try {
      // Load password from file and reverse it
      Properties properties = new Properties();
      properties.load(new FileInputStream("res/dbinfo"));
      String pass = reverseString(properties.getProperty("PASSWORD"));
      // Initialize the driver
      Class.forName(jdbcDriver);
      // Connect to the database using credentials
      conn = DriverManager.getConnection(dbUrl, "", pass);
    } catch (ClassNotFoundException | SQLException | IOException ex) {
      showDialog("Error in Connecting",
          "Failed to connect to database, please ensure database exists and retry.");
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
