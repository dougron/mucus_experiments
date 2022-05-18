package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;



@Builder
@ToString
public class DurationFixedInQuartersRepo 
extends RepoSuperclass 
implements RepoInterface 
{
	
	
	@Getter @Setter private double[] durationPattern;
//	@Getter @Setter private int staccatoDurationInMilliseconds;
	@Getter @Setter private String className;
	
	
	public DurationFixedInQuartersRepo deepCopy()
	{
		return DurationFixedInQuartersRepo.builder()
				.durationPattern(getCopy(durationPattern))
//				.staccatoDurationInMilliseconds(staccatoDurationInMilliseconds)
				.build();
	}
}
