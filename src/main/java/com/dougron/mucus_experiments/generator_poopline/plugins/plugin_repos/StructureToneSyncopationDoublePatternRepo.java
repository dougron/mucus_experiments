package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class StructureToneSyncopationDoublePatternRepo extends RepoSuperclass implements RepoInterface {

	// curretly the only relevant one here is the selected options for the StructureToneSyncopatorInQuartersFixed plugin
	
	@Getter @Setter private double[] selectedOption;
	@Getter @Setter private double[][] options;
	@Getter @Setter private String className;
	
	
	public StructureToneSyncopationDoublePatternRepo deepCopy()
	{
		return StructureToneSyncopationDoublePatternRepo.builder()
				.selectedOption(selectedOption)
				.options(getCopy(options))
				.className(className)
				.build();
	}
}
