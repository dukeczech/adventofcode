/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2015;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day13 {

    String input = null;
    Map<String, Integer> names = null;
    int[][] happiness = null;
    ArrayList<String> bestOrder = null;
    int bestHappiness = 0;

    public Day13() {
        input = "Alice would lose 2 happiness units by sitting next to Bob.\n"
                + "Alice would lose 62 happiness units by sitting next to Carol.\n"
                + "Alice would gain 65 happiness units by sitting next to David.\n"
                + "Alice would gain 21 happiness units by sitting next to Eric.\n"
                + "Alice would lose 81 happiness units by sitting next to Frank.\n"
                + "Alice would lose 4 happiness units by sitting next to George.\n"
                + "Alice would lose 80 happiness units by sitting next to Mallory.\n"
                + "Bob would gain 93 happiness units by sitting next to Alice.\n"
                + "Bob would gain 19 happiness units by sitting next to Carol.\n"
                + "Bob would gain 5 happiness units by sitting next to David.\n"
                + "Bob would gain 49 happiness units by sitting next to Eric.\n"
                + "Bob would gain 68 happiness units by sitting next to Frank.\n"
                + "Bob would gain 23 happiness units by sitting next to George.\n"
                + "Bob would gain 29 happiness units by sitting next to Mallory.\n"
                + "Carol would lose 54 happiness units by sitting next to Alice.\n"
                + "Carol would lose 70 happiness units by sitting next to Bob.\n"
                + "Carol would lose 37 happiness units by sitting next to David.\n"
                + "Carol would lose 46 happiness units by sitting next to Eric.\n"
                + "Carol would gain 33 happiness units by sitting next to Frank.\n"
                + "Carol would lose 35 happiness units by sitting next to George.\n"
                + "Carol would gain 10 happiness units by sitting next to Mallory.\n"
                + "David would gain 43 happiness units by sitting next to Alice.\n"
                + "David would lose 96 happiness units by sitting next to Bob.\n"
                + "David would lose 53 happiness units by sitting next to Carol.\n"
                + "David would lose 30 happiness units by sitting next to Eric.\n"
                + "David would lose 12 happiness units by sitting next to Frank.\n"
                + "David would gain 75 happiness units by sitting next to George.\n"
                + "David would lose 20 happiness units by sitting next to Mallory.\n"
                + "Eric would gain 8 happiness units by sitting next to Alice.\n"
                + "Eric would lose 89 happiness units by sitting next to Bob.\n"
                + "Eric would lose 69 happiness units by sitting next to Carol.\n"
                + "Eric would lose 34 happiness units by sitting next to David.\n"
                + "Eric would gain 95 happiness units by sitting next to Frank.\n"
                + "Eric would gain 34 happiness units by sitting next to George.\n"
                + "Eric would lose 99 happiness units by sitting next to Mallory.\n"
                + "Frank would lose 97 happiness units by sitting next to Alice.\n"
                + "Frank would gain 6 happiness units by sitting next to Bob.\n"
                + "Frank would lose 9 happiness units by sitting next to Carol.\n"
                + "Frank would gain 56 happiness units by sitting next to David.\n"
                + "Frank would lose 17 happiness units by sitting next to Eric.\n"
                + "Frank would gain 18 happiness units by sitting next to George.\n"
                + "Frank would lose 56 happiness units by sitting next to Mallory.\n"
                + "George would gain 45 happiness units by sitting next to Alice.\n"
                + "George would gain 76 happiness units by sitting next to Bob.\n"
                + "George would gain 63 happiness units by sitting next to Carol.\n"
                + "George would gain 54 happiness units by sitting next to David.\n"
                + "George would gain 54 happiness units by sitting next to Eric.\n"
                + "George would gain 30 happiness units by sitting next to Frank.\n"
                + "George would gain 7 happiness units by sitting next to Mallory.\n"
                + "Mallory would gain 31 happiness units by sitting next to Alice.\n"
                + "Mallory would lose 32 happiness units by sitting next to Bob.\n"
                + "Mallory would gain 95 happiness units by sitting next to Carol.\n"
                + "Mallory would gain 91 happiness units by sitting next to David.\n"
                + "Mallory would lose 66 happiness units by sitting next to Eric.\n"
                + "Mallory would lose 75 happiness units by sitting next to Frank.\n"
                + "Mallory would lose 99 happiness units by sitting next to George.";
    }

    public void process(boolean withMe) {
        bestHappiness = 0;
        names = new HashMap<>();
        int count = 0;
        Pattern r = Pattern.compile("(\\w+) .*");
        Scanner sc = new Scanner(input);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = r.matcher(line);
            if (m.matches()) {
                if (!names.containsKey(m.group(1))) {
                    names.put(m.group(1), count);
                    count++;
                }
            }
        }
        if(withMe){
            names.put("Me", count);
        }
        System.out.println("There are " + names.size() + " people");
        happiness = new int[names.size()][names.size()];

        r = Pattern.compile("(\\w+) would (lose|gain) (\\d+) happiness units by sitting next to (\\w+)\\.");
        sc = new Scanner(input);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = r.matcher(line);
            if (m.matches()) {
                String name1 = m.group(1);
                String name2 = m.group(4);
                int units = Integer.parseInt(m.group(3));
                if (m.group(2).equals("lose")) {
                    units = -units;
                }
                happiness[names.get(name1)][names.get(name2)] = units;
            }
        }
        
        ArrayList<String> order = new ArrayList<>();
        ArrayList<String> people = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            people.add(i, getName(i));
        }
        bruteForceFindBestOrder(order, people);
    }

    public void bruteForceFindBestOrder(ArrayList<String> order, ArrayList<String> notSitting) {
        if (!notSitting.isEmpty()) {
            for (int i = 0; i < notSitting.size(); i++) {
                String justRemoved = notSitting.remove(0);
                ArrayList<String> newRoute = (ArrayList<String>) order.clone();
                newRoute.add(justRemoved);

                bruteForceFindBestOrder(newRoute, notSitting);
                notSitting.add(justRemoved);
            }
        } else {
            if (isBestOrder(order)) {
                bestOrder = order;
            }
        }
    }

    private boolean isBestOrder(ArrayList<String> order) {
        int happy = 0;
        for (int i = 0; i < order.size(); i++) {
            String name1 = order.get(i % order.size());
            String name2 = order.get((i + 1) % order.size());
            //System.out.println(name1 + " ** " + name2 + " = " + getHappiness(name1, name2));
            happy += getHappiness(name1, name2);
        }
        if (happy >= bestHappiness) {
            bestHappiness = happy;
            return true;
        }
        return false;
    }

    public int getBestHappiness() {
        return bestHappiness;
    }

    private String getName(int index) {
        for (Map.Entry pair : names.entrySet()) {
            if (((Integer) pair.getValue()) == index) {
                return (String) pair.getKey();
            }
        }
        return null;
    }

    private int getHappiness(String a, String b) {
        return happiness[names.get(a)][names.get(b)] + happiness[names.get(b)][names.get(a)];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("       ");
        for (int i = 0; i < happiness[0].length; i++) {
            sb.append(getName(i));
            sb.append(" ");
        }
        sb.append("\n");
        for (int i = 0; i < happiness.length; i++) {
            sb.append(getName(i));
            sb.append(" ");
            for (int j = 0; j < happiness[0].length; j++) {
                sb.append(String.format("%3d ", happiness[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
