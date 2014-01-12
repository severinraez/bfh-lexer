import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			System.out.println("please provide filename as first argument. stopping here.");
			System.exit(2);
		}
		
        //our parsing automaton (non-deterministic)
        State startState = new State(true, "nothing");
        startState.addTransition(new Transition(startState, "[\\s\\t]"));
        
        //line comments
        State comment = new State(true, "comment");
        startState.addTransition(new Transition(comment, "%"));
        comment.addTransition(new Transition(comment, ".*"));
        
        //multi-line comments
        State multiLineCommentBegin1 = new State(false, "comment");
        State multiLineCommentBegin2 = new State(false, "comment");
        State multiLineComment = new State(false, "comment");
        State multiLineCommentEnd1 = new State(false, "comment");
        State multiLineCommentEnd2 = new State(true, "comment");
        
        startState.addTransition(new Transition(multiLineCommentBegin1, "/"));
        multiLineCommentBegin1.addTransition(new Transition(multiLineCommentBegin2, "\\*"));
        multiLineCommentBegin2.addTransition(new Transition(multiLineComment, ".*"));
        multiLineComment.addTransition(new Transition(multiLineCommentEnd1, "\\*"));
        multiLineCommentEnd1.addTransition(new Transition(multiLineCommentEnd2, "/"));
        multiLineComment.addTransition(new Transition(multiLineComment, ".*"));
        
        //dots
        State dot = new State(true, "dot");
        startState.addTransition(new Transition(dot, "\\."));
              
        //atoms (constants)
        State atomBegin = new State(true, "constant");
        startState.addTransition(new Transition(atomBegin, "[a-z]"));
        State atom = new State(true, "constant");
        atomBegin.addTransition(new Transition(atom, "[a-zA-Z0-9]"));
        atom.addTransition(new Transition(atom, "[a-zA-Z0-1]"));
                
        //numbers(constants)
        State number = new State(true, "constant");
        startState.addTransition(new Transition(number, "0-9"));
        number.addTransition(new Transition(number, "0-9"));
        
        //special char atoms (constants)
        State specialCharAtom = new State(true, "constant");
        startState.addTransition(new Transition(specialCharAtom, "[:!-]"));
        specialCharAtom.addTransition(new Transition(specialCharAtom, "[:-]"));
        
        //variables
        State variableBegin = new State(true, "variable");
        startState.addTransition(new Transition(variableBegin, "[A-Z]"));
        State variable = new State(true, "variable");
        variableBegin.addTransition(new Transition(variable, "[A-Za-z0-9]"));
        variable.addTransition(new Transition(variable, "[A-Za-z0-9]"));
        
        //anonymous variables (variables)
        State anonymousVariable = new State(true, "variable");
        startState.addTransition(new Transition(anonymousVariable, "_"));
        
        //structures
        State structureBegin = new State(true, "structureBegin");
        startState.addTransition(new Transition(structureBegin, "\\("));
        State structureEnd = new State(true, "structureEnd");
        startState.addTransition(new Transition(structureEnd, "\\)"));
        
        //lists
        State listBegin = new State(true, "listBegin");
        startState.addTransition(new Transition(listBegin, "\\["));
        State listEnd = new State(true, "listEnd");
        startState.addTransition(new Transition(listEnd, "\\]"));
        
        State listHeadTailSeparator = new State(true, "listHeadTailSeparator");
        startState.addTransition(new Transition(listHeadTailSeparator, "\\|"));
        
        State commaSeparator = new State(true, "commaSeparator");
        startState.addTransition(new Transition(commaSeparator, ","));
        
        //strings
        State string = new State(false, "string");
        startState.addTransition(new Transition(string, "'"));
        State stringEnd = new State(true, "string");
        string.addTransition(new Transition(stringEnd, "'"));
        string.addTransition(new Transition(string, ".*"));
        
        //tokenize file and store results in parsed 
        List<LexerResult> parsed = new LinkedList<LexerResult>();
        
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        String line;
        while ((line = br.readLine()) != null) { //line by line
        	int i = 0; 
        	while(i < line.length()) { 
        		String toParse = line.substring(i);
        		LexerResult r = startState.feed(toParse);
        	        		        		
        		if(r == null) { //automaton did not finish in end state
        			System.out.println("Could not tokenize..." + line.substring(i));
        			System.exit(1);
        		}
       			else { //something was consumed and tokenized
       				if(r.getTokenName() != "comment" && r.getTokenName() != "nothing") {
       					parsed.add(r);
       				}       		
       				i += r.getContent().length(); //advance by the tokenized length
       				
          			//hack for trailing whitespaces
       				String leftOvers = toParse.substring(r.getContent().length()); 
       				if(leftOvers.trim().isEmpty()) {  
       					i += leftOvers.length();
       				}
      			}
        	}
        	
        }
        	
        //display the lexing results
      	for(LexerResult r : parsed) {
       		System.out.println(r.getContent().trim() + " - " + r.getTokenName());
       	}
	}
}
