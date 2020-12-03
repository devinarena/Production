package io.github.imunsmart.items;

/**
 * Widget to test the functionality of a product. Needs to be in its own file to avoid checkstyle
 * error.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 10/31/2020
 */
public class Widget extends Product {

  /**
   * Invokes the product super constructor.
   *
   * @param name         name of the widget
   * @param manufacturer manufacturer of the widget
   * @param type         item-type of the widget
   */
  public Widget(String name, String manufacturer, ItemType type) {
    super(name, manufacturer, type);
  }
}
