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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hebik
 */
public class Day8 extends TaskSolver {

    private String input = null;
    private List<Instruction> instructions = new ArrayList<>();
    private Map<String, Integer> registers = new HashMap<>();

    public Day8() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day8-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day8.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Scanner sc = new Scanner(input)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Pattern p = Pattern.compile("([a-zA-Z]+) (inc|dec) (-?\\d+) if ([a-zA-Z]+) (<|<=|==|>|>=|!=) (-?\\d+)");
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    String register = m.group(1);
                    Operation operation = m.group(2).equals("dec") ? Operation.DECREMENT : Operation.INCREMENT;
                    int value = Integer.parseInt(m.group(3));
                    String conditionReg = m.group(4);
                    Operator operator = null;
                    switch (m.group(5)) {
                        case "<":
                            operator = Operator.LESS;
                            break;
                        case "<=":
                            operator = Operator.LESS_OR_EQUAL;
                            break;
                        case "==":
                            operator = Operator.EQUAL;
                            break;
                        case ">":
                            operator = Operator.GREATER;
                            break;
                        case ">=":
                            operator = Operator.GREATER_OR_EQUAL;
                            break;
                        case "!=":
                            operator = Operator.NOT_EQUAL;
                            break;
                        default:
                            System.err.println("Unknown operator!");
                            break;
                    }
                    int conditionVal = Integer.parseInt(m.group(6));

                    Condition condition = new Condition(conditionReg, operator, conditionVal);
                    Instruction instruction = new Instruction(register, operation, value, condition);
                    instructions.add(instruction);
                }
            }
        }
    }

    private void part12() {
        int[] highest = {0, 0};

        instructions.stream().forEach((instruction) -> {
            instruction.execute(registers);
            int now = Collections.max(registers.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getValue();
            if (now > highest[1]) {
                highest[1] = now;
            }
        });
        highest[0] = Collections.max(registers.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getValue();
        System.out.println("[1] The largest value in any register: " + highest[0]);
        System.out.println("[2] The highest value held in any register during this process: " + highest[1]);
    }

    @Override
    protected void process() {
        part12();
    }

    public enum Operation {
        INCREMENT, DECREMENT
    }

    public enum Operator {
        LESS, LESS_OR_EQUAL, EQUAL, GREATER, GREATER_OR_EQUAL, NOT_EQUAL
    }

    class Condition {

        private String register = null;
        private Operator operator = null;
        private int value = 0;

        public Condition(String reg, Operator op, int val) {
            register = reg;
            operator = op;
            value = val;
        }

        public boolean evaluate(Map<String, Integer> registers) {
            int r = registers.getOrDefault(register, 0);
            switch (operator) {
                case LESS:
                    return r < value;
                case LESS_OR_EQUAL:
                    return r <= value;
                case EQUAL:
                    return r == value;
                case GREATER:
                    return r > value;
                case GREATER_OR_EQUAL:
                    return r >= value;
                case NOT_EQUAL:
                    return r != value;
                default:
                    System.err.println("Unknown operator!");
                    break;
            }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(register).append(" ");
            switch (operator) {
                case LESS:
                    sb.append("<");
                    break;
                case LESS_OR_EQUAL:
                    sb.append("<=");
                    break;
                case EQUAL:
                    sb.append("==");
                    break;
                case GREATER:
                    sb.append(">");
                    break;
                case GREATER_OR_EQUAL:
                    sb.append(">=");
                    break;
                case NOT_EQUAL:
                    sb.append("!=");
                    break;
                default:
                    break;
            }
            sb.append(" ").append(value);
            return sb.toString();
        }
    }

    class Instruction {

        private String register = null;
        private Operation operation = null;
        private int amount = 0;
        private Condition condition = null;

        public Instruction(String reg, Operation op, int amt, Condition cond) {
            register = reg;
            operation = op;
            amount = amt;
            condition = cond;
        }

        public boolean execute(Map<String, Integer> registers) {
            int value = registers.getOrDefault(register, 0);
            if (condition.evaluate(registers)) {
                // Modify the register
                if (operation == Operation.DECREMENT) {
                    registers.put(register, value - amount);
                } else {
                    registers.put(register, value + amount);
                }
                return true;
            }
            // Skip the instruction
            return false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(register).append(" ");
            if (operation == Operation.DECREMENT) {
                sb.append("dec ");
            } else {
                sb.append("inc ");
            }
            sb.append(amount).append(" ");
            sb.append("if ");
            sb.append(condition);
            return sb.toString();
        }
    }
}
