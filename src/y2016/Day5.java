/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.TaskSolver;
import adventofcode.Utils;

/**
 *
 * @author karel.hebik
 */
public class Day5 extends TaskSolver {

    private String doorid = null;

    public Day5() {
        doorid = "abbhdwsy";
    }

    @Override
    public void process() {
        String passwd = "";
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            String out = Utils.md5(String.format("%s%d", doorid, i));
            if (out.startsWith("00000")) {
                passwd += out.charAt(5);
                if (passwd.length() == 8) {
                    break;
                }
            }
        }
        System.out.println("First password is: " + passwd);

        int digits = 0;
        char[] buffer = new char[8];
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            String out = Utils.md5(String.format("%s%d", doorid, i));
            if (out.startsWith("00000")) {
                if (out.charAt(5) >= '0' && out.charAt(5) <= '7' && buffer[out.charAt(5) - '0'] == 0) {
                    buffer[out.charAt(5) - '0'] = out.charAt(6);
                    digits++;
                }
                if (digits == 8) {
                    break;
                }
            }
        }
        System.out.println("Second password is: " + new String(buffer));
    }
}
