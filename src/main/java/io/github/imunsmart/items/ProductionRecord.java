package io.github.imunsmart.items;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

/**
 * ProductionRecord class, stores information about a production event, including the production
 * number, product ID, serial number, date produced, and product (optional).
 *
 * @author Devin Arena
 * @version 1.0
 * @since 10/31/2020
 */
public class ProductionRecord {

  private int productionNumber;
  private int productId;
  private String serialNumber;
  private Date produced;
  private Product product;
  private String employee;

  /**
   * ProductionRecord constructor for product id only.
   *
   * @param productId id of the product
   */
  public ProductionRecord(int productId) {
    this.productId = productId;
    productionNumber = 0;
    serialNumber = "0";
    employee = "none";
    produced = new Date();
  }

  /**
   * ProductionRecord constructor for production number, product id, serial number, and date.
   * produced
   *
   * @param productionNumber production number for the product type
   * @param productId        product id
   * @param serialNumber     serial number of the product
   * @param produced         date produced
   * @param employee         the employee who ran the production
   */
  public ProductionRecord(int productionNumber, int productId, String serialNumber,
      Date produced, String employee) {
    this.productionNumber = productionNumber;
    this.productId = productId;
    this.serialNumber = serialNumber;
    this.produced = new Date(produced.getTime());
    this.employee = employee;
  }

  /**
   * ProductionRecord constructor for product and number of created, generates a serial number
   * programmatically.
   *
   * @param product         product to record
   * @param numberOfCreated number of that product type
   * @param employee        the employee who ran the production
   */
  public ProductionRecord(Product product, int numberOfCreated, String employee) {
    this.product = product;
    this.productId = product.getId();
    final int numDigits = 5;
    final int manufacturerIndex = 3;
    productionNumber = 0;
    serialNumber =
        product.getManufacturer().substring(0, manufacturerIndex) + product.getType().getCode()
            + String.format("%0" + numDigits + "d", numberOfCreated);
    produced = Timestamp.from(Instant.now());
    this.employee = employee;
  }

  /**
   * Production number for product type.
   *
   * @return production number
   */
  public int getProductionNumber() {
    return productionNumber;
  }

  /**
   * Sets the production number for the product type.
   *
   * @param productionNumber production number for the product type
   */
  public void setProductionNumber(int productionNumber) {
    this.productionNumber = productionNumber;
  }

  /**
   * Product identification number for the record.
   *
   * @return product identification number
   */
  public int getProductId() {
    return productId;
  }

  /**
   * Sets the product identification number for the record.
   *
   * @param productId identification number to set to
   */
  public void setProductId(int productId) {
    this.productId = productId;
  }

  /**
   * The serial number of the production record.
   *
   * @return production record serial number
   */
  public String getSerialNumber() {
    return serialNumber;
  }

  /**
   * Sets the serial number of the production record.
   *
   * @param serialNumber serial number to set to
   */
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  /**
   * The date of the production record.
   *
   * @return date of production
   */
  public Date getProduced() {
    return produced;
  }

  /**
   * Sets the date of the production record.
   *
   * @param produced date of production record
   */
  public void setProduced(Date produced) {
    this.produced = new Date(produced.getTime());
  }

  /**
   * Sets the product of the record.
   *
   * @param product the product to set
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  public void setEmployee(String employee) {
    this.employee = employee;
  }

  public String getEmployee() {
    return employee;
  }

  /**
   * toString() implementation, converts important fields to a readable string based on what
   * information was given for production.
   *
   * @return formatted String containing important fields based on what info was given
   */
  @Override
  public String toString() {
    return "Prod. Num: " + productionNumber + " Product ID: " + productId + " Serial Num: "
        + serialNumber + " Date: " + produced + " Employee: " + employee;
  }
}
