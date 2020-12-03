package io.github.imunsmart.items.visual;

/**
 * Screen class, stores important screen information such as resolution, refresh rate and response
 * time.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 10/31/2020
 */
public class Screen implements ScreenSpec {

  private final String resolution;
  private final int refreshRate;
  private final int responseTime;

  /**
   * The Screen constructor.
   *
   * @param resolution   the resolution of the screen (format: YxZ pixels)
   * @param refreshRate  the refresh rate of the screen
   * @param responseTime the response time of the screen
   */
  public Screen(String resolution, int refreshRate, int responseTime) {
    this.resolution = resolution;
    this.refreshRate = refreshRate;
    this.responseTime = responseTime;
  }

  /**
   * The resolution of the screen.
   *
   * @return resolution as a string in format YxZ
   */
  @Override
  public String getResolution() {
    return resolution;
  }

  /**
   * The refresh rate of the screen.
   *
   * @return refresh rate
   */
  @Override
  public int getRefreshRate() {
    return refreshRate;
  }

  /**
   * The response time of the screen.
   *
   * @return response time
   */
  @Override
  public int getResponseTime() {
    return responseTime;
  }

  /**
   * toString() implementation, converts important fields into a readable string.
   *
   * @return String containing the screen info
   */
  @Override
  public String toString() {
    return "Screen:\nResolution: " + resolution + "\nRefresh rate: " + refreshRate
        + "\nResponse time: " + responseTime;
  }
}
