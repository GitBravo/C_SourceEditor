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
				// InsertString(삽입할 문자열 시작 인덱스, 삽입할 문자, 문자열 속성)

				Frame.text = Frame.text_editor.getText(0, getLength());
				// text_editor에 저장된 문자열을 가져온다.
				// 여기서 getText가 text_Editor 의 독점적 권한을 가져간다.

				int start = FindStartIndex(Frame.text, offset);
				// 각 단어의 시작지점을 찾는다
				int end = FindEndIndex(Frame.text, offset + str.length());
				// 각 단어의 종료지점을 찾는다

				if (start < 0)
					start = 0;

				int left_word = start;
				int right_word = start;

				while (right_word <= end) {
					if (right_word == end || String.valueOf(Frame.text.charAt(right_word)).matches("\\W")) {
						// String.valueOf : 매개변수로 주어진 정수를 문자열로 변환하여 반환
						// 주어진 문자의 각 단어별 아스키 코드를 파악해서 알파벳이나 숫자라면 if문 진행. 이외는
						// 판단하지 않음.
						// 단어의 시작부터 끝 부분까지 한 문자씩 발췌하여 비교하고 유사 키워드가 있다면 문자의 스타일
						// 변경
						// 유사 키워드가 존재하지 않을 시 원래 스타일 유지
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
				
				// 아래까지 새로 추가한 부분
				CommentChecker cc = new CommentChecker();
				Vector<Integer> v = cc.Small_CommentChecker(Frame.text);
				for (int i = 0; i < Frame.text_editor.getText().length(); i++) {
					if (v.contains(i)) { // 소주석 시작지점 찾음
						for (int j = i; j < Frame.text_editor.getText().length(); j++)
							if (Frame.text_editor.getText().charAt(j) == '\n') {
								setCharacterAttributes(i, j-i, Comment_STYLE, false);
								break;
							}
					}
				}
				// 위까지 새로 추가한 부분
				
				Vector<Integer> s = cc.Big_CommentChecker(Frame.text);
				for (int i = 0; i < Frame.text_editor.getText().length(); i++) {
					if (s.contains(i)) { // 대주석 시작지점 찾음
						for (int j = i; j < Frame.text_editor.getText().length()-1; j++)
							if (Frame.text_editor.getText().substring(j, j+2).equals("*/")) {
								setCharacterAttributes(i, j-i+2, Comment_STYLE, false);
								break;
							}
					}
				}
				// 위까지 새로 추가한 부분
			}
		});
	}

	static public int FindEndIndex(String text, int index) {
		// EX) "Hello" 일때 text = "Hello", index = 5 가 된다.
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