/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.TaskSolver;
import java.awt.Point;
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
public class Day2 extends TaskSolver {

    private String[] keyboard1 = null;
    private String[] keyboard2 = null;
    private String sequence = null;
    private Point point = null;

    public Day2() {
        keyboard1 = new String[]{"123", "456", "789"};
        keyboard2 = new String[]{"  1  ", " 234 ", "56789", " ABC ", "  D  "};
    }

    @Override
    public void process() {
        InputStream fis = null;
        try {
            fis = new FileInputStream("input/day2.txt");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            sequence = "";
            point = new Point(1, 1);

            String line;
            while ((line = br.readLine()) != null) {
                if (!move1(line)) {
                    System.err.println("Error on line: " + line);
                    return;
                }
            }

            System.out.println("Bathroom 1st code is: " + sequence);

            fis = new FileInputStream("input/day2.txt");
            isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            sequence = "";
            point = new Point(0, 2);

            while ((line = br.readLine()) != null) {
                if (!move2(line)) {
                    System.err.println("Error on line: " + line);
                    return;
                }
            }

            System.out.println("Bathroom 2nd code is: " + sequence);
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

    private boolean move1(String path) {
        for (Character ch : path.toCharArray()) {
            switch (ch) {
                case 'U':
                    if (point.getY() > 0) {
                        point.translate(0, -1);
                    }
                    break;
                case 'R':
                    if (point.getX() < 2) {
                        point.translate(1, 0);
                    }
                    break;
                case 'D':
                    if (point.getY() < 2) {
                        point.translate(0, 1);
                    }
                    break;
                case 'L':
                    if (point.getX() > 0) {
                        point.translate(-1, 0);
                    }
                    break;
                default:
                    return false;
            }
        }
        sequence += keyboard1[point.y].charAt(point.x);
        return true;
    }

    private boolean move2(String path) {
        for (Character ch : path.toCharArray()) {
            switch (ch) {
                case 'U':
                    if ((point.getX() == 1 || point.getX() == 3) && point.getY() > 1) {
                        point.translate(0, -1);
                    } else if (point.getX() == 2 && point.getY() > 0) {
                        point.translate(0, -1);
                    }
                    break;
                case 'R':
                    if ((point.getY() == 1 || point.getY() == 3) && point.getX() < 3) {
                        point.translate(1, 0);
                    } else if (point.getY() == 2 && point.getX() < 4) {
                        point.translate(1, 0);
                    }
                    break;
                case 'D':
                    if ((point.getX() == 1 || point.getX() == 3) && point.getY() < 3) {
                        point.translate(0, 1);
                    } else if (point.getX() == 2 && point.getY() < 4) {
                        point.translate(0, 1);
                    }
                    break;
                case 'L':
                    if ((point.getY() == 1 || point.getY() == 3) && point.getX() > 1) {
                        point.translate(-1, 0);
                    } else if (point.getY() == 2 && point.getX() > 0) {
                        point.translate(-1, 0);
                    }
                    break;
                default:
                    return false;
            }
        }
        sequence += keyboard2[point.y].charAt(point.x);
        return true;
    }

}
