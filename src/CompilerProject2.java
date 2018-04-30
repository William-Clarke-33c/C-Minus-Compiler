package compiler.project.pkg2;



import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

/**
Your project is to use the grammar definition in the appendix
of your text to guide the construction of a lexical analyzer. 
The lexical analyzer should return tokens as described. Keep 
in mind these tokens will serve as the input to the parser.
You must enhance the definitions by adding a keyword "float"
as a data type to the material on page 493 and beyond.
Specifically, rule 5 on page 492 should state

    type-specifier -> int | void | float

and any other modifications necessary must be included. 
 
@author William Clarke, n00935124
 */

public class CompilerProject2 {
    
    public static int index = 0;
    public static String status;
    public static Stack theStack = new Stack();
    public static String[] tokenArray = new String[900];
    public static HashSet<String> symbols = new HashSet<>();
    

    public CompilerProject2(String tokenFile) throws FileNotFoundException {
        //Takes the file from the command line
        File file = new File(tokenFile);
        
        //Adding Symbols to HashSets
        symbols.add("+");
        symbols.add("-");
        symbols.add("<");
        symbols.add(">");
        symbols.add("=");
        symbols.add("/");
        symbols.add("*");
        symbols.add("<=");
        symbols.add(">=");
        symbols.add("==");
        symbols.add("!=");
        
        parser(file);
    }
    //Attempts to read the file line by line then generates tokens and prints it out
    public static void parser(File file){
        try{
            Scanner input = new Scanner(file);
            for(int i = 0; input.hasNextLine(); i++){
                String text = input.nextLine();
                tokenArray[i] = text;
            }
            start(tokenArray[index]);
            
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    
    //Start -> T ID Q L
    public static void start(String token){
        if(token.contains("int") || token.contains("float") || token.contains("void")){
            T(tokenArray[index]);
            if(tokenArray[index].contains("ID")){
                index++;
            }
            Q(tokenArray[index]);
            L(tokenArray[index]);      
        }else{
            System.out.println("REJECT");
            System.exit(0);                
        }
    }
    
    //L -> T ID Q L| EPSILON
    public static void L(String token){
        if(token.contains("int") || token.contains("float") || token.contains("void")){
            T(tokenArray[index]);
            if(tokenArray[index].contains("ID")){
                index++;
            }
            Q(tokenArray[index]);
            L(tokenArray[index]);
        }else if(token.contains("$")){
            System.out.println("ACCEPT");
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
   // Q-> A|AX
    public static void Q(String token){
        if(token.contains(";") || token.contains("[")){
            A(token);
        }else if(token.contains("(")){
            AX(token);
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //A -> ;|[NUM];
    public static void A(String token){
        if(token.contains(";")){
            index++;
        }else if(token.contains("[")){
            index++;
            if(tokenArray[index].contains("NUM:") || tokenArray[index].contains("FLOAT:")){
                index++;
                if(tokenArray[index].contains("]")){
                    index++;
                    if(tokenArray[index].contains(";")){
                        index++;
                    }
                }
            }
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //AX -> (P) G
    public static void AX(String token){
        if(token.contains("(")){
            index++;
            P(tokenArray[index]);
            if(tokenArray[index].contains(")")){
                index++;
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }
            G(tokenArray[index]);
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //T -> int|float|void
    public static void T(String token){
        if(token.contains("int") || token.contains("float") || token.contains("void")){
            index++;
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
        
    }
    
    //P -> int ID B P'| float ID B P'| void
    public static void P(String token){
        if(token.contains("int")){
            index++;
            if(tokenArray[index].contains("ID")){
                index++;
                B(tokenArray[index]);
                pPrime(tokenArray[index]);          
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }
        }else if(token.contains("float")){
            index++;
            if(tokenArray[index].contains("ID")){
                index++;
                B(tokenArray[index]);
                pPrime(tokenArray[index]);          
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }  
        }else if(token.contains("void")){
            index++;
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //P' -> , T ID B P'| EPSILON
    public static void pPrime(String token){
        if(token.contains(",")){
            index++;
            T(tokenArray[index]);
            if(tokenArray[index].contains("ID")){
                index++;
                B(tokenArray[index]);
                pPrime(tokenArray[index]);
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }  
        }
        return;
    }
    
    // B -> [ ] | EPSILON
    public static void B(String token){
        if(token.contains("[")){
            index++;
            if(tokenArray[index].contains("]")){
                index++;
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }
        }else if(token.contains(",")|| token.contains(")")){
            return;
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }

    
    //G -> { O S' }
    public static void G(String token){
        if(token.contains("{")){
            index++;
            O(tokenArray[index]);
            sPrime(tokenArray[index]);
            if(tokenArray[index].contains("}")){
                index++;
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }                   
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //O ->  K O| EPSILON
    public static void O(String token){
        if(token.contains("int") || token.contains("float") || token.contains("void")){
            K(token);
            O(tokenArray[index]);
        }else{
            return;
        }
    }   
    //S' -> S S'| EPSILON
    public static void sPrime(String token){
        if(token.contains("{") || token.contains(";") || token.contains("ID") ||
                token.contains("(") || token.contains("if") || token.contains("return") ||
                token.contains("[") || token.contains("while")){
            S(token);
            sPrime(tokenArray[index]);     
        }else if(token.contains("}")){
            return;
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }    
    }
    
    //K -> T ID A
    public static void K(String token){
        T(token);
        if(tokenArray[index].contains("ID:")){
            index++;
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
        A(tokenArray[index]);
    }
    
    //S -> D|G|N|IS|R CHECK THIS ONE
    public static void S(String token){
        if(token.contains("ID:") || token.contains(";") || token.contains("(")){
            D(token);
        }
        //First of G
        else if(token.contains("{")){
            G(token);
        }
        //First of N
        else if(token.contains("if")){
            N(token);
        }
        //First of IS
        else if(token.contains("while")){
            IS(token);
        }
        //First of R
        else if(token.contains("return")){
            R(token);
        }else{
            System.out.println("REJECT"); // CHECK HERE
            System.exit(0);
       }
    }
    //D -> EX ;| ;
    public static void D(String token){
        if(token.contains("ID:") || token.contains("(")){
            EX(token);
            if(tokenArray[index].contains(";")){
                index++;
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }
        }else if(token.contains(";")){
            index++;
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }     
    }
    
    //N -> if (EX) S
    public static void N(String token){
        if(token.contains("if")){
            index++;
            if(tokenArray[index].contains("(")){
                EX(tokenArray[index]);
                if(tokenArray[index].contains(")")){
                    index++;
                    S(tokenArray[index]);
                    C(tokenArray[index]);
                }else{
                    System.out.println("REJECT");
                    System.exit(0);    
                }
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //C -> else S|EMPTY
    public static void C(String token){
        if(token.contains("else")){
            index++;
            S(tokenArray[index]);
        }
        return;
    }
    //IS -> while (EX) S
    public static void IS(String token){
        if(token.contains("while")){
            index++;
            if(tokenArray[index].contains("(")){
                index++;
                EX(tokenArray[index]);
                if(tokenArray[index].contains(")")){
                    index++;
                    S(tokenArray[index]);
                }else{
                    System.out.println("REJECT");
                    System.exit(0);
                }
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //R -> return D
    public static void R(String token){
        if(token.contains("return")){
            index++;
            D(tokenArray[index]);
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //EX -> ID EV| (EX) TE AE F| NUM TE AE F
    public static void EX(String token){
        if(token.contains("ID")){
            index++;
            EV(tokenArray[index]);
        }else if(token.contains("(")){
            index++;
            EX(tokenArray[index]);
            if(tokenArray[index].contains(")")){
                TE(tokenArray[index]);
                AE(tokenArray[index]);
                F(tokenArray[index]);
            }
        }else if(token.contains("NUM:") || (token.contains("FLOAT:"))){
            index++;
            TE(tokenArray[index]);
            AE(tokenArray[index]);
            F(tokenArray[index]);
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //EV -> E EV2| TE AE F
    public static void EV(String token){ // ','
        //First of E
        if(token.contains("[")){
            E(token);
            EV2(tokenArray[index]);
        }
        //First of EV2
        else if(symbols.contains(token)){
            EV2(token);
        }
        else if(token.contains("(")){
            index++;
            AG(tokenArray[index]);
            if(tokenArray[index].contains(")")){
                index++;
                TE(tokenArray[index]);
                AE(tokenArray[index]);
            }else{
                System.out.println("REJECT");
                System.exit(0);            
            }       
        }
        return;
    }
    
    //E -> [EX]|EMPTY
    public static void E(String token){
        if(token.contains("[")){
            index++;
            EX(tokenArray[index]);
            if(tokenArray[index].contains("]")){
                index++;
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }
        }else{
            return;
        }
    }
    
    //EV2 -> = EX| TE AE F
    public static void EV2(String token){
        
        //EV2 -> = EX
        if(token.contains("=")){
            index++;
            EX(tokenArray[index]);
        }
        //first of TE
        else if(token.contains("*") || token.contains("/")){
            TE(token);
            AE(tokenArray[index]);
            F(tokenArray[index]);
        }     
        else if(token.contains("+") || token.contains("-")){
            AE(token);
            F(tokenArray[index]);
        }
        else if(token.contains("<=") || token.contains("<") ||
                token.contains(">") || token.contains(">=") ||
                token.contains("==") || token.contains("!=")){
            F(token);
        }
        else{

        }
    }
    
    public static void TE(String token){
        if(token.contains("*") || token.contains("/")){
            M(token);
            factor(tokenArray[index]);
            TE(tokenArray[index]);
        }else{
            return;
        }
    }
    
    //AE -> AO factor TE AE| EPSILON
    public static void AE(String token){
        if(token.contains("+") || token.contains("-")){
            AO(token);
            factor(tokenArray[index]);
            TE(tokenArray[index]);
            AE(tokenArray[index]);
        }else{
            return;
        }
    }
    
    //F -> RE factor TE AE| EPSILON
    public static void F(String token){
        if(token.contains("<=") || token.contains("<") || token.contains(">") ||
                token.contains(">=") || token.contains("==") || token.contains("!=")){
            RE(token);
            factor(tokenArray[index]);
            TE(tokenArray[index]);
            AE(tokenArray[index]);
        }else{
            return;
        }
    }
    
    //M-> *|/
    public static void M(String token){
        if(token.contains("*") || token.contains("/")){
            index++;
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //AG -> AL|EPSILON
    public static void AG(String token){
        if(token.contains("(") || token.contains("ID:") || token.contains("NUM") || token.contains("FLOAT")){
            AL(tokenArray[index]);
        }else{
            return;
        }
    }
    
    //RE -> <=|<|>|>=|==|!=
    public static void RE(String token){
        if(token.contains("<=") || token.contains("<") || token.contains(">") ||
                token.contains(">=") || token.contains("==") || token.contains("!=")){
            index++;
        }
        else{
            System.out.println("REJECT");
            System.exit(0);
        }      
    }
    
    //factor -> (EX)| ID FX| NUM
    public static void factor(String token){
        if(token.contains("(")){
            index++;
            EX(tokenArray[index]);
            if(tokenArray[index].equals(")")){
                index++;
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }
        }else if(token.contains("ID:")){
            index++;
            FX(tokenArray[index]);
        }else if(token.contains("NUM:") || token.contains("FLOAT:")){
            index++;
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //FX -> E|(args)
    public static void FX(String token){
        if(token.contains("[")){
            E(token);
        }
        else if(token.contains("(")){
            index++;
            AG(tokenArray[index]);
            if(tokenArray[index].contains(")")){
                index++;
            }else{
                System.out.println("REJECT");
                System.exit(0);
            }
        }else{
            return;
        }
    }
    
    //AO -> +/-
    public static void AO(String token){
        if(token.contains("+") || token.contains("-")){
            index++;
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    //AL -> EX AL'
    public static void AL(String token){
        if(token.contains("(") || token.contains("ID:") || token.contains("NUM") || token.contains("FLOAT")){
            EX(token);
            alPrime(tokenArray[index]);
        }else{
            System.out.println("REJECT");
            System.exit(0);
        }
    }
    
    public static void alPrime(String token){
        if(token.contains(",")){
            index++;
            EX(tokenArray[index]);
            alPrime(tokenArray[index]);
        }else{
            return;
        }
    }
}
               


