package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
public class DiatonicTriadRepo extends RepoSuperclass implements RepoInterface {

	@Getter @Setter private double[] rndValue;
	@Getter @Setter private int[] selectedValues;
	@Getter @Setter private String[] options;
	@Getter @Setter private Map<Double, String> floatBarChordMap;
	@Getter @Setter private String className;
	
	
	public DiatonicTriadRepo deepCopy()
	{
		return DiatonicTriadRepo.builder()
				.rndValue(getCopy(rndValue))
				.selectedValues(getCopy(selectedValues))
				.options(getCopy(options))
				.floatBarChordMap(getDoubleStringMapCopy(floatBarChordMap))
				.className(className)
				.build();
	}
	
	




	public Object[] getMapAsObjectList(double lengthInFloatBars) {
		Object[] list = new Object[floatBarChordMap.size() * 2 + 2] ;
		list[0] = "FloatBarChordProgression";
		list[1] = lengthInFloatBars;
		int index = 2;
		for (Double pos: floatBarChordMap.keySet()) {
			list[index] = pos;
			list[index + 1] = floatBarChordMap.get(pos);
			index += 2;
		}
		return list;
	}

}
