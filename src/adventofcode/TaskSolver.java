/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode;

/**
 *
 * @author karel.hebik
 */
public abstract class TaskSolver {

    protected abstract void process();

    public void run() {
        long startTime = System.currentTimeMillis();
        System.out.println("");
        System.out.println("Task " + getClass() + " is running...");
        process();
        long estimatedTime = System.currentTimeMillis() - startTime;

        System.out.println("Task " + getClass() + " end.");
        System.out.println("Time elapsed: " + estimatedTime + "ms.");
        System.out.println("");
    }

}
