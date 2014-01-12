import java.util.LinkedList;
import java.util.List;


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
	
	public LexerResult feed(String str, String consumed) {
		if(str.trim().isEmpty()) { //the trim is a little hack for trailing whitespaces
			if(isEnd) {
				return new LexerResult(consumed, tokenName);
			}
			else {
				return null; //error while parsing!
			}
		}
		String nextChar = str.substring(0, 1);
		if(isEnd && transitions.isEmpty()) {
			return new LexerResult(consumed, tokenName);
		}
		else {
			for(Transition t : transitions) {
				if(t.applicable(nextChar)) { //transition would fire
					LexerResult result = t.getTargetState().feed(str.substring(1), consumed + nextChar);
					if(result != null) { //token parsed, return it
						if(result.getTokenName() == "fallback") //lookahead failed
	   						result.setTokenName(tokenName);
						return result;
					}
				}
			}			
		}
		return new LexerResult(consumed, "fallback"); //we did a failed lookahed
	}
}
