package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import DataObjects.contour.FourPointContour;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class ContourChordTonesRepo implements RepoInterface{

	@Getter @Setter private double rndValue;
	@Getter @Setter private FourPointContour selectedOption;
	@Getter @Setter private FourPointContour[] options;
	@Getter @Setter private String className;
}
