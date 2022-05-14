package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus_experiments.artefact_to_parameter.EmbellishmentSchema;

@Builder
@ToString
public class EmbellishmentFixedRepo extends RepoSuperclass implements RepoInterface
{
	
	public enum EmbellishmentModel {
		QUARTERS_FROM_BACK,
		QUARTERS_FROM_FRONT
	}

//	@Getter @Setter private double rndValue;
	@Getter @Setter private EmbellishmentModel embellishmentModel;
	@Getter @Setter private List<EmbellishmentSchema> schemaList;
	@Getter @Setter private String className;
	
	
	
	public EmbellishmentFixedRepo deepCopy()
	{
		return EmbellishmentFixedRepo.builder()
//				.rndValue(rndValue)
				.embellishmentModel(embellishmentModel)
				.schemaList(schemaList)
				.className(className)
				.build();
	}

}
