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

/**
 *
 * @author karel.hebik
 */
public class Day3 extends TaskSolver {

    private int triangles = 0;

    public Day3() {
    }

    @Override
    public void process() {
        InputStream fis = null;
        try {
            fis = new FileInputStream("input/day3.txt");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                if (isTriangle1(line)) {
                    triangles++;
                }
            }

            System.out.println("Possible triangles horizontally: " + triangles);

            fis = new FileInputStream("input/day3.txt");
            isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);

            triangles = 0;
            String row = "";
            int lines = 0;
            while ((line = br.readLine()) != null) {
                lines++;
                row += line + " ";
                if (lines > 1 && lines % 3 == 0) {
                    triangles += isTriangle2(row.trim());
                    row = "";
                }
            }

            System.out.println("Possible triangles vertically: " + triangles);

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

    private boolean isTriangle1(String line) {
        String[] sides = line.trim().split("\\s+");
        if (sides.length != 3) {
            System.err.println("Invalid input: " + line);
            return false;
        }
        int a = Integer.parseInt(sides[0].trim());
        int b = Integer.parseInt(sides[1].trim());
        int c = Integer.parseInt(sides[2].trim());

        if (a + b > c && a + c > b && b + c > a) {
            return true;
        } else {
            return false;
        }
    }

    private int isTriangle2(String line) {
        String[] sides = line.trim().split("\\s+");
        if (sides.length != 9) {
            System.err.println("Invalid input: " + line);
            return 0;
        }
        int a1 = Integer.parseInt(sides[0].trim());
        int b1 = Integer.parseInt(sides[3].trim());
        int c1 = Integer.parseInt(sides[6].trim());

        int a2 = Integer.parseInt(sides[1].trim());
        int b2 = Integer.parseInt(sides[4].trim());
        int c2 = Integer.parseInt(sides[7].trim());

        int a3 = Integer.parseInt(sides[2].trim());
        int b3 = Integer.parseInt(sides[5].trim());
        int c3 = Integer.parseInt(sides[8].trim());

        int count = 0;
        if (a1 + b1 > c1 && a1 + c1 > b1 && b1 + c1 > a1) {
            count++;
        }

        if (a2 + b2 > c2 && a2 + c2 > b2 && b2 + c2 > a2) {
            count++;
        }

        if (a3 + b3 > c3 && a3 + c3 > b3 && b3 + c3 > a3) {
            count++;
        }
        return count;
    }
}
