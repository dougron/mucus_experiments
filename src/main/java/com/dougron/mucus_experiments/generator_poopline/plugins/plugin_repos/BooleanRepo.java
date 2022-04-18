package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class BooleanRepo extends RepoSuperclass implements RepoInterface 
{

	@Getter @Setter private double rndValue;
	@Getter @Setter private boolean selectedOption;
	@Getter @Setter private boolean[] options;
	@Getter @Setter private String className;
	
	
	
	public BooleanRepo deepCopy()
	{
		return BooleanRepo.builder()
				.rndValue(rndValue)
				.selectedOption(selectedOption)
				.options(getCopy(options))
				.className(className)
				.build();
	}
}
