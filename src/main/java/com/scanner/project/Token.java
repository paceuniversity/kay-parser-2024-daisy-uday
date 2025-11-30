public class Token {
	// Uday Brathwaite and Daisy Molina
	// Token type Identifier, Keyword, Literal, Separator, Operator, or Other
	private String type;
	// Token value
	private String value;

	public Token(String type, String value) {
		this.type = type;
		this.value = value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}

	public String getType() {
		return type;
	}

	public String toString() {
		return "Value: " + this.getValue() + " " + "Type: " + this.getType();
	}

}

