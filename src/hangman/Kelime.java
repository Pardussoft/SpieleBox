package hangman;

public class Kelime {
	
	String s; 
	
	
	public Kelime(String s) {
		this.s = s;
	}
	
	public char[] varmi(char ch) {
		char[] v = new char[s.length()];
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == ch) {
				v[i] = 1;
			}
			else {
				v[i]=0;
			}
		}
		return v; 
	}
	
	public int len() {
		return s.length();
	}
	
	public String getKelime() {
		return s;
	}
	
}

// - - - - -
// - e - e -