/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2015;

/**
 *
 * @author karel.hebik
 */
public class Day11 {

    public Day11() {
    }

    public String next(String s) {
        String next = s;
        while (true) {
            next = increment(next);
            if (check(next)) {
                break;
            }
        }
        return next;
    }

    private String increment(String s) {
        int length = s.length();
        char c = s.charAt(length - 1);

        if (c == 'z') {
            return length > 1 ? increment(s.substring(0, length - 1)) + 'a' : "aa";
        }

        return s.substring(0, length - 1) + ++c;
    }

    private boolean check(String s) {
        if (s.matches(".*[iol].*")) {
            return false;
        }
        if (!s.matches(".*(\\w)\\1+.*(\\w)\\2+.*")) {
            return false;
        }
        for (int i = 0;; i++) {
            char first = s.charAt(0);
            s = s.substring(1);
            if (s.length() < 2) {
                break;
            }
            if (s.charAt(0) == first + 1 && s.charAt(1) == first + 2) {
                return true;
            }
        }
        return false;
    }
}
