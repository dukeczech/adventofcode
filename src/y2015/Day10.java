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
public class Day10 {

    String input = null;

    public Day10() {
        input = "1113222113";
    }

    public int process(int count) {
        String out = input;
        int len = 0;

        for (int a = 0; a < count; a++) {
            out = lookandsay(out);
        }
        len = out.length();

        return len;
    }

    public static String lookandsay(String number) {
        StringBuilder result = new StringBuilder();

        char repeat = number.charAt(0);
        number = number.substring(1) + " ";
        int times = 1;

        for (char actual : number.toCharArray()) {
            if (actual != repeat) {
                result.append(times);
                result.append(repeat);
                times = 1;
                repeat = actual;
            } else {
                times += 1;
            }
        }
        return result.toString();
    }

}
