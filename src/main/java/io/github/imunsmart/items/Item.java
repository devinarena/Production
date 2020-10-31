package io.github.imunsmart.items;

/**
 * Item interface, contains helper functionality methods for a product.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 10/31/2020
 */
public interface Item {

  /**
   * The identification number of the item.
   *
   * @return item identification number
   */
  int getId();

  /**
   * The name of the item.
   *
   * @return the item's name
   */
  String getName();

  /**
   * The manufacturer of the item.
   *
   * @return the item's manufacturer
   */
  String getManufacturer();

  /**
   * Sets the name of the item.
   *
   * @param name the name to set to
   */
  void setName(String name);

  /**
   * Sets the manufacturer of the item.
   *
   * @param manufacturer the manufacturer to set to
   */
  void setManufacturer(String manufacturer);

}
