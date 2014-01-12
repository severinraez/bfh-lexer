/**
 * a transition, transitioning to a targetState. can fire if the given regex matches the next char.
 *
 */
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
