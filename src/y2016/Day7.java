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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author karel.hebik
 */
public class Day7 extends TaskSolver {

    private List<String> ips = null;

    public Day7() {
        ips = new ArrayList<>();
    }

    @Override
    public void process() {
        InputStream fis = null;
        try {
            fis = new FileInputStream("input/day7.txt");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                ips.add(line);
            }

            int tls = 0;
            int ssl = 0;
            for (String ip : ips) {
                if (supportsTLS(ip)) {
                    tls++;
                }
                if (supportsSSL(ip)) {
                    ssl++;
                }
            }
            System.out.println("TLS support count: " + tls);
            System.out.println("SSL support count: " + ssl);
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

    private boolean supportsTLS(String input) {
        Pattern alpha = Pattern.compile("([a-z]+)|\\[([a-z]+)\\]");
        Matcher mr = alpha.matcher(input);
        boolean supports = false;
        while (mr.find()) {
            if (mr.group(1) != null) {
                if (hasABBA(mr.group(1))) {
                    supports = true;
                }
            } else if (mr.group(2) != null) {
                if (hasABBA(mr.group(2))) {
                    supports = false;
                    break;
                }
            }
        }
        return supports;
    }

    private boolean supportsSSL(String input) {
        Pattern alpha = Pattern.compile("([a-z]+)|\\[([a-z]+)\\]");
        Matcher mr = alpha.matcher(input);
        List<String> supernet = new ArrayList<>();
        List<String> hypernet = new ArrayList<>();

        while (mr.find()) {
            if (mr.group(1) != null) {
                supernet.add(mr.group(1));
            } else if (mr.group(2) != null) {
                hypernet.add(mr.group(2));
            }
        }

        return hasABABAB(supernet, hypernet);
    }

    private boolean hasABBA(String input) {
        for (int i = 0; i < input.length() - 3; i++) {
            String part = input.substring(i, i + 4);
            if (part.charAt(0) == part.charAt(3) && part.charAt(1) == part.charAt(2) && part.charAt(0) != part.charAt(2)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasABABAB(List<String> supernet, List<String> hypernet) {
        for (String s : supernet) {
            for (int i = 0; i < s.length() - 2; i++) {
                String spart = s.substring(i, i + 3);
                if (spart.charAt(0) == spart.charAt(2) && spart.charAt(0) != spart.charAt(1)) {
                    for (String h : hypernet) {
                        for (int j = 0; j < h.length() - 2; j++) {
                            String hpart = h.substring(j, j + 3);
                            if (hpart.charAt(0) == spart.charAt(1) && hpart.charAt(1) == spart.charAt(0) && hpart.charAt(2) == spart.charAt(1)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
