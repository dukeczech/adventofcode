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
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day4 extends TaskSolver {

    private int sum = 0;
    private int store = 0;

    public Day4() {
    }

    @Override
    public void process() {
        InputStream fis = null;
        try {
            fis = new FileInputStream("input/day4.txt");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                int id = isRoom(line);
                if (id > 0) {
                    sum += id;
                }
            }

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
        System.out.println("Sum of their sector ID: " + sum);
        System.out.println("North Pole objects are stored in sector ID: " + store);
    }

    private int isRoom(String def) {
        String[] params = def.split("[-\\[\\]]");
        Pattern alpha = Pattern.compile("[a-z]+");
        Pattern digits = Pattern.compile("[0-9]+");
        String name = "";
        String id = "";
        String checksum = "";
        String encoded = "";
        for (int i = 0; i < params.length; i++) {
            if (i < params.length - 2) {
                Matcher mr = alpha.matcher(params[i]);
                if (!mr.matches()) {
                    System.err.println("Failed match: " + params[i]);
                    return -1;
                }
                name += params[i];
                encoded += params[i] + " ";
            } else if (i == params.length - 2) {
                // Sector ID
                Matcher mr = digits.matcher(params[i]);
                if (!mr.matches()) {
                    System.err.println("Failed match: " + params[i]);
                    return -1;
                }
                id = params[i];
            } else {
                // Checksum
                Matcher mr = alpha.matcher(params[i]);
                if (!mr.matches()) {
                    System.err.println("Failed match: " + params[i]);
                    return -1;
                }
                checksum = params[i];
            }
        }

        // Parse OK
        Map<Character, Integer> count = name.chars().filter(Character::isLetter).collect(TreeMap::new, (m, c) -> m.merge((char) c, 1, Integer::sum), Map::putAll);
        String common = "";
        for (Object ch : count.entrySet().stream().sorted((l, r) -> r.getValue().compareTo(l.getValue())).toArray()) {
            common += ((Entry) ch).getKey();
            if (common.length() == 5) {
                break;
            }
        }

        if (!common.equals(checksum)) {
            return -1;
        }

        String decoded = "";
        int shift = Integer.parseInt(id) % 26;
        for (int i = 0; i < encoded.length(); i++) {
            if (encoded.charAt(i) != ' ') {
                if (encoded.charAt(i) + shift > 'z') {
                    decoded += (char) ('a' + shift - ('z' - encoded.charAt(i)) - 1);
                } else {
                    decoded += (char) (encoded.charAt(i) + shift);
                }
            } else {
                decoded += " ";
            }
        }
        //System.out.println("Decoded[" + id + "]: " + decoded);
        if (decoded.contains("northpole")) {
            store = Integer.parseInt(id);
        }

        return Integer.parseInt(id);
    }
}
