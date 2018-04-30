# C-Minus-Compiler
 The Front End of a C Minus Compiler that I created during my undergrad.


**Project 1 Lexical Analyzer**

SPECIFICATIONS:
Your project is to use the grammar definition in the appendix
of your text to guide the construction of a lexical analyzer. 
The lexical analyzer should return tokens as described. Keep 
in mind these tokens will serve as the input to the parser.
You must enhance the definitions by adding a keyword "float"
as a data type to the material on page 493 and beyond.
Specifically, rule 5 on page 492 should state

    type-specifier -> int | void | float

and any other modifications necessary must be included. 



**Project 2 Parser**

SPECIFICATION:
Your project is to use the grammar definition in the appendix
of your text to guide the construction of a recursive descent parser.
The parser should follow the grammar as described in A.2 page 492.

You should enhance the grammar to include FLOAT as
appropriate throughout all the grammar rules.

Upon execution, your project should report 

ACCEPT

or 

REJECT


**Project 3 Semantic Analysis**

SPECIFICATION:
Project 3 is the construction of semantic analyzer. You are to include 
in your parser appropriate checking not included in the grammar, but 
defined by the language.

This is going to be the test of the quality of your symbol
table implemented during parsing. You are to determine and
implement appropriate checks as discussed.

Your project should be shar'd containing a makefile, source file,
doc file, and typescript (showing your testing). The makefile file
should be invoked with "make" creating an executable of p3. Your
project will be invoked with p3 fn where fn is the data file to be
analyzed. p3 is the executable resulting from the make command 
or is a script that executes your project. BE SURE TO PROVIDE BOTH A
MAKEFILE AND A P3 EXECUTABLE SCRIPT FOR YOUR PROJECT. Also, be sure
to test the integrity of your shar.

This project must be complete in that the lexical analyzer must be
included to create the tokens required by the parser and
semantic analyzer.

Your project should report on a single line without any additional
characters

ACCEPT

or 

REJECT


**Project 4 Intermediate Code Generation**

You should generate simple quadruples as explained in class and shown
below.  When  you generate simple quadruples you should use the operators 
as described in class.

**Example**

**INPUT**

    int sub(int x)
    {
       return(x+x);
    }
    void main(void)
    {
      int x;
      int y;
      y = sub(x);
    }

**OUTPUT**

    1         func           sub            int            2
    2         param
    3         alloc          4                             x
    4         add            x              x              _t0
    5         return                                       _t0
    6         end            func           sub
    7         func           main           void           0
    8         alloc          4                             x
    9         alloc          4                             y
    10        arg                                          x
    11        call           sub            1              _t1
    12        assign         _t1                           y
    13        end            func           main
