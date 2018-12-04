/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode;

import java.util.ArrayList;
import java.util.List;
import y2018.*;

/**
 *
 * @author karel.hebik
 */
public class AdventOfCode {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        List<TaskSolver> days = new ArrayList<>();

        //days.add(new Day1());
        days.add(new Day2());
        /*days.add(new Day3());
        days.add(new Day4());
        days.add(new Day5());
        days.add(new Day6());
        days.add(new Day7());
        days.add(new Day8());
        days.add(new Day9());
        days.add(new Day10());
        days.add(new Day11());
        days.add(new Day12());
        days.add(new Day13());
        days.add(new Day14());
        days.add(new Day15());
        days.add(new Day16());
        days.add(new Day17());
        days.add(new Day18());
        days.add(new Day19());
        days.add(new Day20());*/

        for (TaskSolver day : days) {
            day.run();
        }

        long estimatedTime = System.currentTimeMillis() - startTime;

        System.out.println("Total time elapsed: " + estimatedTime + "ms");
    }

}
