import java.util.LinkedList;
import java.util.List;

/**
 * each state know what tokenName is assigned to it.
 * end states are valid states to abort further lexing and create a token
 */
public class State {
	private boolean isEnd;
	private String tokenName;
	private List<Transition> transitions = new LinkedList<Transition>(); 

	public State(boolean isEnd) {
		this.isEnd = isEnd;
	}
	
	public State(boolean isEnd, String tokenName) {
		this(isEnd);
		
		this.tokenName = tokenName;
	}	
	
	public void addTransition(Transition t) {
		transitions.add(t);
	}
	
	public LexerResult feed(String str) { 
		return feed(str, "");
	}	
	
	/**
	 * tries to identify a token.
	 * @param str the string left to parse
	 * @param consumed the chars already consumed
	 * @return
	 */
	public LexerResult feed(String str, String consumed) {
		if(str.trim().isEmpty()) { //the trim is a little hack for trailing whitespaces
			if(isEnd)
				return new LexerResult(consumed, tokenName);
			else
				return null; //error while parsing!
		}
		String nextChar = str.substring(0, 1);
		if(isEnd && transitions.isEmpty()) { //we're in an end state, return the found token
			return new LexerResult(consumed, tokenName);
		}
		else {
			for(Transition t : transitions) {
				if(t.applicable(nextChar)) { //transition would fire
					LexerResult result = t.getTargetState().feed(str.substring(1), consumed + nextChar);
					if(result != null) { //token parsed, return it
						return result;
					}
				}
			}			
		}
		if(isEnd)
		    return new LexerResult(consumed, tokenName); //all transitions failed, fall back to consumed chars (lookahead behaviour)
		else
			return null; //automaton finished in non-end state
	}
}
