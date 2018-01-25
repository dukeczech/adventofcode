/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2017;

import adventofcode.TaskSolver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hebik
 */
public class Day18 extends TaskSolver {

    private String input = null;
    private List<Instruction> instructions = new ArrayList<>();
    private Program[] pool = new Program[2];

    public Day18() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day18-2017.txt"))).trim();
        } catch (IOException ex) {
            Logger.getLogger(Day18.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void part1() {
        instructions.clear();
        try (Scanner sc = new Scanner(input)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Pattern p = Pattern.compile("(snd|rcv) ([a-z])|(set|add|mul|mod|jgz) (-?\\d+|[a-z]) (-?\\d+|[a-z])");
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    if (m.group(1) != null) {
                        switch (m.group(1)) {
                            case "snd":
                                instructions.add(new Sound(m.group(2)));
                                break;
                            case "rcv":
                                instructions.add(new Recover(m.group(2)));
                                break;
                            default:
                                System.err.println("Invalid instruction!");
                                break;
                        }
                    }
                    if (m.group(3) != null) {
                        switch (m.group(3)) {
                            case "set":
                                instructions.add(new Set(m.group(4), m.group(5)));
                                break;
                            case "add":
                                instructions.add(new Increase(m.group(4), m.group(5)));
                                break;
                            case "mul":
                                instructions.add(new Multiply(m.group(4), m.group(5)));
                                break;
                            case "mod":
                                instructions.add(new Remainder(m.group(4), m.group(5)));
                                break;
                            case "jgz":
                                instructions.add(new Jump(m.group(4), m.group(5)));
                                break;
                            default:
                                System.err.println("Invalid instruction!");
                                break;
                        }
                    }
                }
            }
        }

        try {
            Program p0 = new Program(0, instructions, null);
            p0.start();
            p0.join();
            System.out.println("[1] The value of the recovered frequency: " + p0.getFrequency());
        } catch (InterruptedException ex) {
            Logger.getLogger(Day18.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void part2() {
        instructions.clear();
        try (Scanner sc = new Scanner(input)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Pattern p = Pattern.compile("(snd|rcv) (-?\\d+|[a-z])|(set|add|mul|mod|jgz) (-?\\d+|[a-z]) (-?\\d+|[a-z])");
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    if (m.group(1) != null) {
                        switch (m.group(1)) {
                            case "snd":
                                instructions.add(new Send(m.group(2)));
                                break;
                            case "rcv":
                                instructions.add(new Receive(m.group(2)));
                                break;
                            default:
                                System.err.println("Invalid instruction!");
                                break;
                        }
                    }
                    if (m.group(3) != null) {
                        switch (m.group(3)) {
                            case "set":
                                instructions.add(new Set(m.group(4), m.group(5)));
                                break;
                            case "add":
                                instructions.add(new Increase(m.group(4), m.group(5)));
                                break;
                            case "mul":
                                instructions.add(new Multiply(m.group(4), m.group(5)));
                                break;
                            case "mod":
                                instructions.add(new Remainder(m.group(4), m.group(5)));
                                break;
                            case "jgz":
                                instructions.add(new Jump(m.group(4), m.group(5)));
                                break;
                            default:
                                System.err.println("Invalid instruction!");
                                break;
                        }
                    }
                }
            }
        }

        try {
            Object lock = new Object();
            pool[0] = new Program(0, instructions, lock);
            pool[1] = new Program(1, instructions, lock);

            pool[0].start();
            pool[1].start();
            pool[0].join();
            pool[1].join();
            System.out.println("[2] Program 1 send a value: " + pool[1].getCounter());
        } catch (InterruptedException ex) {
            Logger.getLogger(Day18.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    private class Program extends Thread {

        private int id = 0;
        private int counter = 0;
        private Map<Character, Long> registers = new HashMap<>();
        private List<Instruction> instructions = new ArrayList<>();
        private AtomicLong index = new AtomicLong(0);
        private AtomicLong frequency = new AtomicLong(0);
        private BlockingQueue<Long> queue = new ArrayBlockingQueue<>(1000);
        public Object common = null;
        public ReentrantLock lock = null;

        public Program(int thr, List<Instruction> ins, Object ll) {
            id = thr;
            instructions = ins;
            common = ll;
            lock = new ReentrantLock();
            registers.put('p', new Long(thr));
        }

        public BlockingQueue<Long> getQueue() {
            return queue;
        }

        public long getFrequency() {
            return frequency.get();
        }

        public int getCounter() {
            return counter;
        }

        @Override
        public void run() {

            while (true) {
                Instruction instruction = instructions.get((int) index.get());
                BlockingQueue<Long> destination = null;
                if (instruction instanceof Send) {
                    // Sends the value of X to the other program
                    // System.out.println("[id-" + id + "] Sends to another program");
                    destination = pool[(id == 0) ? 1 : 0].getQueue();
                    counter++;
                } else if (instruction instanceof Receive) {
                    // Receives the next value and stores it in register X. If no values are in the queue, 
                    // the program waits for a value to be sent to it.

                    // System.out.println("[id-" + id + "] Waiting to receive from another program (" + queue.size() + ")" + " - " + pool[(id == 0) ? 1 : 0].isAlive());
                    destination = pool[id].getQueue();

                    boolean quit = false;
                    synchronized (common) {
                        if (pool[(id == 0) ? 1 : 0].lock.isLocked() && pool[(id == 0) ? 1 : 0].queue.isEmpty() && queue.isEmpty()) {
                            System.out.println("[id-" + id + "] The others lock is locked and both queues is empty");
                            quit = true;
                        }

                        if (!pool[(id == 0) ? 1 : 0].isAlive() && queue.isEmpty()) {
                            System.out.println("[id-" + id + "] The other program is dead and my queue is empty");
                            quit = true;
                        }
                    }
                    if (quit) {
                        // Interrupt the other program
                        pool[(id == 0) ? 1 : 0].interrupt();
                        System.out.println("[id-" + id + "] Killing the other program");
                        break;
                    }
                    lock.lock();
                }
                boolean result = instruction.perform(registers, index, frequency, destination);
                if (instruction instanceof Recover && result) {
                    break;
                }
                if (instruction instanceof Receive) {
                    // System.out.println("[id-" + id + "] Received from another program");
                    synchronized (common) {
                        lock.unlock();
                    }
                }
                if (instruction instanceof Jump && result) {
                    // Do nothing
                } else {
                    index.incrementAndGet();
                }
                if (index.get() >= instructions.size() || index.get() < 0) {
                    break;
                }
            }
            System.out.println("[id-" + id + "] Thread finished");
        }
    }

    abstract class Instruction {

        protected Character registerx = null;
        protected Character registery = null;
        protected long valuex = 0;
        protected long valuey = 0;

        public Instruction() {
        }

        public Instruction(Character x) {
            registerx = x;
        }

        public Instruction(Character x, int y) {
            registerx = x;
            valuey = y;
        }

        public Instruction(int x, int y) {
            valuex = x;
            valuey = y;
        }

        public Instruction(Character x, Character y) {
            registerx = x;
            registery = y;
        }

        public Instruction(String x) {
            if (Character.isLetter(x.charAt(0))) {
                registerx = x.charAt(0);
            } else {
                valuex = Integer.parseInt(x);
            }
        }

        public Instruction(String x, String y) {
            this(x);
            if (Character.isLetter(y.charAt(0))) {
                registery = y.charAt(0);
            } else {
                valuey = Integer.parseInt(y);
            }
        }

        public abstract boolean perform(Map<Character, Long> registers, AtomicLong index, AtomicLong frequency, BlockingQueue<Long> queue);
    }

    class Sound extends Instruction {

        public Sound(String x) {
            super(x);
        }

        @Override
        public boolean perform(Map<Character, Long> registers, AtomicLong index, AtomicLong frequency, BlockingQueue<Long> queue) {
            frequency.set(registers.getOrDefault(registerx, 0L));
            // System.out.println("Playing: " + frequency);
            return true;
        }

    }

    class Recover extends Instruction {

        public Recover(String x) {
            super(x);
        }

        @Override
        public boolean perform(Map<Character, Long> registers, AtomicLong index, AtomicLong frequency, BlockingQueue<Long> queue) {
            long v = registers.getOrDefault(registerx, 0L);
            if (v != 0) {
                // System.out.println("Recovering: " + frequency);
                return true;
            }
            return false;
        }

    }

    class Send extends Instruction {

        public Send(String x) {
            super(x);
        }

        @Override
        public boolean perform(Map<Character, Long> registers, AtomicLong index, AtomicLong frequency, BlockingQueue<Long> queue) {
            long x = 0;
            if (registerx != null) {
                x = registers.getOrDefault(registerx, 0L);
            } else {
                x = valuex;
            }

            // System.out.println("Sends " + x + " to another program");
            queue.add(x);
            return true;
        }

        @Override
        public String toString() {
            return "snd " + ((registerx != null) ? registerx : valuex);
        }
    }

    class Receive extends Instruction {

        public Receive(String x) {
            super(x);
        }

        @Override
        public boolean perform(Map<Character, Long> registers, AtomicLong index, AtomicLong frequency, BlockingQueue<Long> queue) {
            try {
                Long value = queue.take();
                //System.out.println("Received " + value + " from another program");
                registers.put(registerx, value);
                return true;
            } catch (InterruptedException ex) {
                //Logger.getLogger(Day18.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }

        @Override
        public String toString() {
            return "rcv " + ((registerx != null) ? registerx : valuex);
        }
    }

    class Set extends Instruction {

        public Set(String x, String y) {
            super(x, y);
        }

        @Override
        public boolean perform(Map<Character, Long> registers, AtomicLong index, AtomicLong frequency, BlockingQueue<Long> queue) {
            if (registery != null) {
                registers.put(registerx, registers.getOrDefault(registery, 0L));
            } else {
                registers.put(registerx, valuey);
            }
            return true;
        }

    }

    class Increase extends Instruction {

        public Increase(String x, String y) {
            super(x, y);
        }

        @Override
        public boolean perform(Map<Character, Long> registers, AtomicLong index, AtomicLong frequency, BlockingQueue<Long> queue) {
            registers.putIfAbsent(registerx, 0L);
            if (registery != null) {
                registers.merge(registerx, registers.getOrDefault(registery, 0L), (a, b) -> a + b);
            } else {
                registers.merge(registerx, valuey, (a, b) -> a + b);
            }
            return true;
        }

    }

    class Multiply extends Instruction {

        public Multiply(String x, String y) {
            super(x, y);
        }

        @Override
        public boolean perform(Map<Character, Long> registers, AtomicLong index, AtomicLong frequency, BlockingQueue<Long> queue) {
            registers.putIfAbsent(registerx, 0L);
            if (registery != null) {
                registers.merge(registerx, registers.getOrDefault(registery, 0L), (a, b) -> a * b);
            } else {
                registers.merge(registerx, valuey, (a, b) -> a * b);
            }
            return true;
        }

    }

    class Remainder extends Instruction {

        public Remainder(String x, String y) {
            super(x, y);
        }

        @Override
        public boolean perform(Map<Character, Long> registers, AtomicLong index, AtomicLong frequency, BlockingQueue<Long> queue) {
            registers.putIfAbsent(registerx, 0L);
            if (registery != null) {
                if (registers.getOrDefault(registery, 0L) == 0) {
                    // Prevent zero division
                    return false;
                }
                registers.merge(registerx, registers.getOrDefault(registery, 0L), (a, b) -> a % b);
            } else {
                if (valuey == 0) {
                    // Prevent zero division
                    return false;
                }
                registers.merge(registerx, valuey, (a, b) -> a % b);
            }
            return true;
        }

    }

    class Jump extends Instruction {

        public Jump(String x, String y) {
            super(x, y);
        }

        @Override
        public boolean perform(Map<Character, Long> registers, AtomicLong index, AtomicLong frequency, BlockingQueue<Long> queue) {
            long x = 0;
            if (registerx != null) {
                x = registers.getOrDefault(registerx, 0L);
            } else {
                x = valuex;
            }

            if (x > 0) {
                if (registery != null) {
                    index.addAndGet(registers.getOrDefault(registery, 0L));
                } else {
                    index.addAndGet(valuey);
                }
                return true;
            }
            return false;
        }

    }

}
