public class EnumTest {
	
	public enum Color {
		RED, GREEN, BLUE;
		
		@Override
		public String toString() {
			return "hehe";
		}
	}
	
	public static void main(String[] args) {
		for (Color c : Color.values()) {
			System.out.println(c.ordinal());
		}
	}
}
