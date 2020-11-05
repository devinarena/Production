package io.github.imunsmart.items;

/**
 * Product abstract class, top level implementation for product subclasses, contains important
 * information such as name, manufacturer, and item-type which are stored in a database.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 10/31/2020
 */
public abstract class Product implements Item {

  private int id;
  private ItemType type;
  private String manufacturer;
  private String name;

  /**
   * Product constructor, initializes important fields.
   *
   * @param name         name of the product
   * @param manufacturer manufacturer of the product
   * @param type         item-type of the product
   */
  public Product(String name, String manufacturer, ItemType type) {
    this.name = name;
    this.manufacturer = manufacturer;
    this.type = type;
  }

  /**
   * Identification number of the product.
   *
   * @return product's identification number
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * Name of the product.
   *
   * @return the product's name
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Manufacturer of the product.
   *
   * @return product's manufacturer name
   */
  @Override
  public String getManufacturer() {
    return manufacturer;
  }

  /**
   * The item-type of the product.
   *
   * @return the product's item-type
   */
  public ItemType getType() {
    return type;
  }

  /**
   * Sets the name of the item.
   *
   * @param name the name to set to
   */
  @Override
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the manufacturer of the item.
   *
   * @param manufacturer the manufacturer to set to
   */
  @Override
  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  /**
   * Sets the ID of the item.
   *
   * @param id the ID of the item
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Sets the type of the item.
   *
   * @param type the type of the item
   */
  public void setType(ItemType type) {
    this.type = type;
  }

  /**
   * toString() implementation, converts important fields into a readable string.
   *
   * @return a formatted string containing the name, manufacturer and item-type of the product
   */
  @Override
  public String toString() {
    return "Name: " + name + "\nManufacturer: " + manufacturer + "\nType: " + type.name();
  }
}