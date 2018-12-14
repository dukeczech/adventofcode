/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2018;

import adventofcode.TaskSolver;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 *
 * @author hebik
 */
public class Day9 extends TaskSolver {

    public Day9() {

    }

    private long run(int pl, int count) {
        Deque<Long> marbles = new LinkedList<>();
        long[] players = new long[pl];

        int player = 0;

        marbles.add(0L);
        for (long marble = 1; marble <= count; marble++) {
            if (marble % 23 == 0) {
                marbles.addLast(marbles.pollFirst());
                marbles.addLast(marbles.pollFirst());
                marbles.addLast(marbles.pollFirst());
                marbles.addLast(marbles.pollFirst());
                marbles.addLast(marbles.pollFirst());
                marbles.addLast(marbles.pollFirst());

                players[player] += marble + marbles.pollFirst();
            } else {
                marbles.addFirst(marbles.pollLast());
                marbles.addFirst(marbles.pollLast());
                marbles.addLast(marble);
            }
            player = (player + 1) % players.length;
        }

        return Arrays.stream(players).max().getAsLong();
    }

    private void part1() {
        System.out.println("[1] Winning Elf's score: " + run(452, 70784));
    }

    private void part2() {
        System.out.println("[1] Winning Elf's score: " + run(452, 70784 * 100));
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

}
