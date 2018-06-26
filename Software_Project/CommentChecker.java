import java.util.Vector;

public class CommentChecker {
	public Vector<Integer> Small_CommentChecker(String st) {
		Vector<Integer> v = new Vector<Integer>();
		for (int i = 0; i < st.length() -1  ; i++) {
			if(st.substring(i, i+2).equals("//")) // 家林籍 眉农
				v.add(i);
		}
		return v;
	}
	
	public Vector<Integer> Big_CommentChecker(String st) {
		Vector<Integer> e = new Vector<Integer>();
		for (int i = 0; i < st.length() -1  ; i++) {
			if(st.substring(i, i+2).equals("/*")) // 措林籍 眉农
				e.add(i);
		}
		return e;
	}
}