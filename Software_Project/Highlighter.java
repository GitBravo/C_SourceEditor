import java.awt.Color;
import java.util.Vector;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Highlighter {
	final private StyleContext cont;
	final private AttributeSet Macro_STYLE;
	final private AttributeSet Keyword_STYLE;
	final private AttributeSet Comment_STYLE;
	final private AttributeSet Default_STYLE;

	public Highlighter() {
		cont = StyleContext.getDefaultStyleContext();
		Macro_STYLE = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.LIGHT_GRAY);
		Keyword_STYLE = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.blue);
		Comment_STYLE = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.GREEN);
		Default_STYLE = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.black);

		Frame.SetDocument(new DefaultStyledDocument() {
			private static final long serialVersionUID = 1L;
			public void insertString(int offset, String str, AttributeSet ats) throws BadLocationException {
				super.insertString(offset, str, ats);
				// InsertString(������ ���ڿ� ���� �ε���, ������ ����, ���ڿ� �Ӽ�)

				Frame.text = Frame.text_editor.getText(0, getLength());
				// text_editor�� ����� ���ڿ��� �����´�.
				// ���⼭ getText�� text_Editor �� ������ ������ ��������.

				int start = FindStartIndex(Frame.text, offset);
				// �� �ܾ��� ���������� ã�´�
				int end = FindEndIndex(Frame.text, offset + str.length());
				// �� �ܾ��� ���������� ã�´�

				if (start < 0)
					start = 0;

				int left_word = start;
				int right_word = start;

				while (right_word <= end) {
					if (right_word == end || String.valueOf(Frame.text.charAt(right_word)).matches("\\W")) {
						// String.valueOf : �Ű������� �־��� ������ ���ڿ��� ��ȯ�Ͽ� ��ȯ
						// �־��� ������ �� �ܾ �ƽ�Ű �ڵ带 �ľ��ؼ� ���ĺ��̳� ���ڶ�� if�� ����. �ܴ̿�
						// �Ǵ����� ����.
						// �ܾ��� ���ۺ��� �� �κб��� �� ���ھ� �����Ͽ� ���ϰ� ���� Ű���尡 �ִٸ� ������ ��Ÿ��
						// ����
						// ���� Ű���尡 �������� ���� �� ���� ��Ÿ�� ����
						if (Frame.text.substring(left_word, right_word).matches(
								"(\\W)*(#include|#define|#undef|#if|#ifdef|#ifndef|#else|#elif|#endif|#error|#line|#pragma)"))
							setCharacterAttributes(left_word, right_word - left_word, Macro_STYLE, false);
						else if (Frame.text.substring(left_word, right_word).matches(
								"(\\W)*(unsigned|short|int|long|float|double|char|case|if|else|break|continue|switch|static|return|while|for|struct|class|void|bool|public|protected|private|delete|true|false|NULL)"))
							setCharacterAttributes(left_word, right_word - left_word, Keyword_STYLE, false);
						else
							setCharacterAttributes(left_word, right_word - left_word, Default_STYLE, false);
						left_word = right_word;
					}
					right_word++;
				}
				
				// �Ʒ����� ���� �߰��� �κ�
				CommentChecker cc = new CommentChecker();
				Vector<Integer> v = cc.Small_CommentChecker(Frame.text);
				for (int i = 0; i < Frame.text_editor.getText().length(); i++) {
					if (v.contains(i)) { // ���ּ� �������� ã��
						for (int j = i; j < Frame.text_editor.getText().length(); j++)
							if (Frame.text_editor.getText().charAt(j) == '\n') {
								setCharacterAttributes(i, j-i, Comment_STYLE, false);
								break;
							}
					}
				}
				// ������ ���� �߰��� �κ�
				
				Vector<Integer> s = cc.Big_CommentChecker(Frame.text);
				for (int i = 0; i < Frame.text_editor.getText().length(); i++) {
					if (s.contains(i)) { // ���ּ� �������� ã��
						for (int j = i; j < Frame.text_editor.getText().length()-1; j++)
							if (Frame.text_editor.getText().substring(j, j+2).equals("*/")) {
								setCharacterAttributes(i, j-i+2, Comment_STYLE, false);
								break;
							}
					}
				}
				// ������ ���� �߰��� �κ�
			}
		});
	}

	static public int FindEndIndex(String text, int index) {
		// EX) "Hello" �϶� text = "Hello", index = 5 �� �ȴ�.
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}

	static public int FindStartIndex(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}
}