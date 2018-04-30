/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.project.pkg2;

import java.util.Hashtable;

/**
 *
 * @author LiamClarke
 */
public class SymbolTable extends Hashtable<String, Symbol> {
    
    SymbolTable back;
    SymbolTable front;
    
    public SymbolTable moveForward(){
        SymbolTable newTable = new SymbolTable();
        this.front = newTable;
        newTable.back = this;
        
        return newTable;
    }
    
    public SymbolTable moveBackward(){
        this.back.front = null;
        
        return this.back;
    }
    
    public boolean find(String key){
        SymbolTable searchTable;
        searchTable = this;
        while(searchTable != null){
            if(searchTable.containsKey(key)){
                return true;
            }else{
                searchTable = this.back;
            }         
        }
        return false;
    }
    
}
