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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private List<String> spreads = new ArrayList<>();

    public Day12() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day12-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day12.class.getName()).log(Level.SEVERE, null, ex);
        }

        initial = Arrays.stream(input.split("\n")).filter(l -> l.startsWith("initial state")).findFirst().map(l -> l.replace("initial state: ", "")).get();
        spreads = Arrays.stream(input.split("\n")).filter(l -> l.startsWith(".") || l.startsWith("#")).collect(Collectors.toList());
    }

    private void part1() {
        System.out.println("[1] After 20 generations, the sum of the numbers of all pots which contain a plant: " + 0);
    }

    private void part2() {
        System.out.println("[2] After 20 generations, the sum of the numbers of all pots which contain a plant: " + 0);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

}
