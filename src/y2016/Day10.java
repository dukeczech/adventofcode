/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.LineProcessor;
import adventofcode.ReadLineFileReader;
import adventofcode.TaskSolver;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day10 extends TaskSolver implements LineProcessor {

    private Map<Integer, Bot> bots = null;
    private Map<Integer, Output> output = null;
    private Map<String, Boolean> actions = null;

    public Day10() {
        bots = new HashMap<>();
        output = new HashMap<>();
        actions = new LinkedHashMap<>();
    }

    @Override
    public void processLine(String line) {
        actions.put(line.trim(), false);
    }

    @Override
    public void process() {
        ReadLineFileReader reader = new ReadLineFileReader("input/day10.txt", this);
        reader.start();

        Pattern actionLine = Pattern.compile("(?<put>value (\\d+) goes to bot (\\d+))|(?<gives>bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+))");

        boolean done = false;
        while (!done) {
            done = true;
            for (Map.Entry<String, Boolean> action : actions.entrySet()) {
                if (!action.getValue()) {
                    done = false;
                    Matcher mr = actionLine.matcher(action.getKey());

                    if (mr.find()) {
                        if (mr.group("put") != null) {
                            int value = Integer.parseInt(mr.group(2));
                            int id = Integer.parseInt(mr.group(3));
                            put(id, value);
                            action.setValue(true);
                        } else if (mr.group("gives") != null) {
                            int id = Integer.parseInt(mr.group(5));
                            String lowTarget = mr.group(6);
                            int lowId = Integer.parseInt(mr.group(7));
                            String highTarget = mr.group(8);
                            int highId = Integer.parseInt(mr.group(9));
                            if (give(id, lowTarget, lowId, highTarget, highId)) {
                                action.setValue(true);
                            }
                        } else {
                            System.err.println("Unknown action (" + action + ")!");
                        }
                    } else {
                        System.err.println("Unknown action (" + action + ")!");
                    }
                }
            }
        }
        //bots.forEach((k, v) -> System.out.println("Bot: " + v));
        //output.forEach((k, v) -> System.out.println("Output: " + v));

        int result = 1;
        for (Map.Entry<Integer, Output> out : output.entrySet()) {
            if (out.getKey() < 3) {
                BinaryOperator<Integer> mult = (accumulator, value) -> accumulator * value;
                result = out.getValue().getValues().stream().map((i) -> i).reduce(result, mult);
            }
        }
        System.out.println("Result of multiply together the values of one chip in each of outputs 0, 1, and 2: " + result);
    }

    private void put(int id, int value) {
        Bot bot = bots.getOrDefault(id, new Bot(id));
        bot.addValue(value);
        bots.put(id, bot);
    }

    private boolean give(int id, String lowTarget, int lowId, String highTarget, int highId) {
        if (bots.containsKey(id)) {
            Bot bot = bots.get(id);
            if (bot.hasValues(61, 17)) {
                System.out.println("Bot " + bot.getID() + " is responsible for comparing value-61 microchips with value-17 microchips.");
            }
            if (bot.isReady()) {
                switch (lowTarget) {
                    case "bot":
                        Bot lowBot = bots.getOrDefault(lowId, new Bot(lowId));
                        lowBot.addValue(bot.getLow());
                        bots.put(lowId, lowBot);
                        break;
                    case "output":
                        Output lowOut = output.getOrDefault(lowId, new Output(lowId));
                        lowOut.addValue(bot.getLow());
                        output.put(lowId, lowOut);
                        break;
                    default:
                        System.err.println("Unknown target (" + lowTarget + ")!");
                        break;
                }
                switch (highTarget) {
                    case "bot":
                        Bot highBot = bots.getOrDefault(highId, new Bot(highId));
                        highBot.addValue(bot.getHigh());
                        bots.put(highId, highBot);
                        break;
                    case "output":
                        Output highOut = output.getOrDefault(highId, new Output(highId));
                        highOut.addValue(bot.getHigh());
                        output.put(highId, highOut);
                        break;
                    default:
                        System.err.println("Unknown target (" + highTarget + ")!");
                        break;
                }
                bot.removeLow();
                bot.removeHigh();
                return true;
            }
        }
        return false;
    }

    private class Bot {

        private int id = 0;
        private Set<Integer> values = null;

        public Bot(int number) {
            id = number;
            values = new HashSet<>();
        }

        public int getID() {
            return id;
        }

        public int getCount() {
            return values.size();
        }

        public boolean isReady() {
            return values.size() == 2;
        }

        public boolean hasValues(int v1, int v2) {
            return values.contains(v1) && values.contains(v2);
        }

        public void addValue(int value) {
            values.add(value);
        }

        public Integer getLow() {
            return values.stream().min(Comparator.naturalOrder()).get();
        }

        public Integer getHigh() {
            return values.stream().max(Comparator.naturalOrder()).get();
        }

        public void removeLow() {
            values.remove(getLow());
        }

        public void removeHigh() {
            values.remove(getHigh());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(id).append("]:");
            values.stream().forEach((v) -> {
                sb.append(" ").append(v);
            });
            return sb.toString();
        }
    }

    private class Output {

        private int id = 0;
        private Set<Integer> values = null;

        public Output(int number) {
            id = number;
            values = new HashSet<>();
        }

        public int getID() {
            return id;
        }

        public int getCount() {
            return values.size();
        }

        public void addValue(int value) {
            values.add(value);
        }

        public Set<Integer> getValues() {
            return values;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(id).append("]:");
            values.stream().forEach((v) -> {
                sb.append(" ").append(v);
            });
            return sb.toString();
        }
    }

}
