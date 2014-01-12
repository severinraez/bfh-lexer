
public class Transition {
	State targetState;
	String regex;
	
	public Transition(State targetState, String regex) {
		this.targetState = targetState;
		this.regex = regex;
	}
	
	public boolean applicable(String str) {
		return str.matches(regex);
	}
	
	public State getTargetState() {
		return targetState;
	}
}
