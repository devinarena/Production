package io.github.imunsmart.items.audio;

import io.github.imunsmart.items.ItemType;
import io.github.imunsmart.items.MultimediaControl;
import io.github.imunsmart.items.Product;

/**
 * AudioPlayer product, has functionality for storing supported audio and playlist formats as well
 * as functions for audio clip management.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 10/31/2020
 */
public class AudioPlayer extends Product implements MultimediaControl {

  private String supportedAudioFormats;
  private String supportedPlaylistFormats;

  /**
   * AudioPlayer constructor that invokes product super constructor and initializes fields.
   *
   * @param manufacturer             the manufacturer of the product
   * @param name                     the name of the product
   * @param supportedAudioFormats    the supported audio formats as a string
   * @param supportedPlaylistFormats the supported playlist formats as a string
   */
  public AudioPlayer(String manufacturer, String name, String supportedAudioFormats,
      String supportedPlaylistFormats) {
    super(manufacturer, name, ItemType.AUDIO);
    this.supportedAudioFormats = supportedAudioFormats;
    this.supportedPlaylistFormats = supportedPlaylistFormats;
  }

  /**
   * Plays an audio clip.
   */
  @Override
  public void play() {
    // TODO: Implement functionality
    System.out.println("Playing");
  }

  /**
   * Stops an audio clip.
   */
  @Override
  public void stop() {
    // TODO: Implement functionality
    System.out.println("Stopping");
  }

  /**
   * Moves to the previous audio clip in the playlist.
   */
  @Override
  public void previous() {
    // TODO: Implement functionality
    System.out.println("Previous");
  }

  /**
   * Moves to the next audio clip in the playlist.
   */
  @Override
  public void next() {
    System.out.println("Next");
  }

  /**
   * toString() implementation, converts important fields into a readable string.
   *
   * @return String containing the product info followed by the audio player info
   */
  @Override
  public String toString() {
    return super.toString() + "\nSupported Audio Formats: " + supportedAudioFormats
        + "\nSupported Playlist Formats: " + supportedPlaylistFormats;
  }
}
