package compiler.project.pkg2;

import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author William Clarke
 */
public class p2 {
    public static void main(String[] args){
        try {
            p1 sc = new p1(args);
            project3 cP3 = new project3 ("tokens.txt");
        } catch (IOException ex) {
            Logger.getLogger(p2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
