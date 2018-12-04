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
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author hebik
 */
public class Day2 extends TaskSolver {

    private String input = null;

    public Day2() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day2-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void part1() {
        AtomicLong doubles = new AtomicLong(0);
        AtomicLong triples = new AtomicLong(0);
        Stream.of(input.split("\n")).forEach(s -> {
            Collection<Long> counts = s.chars().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).values();

            if (counts.stream().filter(n -> n > 1 && n < 3).findFirst().isPresent()) {
                doubles.incrementAndGet();
            }
            if (counts.stream().filter(n -> n > 2 && n < 4).findFirst().isPresent()) {
                triples.incrementAndGet();
            }
        });
        System.out.println("[1] Checksum for the list of box IDs: " + doubles.intValue() * triples.intValue());
    }

    private void part2() {
        StringBuilder sb = new StringBuilder();
        Stream.of(input.split("\n")).forEach(s1 -> {
            Optional<String> common = Stream.of(input.split("\n")).filter(s2 -> !s1.equals(s2) && LevenshteinDistance.computeLevenshteinDistance(s1, s2) == 1).findFirst();
            if (common.isPresent() && sb.length() == 0) {
                sb.append(LevenshteinDistance.getCommonLetters(s1, common.get()));
            }
        });
        System.out.println("[2] Letters common between the two correct box IDs: " + sb.toString());
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    private static class LevenshteinDistance {

        private static int minimum(int a, int b, int c) {
            return Math.min(Math.min(a, b), c);
        }

        public static int computeLevenshteinDistance(String lhs, String rhs) {
            int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

            for (int i = 0; i <= lhs.length(); i++) {
                distance[i][0] = i;
            }
            for (int j = 1; j <= rhs.length(); j++) {
                distance[0][j] = j;
            }

            for (int i = 1; i <= lhs.length(); i++) {
                for (int j = 1; j <= rhs.length(); j++) {
                    distance[i][j] = minimum(
                            distance[i - 1][j] + 1,
                            distance[i][j - 1] + 1,
                            distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));
                }
            }

            return distance[lhs.length()][rhs.length()];
        }

        public static String getCommonLetters(String lhs, String rhs) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lhs.length(); i++) {
                if (lhs.charAt(i) == rhs.charAt(i)) {
                    sb.append(lhs.charAt(i));
                }
            }
            return sb.toString();
        }
    }

}
