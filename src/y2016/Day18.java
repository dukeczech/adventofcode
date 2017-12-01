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

/**
 *
 * @author karel.hebik
 */
public class Day18 extends TaskSolver implements LineProcessor {

    private List<Boolean[]> area = null;
    private int safe = 0;

    public Day18() {
        area = new ArrayList<>();
    }

    @Override
    public void processLine(String line) {
        Boolean[] row = new Boolean[line.length()];
        for (int i = 0; i < row.length; i++) {
            switch (line.charAt(i)) {
                case '.':
                    row[i] = false;
                    safe++;
                    break;
                case '^':
                    row[i] = true;
                    break;
                default:
                    System.err.println("Unknown tile type!");
                    break;
            }
        }
        area.add(row);
    }

    @Override
    public void process() {
        ReadLineFileReader reader = new ReadLineFileReader("input/day18.txt", this);
        reader.start();

        for (int i = 0; i < 399999; i++) {
            Boolean[] prev = area.get(0);
            Boolean[] next = new Boolean[prev.length];

            for (int j = 0; j < next.length; j++) {
                boolean left = (j > 0) ? prev[j - 1] : false;
                boolean center = prev[j];
                boolean right = (j < next.length - 1) ? prev[j + 1] : false;
                next[j] = false;
                if (left && center && !right) {
                    next[j] = true;
                }
                if (!left && center && right) {
                    next[j] = true;
                }
                if (left && !center && !right) {
                    next[j] = true;
                }
                if (!left && !center && right) {
                    next[j] = true;
                }
                if (!next[j]) {
                    safe++;
                }
            }
            area.add(next);
            area.remove(0);

            if (i == 38) {
                System.out.println("Safe tiles (40): " + safe);
            }
        }

        System.out.println("Safe tiles (40000): " + safe);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Boolean[] row : area) {
            for (Boolean tile : row) {
                if (tile) {
                    sb.append("^");
                } else {
                    sb.append(".");
                }
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

}
