package compiler.project.pkg2;

import java.io.*;
import java.util.*;
import java.math.*;
import java.util.ArrayList;
import java.util.List;

////////////////////////////////////////////////////////////////////////

public class p1
	{
	public static int parenNest;
	public static int bracketNest;
	public static int braceNest;
        static FileWriter output;
	public static List<String> LineOfCode = new ArrayList<>();
	public static List<Tokens> tokenArray = new ArrayList<>();
	public p1(String[] args) throws IOException
		{
                output = new FileWriter("tokens.txt");
		int i = 0;
		int counter = 0;
		//String line;
		List<String> line = new ArrayList<>();
		File f = new File(args[0]);
		Scanner scan = new Scanner(f);
		while(scan.hasNextLine())
			{
			counter++;
			line.add(scan.nextLine());
			}
		tokenArray = lexical(line, counter);
		}

/////////////////////////////////////////////////////////////////////////////

	public static ArrayList<Tokens> lexical(List<String> line, int count) throws IOException
		{
		ArrayList<Tokens> tokArray = new ArrayList<Tokens>();
		int commentNest = 0;
		int b = 0;

		lineloop: for(String LineList: line)
			{
			LineList = LineList + " ";
			char[] elements = LineList.toCharArray();
			if(elements.length > 0)
			for(int point = 0; point < elements.length; point++)
				{
				if(elements[point] == ' ')
					continue;
				if(elements[point] == '/')
					{
					if(point < elements.length - 1)
						{
						if(elements[point + 1] == '*')         // regular comment
							{
							point++;
							commentNest++;                 // comment nesting depth
							continue;
							}
						else if(elements[point + 1] == '/')   // line comment
							continue lineloop;
						}
					}
				if(elements[point] == '*')
					{
					if((point < elements.length - 1) && (elements[point + 1] == '/'))
						{
						if((commentNest > 0))
							{
							commentNest--;                // decrement comment nest by 1
							elements[point] = ' ';        // eliminate end comment token
							elements[point + 1] = ' ';    // eliminate end comment token	
							continue;
							}
						}
					}
				

				if(commentNest == 0)
					{
					if(elements[point] == '(')
						{
						//parenNest++;
						output.write("(");
                                                output.write("\n");
						tokArray.add(new Tokens("(", "symbol"));
						continue;
						}
					if(elements[point] == ')')
						{
						//parenNest--;
						output.write(")");
                                                output.write("\n");
						tokArray.add(new Tokens(")", "symbol"));
						continue;
						}
					if(elements[point] == '[')
						{
						//bracketNest++;
						output.write("[");
                                                output.write("\n");
						tokArray.add(new Tokens("[", "symbol"));
						continue;
						}
					if(elements[point] == ']')
						{
						//bracketNest--;
						output.write("]");
                                                output.write("\n");
						tokArray.add(new Tokens("]", "symbol"));
						continue;
						}
					if(elements[point] == '{')
						{
						//braceNest++;
						output.write("{");
                                                output.write("\n");
						tokArray.add(new Tokens("{", "symbol"));
						continue;
						}
					if(elements[point] == '}')
						{
						//braceNest--;
						output.write("}");
                                                output.write("\n");
						tokArray.add(new Tokens("}", "symbol"));
						continue;
						}
					if(point < elements.length - 1)
						{
						if((elements[point] == '<') && (elements[point + 1] == '='))
							{
							output.write("<=");
                                                        output.write("\n");
							point++;
							tokArray.add(new Tokens("<=", "symbol"));
							continue;
							}
						if((elements[point] == '>') && (elements[point + 1] == '='))
                                                        {
                                                        output.write(">=");
                                                        output.write("\n");
                                                        point++;
                                                        tokArray.add(new Tokens(">=", "symbol"));
                                                        continue;
                                                        }
						if((elements[point] == '=') && (elements[point + 1] == '='))
                                                        {
                                                        output.write("==");
                                                        output.write("\n");
                                                        point++;
                                                        tokArray.add(new Tokens("==", "symbol"));
                                                        continue;
                                                        }
						if((elements[point] == '!') && (elements[point + 1] == '='))
							{
							output.write("!=");
                                                        output.write("\n");
							point++;
							tokArray.add(new Tokens("!=", "symbol"));
							continue;
							}
						if((elements[point] == '!') && (elements[point + 1] != '='))
							{
							output.write("ERROR: !");
                                                        output.write("\n");
							continue;
							}
						if(elements[point] == '+')
							{
							output.write("+");
                                                        output.write("\n");
							tokArray.add(new Tokens("+", "symbol"));
							continue;
							}
						if(elements[point] == '-')
                                                        {
                                                        output.write("-");
                                                        output.write("\n");
                                                        tokArray.add(new Tokens("-", "symbol"));
							continue;
                                                        }
						if(elements[point] == '*')
                                                        {
                                                        output.write("*");
                                                        output.write("\n");
                                                        tokArray.add(new Tokens("*", "symbol"));
							continue;
                                                        }
						if(elements[point] == '/')
                                                        {
                                                        output.write("/");
                                                        output.write("\n");
                                                        tokArray.add(new Tokens("/", "symbol"));
							continue;
                                                        }
						if(elements[point] == '<')
                                                        {
                                                        output.write("<");
                                                        output.write("\n");
                                                        tokArray.add(new Tokens("<", "symbol"));
                                                        continue;
                                                        }
						if(elements[point] == '>')
                                                        {
                                                        output.write(">");
                                                        output.write("\n");
                                                        tokArray.add(new Tokens(">", "symbol"));
                                                        continue;
                                                        }
						if(elements[point] == '=')
                                                        {
                                                        output.write("=");
                                                        output.write("\n");
                                                        tokArray.add(new Tokens("=", "symbol"));
                                                        continue;
                                                        }
						if(elements[point] == ';')
                                                        {
                                                        output.write(";");
                                                        output.write("\n");
                                                        tokArray.add(new Tokens(";", "symbol"));
                                                        continue;
                                                        }
						if(elements[point] == ',')
                                                        {
                                                        output.write(",");
                                                        output.write("\n");
                                                        tokArray.add(new Tokens(",", "symbol"));
                                                        continue;
                                                        }
				// *************** Word Creator ******************
						if(Character.isLetter(elements[point]))
							{
							int point2 = point;
							String WordBuilder = "";
							while((point2 < elements.length) && (Character.isLetter(elements[point2])))
								WordBuilder = WordBuilder + elements[point2++];
							point = point2 - 1;
							if((WordBuilder.equals("else")) || (WordBuilder.equals("if")) || (WordBuilder.equals("int")) || (WordBuilder.equals("return")) || (WordBuilder.equals("void")) || (WordBuilder.equals("while")) || (WordBuilder.equals("float")))
								{
								output.write("keyword: " + WordBuilder);
                                                                output.write("\n");
								tokArray.add(new Tokens(WordBuilder, "keyword"));
								}
							else
								{
								output.write("ID: " + WordBuilder);
                                                                output.write("\n");
								tokArray.add(new Tokens(WordBuilder, "ID"));
								}
							}
                                // *************** Number Creator ****************
                                		if(Character.isDigit(elements[point]))
							{
							int point2 = point;
							String NumBuilder = "";
							int state = 1;
							while((point2 < elements.length) && (state != 9))
								{	
								switch (state)
									{
									case 1: NumBuilder = NumBuilder + elements[point2++];
										if(Character.isDigit(elements[point2]))
											state = 1;
										else if(elements[point2] == '.')
											state = 2;
										else if(elements[point2] == 'E')
											state = 4;
										else
											state = 10;
										break;
									case 2: NumBuilder = NumBuilder + elements[point2++];
										if(Character.isDigit(elements[point2]))
											state = 3;
										else
											{
											
											state = 8;
											}
										break;
									case 3: NumBuilder = NumBuilder + elements[point2++];
										if(Character.isDigit(elements[point2]))
											state = 3;
										else if(elements[point2] == 'E')
											state = 4;
										else
                                                                                        state = 7;
                                                                                break;
									case 4: NumBuilder = NumBuilder + elements[point2++];
										if(Character.isDigit(elements[point2]))
											state = 6;
										else if((elements[point2] == '-') || (elements[point2] == '+'))
											state = 5;
										else
                                                                                        {                                                                                   
                                                                                        state = 8;
                                                                                        }
                                                                                break;
									case 5: NumBuilder = NumBuilder + elements[point2++];
										if(Character.isDigit(elements[point2]))
											state = 6;
										else
											{
											state = 8;
											}
										break;
									case 6: NumBuilder = NumBuilder + elements[point2++];
										if((Character.isDigit(elements[point2])))
                                                                                        state = 6;
										else
											state = 7;
										break;
									case 7: output.write("FLOAT: " + NumBuilder);
                                                                                output.write("\n");
										point = point2 - 1;
										tokArray.add(new Tokens(NumBuilder, "floatNUM"));
										state = 9;
										break;
									case 8: point = point2 - 1;
										state = 9;
										break;
									case 9: break;
									case 10: output.write("NUM: " + NumBuilder);
                                                                                 output.write("\n");
										 point = point2 - 1;
										 tokArray.add(new Tokens(NumBuilder, "intNUM"));
                                                                                 state = 9;
                                                                                 break;
									}
								}
							}
							if((elements[point] == '.') || (elements[point] == '!') || (elements[point] == '@') || (elements[point] == '#') || (elements[point] == '_') ||(elements[point] == '$') || (elements[point] == '%') || (elements[point] == '^') || (elements[point] == '&') || (elements[point] == '"') || (elements[point] == ':') || (elements[point] == '?')){
                                                        }
						}
					}
				}
			}
                output.write("$");
                output.close();
		return tokArray;
		}
	}

////////////////////////////////////////////////////////////////////////////////////////////

class Tokens
	{
	String token;         //Actual token read from input
	String tokenTypeI;    //Type of input token
	String tokenTypeO;    //Type for token output
	
	Tokens(String token, String tokenTypeI)
		{
		this.token = token;
		this.tokenTypeI = tokenTypeI;
		setTokenType();
		}
	Tokens(String n)
		{
		this.tokenTypeO = n;
		}
	public void setTokenType()
		{
		if (tokenTypeI.equalsIgnoreCase("ID"))
			tokenTypeO = "ID";                       //identifier
		else if (tokenTypeI.equalsIgnoreCase("intNUM"))
			tokenTypeO = "NUM";                      //int number
		else if (tokenTypeI.equalsIgnoreCase("int"))
			tokenTypeO = "int";                      //int declaration
		else if (tokenTypeI.equalsIgnoreCase("floatNUM"))
			tokenTypeO = "NUM";                      //float number
		else if (tokenTypeI.equalsIgnoreCase("float"))
			tokenTypeO = "float";                    //float declaration
// Next Ones are for the keywords
		else if (tokenTypeI.equalsIgnoreCase("keyword") && (token.equalsIgnoreCase("if")))
			tokenTypeO = "if";
		else if (tokenTypeI.equalsIgnoreCase("keyword") && (token.equalsIgnoreCase("else")))
			tokenTypeO = "else";
		else if (tokenTypeI.equalsIgnoreCase("keyword") && (token.equalsIgnoreCase("while")))
			tokenTypeO = "while";
		else if (tokenTypeI.equalsIgnoreCase("keyword") && (token.equalsIgnoreCase("void")))
			tokenTypeO = "void";
		else if (tokenTypeI.equalsIgnoreCase("keyword") && (token.equalsIgnoreCase("return")))
			tokenTypeO = "return";
		else if (tokenTypeI.equalsIgnoreCase("keyword") && (token.equalsIgnoreCase("int")))
			tokenTypeO = "int";
		else if (tokenTypeI.equalsIgnoreCase("keyword") && (token.equalsIgnoreCase("float")))
			tokenTypeO = "float";
// Next Ones are for the mathematical symbols
 		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("+")))
			tokenTypeO = "PLUS";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("-")))
                        tokenTypeO = "MINUS";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("*")))
                        tokenTypeO = "TIMES";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("/")))
                        tokenTypeO = "DIVIDE";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("<")))
                        tokenTypeO = "LT";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("<=")))
                        tokenTypeO = "LTEQ";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals(">")))
                        tokenTypeO = "GT";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals(">=")))
                        tokenTypeO = "GTEQ";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("==")))
                        tokenTypeO = "EQEQ";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("!=")))
                        tokenTypeO = "NOTEQ";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("=")))
                        tokenTypeO = "EQ";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals(";")))
                        tokenTypeO = "SEMICOL";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals(",")))
                        tokenTypeO = "COMMA";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals(")")))
                        tokenTypeO = "LPAREN";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("(")))
                        tokenTypeO = "RPAREN";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("[")))
                        tokenTypeO = "LBRAK";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("]")))
                        tokenTypeO = "RBRAK";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("{")))
                        tokenTypeO = "LBRACE";
		else if (tokenTypeI.equalsIgnoreCase("symbol") && (token.equals("}")))
                        tokenTypeO = "RBRACE";	
		}
	
		public String getTokenType()
			{
			return tokenTypeO;
			}
		@Override
		public String toString()
			{
			return token;
			}
	}
