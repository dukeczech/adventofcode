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
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hebik
 */
public class Day8 extends TaskSolver {

    private String input = null;
    private Node root = null;

    public Day8() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day8-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day8.class.getName()).log(Level.SEVERE, null, ex);
        }

        Scanner sc = new Scanner(input);
        root = next(sc);
    }

    public Node next(Scanner sc) {
        Node result = new Node();
        int n1 = sc.nextInt();
        int n2 = sc.nextInt();

        for (int i = 0; i < n1; i++) {
            result.child.add(next(sc));
        }
        for (int i = 0; i < n2; i++) {
            result.metadata.add(sc.nextInt());
        }
        return result;
    }

    private void part1() {

        System.out.println("[1] Sum of all metadata entries: " + root.getSum());
    }

    private void part2() {

        System.out.println("[2] Value of the root node: " + root.getValue());
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    class Node {

        List<Node> child = new ArrayList<>();
        List<Integer> metadata = new ArrayList<>();

        public Node() {
        }

        public int getSum() {
            int total = 0;
            total += child.stream().mapToInt(n -> n.getSum()).sum();
            total += metadata.stream().mapToInt(n -> n).sum();
            return total;
        }

        public int getValue() {
            int value = 0;
            if (child.isEmpty()) {
                value += metadata.stream().mapToInt(n -> n).sum();
            } else {
                value += metadata.stream().mapToInt(n -> (n > 0 && n <= child.size()) ? child.get(n - 1).getValue() : 0).sum();
            }
            return value;
        }
    }
}
