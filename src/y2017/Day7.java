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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hebik
 */
public class Day7 extends TaskSolver {

    private String input = null;
    Map<String, Node> tree = null;

    public Day7() {
        try {
            input = new String(Files.readAllBytes(Paths.get("input/day7-2017.txt")));
        } catch (IOException ex) {
            Logger.getLogger(Day5.class.getName()).log(Level.SEVERE, null, ex);
        }

        tree = new HashMap<>();

        try (Scanner sc = new Scanner(input)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Pattern p = Pattern.compile("([a-z]+)\\s+\\((\\d+)\\)(?:\\s+->\\s+(.*))?");
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    String name = m.group(1);
                    int weight = Integer.parseInt(m.group(2));

                    Node n = tree.getOrDefault(name, new Node(name, weight));
                    n.setWeight(weight);

                    if (m.group(3) != null) {
                        String[] childs = m.group(3).split(",\\s+");
                        for (String child : childs) {
                            Node ch = tree.getOrDefault(child, new Node(child));
                            ch.setParent(n);
                            n.addChild(ch);
                            tree.put(child, ch);
                        }
                    }
                    tree.put(name, n);
                }
            }
        }

        Iterator it = tree.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
        }
    }

    private void part1() {
        for (Map.Entry pair : tree.entrySet()) {
            Node n = (Node) pair.getValue();
            if (n.isRoot()) {
                System.out.println("[1] Root node found: " + n.getName());
            }
        }
    }

    private void part2() {
        for (Map.Entry pair : tree.entrySet()) {
            Node n = (Node) pair.getValue();
            if (n.isRoot()) {
                System.out.println("Top balance: " + n.isBalanced());
            }
        }
    }

    @Override
    protected void process() {
        part1();
        part2();
    }

    public class Node {

        private Map<String, Node> children;
        private Node parent = null;
        private String name = null;
        private int weight = 0;

        public Node(String n) {
            this.children = null;
            this.children = new HashMap<>();
            this.name = n;
        }

        public Node(String n, int w) {
            this.children = new HashMap<>();
            this.name = n;
            this.weight = w;
        }

        public Node(String n, Node parent) {
            this.children = null;
            this.children = new HashMap<>();
            this.name = n;
            this.parent = parent;
        }

        public Map<String, Node> getChildren() {
            return children;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public void addChild(String n) {
            Node child = new Node(n);
            child.setParent(this);
            this.children.put(n, child);
        }

        public void addChild(Node child) {
            child.setParent(this);
            this.children.put(child.getName(), child);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String n) {
            this.name = n;
        }

        public int getWeight() {
            return this.weight;
        }

        public void setWeight(int w) {
            this.weight = w;
        }

        public boolean isRoot() {
            return (this.parent == null);
        }

        public boolean isLeaf() {
            return this.children.isEmpty();
        }

        public int getBalance() {
            if (isLeaf()) {
                return weight;
            }

            int balance = -1;
            for (Map.Entry pair : this.children.entrySet()) {
                Node ch = (Node) pair.getValue();
                System.out.println("Balance: " + ch.getBalance());
                if (balance < 0) {
                    balance = ch.getBalance();
                } else if (balance != ch.getBalance()) {
                    return -1;
                }
            }
            return this.children.size() * balance;
        }

        public boolean isBalanced() {
            if (isLeaf()) {
                return true;
            }

            Map<Integer, Integer> values = new HashMap<>();
            for (Map.Entry pair : this.children.entrySet()) {
                Node ch = (Node) pair.getValue();
                int balance = ch.getBalance();
                values.put(balance, values.getOrDefault(balance, 0) + 1);
            }

            if (values.size() == 2) {
                // Two different balances
                for (Map.Entry pair : values.entrySet()) {
                    if ((Integer) pair.getValue() == 1) {
                        System.out.println("Wrong balance: " + pair.getKey());
                    }
                }
                Iterator<Entry<Integer, Integer>> it = values.entrySet().iterator();
                int diff = it.next().getKey() - it.next().getKey();

                /*for (Map.Entry pair : values.entrySet()) {
                    System.out.println(pair.getKey() + " " + pair.getValue());
                    
                }*/
                System.out.println("Diff is " + diff);
            }

            return true;
        }

        public void removeParent() {
            this.parent = null;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + Objects.hashCode(this.name);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Node other = (Node) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.name + " (" + this.weight + ")");
            if (!this.children.isEmpty()) {
                sb.append(" -> ");
            }
            for (Map.Entry pair : this.children.entrySet()) {
                Node ch = (Node) pair.getValue();
                sb.append(ch.getName() + ", ");
            }
            return sb.toString();
        }

    }

}
