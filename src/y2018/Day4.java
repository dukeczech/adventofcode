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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hebik
 */
public class Day4 extends TaskSolver {

    private String input = null;
    Map<Integer, Guard> guards = new HashMap<>();

    public Day4() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day4-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day4.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<String> lines = Arrays.asList(input.split("\n"));
        List<Action> actions = new ArrayList<>();
        lines.forEach(s -> {
            actions.add(new Action(s));
        });
        Collections.sort(actions);

        Guard gd = null;
        for (Action a : actions) {
            String act = a.getAction();
            if (act.startsWith("Guard")) {
                if (gd != null) {
                    guards.put(gd.id, gd);
                }
                int id = Integer.parseInt(act.substring(act.indexOf("#") + 1, act.indexOf(" ", act.indexOf("#"))));
                gd = guards.getOrDefault(id, new Guard(id));
            } else if (act.startsWith("falls")) {
                gd.addFall(a.getDateTime());
            } else if (act.startsWith("wakes")) {
                gd.addAwake(a.getDateTime());
            }
        }
    }

    private void part1() {
        Guard most = guards.values().stream().reduce((a, b) -> a.getSleepTime() > b.getSleepTime() ? a : b).get();

        System.out.println("[1] ID of the guard you chose multiplied by the minute choosen: " + (most.id * most.getMostSleepy()[0]));
    }

    private void part2() {
        Guard most = guards.values().stream().reduce((a, b) -> a.getMostSleepy()[1] > b.getMostSleepy()[1] ? a : b).get();

        System.out.println("[2] ID of the guard you chose multiplied by the minute choosen: " + (most.id * most.getMostSleepy()[0]));
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    private class Action implements Comparable<Action> {

        public Date datetime = null;
        public String action = null;

        public Action(String s) {
            Pattern pt = Pattern.compile("\\[(.+)\\] (Guard #(\\d+) begins shift|wakes up|falls asleep)");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Matcher m = pt.matcher(s);
            if (m.matches()) {
                try {
                    datetime = df.parse(m.group(1));
                    action = m.group(2);
                } catch (ParseException ex) {
                    Logger.getLogger(Day4.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        public Date getDateTime() {
            return datetime;
        }

        public String getAction() {
            return action;
        }

        @Override
        public String toString() {
            return "Action{" + "datetime=" + datetime + ", action=" + action + '}';
        }

        @Override
        public int compareTo(Action o) {
            return datetime.compareTo(o.datetime);
        }
    }

    private class Guard {

        public int id = 0;
        public List<Date> falls = new ArrayList<>();
        public List<Date> awakes = new ArrayList<>();

        public Guard(int i) {
            id = i;
        }

        public void addFall(Date d) {
            falls.add(d);
        }

        public void addAwake(Date d) {
            awakes.add(d);

        }

        public boolean check() {
            if ((falls.size() != awakes.size())) {
                try {
                    System.err.println(this);
                    throw new Exception("List length error: " + falls.size() + " - " + awakes.size());
                } catch (Exception ex) {
                    Logger.getLogger(Day4.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return true;
        }

        public int getSleepTime() {
            check();
            Collections.sort(awakes);

            int time = 0;
            for (int i = 0; i < awakes.size(); i++) {
                time += awakes.get(i).getMinutes() - falls.get(i).getMinutes();
            }
            return time;
        }

        public int[] getMostSleepy() {
            check();
            Collections.sort(awakes);

            int[] result = new int[2];
            int[] minutes = new int[60];
            for (int i = 0; i < falls.size(); i++) {
                for (int j = falls.get(i).getMinutes(); j < awakes.get(i).getMinutes(); j++) {
                    minutes[j] = minutes[j] + 1;
                }
            }
            for (int i = 0; i < minutes.length; i++) {
                if (minutes[i] > result[1]) {
                    result[0] = i;
                    result[1] = minutes[i];
                }
            }
            return result;
        }

        @Override
        public String toString() {
            return "Guard{" + "id=" + id + ", falls=" + falls + ", awakes=" + awakes + '}';
        }
    }
}
