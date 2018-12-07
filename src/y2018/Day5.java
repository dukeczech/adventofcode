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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author hebik
 */
public class Day5 extends TaskSolver {

    private String input = null;

    public Day5() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day5-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day5.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void part1() {
        System.out.println("[1] Units remain after fully reacting the polymer scanned: " + react(input).length());
    }

    private void part2() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Map<Character, Integer> polymers = new HashMap<>();
        alphabet.chars().mapToObj(c -> (char) c).forEach(c -> polymers.put(c, react(removeUnit(input, c)).length()));
        Integer shortest = polymers.values().stream().min((p1, p2) -> Integer.compare(p1, p2)).get();

        System.out.println("[2] Length of the shortest polymer that can be produced by removing all units of exactly one type and fully reacting the result: " + shortest.intValue());
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    public String removeUnit(String in, Character u) {
        return in.chars().mapToObj(c -> (char) c).filter(c -> Character.toLowerCase(c) != Character.toLowerCase(u)).map(c -> c.toString()).collect(Collectors.joining());
    }

    public String react(String in) {
        StringBuilder sb = new StringBuilder(in);
        for (int i = 0; i < 100000; i++) {
            boolean done = true;
            for (int j = 0; j < sb.length() - 1; j++) {
                Character c1 = sb.charAt(j);
                Character c2 = sb.charAt(j + 1);
                if (Math.abs((int) c1 - (int) c2) == 0x20) {
                    sb.delete(j, j + 2);
                    done = false;
                }
            }
            if (done) {
                break;
            }
        }
        return sb.toString();
    }
}
