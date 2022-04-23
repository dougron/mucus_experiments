package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;

@Builder
@ToString
public class TessituraSolverRepo extends RepoSuperclass implements RepoInterface {

	public enum BreakPointChoice {FIRST, LAST, RANDOM}
	public static final BreakPointChoice[] breakPointOptions = new BreakPointChoice[] 
			{
					BreakPointChoice.FIRST, 
					BreakPointChoice.LAST, 
					BreakPointChoice.RANDOM
			};
	
	@Getter @Setter private double rndValue;
	@Getter @Setter private BreakPointChoice breakPointChoice;
	@Getter @Setter private double rndValueForRandomChoice;
	@Getter @Setter private Parameter tessituraParameter;
	@Getter @Setter private MuTag muTagToActUpon;
	@Getter @Setter private List<Mu> muList;
	@Getter @Setter private String className;
	
	
	
	public TessituraSolverRepo deepCopy()
	{
		return TessituraSolverRepo.builder()
				.rndValue(rndValue)
				.breakPointChoice(breakPointChoice)
				.rndValueForRandomChoice(rndValueForRandomChoice)
				.tessituraParameter(tessituraParameter)
				.muTagToActUpon(muTagToActUpon)
				.className(className)
				.build();
	}

}
