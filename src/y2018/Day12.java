/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2018;

import adventofcode.TaskSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author hebik
 */
public class Day12 extends TaskSolver {

    private String input = null;
    private String initial = null;
    private Map<String, String> spreads = new HashMap<>();

    public Day12() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day12-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day12.class.getName()).log(Level.SEVERE, null, ex);
        }

        initial = Arrays.stream(input.split("\n")).filter(l -> l.startsWith("initial state")).findFirst().map(l -> l.replace("initial state: ", "")).get();
        spreads = Arrays.stream(input.split("\n")).filter(l -> l.startsWith(".") || l.startsWith("#")).
                collect(Collectors.toMap(l -> l.split(" => ")[0], l -> l.split(" => ")[1]));
    }

    private void part1() {
        Map<Long, Pots> results = new HashMap<>();

        StringBuilder out = new StringBuilder(initial);
        int pad[] = new int[2];
        for (long i = 1; i <= 50000000000l; i++) {

            int s = out.indexOf("#");
            if (s < 4) {
                for (int j = 0; j < 4 - s; j++) {
                    out.insert(0, ".");
                    pad[0]++;
                }
            } else if (s > 4) {
                out.delete(0, s - 4);
                pad[0] -= s - 4;
            }

            int e = out.lastIndexOf("#") - pad[0];
            int l = out.length() - pad[0];
            if (l - e - 1 < 4) {
                for (int j = 0; j < 4 - l + e + 1; j++) {

                    out.append(".");
                    pad[1]++;
                }
            } else if (l - e - 1 > 4) {
                out.delete(e + pad[0] + 4 + 1, l + pad[0] + 1);
                pad[1] -= (l + pad[0] + 1) - (e + pad[0] + 4 + 1);
            }
            StringBuilder in = new StringBuilder();
            in.append(out);

            out = new StringBuilder();
            out.append("..");

            for (int j = 2; j < in.length() - 2; j++) {
                String actual = in.substring(j - 2, j + 3);
                if (spreads.containsKey(actual)) {
                    out.append(spreads.get(actual));
                } else {
                    out.append(".");
                }
            }
            out.append("..");

            Pots pots = new Pots(out.toString(), pad);

            if (i == 20) {
                System.out.println("[1] After 20 generations, the sum of the numbers of all pots which contain a plant: " + pots.getScore());
            }

            if (results.containsValue(pots)) {
                long last = results.get(i - 1).getScore();
                long now = pots.getScore();
                long sum = now + ((50000000000l - i) * (now - last));

                System.out.println("[2] After 50000000000 generations, the sum of the numbers of all pots which contain a plant: " + sum);
                break;
            }
            results.put(i, pots);
        }
    }

    private void part2() {
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    class Pots {

        public String pots = null;
        public int padding[] = new int[2];

        public Pots(String s, int[] p) {
            pots = s;
            System.arraycopy(p, 0, padding, 0, p.length);
        }

        public int getScore() {
            int sum = 0;
            for (int j = 0; j < pots.length(); j++) {
                if (pots.charAt(j) == '#') {
                    sum += j - padding[0];
                }
            }
            return sum;
        }

        @Override
        public int hashCode() {
            return getScore();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Pots other = (Pots) obj;
            if (!Objects.equals(this.pots, other.pots)) {
                return false;
            }
            return true;
        }
    }
}
