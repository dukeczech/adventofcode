/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.TaskSolver;
import adventofcode.Utils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day14 extends TaskSolver {

    private String salt = null;
    private Queue<Match> matches = null;
    private Set<Match> hashes = null;

    public Day14() {
        salt = "ngcjuoqr";
        matches = new LinkedList<>();
        hashes = new TreeSet<>();
    }

    @Override
    public void process() {
        part1();
        part2();
    }

    private void part1() {
        matches.clear();
        hashes.clear();
        
        Pattern same3 = Pattern.compile("(.)\\1{2,}");
        Pattern same5 = Pattern.compile("(.)\\1{4,}");

        long limit = Long.MAX_VALUE;
        for (long i = 0; i < limit; i++) {
            String hash = Utils.md5(String.format("%s%d", salt, i));

            Matcher mr = same5.matcher(hash);

            while (mr.find()) {
                if (mr.group().length() >= 5) {
                    for (Match item : matches) {
                        if (item.hasGroup(mr.group().charAt(0), 3)) {
                            //System.out.println(item + " -> " + i + ": [" + hash + "]");

                            hashes.add(item);
                            if (hashes.size() > 64) {
                                // Run this next 1000 loops
                                limit = i + 1000;
                            }
                        }
                    }
                }
            }

            mr = same3.matcher(hash);

            Match ma = new Match(i);
            if (mr.find()) {
                ma.setHash(hash);
            }
            matches.add(ma);

            if (matches.size() > 1000) {
                matches.poll();
            }
        }

        List<Match> keys = new ArrayList(hashes);
        if (keys.size() > 64) {
            do {
                keys.remove(keys.size() - 1);
            } while (keys.size() > 64);
        }

        /*for (Match m : keys) {
            System.out.println(m);
        }*/
        System.out.println("Index " + keys.get(keys.size() - 1).getIndex() + " produces the 64th key");
    }

    private void part2() {
        matches.clear();
        hashes.clear();
        
        Pattern same3 = Pattern.compile("(.)\\1{2,}");
        Pattern same5 = Pattern.compile("(.)\\1{4,}");

        long limit = Long.MAX_VALUE;
        for (long i = 0; i < limit; i++) {
            String hash = Utils.md5(String.format("%s%d", salt, i));

            for (int j = 0; j < 2016; j++) {
                hash = Utils.md5(hash);
            }

            Matcher mr = same5.matcher(hash);

            while (mr.find()) {
                if (mr.group().length() >= 5) {
                    for (Match item : matches) {
                        if (item.hasGroup(mr.group().charAt(0), 3)) {
                            //System.out.println(item + " -> " + i + ": [" + hash + "]");

                            hashes.add(item);
                            if (hashes.size() > 64) {
                                // Run this next 1000 loops
                                limit = i + 1000;
                            }
                        }
                    }
                }
            }

            mr = same3.matcher(hash);

            Match ma = new Match(i);
            if (mr.find()) {
                ma.setHash(hash);
            }
            matches.add(ma);

            if (matches.size() > 1000) {
                matches.poll();
            }
        }

        List<Match> keys = new ArrayList(hashes);
        if (keys.size() > 64) {
            do {
                keys.remove(keys.size() - 1);
            } while (keys.size() > 64);
        }

        /*for (Match m : keys) {
            System.out.println(m);
        }*/
        System.out.println("Index " + keys.get(keys.size() - 1).getIndex() + " produces the 64th stretching key");
    }

    class Match implements Comparable {

        protected long index = 0;
        protected String hash = null;

        public Match(long num) {
            index = num;
            hash = "";
        }

        public long getIndex() {
            return index;
        }

        public void setHash(String str) {
            hash = str;
        }

        public boolean hasGroup(char letter, int length) {
            String str = "";
            for (int i = 0; i < length; i++) {
                str += letter;
            }
            Pattern same = Pattern.compile("(.)\\1{2,}");
            Matcher mr = same.matcher(hash);
            while (mr.find()) {
                if (mr.group().length() >= length) {
                    return mr.group().substring(0, length).equals(str);
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 61 * 7 + Objects.hashCode(this.hash);
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
            final Match other = (Match) obj;
            if (!Objects.equals(hash, other.hash)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(index).append(": [").append(hash).append("]");
            return sb.toString();
        }

        @Override
        public int compareTo(Object o) {
            if (index == ((Match) o).index) {
                return 0;
            } else if (index > ((Match) o).index) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
