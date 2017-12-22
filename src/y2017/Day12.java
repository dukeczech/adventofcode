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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author hebik
 */
public class Day12 extends TaskSolver {

    private String input = null;
    private Map<Integer, Program> programs = null;

    public Day12() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day12-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day12.class.getName()).log(Level.SEVERE, null, ex);
        }
        programs = new HashMap<>();

        try (Scanner sc = new Scanner(input)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Pattern p = Pattern.compile("(\\d+) <-> ([\\d, ]*)");
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    int id = Integer.parseInt(m.group(1));
                    List<Integer> others = Arrays.stream(m.group(2).split(",")).map(s -> s.trim()).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

                    Program prog = programs.getOrDefault(id, new Program(id));
                    others.stream().forEach((chid) -> {
                        prog.addProgram(chid);
                    });

                    programs.put(id, prog);
                }
            }
        }
    }

    private Set<Program> walk(Program prg, Map<Integer, Program> programs, Set<Program> remaining) {
        remaining.remove(prg);
        prg.getPrograms().stream().filter((ch) -> (remaining.contains(programs.get(ch)))).forEach((ch) -> {
            walk(programs.get(ch), programs, remaining);
        });
        return remaining;
    }

    private void part1() {
        int groups = 0;
        Set<Program> remaining = new HashSet<>();

        programs.entrySet().stream().forEach((pair) -> {
            remaining.add((Program) pair.getValue());
        });

        Program main = programs.get(0);
        walk(main, programs, remaining);
        groups++;

        System.out.println("[1] Number of programs in group ID 0: " + (programs.size() - remaining.size()));

        while (!remaining.isEmpty()) {
            main = programs.get(remaining.iterator().next().id);
            walk(main, programs, remaining);
            groups++;
        }
        System.out.println("[2] Number of groups: " + groups);
    }

    private void part2() {

    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    class Program {

        private int id = 0;
        private List<Integer> programs = null;

        public Program(int pid) {
            id = pid;
            programs = new ArrayList<>();
        }

        public Program(int pid, List<Integer> prg) {
            this(pid);
            programs.addAll(prg);
        }

        public List<Integer> getPrograms() {
            return programs;
        }

        public void addProgram(Integer p) {
            programs.add(p);
        }

        @Override
        public int hashCode() {
            return this.id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Program other = (Program) obj;
            return this.id == other.id;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(id).append(" <->");
            String separator = "";
            for (Integer program : programs) {
                sb.append(separator).append(" ").append(program);
                separator = ",";
            }
            return sb.toString();
        }
    }
}
