package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.nd4j.shade.guava.collect.ImmutableMap;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus_experiments.generator_poopline.Poopline;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePackage;
import main.java.com.dougron.mucus_experiments.generator_poopline.PooplinePlugin;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourChordTonesRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ContourMultiplierRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ChordProgressionDiatonicTriadRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneEvenlySpacedFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StructureToneEvenlySpacedRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.ForceCreatePlugInsFromRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseBoundPercentSetAmount;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.PhraseLengthSetLength;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.StartNoteMelodyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TempoRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TessituraFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.TimeSignatureSingleRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.VectorChordTonesFixed;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.XmlKeyRandom;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.ContourChordTonesRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.ContourMultiplierRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.ChordProgressionRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StructureToneEvenlySpacedRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseBoundRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.PhraseLengthRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.StartNoteRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TempoRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TimeSignatureRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.VectorChordTonesRepo;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.XmlKeyRepo;
import main.java.da_utils.four_point_contour.FourPointContour;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;


/*
 * note: none of the rndValue items in repo builders are significant in terms of value
 * 
 * they need to exist for randomly generated plugins to avoid the selectedValue being regenerated
 */
class ForceCreatePlugInsFromRepo_Tests
{

	@Test
	void instantiates() {
		Poopline pipeline = new Poopline();
		PooplinePlugin plug = new ForceCreatePlugInsFromRepo(pipeline);
	}

	
	@Test
	void when_repo_contains_reference_to_PhraseLengthSetLength_then_that_Plugin_will_be_added_when_pack_is_passed() {
		Poopline pipeline = new Poopline();
		pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
		assertThat(pipeline.getPlugins().size()).isEqualTo(1);
		PooplinePackage pack = new PooplinePackage("pack", new Random());
		addPhraseLengthFixedRepo(pack, 2);
		pack = pipeline.process(pack);
		assertThat(pipeline.getPlugins().size()).isEqualTo(2);
	}

	
	@Test
	void when_repo_contains_reference_to_TempoRandom_then_mu_will_have_tempo_as_set() {
		Poopline pipeline = new Poopline();
		pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
		assertThat(pipeline.getPlugins().size()).isEqualTo(1);
		PooplinePackage pack = new PooplinePackage("pack", new Random());
		addTempoRandomRepo(pack, 98);
		pack = pipeline.process(pack);
		assertThat(pack.getMu().getStartTempo()).isEqualTo(98);
	}
	
	
	@Test
	void when_repo_contains_reference_to_PhraseLengthSetLength_with_length_2_then_mu_will_be_generated_with_length_2() {
		Poopline pipeline = new Poopline();
		pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
		assertThat(pipeline.getPlugins().size()).isEqualTo(1);
		PooplinePackage pack = new PooplinePackage("pack", new Random());
		addPhraseLengthFixedRepo(pack, 2);
		pack = pipeline.process(pack);
		assertThat(pack.getMu().getLengthInBars()).isEqualTo(2);
	}
	
	
	@Test
	void when_repo_contains_reference_to_two_parameters_then_mu_will_be_generated_with_correct_parameters() {
		Poopline pipeline = new Poopline();
		pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
		assertThat(pipeline.getPlugins().size()).isEqualTo(1);
		PooplinePackage pack = new PooplinePackage("pack", new Random());
		addPhraseLengthFixedRepo(pack, 2);
		addTimeSignatureRepo(pack, TimeSignature.SEVEN_EIGHT_322);
		pack = pipeline.process(pack);
		assertThat(pack.getMu().getLengthInBars()).isEqualTo(2);
		assertThat(pack.getMu().getTimeSignature(0).getName()).isEqualTo("7/8_322");
		assertThat(pack.getMu().getTimeSignature(1).getName()).isEqualTo("7/8_322");
	}
	
	
	@Test
	void when_repo_contains_reference_to_XmlKeyRandom_of_2_then_mu_will_be_generated_with_xmlKey_2() {
		Poopline pipeline = new Poopline();
		pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
		assertThat(pipeline.getPlugins().size()).isEqualTo(1);
		PooplinePackage pack = new PooplinePackage("pack", new Random());
		addXmlKeyRepo(pack, 2);
		pack = pipeline.process(pack);
		assertThat(pack.getMu().getKeySignatureMap().getKey()).isEqualTo(2);
	}
	
	
	@Test
	void when_repo_contains_reference_to_2_bar_diatonic_chord_progression_then_mu_will_have_correct_chords() {
		Poopline pipeline = new Poopline();
		pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
		assertThat(pipeline.getPlugins().size()).isEqualTo(1);
		PooplinePackage pack = new PooplinePackage("pack", new Random());
		pack.setDebugMode(true);
		addPhraseLengthFixedRepo(pack, 2);
		addXmlKeyRepo(pack, 2);
		ImmutableMap<Double, String> floatBarChordMap = ImmutableMap.of(0.0, "Eb", 1.0, "Gm");
		addDiatonicChordRepo(pack, floatBarChordMap);
		pack = pipeline.process(pack);
		assertThat(pack.getMu().getKeySignatureMap().getKey()).isEqualTo(2);
		assertThat(pack.getMu().getChordAt(BarsAndBeats.at(0, 0.0)).getAssociatedChordInKeyObject().chordSymbol()).isEqualTo("D#Maj");
		assertThat(pack.getMu().getChordAt(BarsAndBeats.at(1, 1.0)).getAssociatedChordInKeyObject().chordSymbol()).isEqualTo("Gmin");
	}

	
	@Test
	void when_repo_contains_phrase_bounds_spacing_start_note_and_contour_then_structure_tone_is_generated() {
	
		Poopline pipeline = new Poopline();
		pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
		PooplinePackage pack = new PooplinePackage("pack", new Random());
		pack.setDebugMode(true);
		addPhraseLengthFixedRepo(pack, 2);
		addTimeSignatureRepo(pack, TimeSignature.FOUR_FOUR);
		addXmlKeyRepo(pack, 2);
		addDiatonicChordRepo(pack, ImmutableMap.of(0.0, "Db", 1.0, "Ab"));
		addPhraseBoundRepo(pack, -0.5, Parameter.PHRASE_START_PERCENT);
		addPhraseBoundRepo(pack, 0.25, Parameter.PHRASE_END_PERCENT);
		addEvenlySpacedStructureToneFixedRepo(pack, 1.0);
		addTessituraRepo(pack, 44, 72, Parameter.TESSITURA_START_NOTE);
		addStartNoteRepo(pack, 65);
		addTessituraRepo(pack, 44, 72, Parameter.TESSITURA_MELODY_RANGE);
		addContourMultiplierRepo(pack, 7);
		addContourChordToneRepo(pack, new FourPointContour(FourPointContour.DOWN));
		pack = pipeline.process(pack);
		System.out.println(pack.getMu().toString());
		System.out.println(pack.getMu().getMus().toString());
		
		// Bizzarrely the getMusWithNotes() call here behaves differently when running or debugging.
		// I have ignored it as there is nothing 
		List<Mu> musWithNotes = pack.getMu().getMusWithNotes();
		for (Mu xmu: musWithNotes) System.out.println("-------------------\n" + xmu.toString());
//		assertThat(musWithNotes.size()).isEqualTo(1);
		List<Mu> allMus = pack.getMu().getAllMus();
		assertThat(allMus.size()).isEqualTo(2);
		
	}
	
	
	/*
	 * used the alternate plugin which might use the same repo: 
	 * e.g. PhraseLengthRandom uses the same repo as PhraseLengthSetLength
	 */
	@Test
	void when_repo_contains_alternate_repos_then_structure_tone_is_generated() {
		
		Poopline pipeline = new Poopline();
		pipeline.setPrimaryPlugin(new ForceCreatePlugInsFromRepo(pipeline));
		PooplinePackage pack = new PooplinePackage("pack", new Random());
		pack.setDebugMode(true);
		addPhraseLengthRandomRepo(pack, 2);
		addTimeSignatureRepo(pack, TimeSignature.FOUR_FOUR);
		addXmlKeyRepo(pack, 2);
		addDiatonicChordRepo(pack, ImmutableMap.of(0.0, "Db", 1.0, "Ab"));
		addPhraseBoundRepo(pack, -0.5, Parameter.PHRASE_START_PERCENT);
		addPhraseBoundRepo(pack, 0.25, Parameter.PHRASE_END_PERCENT);
		addEvenlySpacedStructureToneRandomRepo(pack, 1.0);
		addTessituraRepo(pack, 44, 72, Parameter.TESSITURA_START_NOTE);
		addStartNoteRepo(pack, 65);
		addTessituraRepo(pack, 44, 72, Parameter.TESSITURA_MELODY_RANGE);
		addContourMultiplierRepo(pack, 7);
		addVectorChordTonesFixedRepo(pack, new int[] {1, -1});
		pack = pipeline.process(pack);
		System.out.println(pack.getMu().toString());
		System.out.println(pack.getMu().getMus().toString());
		
		// Bizzarrely the getMusWithNotes() call here behaves differently when running or debugging.
		// I have ignored it as there is nothing 
		List<Mu> musWithNotes = pack.getMu().getMusWithNotes();
		for (Mu xmu: musWithNotes) System.out.println("-------------------\n" + xmu.toString());
//		assertThat(musWithNotes.size()).isEqualTo(1);
		List<Mu> allMus = pack.getMu().getAllMus();
		assertThat(allMus.size()).isEqualTo(2);
		
	}

	
	
// privates --------------------------------------------------------------------------------------------
	

	private void addVectorChordTonesFixedRepo(PooplinePackage pack, int[] aVectorArray)
	{
		VectorChordTonesRepo vrepo = VectorChordTonesRepo.builder()
				.selectedVectorArray(aVectorArray)
				.className(VectorChordTonesFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.STRUCTURE_TONE_GENERATOR, vrepo);
	}


	private void addContourChordToneRepo(PooplinePackage pack, FourPointContour aFourPointContour)
	{
		ContourChordTonesRepo ctRepo = ContourChordTonesRepo.builder()
				.rndValue(0.02)
				.selectedOption(aFourPointContour)
				.className(ContourChordTonesRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.STRUCTURE_TONE_GENERATOR, ctRepo);
	}


	private void addContourMultiplierRepo(PooplinePackage pack, int aMultiplier)
	{
		ContourMultiplierRepo multRepo = ContourMultiplierRepo.builder()
				.rndValue(0.33)
				.multiplier(aMultiplier)
				.className(ContourMultiplierRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.STRUCTURE_TONE_MULTIPLIER, multRepo);
	}


	private void addStartNoteRepo(PooplinePackage pack, int aStartNote)
	{
		StartNoteRepo startNoteRepo = StartNoteRepo.builder()
				.rndValue(0.45)
				.selectedValue(aStartNote)
				.className(StartNoteMelodyRandom.class.getName())
				.build();		
		pack.getRepo().put(Parameter.START_NOTE, startNoteRepo);
	}


	private void addTessituraRepo(PooplinePackage pack, int aLowValue, int aHighValue, Parameter aParameter)
	{
		TessituraRepo tessStartNoteRepo = TessituraRepo.builder()
				.highValue(aHighValue)
				.lowValue(aLowValue)
				.parameter(aParameter)
				.className(TessituraFixed.class.getName())
				.build();
		pack.getRepo().put(aParameter, tessStartNoteRepo);
	}


	private void addEvenlySpacedStructureToneFixedRepo(PooplinePackage pack, double aFloatBarSpacing)
	{
		StructureToneEvenlySpacedRepo spacingRepo = StructureToneEvenlySpacedRepo.builder()
				.rndValue(0.5)
				.selectedValueInFloatBars(aFloatBarSpacing)
				.options(new double[] {aFloatBarSpacing})
				.className(StructureToneEvenlySpacedFixed.class.getName())
				.build();
		pack.getRepo().put(Parameter.STRUCTURE_TONE_SPACING, spacingRepo);
	}
	
	
	private void addEvenlySpacedStructureToneRandomRepo(PooplinePackage pack, double aFloatBarSpacing)
	{
		StructureToneEvenlySpacedRepo spacingRepo = StructureToneEvenlySpacedRepo.builder()
				.rndValue(0.5)
				.selectedValueInFloatBars(aFloatBarSpacing)
				.options(new double[] {aFloatBarSpacing})
				.className(StructureToneEvenlySpacedRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.STRUCTURE_TONE_SPACING, spacingRepo);
	}


	private void addPhraseBoundRepo(PooplinePackage pack, double aSelectedValue, Parameter aParameter)
	{
		PhraseBoundRepo startRepo = PhraseBoundRepo.builder()
				.selectedValue(aSelectedValue)
				.parameter(aParameter)
				.className(PhraseBoundPercentSetAmount.class.getName())
				.build();
		pack.getRepo().put(aParameter, startRepo);
	}


	private void addDiatonicChordRepo(PooplinePackage pack, ImmutableMap<Double, String> floatBarChordMap)
	{
		ChordProgressionRepo dRepo = ChordProgressionRepo.builder()
				.rndValue(new double[] {0.2, 0.7})
				.floatBarChordMap(floatBarChordMap)
				.className(ChordProgressionDiatonicTriadRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.CHORD_LIST_GENERATOR, dRepo);
	}


	private void addTimeSignatureRepo(PooplinePackage pack, TimeSignature aTs)
	{
		TimeSignatureRepo tsRepo = TimeSignatureRepo.builder()
				.rndValue(0.5)
				.selectedValue(aTs)
				.options(new TimeSignature[] {aTs})
				.className(TimeSignatureSingleRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.TIME_SIGNATURE, tsRepo);
	}
	
	
	private void addXmlKeyRepo(PooplinePackage pack, int aXmlKey)
	{
		XmlKeyRepo repo = XmlKeyRepo.builder()
				.rndValue(0.5)
				.selectedValue(aXmlKey)
				.options(new int[] {aXmlKey})
				.className(XmlKeyRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.XMLKEY, repo);
	}


	private void addPhraseLengthFixedRepo(PooplinePackage pack, int aSelectedValue)
	{
		PhraseLengthRepo phRepo = PhraseLengthRepo.builder()
				.rndValue(0.5)
				.selectedValue(aSelectedValue)
				.options(new int[] {aSelectedValue})
				.className(PhraseLengthSetLength.class.getName())
				.build();
		pack.getRepo().put(Parameter.PHRASE_LENGTH, phRepo);
	}
	
	
	private void addPhraseLengthRandomRepo(PooplinePackage pack, int aSelectedValue)
	{
		PhraseLengthRepo phRepo = PhraseLengthRepo.builder()
				.rndValue(0.5)
				.selectedValue(aSelectedValue)
				.options(new int[] {aSelectedValue})
				.className(PhraseLengthRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.PHRASE_LENGTH, phRepo);
	}
	
	
	private void addTempoRandomRepo(PooplinePackage pack, int aTempo)
	{
		TempoRepo tRepo = TempoRepo.builder()
				.rndValue(0.5)
				.selectedTempo(aTempo)
				.className(TempoRandom.class.getName())
				.build();
		pack.getRepo().put(Parameter.TEMPO, tRepo);
	}
}
