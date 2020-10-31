package io.github.imunsmart.items.visual;

import io.github.imunsmart.items.ItemType;
import io.github.imunsmart.items.MultimediaControl;
import io.github.imunsmart.items.Product;

/**
 * MoviePlayer product, has functionality for storing a screen and monitor type as well as functions
 * for movie clip management.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 10/31/2020
 */
public class MoviePlayer extends Product implements MultimediaControl {

  private Screen screen;
  private MonitorType monitorType;

  /**
   * The MoviePlayer constructor which calls the product super constructor and initializes important
   * fields.
   *
   * @param name         the name of the product
   * @param manufacturer the manufacturer of the product
   * @param screen       the screen object contained by the MoviePlayer
   * @param monitorType  the monitor type contained by the MoviePlayer
   */
  public MoviePlayer(String name, String manufacturer, Screen screen, MonitorType monitorType) {
    super(name, manufacturer, ItemType.VISUAL);
    this.screen = screen;
    this.monitorType = monitorType;
  }

  /**
   * Plays a movie.
   */
  @Override
  public void play() {
    // TODO: Implement functionality
    System.out.println("Playing movie");
  }

  /**
   * Stops the movie.
   */
  @Override
  public void stop() {
    // TODO: Implement functionality
    System.out.println("Stopping movie");
  }

  /**
   * Moves to the previous movie.
   */
  @Override
  public void previous() {
    // TODO: Implement functionality
    System.out.println("Previous movie");
  }

  /**
   * Moves to the next movie.
   */
  @Override
  public void next() {
    // TODO: Implement functionality
    System.out.println("Next movie");
  }

  /**
   * toString() implementation, converts important fields into a readable string.
   *
   * @return String containing the product info followed by the screen info and monitor type
   */
  @Override
  public String toString() {
    return super.toString() + "\n" + screen.toString() + "\nMonitor Type: " + monitorType;
  }
}
