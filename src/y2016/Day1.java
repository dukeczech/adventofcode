/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.TaskSolver;
import java.awt.Point;

/**
 *
 * @author karel.hebik
 */
public class Day1 extends TaskSolver {

    private String[] input = null;
    private Point point = null;
    private boolean[][] visits = null;
    private Point meet = null;

    private enum DIRECTION {
        NORTH, EAST, SOUTH, WEST
    };
    private DIRECTION direction = DIRECTION.NORTH;

    public Day1() {
        String path = "R5, L2, L1, R1, R3, R3, L3, R3, R4, L2, R4, L4, R4, R3, L2, L1, L1, R2, R4, R4, L4, R3, L2, R1, L4, R1, R3, L5, L4, L5, R3, L3, L1, L1, R4, R2, R2, L1, L4, R191, R5, L2, R46, R3, L1, R74, L2, R2, R187, R3, R4, R1, L4, L4, L2, R4, L5, R4, R3, L2, L1, R3, R3, R3, R1, R1, L4, R4, R1, R5, R2, R1, R3, L4, L2, L2, R1, L3, R1, R3, L5, L3, R5, R3, R4, L1, R3, R2, R1, R2, L4, L1, L1, R3, L3, R4, L2, L4, L5, L5, L4, R2, R5, L4, R4, L2, R3, L4, L3, L5, R5, L4, L2, R3, R5, R5, L1, L4, R3, L1, R2, L5, L1, R4, L1, R5, R1, L4, L4, L4, R4, R3, L5, R1, L3, R4, R3, L2, L1, R1, R2, R2, R2, L1, L1, L2, L5, L3, L1";
        input = path.split(", ");
        point = new Point(0, 0);
        visits = new boolean[1000][1000];
    }

    @Override
    public void process() {
        for (String c : input) {
            if (!move(c)) {
                System.err.println("Wrong move!");
                return;
            }
        }
        System.out.println("Shortest path to the destination: " + (int) (Math.abs(point.getX()) + Math.abs(point.getY())));
        System.out.println("First meeting point distance: " + (int) (Math.abs(meet.getX()) + Math.abs(meet.getY())));
    }

    private boolean move(String coord) {
        if (coord.length() < 2) {
            return false;
        }
        int distance = Integer.parseInt(coord.substring(1));
        if (coord.startsWith("L")) {
            int next = (direction.ordinal() > 0) ? direction.ordinal() - 1 : 3;
            direction = DIRECTION.values()[next];
        } else if (coord.startsWith("R")) {
            int next = (direction.ordinal() < 3) ? direction.ordinal() + 1 : 0;
            direction = DIRECTION.values()[next];
        } else {
            return false;
        }
        switch (direction) {
            case NORTH:
                for (int i = 0; i < distance; i++) {
                    if (visits[500 + point.x][500 + point.y + i] && meet == null) {
                        meet = new Point(point.x, point.y + i);
                    } else {
                        visits[500 + point.x][500 + point.y + i] = true;
                    }
                }
                point.translate(0, distance);
                break;
            case EAST:
                for (int i = 0; i < distance; i++) {
                    if (visits[500 + point.x + i][500 + point.y] && meet == null) {
                        meet = new Point(point.x + i, point.y);
                    } else {
                        visits[500 + point.x + i][500 + point.y] = true;
                    }
                }
                point.translate(distance, 0);
                break;
            case SOUTH:
                for (int i = 0; i < distance; i++) {
                    if (visits[500 + point.x][500 + point.y - i] && meet == null) {
                        meet = new Point(point.x, point.y - i);
                    } else {
                        visits[500 + point.x][500 + point.y - i] = true;
                    }
                }
                point.translate(0, -distance);
                break;
            case WEST:
                for (int i = 0; i < distance; i++) {
                    if (visits[500 + point.x - i][500 + point.y] && meet == null) {
                        meet = new Point(point.x - i, point.y);
                    } else {
                        visits[500 + point.x - i][500 + point.y] = true;
                    }

                }
                point.translate(-distance, 0);
                break;
            default:
                return false;
        }
        return true;
    }
}
