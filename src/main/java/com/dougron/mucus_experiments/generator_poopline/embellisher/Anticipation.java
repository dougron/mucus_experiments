package main.java.com.dougron.mucus_experiments.generator_poopline.embellisher;

public class Anticipation implements MuEmbellisher {

	
	private static MuEmbellisher instance;
	
	private Anticipation() {
		
	}
	
	
	public static MuEmbellisher getInstance() {
		if (instance == null) {
			instance = new Anticipation();
		}
		return instance;
	}

}
