import io.github.imunsmart.items.ItemType;
import io.github.imunsmart.items.Product;

/**
 * Widget to test the functionality of a product. Didn't know if this needed to still exist but
 * checkstyle gives an error for it not being in its own file.
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