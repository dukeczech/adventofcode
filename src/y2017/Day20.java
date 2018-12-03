/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2017;

import adventofcode.Point3D;
import adventofcode.TaskSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hebik
 */
public class Day20 extends TaskSolver {

    private String input = null;
    private List<Particle> particles = new ArrayList<>();

    public Day20() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day20-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day20.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Scanner sc = new Scanner(input)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Pattern p = Pattern.compile("p=<(-?\\d+),(-?\\d+),(-?\\d+)>, v=<(-?\\d+),(-?\\d+),(-?\\d+)>, a=<(-?\\d+),(-?\\d+),(-?\\d+)>");
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    Point3D pos = new Point3D(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
                    Point3D vel = new Point3D(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)));
                    Point3D acc = new Point3D(Integer.parseInt(m.group(7)), Integer.parseInt(m.group(8)), Integer.parseInt(m.group(9)));
                    particles.add(new Particle(pos, vel, acc));
                }
            }
        }
    }

    private void part1() {
        for (int i = 0; i < 10000000; i++) {
            for (Particle particle : particles) {
                //System.out.println(particle);
                particle.update();
            }
            //System.out.println("");
        }
        double distance = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < particles.size(); i++) {
            if (distance > particles.get(i).averageDistance()) {
                distance = particles.get(i).averageDistance();
                index = i;
            }
        }
        System.out.println("[1] Particle " + index + " will stay closest to position <0,0,0>: " + particles.get(index).averageDistance());
    }

    private void part2() {

    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    class Particle {

        private Point3D position = null;
        private Point3D velocity = null;
        private Point3D acceleration = null;
        private double distance = 0;
        private int moves = 0;

        public Particle(Point3D p, Point3D v, Point3D a) {
            position = p;
            velocity = v;
            acceleration = a;
            distance = position.distance(new Point3D(0, 0, 0));
        }

        public void update() {
            velocity.add(acceleration);
            position.add(velocity);
            moves++;
            //System.out.println("dist: " + position.distance(new Point3D(0, 0, 0)));
            distance = (distance + position.distance(new Point3D(0, 0, 0))) / 2;
        }

        public double averageDistance() {
            return distance;
        }

        @Override
        public String toString() {
            return "p=" + position + ", v=" + velocity + ", a=" + acceleration + ", d=" + distance;
        }

    }
}
