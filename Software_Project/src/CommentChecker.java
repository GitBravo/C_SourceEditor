import java.util.Vector;

public class CommentChecker {
	public Vector<Integer> Small_CommentChecker(String st) {
		Vector<Integer> v = new Vector<Integer>();
		for (int i = 0; i < st.length() -1  ; i++) {
			if(st.substring(i, i+2).equals("//")) // ���ּ� üũ
				v.add(i);
		}
		return v;
	}
	
	public Vector<Integer> Big_CommentChecker(String st) {
		Vector<Integer> e = new Vector<Integer>();
		for (int i = 0; i < st.length() -1  ; i++) {
			if(st.substring(i, i+2).equals("/*")) // ���ּ� üũ
				e.add(i);
		}
		return e;
	}
}