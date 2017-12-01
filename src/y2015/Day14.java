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
public class Day14 {

    public Day14() {
    }

    public void travel(int time) {
        int comet = 0;
        int dancer = 0;
        int cometPoints = 0;
        int dancerPoints = 0;
        for (int t = 0; t < time; t++) {
            comet = ((t / 137) * 140) + (Math.min((t % 137), 10) * 14);
            dancer = ((t / 173) * 160) + (Math.min((t % 173), 11) * 16);
            if (comet == 0 && dancer == 0) {
                continue;
            }
            if (comet > dancer) {
                cometPoints++;
            } else if (dancer > comet) {
                dancerPoints++;
            } else {
                cometPoints++;
                dancerPoints++;
            }
            //System.out.println(t + ": " + comet + "[" + cometPoints + "] " + dancer + "[" + dancerPoints + "]");
        }

        System.out.print("Comet: ");
        for (int i = 0; i < (time / 137); i++) {
            System.out.print("-> ZzZz -> 14km ");
        }
        if (Math.min((time % 137), 10) * 14 > 0) {
            System.out.print("-> " + Math.min((time % 137), 10) * 14 + "km");
        }
        System.out.println("");

        System.out.println("Comet: " + comet + " [" + cometPoints + "]");
        System.out.println("Dancer: " + dancer + " [" + dancerPoints + "]");
    }
}
