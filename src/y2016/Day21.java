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
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day21 extends TaskSolver implements LineProcessor {

    private StringBuilder password = null;
    private List<String> operations = null;

    public Day21() {
        password = new StringBuilder("abcdefgh");
        operations = new ArrayList<>();
    }

    @Override
    public void processLine(String line) {
        operations.add(line.trim());
    }

    @Override
    public void process() {
        ReadLineFileReader reader = new ReadLineFileReader("input/day21.txt", this);
        reader.start();

        password = new StringBuilder("abcdefgh");
        part1();

        password = new StringBuilder("fbgdceah");
        part2();
    }

    private void part1() {
        Pattern commLine = Pattern.compile("(?:swap position (\\d+) with position (\\d+))|(?:swap letter (.) with letter (.))|(?:reverse positions (\\d+) through (\\d+))|(?:rotate (left|right) (\\d+) step[s]?)|(?:move position (\\d+) to position (\\d+))|(?:rotate based on position of letter (.))");
        for (String operation : operations) {
            Matcher mr = commLine.matcher(operation);

            if (mr.matches()) {
                if (mr.group(1) != null) {
                    swap(Integer.parseInt(mr.group(1)), Integer.parseInt(mr.group(2)));
                } else if (mr.group(3) != null) {
                    swap(mr.group(3).charAt(0), mr.group(4).charAt(0));
                } else if (mr.group(5) != null) {
                    reverse(Integer.parseInt(mr.group(5)), Integer.parseInt(mr.group(6)));
                } else if (mr.group(7) != null) {
                    if (mr.group(7).equals("left")) {
                        rotateLeft(Integer.parseInt(mr.group(8)));
                    } else {
                        rotateRight(Integer.parseInt(mr.group(8)));
                    }
                } else if (mr.group(9) != null) {
                    move(Integer.parseInt(mr.group(9)), Integer.parseInt(mr.group(10)));
                } else if (mr.group(11) != null) {
                    rotate(mr.group(11).charAt(0), false);
                }
            } else {
                System.err.println("Unknown operation (" + operation + ")!");
            }
        }
        System.out.println("Result of scrambling abcdefgh: " + password);
    }

    private void part2() {
        Pattern commLine = Pattern.compile("(?:swap position (\\d+) with position (\\d+))|(?:swap letter (.) with letter (.))|(?:reverse positions (\\d+) through (\\d+))|(?:rotate (left|right) (\\d+) step[s]?)|(?:move position (\\d+) to position (\\d+))|(?:rotate based on position of letter (.))");

        Collections.reverse(operations);

        for (String operation : operations) {
            Matcher mr = commLine.matcher(operation);

            if (mr.matches()) {
                if (mr.group(1) != null) {
                    swap(Integer.parseInt(mr.group(1)), Integer.parseInt(mr.group(2)));
                } else if (mr.group(3) != null) {
                    swap(mr.group(3).charAt(0), mr.group(4).charAt(0));
                } else if (mr.group(5) != null) {
                    reverse(Integer.parseInt(mr.group(5)), Integer.parseInt(mr.group(6)));
                } else if (mr.group(7) != null) {
                    if (mr.group(7).equals("left")) {
                        rotateRight(Integer.parseInt(mr.group(8)));
                    } else {
                        rotateLeft(Integer.parseInt(mr.group(8)));
                    }
                } else if (mr.group(9) != null) {
                    move(Integer.parseInt(mr.group(10)), Integer.parseInt(mr.group(9)));
                } else if (mr.group(11) != null) {
                    rotate(mr.group(11).charAt(0), true);
                }
            } else {
                System.err.println("Unknown operation (" + operation + ")!");
            }
        }
        System.out.println("Result of scrambling abcdefgh: " + password);
    }

    private void swap(int pos1, int pos2) {
        char ch1 = password.charAt(pos1);
        char ch2 = password.charAt(pos2);
        password.setCharAt(pos1, ch2);
        password.setCharAt(pos2, ch1);
    }

    private void swap(char ch1, char ch2) {
        int pos1 = password.indexOf(ch1 + "");
        int pos2 = password.indexOf(ch2 + "");
        swap(pos1, pos2);
    }

    private void reverse(int pos1, int pos2) {
        StringBuilder sb = new StringBuilder(password.substring(pos1, pos2 + 1));
        sb.reverse();
        password.replace(pos1, pos2 + 1, sb.toString());
    }

    private void rotateLeft(int count) {
        while (count > 0) {
            password.append(password.charAt(0));
            password.deleteCharAt(0);
            count--;
        }
    }

    private void rotateRight(int count) {
        password.reverse();
        rotateLeft(count);
        password.reverse();
    }

    private void rotate(char ch, boolean reverse) {
        int pos = password.indexOf(ch + "");
        if (!reverse) {
            rotateRight(pos + 1);
            if (pos >= 4) {
                rotateRight(1);
            }
        } else {
            int count = 0;
            switch (pos) {
                case 0:
                case 1:
                    count = 1;
                    break;
                case 2:
                    count = 6;
                    break;
                case 3:
                    count = 2;
                    break;
                case 4:
                    count = 7;
                    break;
                case 5:
                    count = 3;
                    break;
                case 6:
                    count = 0;
                    break;
                case 7:
                    count = 4;
                    break;
                default:
                    break;
            }
            rotateLeft(count);
        }
    }

    private void move(int pos1, int pos2) {
        char ch = password.charAt(pos1);
        password.deleteCharAt(pos1);
        password.insert(pos2, ch);

    }
}
