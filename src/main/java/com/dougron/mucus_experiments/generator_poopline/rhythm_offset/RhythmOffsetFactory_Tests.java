package main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;

class RhythmOffsetFactory_Tests
{

	@Test
	void when_start_is_on_bar_1_and_end_is_on_bar_2_then_rhythmOffset_bar_offset_is_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  1);
		parent.addMu(end,  2);
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(1);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(0);
		assertThat(ro.getTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(0);
	}

	
	@Test
	void when_start_is_on_bar_2_and_end_is_on_bar_1_then_rhythmOffset_bar_offset_is_minus_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  2);
		parent.addMu(end,  1);
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(-1);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(0);
		assertThat(ro.getTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(0);
	}
	
	
	@Test
	void when_start_is_somewhere_in_bar_1_and_end_is_on_bar_2_then_rhythmOffset_bar_offset_is_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  BarsAndBeats.at(1, 2.5));
		parent.addMu(end,  2);
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(1);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(0);
		assertThat(ro.getTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(0);
		
	}
	
	
	@Test
	void when_start_is_on_bar_1_and_end_is_on_next_supertactus_then_rhythmOffset_supertactus_offset_is_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  1);
		parent.addMu(end,  BarsAndBeats.at(1,  2.0));
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(0);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(1);
		assertThat(ro.getTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(0);
		
	}
	
	
	@Test
	void when_start_is_on_bar_2_and_end_is_on_previous_supertactus_then_rhythmOffset_supertactus_offset_is_minus_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  2);
		parent.addMu(end,  BarsAndBeats.at(1,  2.0));
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(0);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(-1);
		assertThat(ro.getTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(0);
		
	}
	
	
	@Test
	void when_start_is_on_bar_1_and_end_is_on_next_tactus_then_rhythmOffset_tactus_offset_is_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  1);
		parent.addMu(end,  BarsAndBeats.at(1,  1.0));
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(0);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(0);
		assertThat(ro.getTactusOffset()).isEqualTo(1);
		assertThat(ro.getSubTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(0);
	}
	
	
	@Test
	void when_start_is_on_bar_2_and_end_is_on_previous_tactus_then_rhythmOffset_tactus_offset_is_minus_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  2);
		parent.addMu(end,  BarsAndBeats.at(1,  3.0));
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(0);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(0);
		assertThat(ro.getTactusOffset()).isEqualTo(-1);
		assertThat(ro.getSubTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(0);
	}
	
	
	@Test
	void when_start_is_on_bar_1_and_end_is_on_next_subtactus_then_rhythmOffset_subtactus_offset_is_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  1);
		parent.addMu(end,  BarsAndBeats.at(1,  0.5));
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(0);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(0);
		assertThat(ro.getTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubTactusOffset()).isEqualTo(1);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(0);
	}
	
	
	@Test
	void when_start_is_on_bar_2_and_end_is_on_previous_subtactus_then_rhythmOffset_subtactus_offset_is_minus_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  2);
		parent.addMu(end,  BarsAndBeats.at(1,  3.5));
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(0);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(0);
		assertThat(ro.getTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubTactusOffset()).isEqualTo(-1);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(0);
	}
	
	
	@Test
	void when_start_is_on_bar_1_and_end_is_on_next_subsubtactus_then_rhythmOffset_subsubtactus_offset_is_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  1);
		parent.addMu(end,  BarsAndBeats.at(1,  0.25));
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(0);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(0);
		assertThat(ro.getTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(1);
	}
	
	
	@Test
	void when_start_is_on_bar_2_and_end_is_on_previous_subsubtactus_then_rhythmOffset_subsubtactus_offset_is_minus_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  2);
		parent.addMu(end,  BarsAndBeats.at(1,  3.75));
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(0);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(0);
		assertThat(ro.getTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubTactusOffset()).isEqualTo(0);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(-1);
	}
	
	
	@Test
	void when_start_is_on_bar_1_and_end_is_subsubtactus_before_next_next_bar_then_rhythmOffset_all_offsets_are_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  1);
		parent.addMu(end,  BarsAndBeats.at(2,  3.75));
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(1);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(1);
		assertThat(ro.getTactusOffset()).isEqualTo(1);
		assertThat(ro.getSubTactusOffset()).isEqualTo(1);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(1);
	}
	
	
	@Test
	void when_start_is_on_bar_2_and_end_is_subsubtactus_before__previous_previous_bar_then_rhythmOffset_all_offsets_are_minus_1()
	{
		Mu parent = new Mu("parent");
		Mu start = new Mu("start");
		Mu end = new Mu("end");
		parent.addMu(start,  2);
		parent.addMu(end,  BarsAndBeats.at(0,  0.25));
		
		RhythmOffset ro = RhythmOffsetFactory.getRhythmOffset(start, end);
		assertThat(ro.getBarOffset()).isEqualTo(-1);
		assertThat(ro.getSuperTactusOffset()).isEqualTo(-1);
		assertThat(ro.getTactusOffset()).isEqualTo(-1);
		assertThat(ro.getSubTactusOffset()).isEqualTo(-1);
		assertThat(ro.getSubSubTactusOffset()).isEqualTo(-1);
	}

}
