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
public class Day8 extends TaskSolver {

    private boolean[][] lcd = null;

    public Day8() {
        lcd = new boolean[6][50];
    }

    @Override
    public void process() {
        InputStream fis = null;
        try {
            fis = new FileInputStream("input/day8.txt");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);

            Pattern values = Pattern.compile("(rect (\\d+)x(\\d+))|(rotate row y=(\\d+) by (\\d+))|(rotate column x=(\\d+) by (\\d+))");

            String line;
            while ((line = br.readLine()) != null) {
                Matcher mr = values.matcher(line);
                if (mr.matches()) {
                    if (mr.group(1) != null) {
                        int a = Integer.parseInt(mr.group(2));
                        int b = Integer.parseInt(mr.group(3));
                        rect(a, b);
                    } else if (mr.group(4) != null) {
                        int a = Integer.parseInt(mr.group(5));
                        int b = Integer.parseInt(mr.group(6));
                        rotateColumn(a, b);
                    } else if (mr.group(7) != null) {
                        int a = Integer.parseInt(mr.group(8));
                        int b = Integer.parseInt(mr.group(9));
                        rotateRow(a, b);
                    }
                }
            }
            int lit = 0;
            for (int i = 0; i < lcd.length; i++) {
                for (int j = 0; j < lcd[0].length; j++) {
                    if (lcd[i][j]) {
                        lit++;
                    }
                }
            }

            System.out.println("Total pixels lit: " + lit);
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

    private void rect(int a, int b) {
        for (int i = 0; i < b; i++) {
            for (int j = 0; j < a; j++) {
                lcd[i][j] = true;
            }
        }
    }

    private void rotateColumn(int a, int b) {
        boolean[] tmp = new boolean[b];
        System.arraycopy(lcd[a], lcd[a].length - b, tmp, 0, b);
        System.arraycopy(lcd[a], 0, lcd[a], b, lcd[a].length - b);
        System.arraycopy(tmp, 0, lcd[a], 0, b);
    }

    private void rotateRow(int a, int b) {
        boolean[] tmp = new boolean[lcd.length];
        for (int i = 0; i < b; i++) {
            tmp[i] = lcd[lcd.length - b + i][a];
        }
        for (int i = b; i < lcd.length; i++) {
            tmp[i] = lcd[i - b][a];
        }
        for (int i = 0; i < lcd.length; i++) {
            lcd[i][a] = tmp[i];
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lcd.length; i++) {
            for (int j = 0; j < lcd[0].length; j++) {
                if (lcd[i][j]) {
                    sb.append("#");
                } else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
