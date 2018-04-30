package compiler.project.pkg2;

import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

/**
Code Generation
* Add/Sub/Div/Mul - Done
* If/Else - DONE
* While - DONE
* Function Calls - DONE
* Arrays DO Displacement - DONE
* Return - Done
 
@author William Clarke, n00935124

 */

public class project3 {
    
    public static int index = 0;
    public static int codeStorIndex = 0;
    public static String format = "%-5s %-10s %-10s %-10s %-10s";
    public static String mulOpOn = "";
    public static String currentTemp = "";
    public static String compString = "";
    public static int variableCount = 0;
    public static int lineNum = 0;
    public static int argAmount = 0;
    public static String currentid = "";
    public static String[] tokenArray = new String[900];
    public static String[][] codeStor = new String[500][500];
    public static HashSet<String> symbols = new HashSet<>();
    public static SymbolTable theTable = new SymbolTable();
    
    

    public project3(String tokenFile) throws FileNotFoundException {
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
    
    //Start -> type-spec ID Q declaration_listA
    public static void start(String token){
        if(token.contains("int") || token.contains("float") || token.contains("void")){
            Symbol s = new Symbol();
            type_spec(tokenArray[index], s);
            if(tokenArray[index].contains("ID")){
                s.id = tokenArray[index].substring(4);
                index++;
            }

            Q(tokenArray[index], s);
            lineNum++;
            storeVar(lineNum,"end","func",s.id,"");
            declaration_listA(tokenArray[index]); 
        }else{              
        }
    }
    
    //declaration_listA -> type_spec ID Q declaration_listA| EPSILON
    public static void declaration_listA(String token){
        if(token.contains("int") || token.contains("float") || token.contains("void")){
            Symbol s = new Symbol();
            type_spec(tokenArray[index], s);
            if(tokenArray[index].contains("ID")){
                s.id = tokenArray[index].substring(4);
                index++;
            }
            Q(tokenArray[index], s);
            lineNum++;
            storeVar(lineNum,"end","func",s.id,"");
            declaration_listA(tokenArray[index]);
        }else if(token.contains("$")){
            if(theTable.find("main")){
            printCodeGen();
            //    System.out.println("ACCEPT");
            }else{
            }
        }else{
        }
    }
    
   // Q-> A|AX
    public static void Q(String token, Symbol s){
        if(token.contains(";") || token.contains("[")){
            if(s.type.equals("void")){
            }
            theTable.put(s.id, s);
            A(token, s);
        }else if(token.contains("(")){
            s.parameters = new ArrayList();
            AX(token, s);
        }else{
        }
    }
    
    //A -> ;|[NUM];
    public static void A(String token, Symbol s){
        if(token.contains(";")){
            if(s.type.equals("void")){
            }
            printCodeGen(s);
            index++;
        }else if(token.contains("[")){
            s.isArray = true;
            index++;
            if(tokenArray[index].contains("NUM:")){
                s.isInt = true;
                s.number = Integer.parseInt(tokenArray[index].substring(5));
                index++;
                if(tokenArray[index].contains("]")){
                    index++;
                    if(tokenArray[index].contains(";")){
                        index++;
                        printCodeGen(s);
                    }
                }
            }
            if(tokenArray[index].contains("FLOAT:")){
            }
        }else{
        }
    }
    
    //AX -> (params) compound_stmt
    public static void AX(String token, Symbol s){
        if(token.contains("(")){
            index++;
            s.isMethod = true;
            params(tokenArray[index], s);
            if(tokenArray[index].contains(")")){
                theTable.put(s.id, s);
                currentid = s.id;
                printCodeGen(s);
                index++;
                theTable = theTable.moveForward();
                theTable.put(s.id, s);
                for(int i = 0; i < s.parameters.size(); i++){
                    theTable.put(s.parameters.get(i).id, s.parameters.get(i));
                }
            }else{
            }
            compound_stmt(tokenArray[index]);
        }else{
        }
    }
    
    //type_spec -> int|float|void
    public static void type_spec(String token, Symbol s){
        if(token.contains("int") || token.contains("float") || token.contains("void")){
            if(token.contains("int")){
                s.isInt = true;
            }
            if(token.contains("float")){
                s.isFloat = true;
            }
            s.type = token.substring(9);
            index++;
        }else{
        }
        
    }
    
    //params -> int ID B params_list| float ID B params_list| void
    public static void params(String token, Symbol s){
        if(token.contains("int")){
            Symbol s2 = new Symbol();
            s2.type = token.substring(9);
            s2.isInt = true;
            index++;
            if(tokenArray[index].contains("ID")){
                if(theTable.containsKey(tokenArray[index].substring(4))){
                }
                s2.id = tokenArray[index].substring(4);
                index++;
                //printCodeGen(s2);
                B(tokenArray[index], s2);
                params_list(tokenArray[index], s); 
                s.parameters.add(s2);
            }else{
            }
        }else if(token.contains("float")){
            index++;
            if(tokenArray[index].contains("ID")){
                index++;
                B(tokenArray[index], s);
                params_list(tokenArray[index], s);          
            }else{
            }  
        }else if(token.contains("void")){
            index++;
        }else{
        }
    }
    
    //params_list -> , type_spec ID B params'| EPSILON
    public static void params_list(String token, Symbol s){
        if(token.contains(",")){
            Symbol s3 = new Symbol();
            index++;
            type_spec(tokenArray[index], s3);
            if(tokenArray[index].contains("ID")){
                if(theTable.containsKey(tokenArray[index].substring(4))){
                }
                s3.id = tokenArray[index].substring(4);
                index++;
                B(tokenArray[index], s3);
                params_list(tokenArray[index], s);
                s.parameters.add(s3);
            }else{
            }  
        }
        return;
    }
    
    // B -> [ ] | EPSILON
    public static void B(String token, Symbol s){
        if(token.contains("[")){
            s.isArray = true;
            index++;
            if(tokenArray[index].contains("]")){
                index++;
            }else{
            }
        }else if(token.contains(",")|| token.contains(")")){
            return;
        }else{
        }
    }

    
    //compound_stmt -> { local_declarations statement_list }
    public static void compound_stmt(String token){
        if(token.contains("{")){
            index++;
            local_declarations(tokenArray[index]);
            statement_list(tokenArray[index]);
            if(tokenArray[index].contains("}")){ // FIX THIS
                //lineNum++;
                //storeVar(lineNum,"end","func","uh","");
                theTable = theTable.moveBackward();
                index++;
            }else{

            }                   
        }else{
        }
    }
    
    //local_declarations ->  var_declaration local_declarations| EPSILON
    public static void local_declarations(String token){
        if(token.contains("int") || token.contains("float") || token.contains("void")){
            var_declaration(token);
            local_declarations(tokenArray[index]);
        }else{
            return;
        }
    }   
    //statement_list -> statement statement_list| EPSILON
    public static void statement_list(String token){
        if(token.contains("{") || token.contains(";") || token.contains("ID") ||
                token.contains("(") || token.contains("if") || token.contains("return") ||
                token.contains("while")){
            statement(token);
            statement_list(tokenArray[index]);     
        }else{
            return; 
        }
    }
    
    //var_declaration -> type_spec ID A
    public static void var_declaration(String token){
        Symbol s = new Symbol();
        type_spec(token, s);
        if(tokenArray[index].contains("ID:")){
            if(theTable.containsKey(tokenArray[index].substring(4))){
                }
            s.id = tokenArray[index].substring(4);
            index++;
        }else{
        }
        A(tokenArray[index], s);
        theTable.put(s.id, s);
    }
    
    //S -> D|compound_stmt|selection_stmt|iteration_stmt|return_stmt CHECK THIS ONE
    public static void statement(String token){
        Symbol s = new Symbol();
        if(token.contains("ID:") || token.contains(";") || token.contains("(")){
            D(token, s);
        }
        //First of compound_stmt
        else if(token.contains("{")){
            theTable = theTable.moveForward();
            compound_stmt(token);
        }
        //First of selection_stmt
        else if(token.contains("if")){
            selection_stmt(token);
        }
        //First of iteration_stmt
        else if(token.contains("while")){
            iteration_stmt(token);
        }
        //First of return_stmt
        else if(token.contains("return")){
            return_stmt(token);
        }else{
       }
    }
    //D -> expression ;| ;
    public static String D(String token, Symbol s){
        if(token.contains("ID:") || token.contains("(")){
            String str = expression(token);
            if(tokenArray[index].contains(";")){
                index++;
            }else{
            }
            return str;
        }else if(token.contains(";")){
            index++;
        }else{
        } 
        return null;
    }
    
    //N -> if (expression) statement
    public static void selection_stmt(String token){
        if(token.contains("if")){
            index++;
            if(tokenArray[index].contains("(")){
                expression(tokenArray[index]);
                if(tokenArray[index - 1].contains(")")){
                    int compareLine = lineNum + 1;
                    if(compString.equals(">")){
                    lineNum++;
                    storeVar(lineNum,"BRLEQ" ,currentTemp,"","hold"); 
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(compString.equals("<")){
                    lineNum++;
                    storeVar(lineNum,"BRGEQ" ,currentTemp,"","hold");
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(compString.equals("<=")){
                    lineNum++;
                    storeVar(lineNum,"BRGT" ,currentTemp,"","hold");
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(compString.equals(">=")){
                    lineNum++;
                    storeVar(lineNum,"BRLT" ,currentTemp,"","hold");
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(compString.equals("==")){
                    lineNum++;
                    storeVar(lineNum,"BRNEQ" ,currentTemp,"","hold");
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(compString.equals("!=")){
                    lineNum++;
                    storeVar(lineNum,"BREQ" ,currentTemp,"","hold");
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                    statement(tokenArray[index]);
                    String line = C(tokenArray[index]);
                    codeStor[compareLine - 1][4] = line; 
                }else{  
                }
            }else{
            }
        }else{
        }
    }
    
    //C -> else statement|EMPTY
    public static String C(String token){
        if(token.contains("else")){
            lineNum++;
            index++;
            int compareLine = lineNum;
            storeVar(lineNum,"BR","","","");
            statement(tokenArray[index]);
            codeStor[compareLine - 1][4] = Integer.toString(lineNum + 1); 
            return Integer.toString(lineNum);
        }else{
            return Integer.toString(lineNum);
        }
    }
    //IS -> while (expression) statement
    public static void iteration_stmt(String token){
        if(token.contains("while")){
            int endNum = (lineNum + 1);
            index++;
            if(tokenArray[index].contains("(")){
                index++;
                expression(tokenArray[index]);
                int compareLine = lineNum + 1;
                if(compString.equals(">")){
                    lineNum++;
                    storeVar(lineNum,"BRLEQ" ,currentTemp,"","hold"); 
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(compString.equals("<")){
                    lineNum++;
                    storeVar(lineNum,"BRGEQ" ,currentTemp,"","hold");
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(compString.equals("<=")){
                    lineNum++;
                    storeVar(lineNum,"BRGT" ,currentTemp,"","hold");
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(compString.equals(">=")){
                    lineNum++;
                    storeVar(lineNum,"BRLT" ,currentTemp,"","hold");
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(compString.equals("==")){
                    lineNum++;
                    storeVar(lineNum,"BRNEQ" ,currentTemp,"","hold");
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(compString.equals("!=")){
                    lineNum++;
                    storeVar(lineNum,"BREQ" ,currentTemp,"","hold");
                    variableCount++;
                    lineNum++;
                    storeVar(lineNum,"block","","","");
                    }
                if(tokenArray[index].contains(")")){
                    index++;
                    statement(tokenArray[index]);
                    lineNum++;
                    storeVar(lineNum,"end","block","","");
                    lineNum++;
                    storeVar(lineNum,"BR","","",Integer.toString(endNum));
                    codeStor[compareLine - 1][4] = Integer.toString(lineNum + 1); 
                }else{
                }
            }else{
            }
        }else{
        }
    }
    
    //R -> return D
    public static void return_stmt(String token){
        if(token.contains("return")){
            Symbol s = new Symbol();
            s.id = token.substring(9);
            s.type = token.substring(9);
            index++;
            theTable.put(s.id, s);
            String str = D(tokenArray[index], s);
            lineNum++;
            if(str == null){
            storeVar(lineNum,"return","","",currentTemp);
            variableCount++;
           // System.out.println(String.format(format,lineNum,"return","","",currentTemp));
            }else{
            storeVar(lineNum,"return","","",str);
            //System.out.println(String.format(format,lineNum,"return","","",str));
            }
        }else{
        }
    }
    
    //EX -> ID EV| (expression) term additive_expression F| NUM term additive_expression F
    public static String expression(String token){
        Symbol s = new Symbol();
        if(token.contains("ID")){
            s.id = tokenArray[index].substring(4);
            index++;
            String str = EV(tokenArray[index], s);
            if(tokenArray[index - 2].contains("return") && tokenArray[index].contains(";")){
                return token.substring(4);
            }else{
                if(str == null){
                    return s.id;
                }else{
                    return str;
                }
            }
        }else if(token.contains("(")){
            index++;
            expression(tokenArray[index]);
            if(tokenArray[index].contains(")")){
                argAmount = 0;
                index++;
                String str = term(tokenArray[index], s.id);
                String temp = additive_expression(tokenArray[index], s, str);
                F(tokenArray[index], temp == null ? str: temp);
                return currentTemp;
            }
        }else if(token.contains("NUM:") || token.contains("FLOAT:")){
            if(token.contains("NUM:")){
            s.number = Integer.parseInt(tokenArray[index].substring(5));
            }
            if(token.contains("FLOAT:")){
             //   System.out.println(token.substring(7));
            }
            index++;
            String temp = term(tokenArray[index], token.substring(4));
            String str = additive_expression(tokenArray[index], s, token.substring(4));
            F(tokenArray[index], temp == null ? str: temp);
            if(str == null){
                return tokenArray[index - 1].substring(4);
            }
            if(temp == null){
                return str;
            }else{
                return temp;
            }
        }else{
        }
        return null;
    }
    
    //EV -> E EV2| term additive_expression F
    public static String EV(String token, Symbol s){ // ','
        //First of E
        if(token.contains("[")){
            E(token);
            EV2(tokenArray[index],s, s.id);
        }
        //First of EV2
        else if(symbols.contains(token)){
            return EV2(token,s, s.id);
        }
        else if(token.contains("(")){
            index++;
            args(tokenArray[index]);
            if(tokenArray[index].contains(")")){
                index++;
                lineNum++;
                newVariable();
                variableCount++;
                Symbol tempSym = theTable.back.get(s.id);
                storeVar(lineNum,"call", s.id,Integer.toString(tempSym.parameters.size()),currentTemp);
                String str = term(tokenArray[index], null);
                String temp = additive_expression(tokenArray[index], s, null);
                return currentTemp; 
            }else{         
            }       
        }
        return null;
    }
    
    //E -> [expression]|EMPTY
    public static String E(String token){
        if(token.contains("[")){
            String array = tokenArray[index - 1].substring(4);
            index++;
            int allocation = 0;
            String str = expression(tokenArray[index]);
            if(tokenArray[index].contains("]")){
            if(str.matches(".*[a-zA-Z]+.*")){
            lineNum++;
            String hold = currentTemp;
            newVariable();
            storeVar(lineNum,"disp",array,hold,currentTemp);
            variableCount++;
            }else{
            int result = Integer.parseInt(str.trim());
            allocation = result * 4;
            lineNum++;
            newVariable();
            storeVar(lineNum,"disp",array,Integer.toString(allocation),currentTemp);
            variableCount++;
            }
            index++;
            return str;
            }else{
            }
        }else{
            return null;
        }
            return null;
        }
    
    //EV2 -> = expression| term additive_expression F
    public static String EV2(String token, Symbol s, String leftSide){
        
        //EV2 -> = expression
        if(token.contains("=")){
            index++;
            String str = expression(tokenArray[index]);
            lineNum++;
           if(s.id != null){
                storeVar(lineNum,"assign",str.trim(),"",s.id);
           }
        }
        //first of term
        else if(token.contains("*") || token.contains("/")){
            String str = term(token, leftSide);
            String temp = additive_expression(tokenArray[index], s, null);
            String hold = F(tokenArray[index], str);
            return hold == null ? (temp == null ? (str == null? s.id: str) : temp) :hold;
        }     
        else if(token.contains("+") || token.contains("-")){
            String temp = "";
            if(tokenArray[index - 1].contains("]")){
            temp = additive_expression(token, s,"disp");    
            }else{
            temp = additive_expression(token, s, tokenArray[index - 1].substring(4));
            }
            F(tokenArray[index],currentTemp);
            return temp;
        }
        else if(token.contains("<=") || token.contains("<") ||
                token.contains(">") || token.contains(">=") ||
                token.contains("==") || token.contains("!=")){
            F(token,leftSide);
        }
        else{

        }
        return null;
    }
    
    public static String term(String token, String leftSide){
        if(token.contains("*") || token.contains("/")){
            String str = mulop(token);
            String rightSide = factor(tokenArray[index]);
            if(str.contains("*")){
                if(leftSide == null){
                lineNum++;
                String hold = currentTemp;
                newVariable();
                storeVar(lineNum,"mul",hold,rightSide,currentTemp);
                variableCount++;  
                }else{
                    newVariable();
                    lineNum++;
                    storeVar(lineNum,"mul",leftSide.trim(),rightSide,currentTemp);
                    variableCount++;  
                }
            }if(str.contains("/")){
                newVariable();
                lineNum++;
                //System.out.println(String.format(format,lineNum,"div",leftSide,rightSide,currentTemp));
                storeVar(lineNum,"div",leftSide.trim(),rightSide,currentTemp);
                variableCount++;
            }
            String temp = term(tokenArray[index], currentTemp);
            if(temp != null){
                return temp;
            }else{
                return currentTemp;
            }
        }else{
            return null;
        }    
    }
    
    //additive_expression -> addop factor term additive_expression| EPSILON
    public static String additive_expression(String token, Symbol s, String leftSide){
        if(token.contains("+") || token.contains("-")){
            addop(token, s);
            String str = factor(tokenArray[index]); 
            String termS = term(tokenArray[index], str);
            if(token.contains("+")){
                if(termS == null){
                    if(leftSide.equals("disp")){
                        String hold = currentTemp;
                        newVariable();
                        lineNum++;
                       // System.out.println(String.format(format,lineNum,"add",hold,str.trim(),currentTemp));
                        storeVar(lineNum,"add",hold,str.trim(),currentTemp);
                    }else{
                        String hold = currentTemp;
                        newVariable();
                        lineNum++;
                       // System.out.println(String.format(format,lineNum,"add",leftSide.trim(),str.trim(),currentTemp));
                        storeVar(lineNum,"add",leftSide.trim(),str.trim(),currentTemp);
                    }
                }else if(str == null){
                String hold = currentTemp;
                newVariable();
                lineNum++;
                System.out.println(String.format(format,lineNum,"add",leftSide.trim(),hold,currentTemp)); 
                storeVar(lineNum,"add",leftSide.trim(),hold,currentTemp);
                }else{
                    newVariable();
                    lineNum++;
                    //System.out.println(String.format(format,lineNum,"add",leftSide.trim(),termS,currentTemp));
                    storeVar(lineNum,"add",leftSide.trim(),termS,currentTemp);
                }
                variableCount++;
            }else if(token.contains("-")){
                lineNum++;
                newVariable();
                //System.out.println(String.format(format,lineNum,"sub",leftSide,str,currentTemp));
                storeVar(lineNum,"sub",leftSide,str,currentTemp);
                variableCount++;
            }
            String temp = additive_expression(tokenArray[index], s, currentTemp);
            if(temp ==  null){
                return currentTemp;
            }else{
                return temp;
            }
        }else{
            return null;
        }
    }
    
    //F -> RE factor term additive_expression| EPSILON
    public static String F(String token, String leftSide){
        Symbol s = new Symbol();
        if(token.contains("<=") || token.contains("<") || token.contains(">") ||
                token.contains(">=") || token.contains("==") || token.contains("!=")){
            RE(token);
            String temptwo = factor(tokenArray[index]);
            String str = term(tokenArray[index], temptwo);
            String temp = additive_expression(tokenArray[index], s, null);
            String rightSide = temp == null ? (str == null ? temptwo : str) :temp;
            lineNum++;
            newVariable();
            compString = token;
            storeVar(lineNum,"comp",leftSide,rightSide,currentTemp);
            return temp == null ? (str == null ? (temptwo == null? leftSide: temptwo) : str) :temp;
        }else{
            return null;
        }
    }
    
    //M-> *|/
    public static String mulop(String token){
        if(token.contains("*") || token.contains("/")){
            index++;
            return token;
        }else{
        }
        return null;
    }
    
    //args -> arg_list|EPSILON
    public static void args(String token){
        if(token.contains("(") || token.contains("ID:") || token.contains("NUM") || token.contains("FLOAT")){
            arg_list(tokenArray[index]);
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
        }      
    }
    
    //factor -> (expression)| ID FX| NUM
    public static String factor(String token){
        if(token.contains("(")){
            index++;
            String str = expression(tokenArray[index]);
            if(tokenArray[index].equals(")")){
                index++;
            }else{
            }
            return str;
        }else if(token.contains("ID:")){
            index++;
            FX(tokenArray[index]);
            return token.substring(4);
        }else if(token.contains("NUM") || token.contains("FLOAT")){
            index++;
            return token.substring(4);
        }else{
        }
        return null;
    }
    
    //FX -> E|(args)
    public static void FX(String token){
        if(token.contains("[")){
            E(token);
        }
        else if(token.contains("(")){
            index++;
            args(tokenArray[index]);
            if(tokenArray[index].contains(")")){
                index++;
            }else{
            }
        }else{
            return;
        }
    }
    
    //addop -> +/-
    public static void addop(String token, Symbol s){
        if(token.contains("+")){
            index++;
        }else if(token.contains("-")){
            index++;
        }else{
        }
    }
    
    //arg_list -> expression arg_list'
    public static void arg_list(String token){
        if(token.contains("(") || token.contains("ID:") || token.contains("NUM") || token.contains("FLOAT")){
            String str = expression(token);
            argAmount++;
            arg_listB(tokenArray[index]);
            lineNum++;
            storeVar(lineNum,"args","","",str);
        }else{
        }
    }
    
    //arg_listB -> , expression arg_listB | EPSILON
    public static void arg_listB(String token){
        if(token.contains(",")){
            index++;
            lineNum++;
            argAmount++;
           // System.out.println(String.format(format,lineNum,"args"," ","",tokenArray[index].substring(4)));
            storeVar(lineNum,"args","","",tokenArray[index].substring(4));
            expression(tokenArray[index]);
            arg_listB(tokenArray[index]);
        }else{
            return;
        }
    }
    
    public static void printCodeGen(Symbol s){
        int i = 0;
        int j = 0;
        
        if(s.isMethod){
            if(s.type.equalsIgnoreCase("void")){
                lineNum++;
                storeVar(lineNum,"func",s.id,s.type,Integer.toString(s.parameters.size()));
                s.isMethod = false;
             //   System.out.println(String.format(format,lineNum,"func",s.id,s.type,s.parameters.size()));
            }
            if(s.type.equalsIgnoreCase("int")){
                lineNum++;
                storeVar(lineNum,"func",s.id,s.type,Integer.toString(s.parameters.size()));
                s.isMethod = false;
               // System.out.println(String.format(format,lineNum,"func",s.id,s.type,s.parameters.size()));
                while( i < s.parameters.size()){
                    lineNum++;
                    storeVar(lineNum,"param","","","");
                 //   System.out.println(String.format(format,lineNum,"param","","",""));
                    i++;
                }
                while( j < s.parameters.size()){
                    lineNum++;
                    storeVar(lineNum,"alloc"," ","4",s.parameters.get(j).id);
                    //System.out.println(String.format(format,lineNum,"alloc"," ","4",s.parameters.get(j).id));
                    j++;
                }
            }
        }else if (s.isInt){
            if(s.isArray){
                lineNum++;
                int allocation = s.number * 4;
                storeVar(lineNum,"alloc"," ",Integer.toString(allocation),s.id);
                //System.out.println(String.format(format,lineNum,"alloc"," ",allocation,s.id));
            }else{
                lineNum++;
             //   System.out.println(String.format(format,lineNum,"alloc"," ","4",s.id));
                storeVar(lineNum,"alloc"," ","4",s.id);
            }
        }
    }
    
    public static void newVariable(){
        String temp = "_t" + Integer.toString(variableCount);
        
        currentTemp = temp;
    }
    
    // Function to insert into 2D Array
    public static void storeVar(int linenum, String type, String left, String right, String var){
        codeStor[codeStorIndex][0] = Integer.toString(linenum);
        codeStor[codeStorIndex][1] = type;
        codeStor[codeStorIndex][2] = left;
        codeStor[codeStorIndex][3] = right;
        codeStor[codeStorIndex][4] = var;
        codeStorIndex++;
    }
    
    public static void printCodeGen(){
        int i = 0;
        while(i < codeStorIndex){
            System.out.println(String.format(format,codeStor[i][0],codeStor[i][1],
            codeStor[i][2],codeStor[i][3],codeStor[i][4]));
            i++;
        }
    }
   
    
}
    