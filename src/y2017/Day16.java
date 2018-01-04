package y2017;

import adventofcode.TaskSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hebik
 */
public class Day16 extends TaskSolver {

    private String input = null;
    private List<Move> moves = null;

    public Day16() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day16-2017.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day16.class.getName()).log(Level.SEVERE, null, ex);
        }

        moves = new ArrayList<>();

        Pattern p = Pattern.compile("(?:s(\\d+))|(?:x(\\d+)\\/(\\d+))|(?:p(.)\\/(.))");
        for (String move : input.split(",")) {
            Matcher m = p.matcher(move);
            if (m.matches()) {
                if (m.group(1) != null) {
                    // Spin move
                    int count = Integer.parseInt(m.group(1));
                    moves.add(new Spin(count));
                } else if (m.group(2) != null && m.group(3) != null) {
                    // Exchange move
                    int p1 = Integer.parseInt(m.group(2));
                    int p2 = Integer.parseInt(m.group(3));
                    moves.add(new Swap(p1, p2));
                } else if (m.group(4) != null && m.group(5) != null) {
                    // Partner move
                    char ch1 = m.group(4).charAt(0);
                    char ch2 = m.group(5).charAt(0);
                    moves.add(new Swap(ch1, ch2));
                }
            }
        }
    }

    private void part1() {
        StringBuffer line = new StringBuffer("abcdefghijklmnop");
        moves.stream().forEach(m -> m.perform(line));
        System.out.println("[1] Programs standing: " + line.toString());
    }

    private void part2() {
        StringBuffer line = new StringBuffer("abcdefghijklmnop");
        Set<String> stands = new HashSet<>();
        boolean found = false;
        for (int i = 0; i < 1000000000; i++) {
            stands.add(line.toString());
            moves.stream().forEach(m -> m.perform(line));
            if (!found && stands.contains(line.toString())) {
                i = 1000000000 - (1000000000 % stands.size()) - 1;
                found = true;
            }
        }

        System.out.println("[2] Programs standing: " + line.toString());
    }

    @Override
    protected void process() {
        part1();
        part2();

    }

    abstract class Move {

        abstract void perform(StringBuffer line);
    }

    class Spin extends Move {

        private int count = 0;

        public Spin(int c) {
            count = c;
        }

        @Override
        void perform(StringBuffer line) {
            int len = line.length();
            line.append(line.substring(0, len - count)).delete(0, len - count);
        }

    }

    class Swap extends Move {

        private int p[] = null;
        private char ch[] = null;

        public Swap(int p1, int p2) {
            p = new int[]{p1, p2};
        }

        public Swap(char ch1, char ch2) {
            ch = new char[]{ch1, ch2};
        }

        @Override
        void perform(StringBuffer line) {
            if (ch != null) {
                p = new int[]{line.indexOf(ch[0] + ""), line.indexOf(ch[1] + "")};
            }
            char ch1 = line.charAt(p[0]);
            char ch2 = line.charAt(p[1]);
            line.setCharAt(p[0], ch2);
            line.setCharAt(p[1], ch1);
        }
    }
}
