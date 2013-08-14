/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package flib.util;

/**
 *
 * @author John-Lee
 */
public class HexCodec {
    private static final char[] kDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
      'b', 'c', 'd', 'e', 'f' };
    //static final String HEXES = "0123456789ABCDEF";

  public static byte[] hexToBytes(char[] hex) {
    int length = hex.length / 2;
    byte[] raw = new byte[length];
    for (int i = 0; i < length; i++) {
      int high = Character.digit(hex[i * 2], 16);
      int low = Character.digit(hex[i * 2 + 1], 16);
      int value = (high << 4) | low;
      if (value > 127)
        value -= 256;
      raw[i] = (byte) value;
    }
    return raw;
  }

  public static byte[] hexToBytes(String hex) {
    return hexToBytes(hex.toCharArray());
  }

  public static String getHex( byte [] raw ) {
    if ( raw == null ) {
      return null;
    }
    final StringBuilder hex = new StringBuilder( 2 * raw.length );
    for ( final byte b : raw ) {
      hex.append(kDigits[(b & 0xF0) >> 4])
         .append(kDigits[(b & 0x0F)]);
    }
    return hex.toString();
  }
}
