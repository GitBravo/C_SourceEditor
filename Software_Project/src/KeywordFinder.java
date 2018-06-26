public class KeywordFinder {
	public void finder(String find_keyword) {
		for (int i = 0; i < Frame.text_editor.getText().length() - find_keyword.length() + 1; i++) {
			if (Frame.text_editor.getText().substring(i, i + find_keyword.length()).equals(find_keyword))
				Frame.text_editor.setCaretPosition(i);
		}
	}
}
