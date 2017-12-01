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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day12 extends TaskSolver implements LineProcessor {

    private List<Instruction> instructions = null;
    private Map<String, Integer> registers = null;

    public Day12() {
        instructions = new ArrayList<>();
        registers = new HashMap<>();
        registers.put("a", 0);
        registers.put("b", 0);
        registers.put("c", 1);
        registers.put("d", 0);
    }

    @Override
    public void processLine(String line) {
        Pattern instrLine = Pattern.compile("(?<copy>cpy ([a-d]|\\d+) ([a-d]))|(?<jump>jnz ([a-d]|\\d+) (-?\\d+))|(?<increment>inc ([a-d]))|(?<decrement>dec ([a-d]))");
        Matcher mr = instrLine.matcher(line.trim());

        if (mr.matches()) {
            if (mr.group("copy") != null) {
                instructions.add(new Copy(mr.group(2), mr.group(3)));
            } else if (mr.group("jump") != null) {
                instructions.add(new Jump(mr.group(5), mr.group(6)));
            } else if (mr.group("increment") != null) {
                instructions.add(new Increment(mr.group(8)));
            } else if (mr.group("decrement") != null) {
                instructions.add(new Decrement(mr.group(10)));
            } else {
                System.err.println("Unknown instruction (" + line + ")!");
            }
        }
    }

    @Override
    public void process() {
        ReadLineFileReader reader = new ReadLineFileReader("input/day12.txt", this);
        reader.start();

        for (int i = 0; i < instructions.size(); i++) {
            Instruction ins = instructions.get(i);
            //System.out.println(ins);

            String p1 = ins.getParam1();
            if (ins instanceof Copy) {
                Copy cpy = (Copy) ins;
                String p2 = cpy.getParam2();
                if (p1.matches("[a-d]")) {
                    // Copies the value of a register x into register y.
                    registers.put(p2, registers.get(p1));
                } else {
                    // Copies an integer x into register y.
                    registers.put(p2, Integer.parseInt(p1));
                }
            } else if (ins instanceof Jump) {
                Jump jmp = (Jump) ins;
                String p2 = jmp.getParam2();
                // Jumps to an instruction y away (positive means forward; negative means backward), but only if x is not zero.
                if (p1.matches("[a-d]")) {
                    if (registers.get(p1) != 0) {
                        i += Integer.parseInt(p2) - 1;
                    }
                } else if (Integer.parseInt(p1) != 0) {
                    i += Integer.parseInt(p2) - 1;
                }
            } else if (ins instanceof Decrement) {
                // Decreases the value of register x by one.
                registers.put(p1, registers.get(p1) - 1);
            } else if (ins instanceof Increment) {
                // Increases the value of register x by one.
                registers.put(p1, registers.get(p1) + 1);
            } else {
                System.err.println("Unknown instruction type!");
            }
        }
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Registers:").append(System.lineSeparator());
        registers.entrySet().stream().forEach((pair) -> {
            sb.append(pair.getKey()).append(" = ").append(pair.getValue()).append(System.lineSeparator());
        });
        return sb.toString();
    }

    abstract class Instruction {

        protected String param1 = null;

        public Instruction(String p1) {
            param1 = p1;
        }

        public String getParam1() {
            return param1;
        }
    }

    class Increment extends Instruction {

        public Increment(String p1) {
            super(p1);
        }

        @Override
        public String toString() {
            return "inc " + param1;
        }
    }

    class Decrement extends Instruction {

        public Decrement(String p1) {
            super(p1);
        }

        @Override
        public String toString() {
            return "dec " + param1;
        }
    }

    class Copy extends Instruction {

        protected String param2 = null;

        public Copy(String p1, String p2) {
            super(p1);
            param2 = p2;
        }

        public String getParam2() {
            return param2;
        }

        @Override
        public String toString() {
            return "cpy " + param1 + " " + param2;
        }
    }

    class Jump extends Instruction {

        protected String param2 = null;

        public Jump(String p1, String p2) {
            super(p1);
            param2 = p2;
        }

        public String getParam2() {
            return param2;
        }

        @Override
        public String toString() {
            return "jmp " + param1 + " " + param2;
        }
    }
}
