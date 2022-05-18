package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;


/*
 * intended for use on embellishments. structure tones are identified by IS_STRUCTURE_TONE tag, and 
 * children are identified by IS_EMBELLISHMENT tag, or any other that may be identified
 * 
 * currently cycles through embellishments backwards in time, and 
 * 
 * patternIndex is used to identify pattern to be used. an item in this array is assumed to be a key in the 
 * durationPatternMap
 * 
 */

@Builder
@ToString
public class DurationPatternsRepo extends RepoSuperclass implements RepoInterface {
	

	@Getter @Setter private Map<Integer, DurationType[]> durationPatternMap;
	@Getter @Setter private int[] patternIndices;
	@Getter @Setter private int staccatoDurationInMilliseconds;
	@Getter @Setter @Builder.Default MuTag tagToFilterStructureTones = MuTag.IS_STRUCTURE_TONE;
	@Getter @Setter @Builder.Default MuTag tagToFilterEmbellishments = MuTag.IS_EMBELLISHMENT;
	@Getter @Setter private String className;
	
	
	public DurationPatternsRepo deepCopy()
	{
		return DurationPatternsRepo.builder()
				.durationPatternMap(getCopy(durationPatternMap))
				.patternIndices(getCopy(patternIndices))
				.staccatoDurationInMilliseconds(staccatoDurationInMilliseconds)
				.tagToFilterStructureTones(tagToFilterStructureTones)
				.tagToFilterEmbellishments(tagToFilterEmbellishments)
				.className(className)
				.build();
	}


	
	private Map<Integer, DurationType[]> getCopy(Map<Integer, DurationType[]> mapToCopy)
	{
		Map<Integer, DurationType[]> map = new HashMap<Integer, DurationType[]>();
		for (Integer key: mapToCopy.keySet())
		{
			map.put(key, getCopy(mapToCopy.get(key)));
		}
		return map;
	}

}
