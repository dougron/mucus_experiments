package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;

@Builder
@ToString
public class DurationPatternRepo extends RepoSuperclass implements RepoInterface {
	

	@Getter @Setter private DurationType[] durationPattern;
	@Getter @Setter private int staccatoDurationInMilliseconds;
	@Getter @Setter private MuTag tagToActUpon;
	@Getter @Setter private String className;
	
	
	public DurationPatternRepo deepCopy()
	{
		return DurationPatternRepo.builder()
				.durationPattern(getCopy(durationPattern))
				.staccatoDurationInMilliseconds(staccatoDurationInMilliseconds)
				.tagToActUpon(tagToActUpon)
				.className(className)
				.build();
	}

}
