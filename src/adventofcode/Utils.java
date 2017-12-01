/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import y2016.Day5;

/**
 *
 * @author karel.hebik
 */
public class Utils {

    public static String md5(String input) {
        try {
            byte[] bytesOfMessage = input.getBytes("UTF-8");

            MessageDigest md = MessageDigest.getInstance("MD5");
            return bytesToHex(md.digest(bytesOfMessage));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(Day5.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    final protected static char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
