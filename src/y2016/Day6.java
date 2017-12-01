/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2016;

import adventofcode.TaskSolver;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author karel.hebik
 */
public class Day6 extends TaskSolver {

    private List<String> messages = null;
    private String message1 = null;
    private String message2 = null;

    public Day6() {
        messages = new ArrayList<>();
        message1 = "";
        message2 = "";
    }

    @Override
    public void process() {
        InputStream fis = null;
        try {
            fis = new FileInputStream("input/day6.txt");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                messages.add(line);
            }

            for (int i = 0; i < messages.get(0).length(); i++) {
                String letters = "";
                for (String s : messages) {
                    letters += s.charAt(i);
                }
                message1 += getMostFrequent(letters);
                message2 += getLeastFrequent(letters);
            }

            System.out.println("First original message is: " + message1);
            System.out.println("Second original message is: " + message2);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Day2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Day2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Day2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private char getMostFrequent(String input) {
        Stream<String> str = Stream.of(input);
        Map<String, Long> col = str.map(w -> w.split("")).flatMap(Arrays::stream).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Optional<String> c = col.entrySet().stream().sorted((l, r) -> r.getValue().compareTo(l.getValue())).map(e -> e.getKey()).findFirst();
        return c.get().charAt(0);
    }

    private char getLeastFrequent(String input) {
        Stream<String> str = Stream.of(input);
        Map<String, Long> col = str.map(w -> w.split("")).flatMap(Arrays::stream).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Optional<String> c = col.entrySet().stream().sorted((l, r) -> l.getValue().compareTo(r.getValue())).map(e -> e.getKey()).findFirst();
        return c.get().charAt(0);
    }
}
