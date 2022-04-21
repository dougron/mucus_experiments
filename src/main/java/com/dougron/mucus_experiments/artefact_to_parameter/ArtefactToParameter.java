package main.java.com.dougron.mucus_experiments.artefact_to_parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import main.java.com.dougron.mucus.algorithms.mu_chord_tone_and_embellishment.ChordToneAndEmbellishmentTagger;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.chord_list.Chord;
import main.java.com.dougron.mucus.mu_framework.chord_list.ChordEvent;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.mu_tags.MuTag;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.DiatonicTriadProgressionRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.EvenlySpacedStructureToneFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentSetAmount;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthSetLength;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.VectorChordTonesFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.DiatonicTriadRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.EvenlySpacedStructureToneRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StartNoteRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TimeSignatureRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.VectorChordTonesRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.XmlKeyRepo;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class ArtefactToParameter
{
	
	private static final double PHRASE_BOUND_GAP = 0.05;
	private static final int MELODY_TESSITURA_WIGGLE_ROOM = 5;
	private static final int START_NOTE_TESSITURA_WIGGLE_ROOM = 5;

	
	
	public static String sayHello()
	{
		return "hello";
	}

	
	
	public static PooplinePackage getPackFromMu(Mu aMu)
	{
		ChordToneAndEmbellishmentTagger.addTags(aMu);
		justForNowConvertChordTonesToStructureTones(aMu);
		PooplinePackage pack = new PooplinePackage(aMu.getName(), new Random());
		pack = addPhraseLengthSetLengthRepo(aMu, pack);
		pack = addTimeSignatureRandomRepo(aMu, pack);
		pack = addXmlKey(aMu, pack);
		pack = addChordProgression(aMu, pack);
		pack = addReposForStructureTones(aMu, pack);
		
		
		
		return pack;
	}



	private static void justForNowConvertChordTonesToStructureTones(Mu aMu)
	{
		for (Mu mu: aMu.getMuWithTag(MuTag.IS_CHORD_TONE))
		{
			mu.addTag(MuTag.IS_STRUCTURE_TONE);
		}
	}

	
	
	private static PooplinePackage addReposForStructureTones(Mu aMu, PooplinePackage pack)
	{
		pack = addStartNoteRepo(aMu, pack);
		pack = addTessituraForStartNoteRepo(aMu, pack);
		pack = addPhraseStartPercentRepo(aMu, pack);
		pack = addPhraseEndPercentRepo(aMu, pack);
		pack = addStructureToneSpacingRepo(aMu, pack);
		pack = addMelodyTessituraRepo(aMu, pack);
		pack = addVectorChordTonesRepo(aMu, pack);
		return pack;
	}

		
	
	private static PooplinePackage addVectorChordTonesRepo(Mu aMu, PooplinePackage pack)
	{
		VectorChordTonesRepo repo = VectorChordTonesRepo.builder()
//				.rndValue(0.5)
				.selectedVectorArray(new int[] {-1})
				.className(VectorChordTonesFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.STRUCTURE_TONE_VECTOR, repo);
		return pack;
	}
	
	
	
	private static PooplinePackage addMelodyTessituraRepo(Mu aMu, PooplinePackage pack)
	{
		int highRange = 0;
		int lowRange = 127;
		int currentPitch;
		for (Mu mu: aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE))
		{
			currentPitch = mu.getTopPitch();
			if (currentPitch > highRange) highRange = currentPitch;
			if (currentPitch < lowRange) lowRange = currentPitch;
		}
		highRange += MELODY_TESSITURA_WIGGLE_ROOM;
		lowRange -= MELODY_TESSITURA_WIGGLE_ROOM;
		
		TessituraRepo repo = TessituraRepo.builder()
				.highValue(highRange)
				.lowValue(lowRange)
				.parameter(Parameter.TESSITURA_MELODY_RANGE)
				.className(TessituraFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.TESSITURA_MELODY_RANGE, repo);
		return pack;
	}
	
	
	
	private static PooplinePackage addStructureToneSpacingRepo(Mu aMu, PooplinePackage pack)
	{
		EvenlySpacedStructureToneRepo repo = EvenlySpacedStructureToneRepo.builder()
				.rndValue(0.5)
				.selectedValueInFloatBars(0.4)
				.className(EvenlySpacedStructureToneFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.STRUCTURE_TONE_SPACING, repo);
		return pack;
	}
	
	
	
	private static PooplinePackage addPhraseEndPercentRepo(Mu aMu, PooplinePackage pack)
	{
		double length = aMu.getLengthInQuarters();
		List<Mu> structureTones = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE);
		double position = structureTones.get(structureTones.size() - 1).getPositionInQuarters();
		double selectedValue = (position / length) + PHRASE_BOUND_GAP;
		pack = addPhraseBoundRepo(pack, selectedValue, Parameter.PHRASE_END_PERCENT);
		return pack;
	}
	
	
	
	private static PooplinePackage addPhraseStartPercentRepo(Mu aMu, PooplinePackage pack)
	{
		double length = aMu.getLengthInQuarters();
		double position = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE).get(0).getPositionInQuarters();
		double selectedValue = (position / length) - PHRASE_BOUND_GAP;
		pack = addPhraseBoundRepo(pack, selectedValue, Parameter.PHRASE_START_PERCENT);
		return pack;
	}

	
	
	private static PooplinePackage addPhraseBoundRepo(PooplinePackage pack, double selectedValue, Parameter repoKey)
	{
		PhraseBoundRepo repo = PhraseBoundRepo.builder()
				.rndValue(0.5)
				.selectedValue(selectedValue)
				.parameter(repoKey)
				.className(PhraseBoundPercentSetAmount.class.getName())
				.build();
		pack.getRepo().put(repoKey, repo);
		return pack;
	}
	
	
	private static PooplinePackage addTessituraForStartNoteRepo(Mu aMu, PooplinePackage pack)
	{
		int startNotePitch = aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE).get(0).getTopPitch();
		
		int highRange = startNotePitch + START_NOTE_TESSITURA_WIGGLE_ROOM;
		int lowRange = startNotePitch + START_NOTE_TESSITURA_WIGGLE_ROOM;
		
		TessituraRepo repo = TessituraRepo.builder()
				.highValue(highRange)
				.lowValue(lowRange)
				.parameter(Parameter.TESSITURA_START_NOTE)
				.className(TessituraFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.TESSITURA_START_NOTE, repo);
		return pack;
	}
	
	
	
	private static PooplinePackage addStartNoteRepo(Mu aMu, PooplinePackage pack)
	{
		StartNoteRepo repo = StartNoteRepo.builder()
				.rndValue(0.5)
				.selectedValue(aMu.getMuWithTag(MuTag.IS_STRUCTURE_TONE).get(0).getTopPitch())
				.className(StartNoteMelodyRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.START_NOTE, repo);
		return pack;
	}
	
	
	
	private static PooplinePackage addXmlKey(Mu aMu, PooplinePackage pack)
	{
		XmlKeyRepo repo = XmlKeyRepo.builder()
				.rndValue(0.5)
				.selectedValue(0)	// this is what will need to change.....
				.options(new int[] {0})
				.className(XmlKeyRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.XMLKEY, repo);
		return pack;
	}

	
	
	private static PooplinePackage addChordProgression(Mu aMu, PooplinePackage pack)
	{
//		double lengthInFloatBars = (double)aMu.getLengthInBars();
		Map<Double, String> floatBarChordMap = new HashMap<Double, String>();
		for (ChordEvent ce: aMu.getChordList().getChordEventList())
		{
			Chord chord = ce.getChord();
			BarsAndBeats position = ce.getPositionInBarsAndBeats();
			TimeSignature ts = aMu.getTimeSignature(position.getBarPosition());
			double positionInFloatBars = position.getBarPosition() + position.getOffsetInQuarters() / ts.getLengthInQuarters();
			floatBarChordMap.put(positionInFloatBars, chord.getAssociatedChordInKeyObject().chordSymbol());
		}
		DiatonicTriadRepo repo = DiatonicTriadRepo.builder()
				.rndValue(new double[] {0.1, 0.2})
				.floatBarChordMap(floatBarChordMap)
				.className(DiatonicTriadProgressionRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.CHORD_LIST_GENERATOR, repo);
		return pack;
	}

	
	private static PooplinePackage addTimeSignatureRandomRepo(Mu aMu, PooplinePackage pack)
	{
		TimeSignature ts = aMu.getTimeSignature(0);
		TimeSignatureRepo repo = TimeSignatureRepo.builder()
				.rndValue(0.5)
				.selectedValue(ts)
				.options(new TimeSignature[] {ts})
				.className(TimeSignatureSingleRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.TIME_SIGNATURE, repo);
		return pack;
	}

	
	
	private static PooplinePackage addPhraseLengthSetLengthRepo(Mu aMu, PooplinePackage pack)
	{
		PhraseLengthRepo repo = PhraseLengthRepo.builder()
				.rndValue(0.5)
				.selectedValue(aMu.getLengthInBars())
				.options(new int[] {aMu.getLengthInBars()})
				.className(PhraseLengthSetLength.class.getName())
				.build();
		pack.getRepo().put(Parameter.PHRASE_LENGTH, repo);
		return pack;
	}

}
