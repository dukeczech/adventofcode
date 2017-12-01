/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.LineProcessor;
import adventofcode.ReadLineFileReader;
import adventofcode.TaskSolver;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day15 extends TaskSolver implements LineProcessor {

    private List<Disc> dicss = null;

    public Day15() {
        dicss = new ArrayList<>();
    }

    @Override
    public void processLine(String line) {
        Pattern pt = Pattern.compile("Disc #(\\d+) has (\\d+) positions; at time=(\\d+), it is at position (\\d+).");
        Matcher mr = pt.matcher(line.trim());
        if (mr.matches()) {
            Disc di = new Disc(Integer.parseInt(mr.group(2)), Integer.parseInt(mr.group(3)), Integer.parseInt(mr.group(4)));
            dicss.add(di);
        }
    }

    @Override
    public void process() {
        ReadLineFileReader reader = new ReadLineFileReader("input/day15.txt", this);
        reader.start();

        part1();
        part2();
    }

    public void part1() {
        for (int time = 0; time < Integer.MAX_VALUE; time++) {
            if (pass(time)) {
                System.out.println("The first capsule would fall through the discs at time: " + time);
                break;
            }
        }
    }

    public void part2() {
        for (int time = 0; time < Integer.MAX_VALUE; time++) {
            if (time == 1) {
                // Add additional disc
                dicss.add(new Disc(11, 0, 0));
            }
            if (pass(time)) {
                System.out.println("The second capsule would fall through the discs at time: " + time);
                break;
            }
        }
    }

    private boolean pass(int time) {
        // One second elapses before the first disc is reached
        time++;
        for (Disc dics : dicss) {
            if (dics.getState(time) != 0) {
                return false;
            }
            time++;
        }
        return true;
    }

    class Disc {

        private int positions = 0;
        private int state = 0;

        public Disc(int pos, int time, int st) {
            positions = pos;
            state = (st + time) % positions;
        }

        public int getState() {
            return state;
        }

        public int getState(int time) {
            return (state + time) % positions;
        }

        @Override
        public String toString() {
            return "Disc has " + positions + "; at time=0, it is at position " + state + ".";
        }

    }
}
