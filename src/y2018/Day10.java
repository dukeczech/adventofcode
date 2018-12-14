/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2018;

import adventofcode.TaskSolver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.sourceforge.tess4j.*;

/**
 *
 * @author hebik
 */
public class Day10 extends TaskSolver {

    private String input = null;
    private List<Point> points = new ArrayList<>();

    public Day10() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day10-2018.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day10.class.getName()).log(Level.SEVERE, null, ex);
        }

        Pattern p = Pattern.compile("position=<\\s*(-?\\d+),\\s*(-?\\d+)> velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>");
        Stream.of(input.split("\n")).forEach(ln -> {
            Matcher m = p.matcher(ln);
            if (m.matches()) {
                points.add(new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)),
                        Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))));
            } else {
                System.err.println("Input error!");
            }
        });
    }

    private void part1() {
        Grid gr = new Grid();
        DisplayFrame df = new DisplayFrame(400, 400);
        Thread th = new Thread(df);
        th.start();

        boolean display = false;
        BufferedImage image = null;
        int lastwidth = Integer.MAX_VALUE;
        int lastheight = Integer.MAX_VALUE;
        for (int i = 0; i < 1000000; i++) {
            points.stream().forEach(p -> {
                p.move();
            });

            int width = getAreaWidth();
            int height = getAreaHeigth();

            if (lastheight < height || lastwidth < width) {

                ITesseract instance = new Tesseract();
                instance.setLanguage("eng");

                try {
                    String result = instance.doOCR(image);
                    System.out.println("[1] Message will eventually appear in the sky: " + result);
                    System.out.println("[2] How many seconds would they have needed to wait for that message to appear: " + i);
                } catch (TesseractException ex) {
                    Logger.getLogger(Day10.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            }
            lastheight = height;
            lastwidth = width;

            if (!display && width < df.getWidth() && height < df.getHeight()) {
                int minx = Math.abs(points.stream().mapToInt(p -> p.px).min().getAsInt());
                int miny = Math.abs(points.stream().mapToInt(p -> p.py).min().getAsInt());

                gr.init(minx, miny, width + 1, height + 1);
                display = true;
            }

            if (display) {
                gr.resetAll();
                points.stream().forEach(p -> {
                    p.place(gr);
                });

                BufferedImage before = new BufferedImage(gr.grid[0].length, gr.grid.length, BufferedImage.TYPE_BYTE_GRAY);
                for (int y = 0; y < gr.grid.length; y++) {
                    for (int x = 0; x < gr.grid[0].length; x++) {
                        if (gr.grid[y][x]) {
                            before.setRGB(x, y, new Color(255, 255, 255).getRGB());
                        } else {
                            before.setRGB(x, y, new Color(0, 0, 0).getRGB());
                        }
                    }
                }

                AffineTransform at = new AffineTransform();
                at.scale(1.0, 1.0);
                AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = new BufferedImage(before.getWidth(), before.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                image = scaleOp.filter(before, image);
                df.updateImage(image);

                try {
                    Thread.sleep(60);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Day10.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    private void part2() {
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    private int getAreaWidth() {
        int minx = Math.abs(points.stream().mapToInt(p -> p.px).min().getAsInt());
        int maxx = Math.abs(points.stream().mapToInt(p -> p.px).max().getAsInt());
        return minx + maxx;
    }

    private int getAreaHeigth() {
        int miny = Math.abs(points.stream().mapToInt(p -> p.py).min().getAsInt());
        int maxy = Math.abs(points.stream().mapToInt(p -> p.py).max().getAsInt());
        return miny + maxy;
    }

    class DisplayFrame extends JFrame implements Runnable {

        private int width = 0;
        private int height = 0;
        private JPanel panel = null;
        private JLabel label = null;

        public DisplayFrame(int w, int h) {
            super();
            width = w;
            height = h;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public void run() {

        }

        public void updateImage(BufferedImage img) {
            setBounds(100, 100, width, height);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            panel = new JPanel(new BorderLayout());
            getContentPane().add(panel, BorderLayout.NORTH);

            label = new JLabel(new ImageIcon(img));
            label.setVerticalTextPosition(JLabel.BOTTOM);
            label.setHorizontalTextPosition(JLabel.CENTER);
            panel.add(label, BorderLayout.CENTER);

            setVisible(true);
        }
    }

    class Grid {

        public int cx = 0;
        public int cy = 0;
        public boolean grid[][] = null;

        public void init(int cx, int cy, int lenx, int leny) {
            this.cx = cx;
            this.cy = cy;
            this.grid = new boolean[leny][lenx];
        }

        public void set(int x, int y) {
            grid[cy + y][cx + x] = true;
        }

        public void reset(int x, int y) {
            grid[cy + y][cx + x] = false;
        }

        public void resetAll() {
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    grid[y][x] = false;
                }
            }
        }
    }

    class Point {

        public int startx = 0;
        public int starty = 0;
        public int px = 0;
        public int py = 0;
        public int vx = 0;
        public int vy = 0;

        public Point(int x, int y, int vx, int vy) {
            this.startx = x;
            this.starty = y;
            this.px = x;
            this.py = y;
            this.vx = vx;
            this.vy = vy;
        }

        public void place(Grid g) {
            g.set(px, py);
        }

        public void move() {
            px += vx;
            py += vy;
        }

    }
}
