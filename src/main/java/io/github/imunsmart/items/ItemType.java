package io.github.imunsmart.items;

/**
 * ItemType enum, contains 4 classifications for items - Audio, Visual, Audio-Mobile, and
 * Visual-Mobile.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 10/31/2020
 */
public enum ItemType {
  AUDIO("AU"),
  VISUAL("VI"),
  AUDIOMOBILE("AM"),
  VISUALMOBILE("VM");

  private String code;

  /**
   * ItemType constructor, sets the code.
   *
   * @param code code of the item-type
   */
  ItemType(String code) {
    this.code = code;
  }

  /**
   * 2 letter code of the item-type.
   *
   * @return 2 letter code specific to each item
   */
  public String getCode() {
    return code;
  }
}
