/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2017;

import adventofcode.TaskSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hebik
 */
public class Day4 extends TaskSolver {

    private String input = null;

    public Day4() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day4-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void part1() {
        int valid = 0;
        Scanner sc = new Scanner(input);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] words = line.split("\\s+");
            if (words.length == Arrays.stream(words).distinct().count()) {
                valid++;
            }
        }
        sc.close();

        System.out.println("Valid passphrases: " + valid);
    }

    private void part2() {
        int valid = 0;
        Scanner sc = new Scanner(input);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] words = line.split("\\s+");
            if (words.length == Arrays.stream(words).map(w -> new WordWrapper(w)).distinct().count()) {
                valid++;
            }
        }
        sc.close();

        System.out.println("Valid passphrases: " + valid);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    class WordWrapper {

        String word = null;

        public WordWrapper(String w) {
            word = w;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof WordWrapper)) {
                return false;
            }
            WordWrapper other = (WordWrapper) obj;
            if (Objects.equals(word, other.word)) {
                return true;
            }
            if (word.length() != other.word.length()) {
                return false;
            }
            int[] o1 = word.chars().sorted().toArray();
            int[] o2 = other.word.chars().sorted().toArray();
            if (Arrays.equals(o1, o2)) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }
}
