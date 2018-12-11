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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author hebik
 */
public class Day7 extends TaskSolver {

    private String input = null;
    private Map<Character, Step> steps = new HashMap<>();

    public Day7() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day7-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day7.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parse() {
        steps.clear();

        Pattern p = Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin.");
        Stream.of(input.split("\n")).forEach(ln -> {
            Matcher m = p.matcher(ln);
            if (m.matches()) {
                Step s1 = steps.getOrDefault(m.group(1).charAt(0), new Step(m.group(1).charAt(0)));
                Step s2 = steps.getOrDefault(m.group(2).charAt(0), new Step(m.group(2).charAt(0)));
                s1.addNext(s2);
                s2.prev++;
                steps.put(m.group(1).charAt(0), s1);
                steps.put(m.group(2).charAt(0), s2);
            } else {
                System.err.println("Input error!");
            }
        });
    }

    private void part1() {
        parse();

        List<Step> queue = steps.values().stream().filter(s -> s.prev == 0).collect(Collectors.toList());

        String path = "";
        while (!queue.isEmpty()) {
            // Sort next steps
            queue = queue.stream().distinct().sorted().collect(Collectors.toList());
            Step current = queue.get(0);
            queue.remove(0);

            path += current.name;

            current.next.stream().forEach(s -> s.prev--);
            queue.addAll(current.next.stream().filter(s -> s.prev == 0).collect(Collectors.toList()));
        }

        System.out.println("[1] Order the steps in the instructions be completed: " + path);
    }

    private void part2() {
        parse();

        String path = "";
        Step[] workers = new Step[5];
        int time = 1;
        for (time = 1; time < 1000000; time++) {
            for (int i = 0; i < workers.length; i++) {
                if (workers[i] == null) {
                    // Assign a step
                    for (Step step : steps.values()) {
                        if (step.prev == 0 && !step.working) {
                            step.working = true;
                            workers[i] = step;
                            //System.out.println("Worker " + i + " started " + step.name);
                            break;
                        }
                    }
                }
            }
            for (int i = 0; i < workers.length; i++) {
                if (workers[i] != null) {
                    workers[i].time--;
                    if (workers[i].time == 0) {
                        for (Step next : workers[i].next) {
                            next.prev--;
                        }
                        //System.out.println("Worker " + i + " finished " + workers[i].name);
                        path += workers[i].name;
                        workers[i] = null;
                    }
                }
            }
            if (steps.values().stream().allMatch(s -> s.time == 0)) {
                break;
            }
        }

        System.out.println("[2] All steps done after: " + time);
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    class Step implements Comparable<Step> {

        public Character name = null;
        public int prev = 0;
        public List<Step> next = new ArrayList<>();
        public int time = 0;
        public boolean working = false;

        public Step(char n) {
            name = n;
            time = 60 + name - 'A' + 1;
        }

        public void addNext(Step s) {
            next.add(s);
        }

        @Override
        public int hashCode() {
            return 97 * 7 + Objects.hashCode(this.name);
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
            final Step other = (Step) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(Step o) {
            return name.compareTo(o.name);
        }

    }
}
