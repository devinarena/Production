package io.github.imunsmart.items.visual;

/**
 * ScreenSpec interface, contains helper functionality methods for a screen class.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 10/31/2020
 */
public interface ScreenSpec {

  /**
   * the resolution of the screen.
   *
   * @return resolution as a string in format YxZ pixels
   */
  String getResolution();

  /**
   * the refresh rate of the screen.
   *
   * @return refresh rate
   */
  int getRefreshRate();

  /**
   * the response time of the screen.
   *
   * @return response time
   */
  int getResponseTime();
}
