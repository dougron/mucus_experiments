package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.da_utils.four_point_contour.FourPointContour;

@Builder
@ToString
public class ContourChordTonesRepo extends RepoSuperclass implements RepoInterface{

	@Getter @Setter private double rndValue;
	@Getter @Setter private FourPointContour selectedOption;
	@Getter @Setter private FourPointContour[] options;
	@Getter @Setter private String className;
	
	
	public ContourChordTonesRepo deepCopy()
	{
		return ContourChordTonesRepo.builder()
				.rndValue(rndValue)
				.selectedOption(selectedOption)
				.options(options == null ? null : getCopy(options))
				.className(className)
				.build();
	}
}
