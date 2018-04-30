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
public class cminusProject {
    public static void main(String[] args){
        try {
            project1 sc = new project1(args);
            project4 cP4 = new project4 ("tokens.txt");
        } catch (IOException ex) {
            Logger.getLogger(p2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
