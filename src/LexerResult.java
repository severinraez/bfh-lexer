
public class LexerResult {
	protected String content;
	protected String tokenName;
	
	public LexerResult(String content, String tokenName) {
		this.content = content;
		this.tokenName = tokenName;
	}
	
	public String getContent() {
		return content;		
	}
	
	public String getTokenName() {
		return tokenName;
	}
	
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;  
	}
}
