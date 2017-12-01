/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2015;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day9 {

    enum PathType {

        SHORTEST,
        LONGEST
    }

    String input = null;
    Map<Key, Integer> map = null;
    ArrayList<String> cities = null;
    private int shortest = Integer.MAX_VALUE;
    private int longest = Integer.MIN_VALUE;
    private ArrayList<String> bestRoute = null;

    public Day9() {
        input = "Faerun to Norrath = 129\n"
                + "Faerun to Tristram = 58\n"
                + "Faerun to AlphaCentauri = 13\n"
                + "Faerun to Arbre = 24\n"
                + "Faerun to Snowdin = 60\n"
                + "Faerun to Tambi = 71\n"
                + "Faerun to Straylight = 67\n"
                + "Norrath to Tristram = 142\n"
                + "Norrath to AlphaCentauri = 15\n"
                + "Norrath to Arbre = 135\n"
                + "Norrath to Snowdin = 75\n"
                + "Norrath to Tambi = 82\n"
                + "Norrath to Straylight = 54\n"
                + "Tristram to AlphaCentauri = 118\n"
                + "Tristram to Arbre = 122\n"
                + "Tristram to Snowdin = 103\n"
                + "Tristram to Tambi = 49\n"
                + "Tristram to Straylight = 97\n"
                + "AlphaCentauri to Arbre = 116\n"
                + "AlphaCentauri to Snowdin = 12\n"
                + "AlphaCentauri to Tambi = 18\n"
                + "AlphaCentauri to Straylight = 91\n"
                + "Arbre to Snowdin = 129\n"
                + "Arbre to Tambi = 53\n"
                + "Arbre to Straylight = 40\n"
                + "Snowdin to Tambi = 15\n"
                + "Snowdin to Straylight = 99\n"
                + "Tambi to Straylight = 70";

        map = new HashMap<>();
        cities = new ArrayList();

        Pattern r = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");
        Scanner sc = new Scanner(input);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = r.matcher(line);
            if (m.matches()) {
                Key k = new Key(m.group(1), m.group(2));
                map.put(k, Integer.parseInt(m.group(3)));
                if (!cities.contains(m.group(1))) {
                    cities.add(m.group(1));
                }
                if (!cities.contains(m.group(2))) {
                    cities.add(m.group(2));
                }
            } else {
                System.err.println("Unable to parse!");
                return;
            }
        }
    }

    public int deliverShortest() {
        ArrayList<String> route = new ArrayList<>();
        bruteForceFindBestRoute(route, cities, PathType.SHORTEST);
        //System.out.println("Shortest route " + bestRoute.toString());
        return shortest;
    }

    public int deliverLongest() {
        ArrayList<String> route = new ArrayList<>();
        bruteForceFindBestRoute(route, cities, PathType.LONGEST);
        //System.out.println("Longest route " + bestRoute.toString());
        return longest;
    }

    public void bruteForceFindBestRoute(ArrayList<String> route, ArrayList<String> citiesNotInRoute, PathType type) {
        if (!citiesNotInRoute.isEmpty()) {
            for (int i = 0; i < citiesNotInRoute.size(); i++) {
                String justRemoved = citiesNotInRoute.remove(0);
                ArrayList<String> newRoute = (ArrayList<String>) route.clone();
                newRoute.add(justRemoved);

                bruteForceFindBestRoute(newRoute, citiesNotInRoute, type);
                citiesNotInRoute.add(justRemoved);
            }
        } else {
            if (isBestRoute(route, type)) {
                bestRoute = route;
            }
        }
    }

    private boolean isBestRoute(ArrayList<String> route, PathType type) {
        int d = getPathDistance(route);
        //System.out.println(route.toString() + "[" + d + "]");
        if (type == PathType.SHORTEST) {
            if (d < shortest) {
                shortest = d;
                return true;
            }
        } else {
            if (d > longest) {
                longest = d;
                return true;
            }
        }
        return false;
    }

    private int getPathDistance(ArrayList<String> way) {
        int d = 0;
        String previous = null;
        for (String c : way) {
            if (previous == null) {
                previous = c;
            } else {
                d += getDistance(previous, c);
                previous = c;
            }
        }
        return d;
    }

    private int getDistance(String x, String y) {
        if (x.equals(y)) {
            return 0;
        }
        Key k = new Key(x, y);
        if (!map.containsKey(k)) {
            System.err.println("No match!");
            return -1;
        }
        return map.get(k);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            sb.append(pair.getKey());
            sb.append(" = ");
            sb.append(pair.getValue());
            sb.append("\n");
        }
        sb.append("-------------------------");
        sb.append("\n");
        for (String c : cities) {
            sb.append(c);
            sb.append(" ");
        }
        return sb.toString();
    }

    public class Key {

        private final String x;
        private final String y;

        public Key(String x, String y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Key)) {
                return false;
            }
            Key key = (Key) o;
            return x.equals(key.x) && y.equals(key.y) || x.equals(key.y) && y.equals(key.x);
        }

        @Override
        public int hashCode() {
            return x.hashCode() + y.hashCode();
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }

    }

}
