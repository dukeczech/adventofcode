/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.TaskSolver;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day9 extends TaskSolver {

    public Day9() {
    }

    public void process() {
        InputStream fis = null;
        try {
            fis = new FileInputStream("input/day9.txt");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);

            long length1 = 0;
            long length2 = 0;
            String line;
            while ((line = br.readLine()) != null) {
                length1 += decompress1(line);
                length2 += decompress2(line);
            }
            System.out.println("Decompressed length: " + length1);
            System.out.println("Decompressed length with improvement format: " + length2);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Day2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Day2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Day2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private long decompress1(String input) {
        int[] marker = null;
        long length = 0;

        for (int i = 0; i < input.length();) {
            char ch = input.charAt(i);
            if (ch == '(' && marker == null) {
                int shift = input.indexOf(")", i);
                marker = getMarker(input.substring(i, shift + 1));
                i = shift + 1;
            } else {
                length++;
                i++;
            }
            if (marker != null) {
                for (int j = 0; j < marker[1]; j++) {
                    length += marker[0];
                }
                i += marker[0];
                marker = null;
            }
        }
        return length;
    }

    private long decompress2(String input) {
        int[] marker = null;
        long length = 0;

        int[] weights = new int[input.length()];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1;
        }

        for (int i = 0; i < input.length();) {
            char ch = input.charAt(i);
            if (ch == '(' && marker == null) {
                int shift = input.indexOf(")", i);
                marker = getMarker(input.substring(i, shift + 1));
                i = shift + 1;
            } else {
                length += weights[i];
                i++;
            }

            if (marker != null) {
                for (int j = i; j < i + marker[0]; j++) {
                    weights[j] *= marker[1];
                }
                marker = null;
            }
        }
        return length;
    }

    private int[] getMarker(String input) {
        Pattern values = Pattern.compile("\\((\\d+)x(\\d+)\\)");
        Matcher mr = values.matcher(input);
        if (mr.matches()) {
            int[] result = new int[2];
            result[0] = Integer.parseInt(mr.group(1));
            result[1] = Integer.parseInt(mr.group(2));
            return result;
        }
        return null;
    }
}
