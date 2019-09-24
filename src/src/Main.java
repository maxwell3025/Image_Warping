package src;
public class Main {
	public static void main(String[] args) {
		DisplayWindow display = new DisplayWindow(1080,720,"triangle rendering");
		for(;;){
			display.Update();
		}
	}
}
