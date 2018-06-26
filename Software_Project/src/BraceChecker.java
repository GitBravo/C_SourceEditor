import java.util.Stack;

public class BraceChecker {
	private int line_count = 1;
	private int brace_open = 1;

	public boolean Checker(String st) {
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < st.length(); i++) {
			char ch = st.charAt(i);
			if ((ch == '[') || (ch == '(') || (ch == '{')) {
				stack.push(ch);
				brace_open = line_count;
			} else if (ch == ']') {
				if (stack.isEmpty())
					return false;
				if (stack.pop() != '[')
					return false;
			} else if (ch == ')') {
				if (stack.isEmpty())
					return false;
				if (stack.pop() != '(')
					return false;
			} else if (ch == '}') {
				if (stack.isEmpty())
					return false;
				if (stack.pop() != '{')
					return false;
			} else if (ch == '\n') {
				line_count += 1;
			}
		}
		return stack.isEmpty();
	}

	public int GetLineNum() {
		return this.brace_open;
	}
}