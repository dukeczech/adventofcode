/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.TaskSolver;

/**
 *
 * @author karel.hebik
 */
public class Day19 extends TaskSolver {

    public Day19() {
    }

    @Override
    public void process() {
        final int LIMIT = 3014603;
        int num = 0;
        for (int i = Integer.highestOneBit(LIMIT); i <= LIMIT; i++) {
            if ((i & -i) == i) {
                num = 1;
            } else {
                num += 2;
            }
        }
        System.out.println("Elf " + num + " gets all the presents.");

        num = 0;
        boolean inc = false;
        for (int i = 1; i <= LIMIT; i++) {
            num++;
            if (inc) {
                num++;
            }
            if (2 * num == i) {
                if (inc) {
                    num--;
                }
                inc = !inc;
            }
            if (num == i && i != LIMIT) {
                num = 0;
                if (i > 8) {
                    inc = !inc;
                }
            }
        }
        System.out.println("Elf " + num + " gets all the presents.");
    }
}
