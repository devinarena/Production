package io.github.imunsmart.items;

/**
 * MultimediaControl interface, contains 4 functions for a multimedia player.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 10/31/2020
 */
public interface MultimediaControl {

  /**
   * Plays some kind of media.
   */
  void play();

  /**
   * Stops some kind of media.
   */
  void stop();

  /**
   * Moves to the previous media.
   */
  void previous();

  /**
   * Moves to the next media.
   */
  void next();

}
