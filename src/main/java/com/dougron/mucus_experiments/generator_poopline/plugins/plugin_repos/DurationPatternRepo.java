package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.duration_model.DurationModel;

@Builder
@ToString
public class DurationPatternRepo extends RepoSuperclass implements RepoInterface {
	

	@Getter @Setter private DurationModel[] durationModelPattern;
	@Getter @Setter private DurationModel endNoteDurationModel;
	@Getter @Setter private int staccatoDurationInMilliseconds;
	@Getter @Setter private MuTag tagToActUpon;
	@Getter @Setter private String className;
	
	
	public DurationPatternRepo deepCopy()
	{
		return DurationPatternRepo.builder()
				.durationModelPattern(durationModelPattern == null ? null : getCopy(durationModelPattern))
				.endNoteDurationModel(endNoteDurationModel)
				.staccatoDurationInMilliseconds(staccatoDurationInMilliseconds)
				.tagToActUpon(tagToActUpon)
				.className(className)
				.build();
	}

}
