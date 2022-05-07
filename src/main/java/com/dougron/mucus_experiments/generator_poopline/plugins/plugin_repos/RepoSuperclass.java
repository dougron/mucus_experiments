package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.NoArgsConstructor;
import main.java.com.dougron.mucus.algorithms.generic_generator.DurationType;
import main.java.com.dougron.mucus_experiments.generator_poopline.embellisher.MuEmbellisher;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PatternEmbellishmentRepo.EmbellishmentRhythmResolution;
import main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset.RhythmOffset;
import main.java.da_utils.four_point_contour.FourPointContour;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

@NoArgsConstructor
public class RepoSuperclass
{
	
	private String shortClassName = "shortClassName not set";

//	public RepoInterface clone()
//	{
//		Gson gson = new Gson();
//		RepoInterface newRepo = gson.fromJson(gson.toJson(this), RepoInterface.class);
//		return newRepo; 
//	}
	
		
	
	public boolean[] getCopy(boolean[] arr)
	{
		boolean[] brr = new boolean[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	public int[] getCopy(int[] arr)
	{
		int[] brr = new int[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	public int[][] getCopy(int[][] arr)
	{
		int[][] brr = new int[arr.length][];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	public String[] getCopy(String[] arr)
	{
		String[] brr = new String[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	public double[] getCopy(double[] arr)
	{
		double[] brr = new double[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}

	
	public double[][] getCopy(double[][] arr)
	{
		double[][] brr = new double[arr.length][];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	public TimeSignature[] getCopy(TimeSignature[] arr)
	{
		TimeSignature[] brr = new TimeSignature[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	public FourPointContour[] getCopy(FourPointContour[] arr)
	{
		FourPointContour[] brr = new FourPointContour[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	public DurationType[] getCopy(DurationType[] arr)
	{
		DurationType[] brr = new DurationType[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	public EmbellishmentRhythmResolution[] getCopy(EmbellishmentRhythmResolution[] arr)
	{
		EmbellishmentRhythmResolution[] brr = new EmbellishmentRhythmResolution[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	public MuEmbellisher[] getCopy(MuEmbellisher[] arr)
	{
		MuEmbellisher[] brr = new MuEmbellisher[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	public RhythmOffset[] getCopy(RhythmOffset[] arr)
	{
		RhythmOffset[] brr = new RhythmOffset[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			brr[i] = arr[i];
		}
		return brr;
	}
	
	
	
	public Map<Double, String> getDoubleStringMapCopy(Map<Double, String> aMap)
	{
		Map<Double, String> map = new HashMap<Double, String>();
		for (Double key: aMap.keySet())
		{
			map.put(key, aMap.get(key));
		}
		return map;
	}
	
	
	public Map<Integer, Double> getIntegerDoubleMapCopy(Map<Integer, Double> aMap)
	{
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		for (Integer key: aMap.keySet())
		{
			map.put(key, aMap.get(key));
		}
		return map;
	}
	
	
	public Map<Integer, RhythmOffset> getIntegerRhythmOffsetMapCopy(Map<Integer, RhythmOffset> aMap)
	{
		Map<Integer, RhythmOffset> map = new HashMap<Integer, RhythmOffset>();
		for (Integer key: aMap.keySet())
		{
			map.put(key, aMap.get(key));
		}
		return map;
	}
	
	
	public Map<Integer, List<Double>> getIntegerDoubleListMapCopy(Map<Integer, List<Double>> aMap)
	{
		Map<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();
		for (Integer key: aMap.keySet())
		{
			List<Double> list = new ArrayList<Double>(aMap.get(key));
			map.put(key, list);
		}
		return map;
	}
	
	
	public Map<Integer, List<MuEmbellisher>> getIntegerMuEmbellisherListMapCopy(Map<Integer, List<MuEmbellisher>> aMap)
	{
		Map<Integer, List<MuEmbellisher>> map = new HashMap<Integer, List<MuEmbellisher>>();
		for (Integer key: aMap.keySet())
		{
			List<MuEmbellisher> list = new ArrayList<MuEmbellisher>(aMap.get(key));
			map.put(key, list);
		}
		return map;
	}
	
	
	public Map<Integer, List<RhythmOffset>> getIntegerRhythmOffsetListMapCopy(Map<Integer, List<RhythmOffset>> aMap)
	{
		Map<Integer, List<RhythmOffset>> map = new HashMap<Integer, List<RhythmOffset>>();
		for (Integer key: aMap.keySet())
		{
			List<RhythmOffset> list = new ArrayList<RhythmOffset>(aMap.get(key));
			map.put(key, list);
		}
		return map;
	}
	
	
	public Map<Integer, Integer> getIntegerIntegerMapCopy(Map<Integer, Integer> aMap)
	{
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Integer key: aMap.keySet())
		{
			map.put(key, aMap.get(key));
		}
		return map;
	}
}
