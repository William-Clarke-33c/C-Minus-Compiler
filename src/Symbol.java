/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.project.pkg2;

import java.util.ArrayList;

/**
 *
 * @author LiamClarke
 */
public class Symbol {
    public String type;
    public String id;
    public String operation;
    public int number;
    public boolean isMethod;
    public boolean isArray;
    public boolean isInt;
    public boolean isFloat;
    public boolean isEnd;
    public boolean isCalled;
    public boolean isAdd;
    public boolean isSub;
    public boolean isReturn;
    ArrayList<Symbol> parameters;
    ArrayList<String> varParams;

    @Override
    public String toString() {
        if(isMethod == true){
            return "Symbol{" + "type=" + type + ", id=" + id + ", isMethod=" + isMethod + ", parameters=" + parameters + '}';
        }else{
            return "Symbol{" + "type=" + type + ", id=" + id + '}' + ", isArray=" + isArray + ", isInt=" + isInt + ", isFloat=" + isFloat + ", Num: " + number + 
                    "operation=" + operation + '}';
        }
    }
    
}
