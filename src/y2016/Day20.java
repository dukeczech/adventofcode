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
public class Day20 extends TaskSolver implements LineProcessor {

    private List<Range> ranges = null;

    public Day20() {
        ranges = new ArrayList<>();
    }

    @Override
    public void processLine(String line) {
        Pattern rangeLine = Pattern.compile("(\\d+)-(\\d+)");
        Matcher mr = rangeLine.matcher(line.trim());

        if (mr.matches()) {
            Range r1 = new Range(Long.parseLong(mr.group(1)), Long.parseLong(mr.group(2)));
            Range r3 = null;
            for (Range r2 : ranges) {
                r3 = r2.merge(r1);
                if (r3 != null) {
                    r2 = r3;
                    break;
                }
            }
            if (r3 == null) {
                ranges.add(r1);
            }
        } else {
            System.err.println("Unknown range (" + line + ")!");
        }
    }

    @Override
    public void process() {
        ReadLineFileReader reader = new ReadLineFileReader("input/day20.txt", this);
        reader.start();

        Collections.sort(ranges);

        while (true) {
            Range r3 = null;
            for (int i = 0; i < ranges.size(); i++) {
                for (int j = i + 1; j < ranges.size(); j++) {
                    Range r1 = ranges.get(i);
                    Range r2 = ranges.get(j);
                    r3 = r1.merge(r2);
                    if (r3 != null) {
                        ranges.set(i, r3);
                        ranges.remove(j);
                    }
                }
            }
            if (r3 == null) {
                break;
            }
        }

        System.out.println("The lowest-valued IP that is not blocked: " + (ranges.get(0).end + 1));

        long count = 0;
        Range lastRange = null;
        for (Range range : ranges) {
            if (lastRange != null) {
                count += range.begin - lastRange.end - 1;
            }
            lastRange = range;
        }

        System.out.println("IPs are allowed by the blacklist: " + count);
    }

    class Range implements Comparable {

        private long begin = 0;
        private long end = 0;

        public Range(long b, long e) {
            begin = b;
            end = e;
        }

        public boolean isInRange(long n) {
            return n >= begin && n <= end;
        }

        public Range merge(Range another) {
            if (isInRange(another.begin) || isInRange(another.end) || another.isInRange(begin) || another.isInRange(end)) {
                begin = Math.min(begin, another.begin);
                end = Math.max(end, another.end);
            } else if (end + 1 == another.begin) {
                end = another.end;
            } else if (another.end + 1 == begin) {
                begin = another.begin;
            } else {
                return null;
            }
            return new Range(begin, end);
        }

        @Override
        public String toString() {
            return begin + " - " + end;
        }

        @Override
        public int compareTo(Object another) {
            if (begin == ((Range) another).begin) {
                return 0;
            } else if (begin < ((Range) another).begin) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
