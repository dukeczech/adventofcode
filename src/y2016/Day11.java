/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.LineProcessor;
import adventofcode.ReadLineFileReader;
import adventofcode.TaskSolver;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day11 extends TaskSolver implements LineProcessor {

    private Building building = null;
    private Set<Building> moves = null;

    public Day11() {
        building = new Building();
        moves = new HashSet<>();
    }

    @Override
    public void processLine(String line) {

        Pattern descLine = Pattern.compile("(?<floor>\\w+) floor|(?<microchip>\\w+)-compatible microchip|(?<generator>\\w+) generator");
        Matcher mr = descLine.matcher(line);

        int floor = -1;
        while (mr.find()) {
            if (mr.group("floor") != null) {
                //System.out.println("Floor: " + mr.group("floor"));
                switch (mr.group("floor").trim()) {
                    case "first":
                        floor = 0;
                        break;
                    case "second":
                        floor = 1;
                        break;
                    case "third":
                        floor = 2;
                        break;
                    case "fourth":
                        floor = 3;
                        break;
                    default:
                        System.err.println("Unknown floor(" + mr.group("floor").trim() + ")!");
                        break;
                }
            } else if (mr.group("microchip") != null) {
                Microchip mc = new Microchip(mr.group("microchip"));
                building.addComponent(floor, mc);
            } else if (mr.group("generator") != null) {
                Generator gn = new Generator(mr.group("generator"));
                building.addComponent(floor, gn);
            }
        }
    }

    @Override
    public void process() {
        ReadLineFileReader reader = new ReadLineFileReader("input/day11.txt", this);
        reader.start();

        System.out.println("Building:");
        System.out.println(building);

        LinkedList<Building> nodes = building.nextNodes();
        while (!nodes.isEmpty()) {
            LinkedList<Building> nextNodes = new LinkedList<>();
            for (Building node : nodes) {
                if (node.isDone()) {
                    System.out.println("OK");
                    System.out.println(node);
                    System.out.println("Steps: " + node.getSteps());
                    System.out.println("Moves: " + moves.size());
                    return;
                } else {
                    nextNodes.addAll(node.nextNodes());
                }
            }

            nodes = nextNodes;
        }
    }

    abstract class Component {

        private String element = null;

        public Component(String elem) {
            element = elem;
        }

        public Component(Component another) {
            element = another.element;
        }

        public String getElement() {
            return element;
        }

        public boolean isElevator() {
            return (this instanceof Elevator);
        }

        public boolean isGenerator() {
            return (this instanceof Generator);
        }

        public boolean isMicrochip() {
            return (this instanceof Microchip);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(element);
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
            final Component other = (Component) obj;
            if (!Objects.equals(element, other.element)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            if (this instanceof Elevator) {
                return "E";
            }
            if (this instanceof Microchip) {
                return element.toUpperCase().charAt(0) + "M";
            }
            if (this instanceof Generator) {
                return element.toUpperCase().charAt(0) + "G";
            }
            return null;
        }
    }

    class Elevator extends Component {

        public Elevator() {
            super("elevator");
        }

    }

    class Microchip extends Component {

        public Microchip(String elem) {
            super(elem);
        }

    }

    class Generator extends Component {

        public Generator(String elem) {
            super(elem);
        }

    }

    class Building {

        private Set<Component>[] floors = null;
        private int steps = 0;

        public Building() {
            floors = new HashSet[4];
            for (int i = 0; i < floors.length; i++) {
                floors[i] = new HashSet<>();
            }

            // Add elevator to default position
            floors[0].add(new Elevator());
        }

        public Building(Building another) {
            floors = new HashSet[4];
            for (int i = 0; i < another.getFloors().length; i++) {
                floors[i] = new HashSet<>(another.getFloor(i).size());
                for (Component cp : another.getFloor(i)) {
                    floors[i].add(cp);
                }
            }

            steps = another.steps;
        }

        public Set<Component> getFloor(int floor) {
            return floors[floor];
        }

        public Set<Component>[] getFloors() {
            return floors;
        }

        public int getSteps() {
            return steps;
        }

        public void addStep() {
            steps++;
        }

        public void addComponent(int floor, Component comp) {
            floors[floor].add(comp);
        }

        public void removeComponent(int floor, Component comp) {
            floors[floor].remove(comp);
        }

        public int getElevatorFloor() {
            for (int i = 0; i < floors.length; i++) {
                if (floors[i].contains(new Elevator())) {
                    return i;
                }
            }
            return -1;
        }

        public Set<Component> getElevatorComponents() {
            return floors[getElevatorFloor()];
        }

        public LinkedList<Building> nextNodes() {
            LinkedList<Building> output = new LinkedList<>();

            int elevatorFloor = getElevatorFloor();

            // Don't go down if there are no items left
            boolean empty = true;
            for (int i = 0; i < elevatorFloor; i++) {
                if (!getFloor(i).isEmpty()) {
                    empty = false;
                }
            }

            for (int destinationFloor : (empty) ? new int[]{elevatorFloor + 1} : new int[]{elevatorFloor - 1, elevatorFloor + 1}) {
                if (destinationFloor < 0 || destinationFloor > 3) {
                    continue;
                }

                for (Component cp1 : getElevatorComponents()) {
                    if (cp1.isElevator()) {
                        continue;
                    }
                    Building b1 = new Building(this);
                    b1.getElevatorComponents().remove(cp1);
                    b1.getFloor(destinationFloor).add(cp1);
                    b1.removeComponent(elevatorFloor, new Elevator());
                    b1.addComponent(destinationFloor, new Elevator());
                    b1.addStep();

                    if (!moves.contains(b1) && b1.isOK()) {
                        //System.out.println(cp1 + ": " + elevatorFloor + " -> " + destinationFloor);

                        output.add(b1);
                        moves.add(b1);
                    }

                    if (destinationFloor == elevatorFloor - 1) {
                        // Always pick a single item when going down. If possible, pick one that has its counterpart on the floor below.
                        continue;
                    }

                    Component cp2 = getCounterpart(cp1, elevatorFloor);
                    if (cp2 != null) {
                        // Always pick two items when going up. If possible, pick items that form a pair (chip + generator of same type). 
                        // Otherwise just pick any two that can be moved without problems.

                        Building b2 = new Building(this);
                        b2.getElevatorComponents().removeAll(Arrays.asList(cp1, cp2));
                        b2.getFloor(destinationFloor).addAll(Arrays.asList(cp1, cp2));
                        b2.removeComponent(elevatorFloor, new Elevator());
                        b2.addComponent(destinationFloor, new Elevator());
                        b2.addStep();

                        if (b2.isOK()) {
                            if (moves.contains(b2)) {
                                //System.out.println("Duplicate move!");
                            } else {
                                //System.out.println("[" + cp1 + ", " + cp2 + "]: " + elevatorFloor + " -> " + destinationFloor);

                                output.add(b2);
                                moves.add(b2);
                            }
                            continue;
                        }
                    }

                    for (Component cp3 : getElevatorComponents()) {
                        if (cp3.isElevator()) {
                            continue;
                        }
                        if (cp1 != cp3) {
                            Building b2 = new Building(this);
                            b2.getElevatorComponents().removeAll(Arrays.asList(cp1, cp3));
                            b2.getFloor(destinationFloor).addAll(Arrays.asList(cp1, cp3));
                            b2.removeComponent(elevatorFloor, new Elevator());
                            b2.addComponent(destinationFloor, new Elevator());
                            b2.addStep();

                            if (!moves.contains(b2) && b2.isOK()) {
                                //System.out.println(cp1 + ", " + cp3 + ": " + elevatorFloor + " -> " + destinationFloor);

                                output.add(b2);
                                moves.add(b2);
                            }
                        }
                    }
                }
            }

            return output;
        }

        public Component getCounterpart(Component cp1, int floor) {
            Optional<Component> cp = floors[floor].stream().filter((cp2) -> (cp1.getClass() != cp2.getClass() && (cp1.getElement().equals(cp2.getElement())))).findAny();
            if (cp.isPresent()) {
                return cp.get();
            } else {
                return null;
            }
        }

        public boolean hasAnyMicrochip(int floor) {
            return floors[floor].stream().filter((cp) -> (cp instanceof Microchip)).findAny().isPresent();
        }

        public boolean hasMicrochip(String element, int floor) {
            return floors[floor].stream().filter((cp) -> (cp instanceof Microchip)).anyMatch((cp) -> (cp.getElement().equals(element)));
        }

        public boolean hasAnyGenerator(int floor) {
            return floors[floor].stream().filter((cp) -> (cp instanceof Generator)).findAny().isPresent();
        }

        public boolean hasGenerator(String element, int floor) {
            return floors[floor].stream().filter((cp) -> (cp instanceof Generator)).anyMatch((cp) -> (cp.getElement().equals(element)));
        }

        public boolean isOK() {
            for (int i = 0; i < floors.length; i++) {
                if (!hasAnyGenerator(i)) {
                    //System.out.println("F" + (i + 1) + " does not have any generator");
                    continue;
                }
                for (Component cp : floors[i]) {
                    if (cp instanceof Microchip) {
                        if (hasGenerator(cp.getElement(), i)) {
                            //System.out.println("F" + (i + 1) + " does have microchip + generator (" + cp.getElement() + ")");
                        } else {
                            //System.out.println("F" + (i + 1) + " does have microchip, but not generator (" + cp.getElement() + ")");
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        public boolean isDone() {
            for (int i = 0; i < floors.length - 1; i++) {
                if (!floors[i].isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 41 * hash + Arrays.deepHashCode(this.floors);
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
            final Building other = (Building) obj;
            if (!Arrays.deepEquals(this.floors, other.floors)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            int max = 0;
            for (Set<Component> floor : floors) {
                max = Math.max(max, floor.size());
            }

            StringBuilder sb = new StringBuilder();
            for (int i = floors.length - 1; i >= 0; i--) {
                sb.append("F").append(i + 1).append(" ");
                if (i == getElevatorFloor()) {
                    sb.append("E ");
                } else {
                    sb.append(". ");
                }

                int count = 0;

                // Add components
                for (Component cp : floors[i]) {
                    if (cp instanceof Microchip) {
                        sb.append(((Microchip) cp).getElement().toUpperCase().charAt(0)).append("M").append(" ");
                        count++;
                    } else if (cp instanceof Generator) {
                        sb.append(((Generator) cp).getElement().toUpperCase().charAt(0)).append("G").append(" ");
                        count++;
                    }
                }

                // Add empty positions
                for (int j = count; j < max; j++) {
                    sb.append(".  ");
                }

                sb.append(System.lineSeparator());
            }

            return sb.toString();
        }
    }

}
