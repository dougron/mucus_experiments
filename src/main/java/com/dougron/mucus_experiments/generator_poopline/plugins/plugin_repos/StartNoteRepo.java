package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class StartNoteRepo extends RepoSuperclass implements RepoInterface{

	@Getter @Setter private double rndValue;
	@Getter @Setter private int selectedValue;
	@Getter @Setter private int rangeLow;
	@Getter @Setter private int rangeHigh;
	@Getter @Setter private String className;
	
	public StartNoteRepo deepCopy()
	{
		return StartNoteRepo.builder()
				.rndValue(rndValue)
				.selectedValue(selectedValue)
				.rangeLow(rangeLow)
				.rangeHigh(rangeHigh)
				.className(className)
				.build();
	}
}
