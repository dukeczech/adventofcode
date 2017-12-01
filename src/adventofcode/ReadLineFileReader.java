/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author karel.hebik
 */
public class ReadLineFileReader {

    private String fi = null;
    private LineProcessor pr = null;

    public ReadLineFileReader(String file, LineProcessor processor) {
        fi = file;
        pr = processor;
    }

    public void start() {
        if (fi == null) {
            return;
        }
        
        InputStream fis = null;
        try {
            fis = new FileInputStream(fi);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (pr != null) {
                    pr.processLine(line);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadLineFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadLineFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadLineFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
