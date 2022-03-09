package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tessitura_solver;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;

class TessituraSolverUsingAtMostOneBreakPoint_Tests {

	@Test
	void inatantiates() {
		TessituraSolver x = TessituraSolverUsingAtMostOneBreakPoint.getInstance();
		assertThat(x).isNotNull();
	}
	
	
	@Test
	void when_notes_are_in_range_then_no_notes_are_changed() throws Exception {
		TessituraSolver x = TessituraSolverUsingAtMostOneBreakPoint.getInstance();
		TessituraRepo repo = TessituraRepo.builder()
				.lowValue(42)
				.highValue(70)
				.build();
		Mu mu = new Mu("x");
		mu.addMuNote(new MuNote(50, 101));
		List<Mu> muList = new ArrayList<Mu>();
		muList.add(mu);
		muList = x.solveForTessitura(muList, repo);
		assertThat(muList.get(0).getTopPitch()).isEqualTo(50);
	}
	
	
	@Test
	void when_notes_are_below_range_then_notes_are_changed_up_one_octave() throws Exception {
		TessituraSolver x = TessituraSolverUsingAtMostOneBreakPoint.getInstance();
		TessituraRepo repo = TessituraRepo.builder()
				.lowValue(42)
				.highValue(70)
				.build();
		Mu mu = new Mu("x");
		mu.addMuNote(new MuNote(40, 101));
		List<Mu> muList = new ArrayList<Mu>();
		muList.add(mu);
		muList = x.solveForTessitura(muList, repo);
		assertThat(muList.get(0).getTopPitch()).isEqualTo(52);
	}
	
	
	@Test
	void when_notes_are_above_range_then_notes_are_changed_down_one_octave() throws Exception {
		TessituraSolver x = TessituraSolverUsingAtMostOneBreakPoint.getInstance();
		TessituraRepo repo = TessituraRepo.builder()
				.lowValue(42)
				.highValue(70)
				.build();
		Mu mu = new Mu("x");
		mu.addMuNote(new MuNote(80, 101));
		List<Mu> muList = new ArrayList<Mu>();
		muList.add(mu);
		muList = x.solveForTessitura(muList, repo);
		assertThat(muList.get(0).getTopPitch()).isEqualTo(68);
	}
	
	
	@Test
	void when_first_note_is_in_and_2nd_is_above_range_then_2nd_note_is_changed_down_one_octave() throws Exception {
		TessituraSolver x = TessituraSolverUsingAtMostOneBreakPoint.getInstance();
		TessituraRepo repo = TessituraRepo.builder()
				.lowValue(42)
				.highValue(70)
				.build();
		Mu mu1 = new Mu("x");
		mu1.addMuNote(new MuNote(60, 101));
		Mu mu2 = new Mu("x");
		mu2.addMuNote(new MuNote(80, 101));
		List<Mu> muList = new ArrayList<Mu>();
		muList.add(mu1);
		muList.add(mu2);
		muList = x.solveForTessitura(muList, repo);
		assertThat(muList.get(0).getTopPitch()).isEqualTo(60);
		assertThat(muList.get(1).getTopPitch()).isEqualTo(68);
	}
	
	
	@Test
	void when_first_note_is_in_and_2nd_is_below_range_then_2nd_note_is_changed_up_one_octave() throws Exception {
		TessituraSolver x = TessituraSolverUsingAtMostOneBreakPoint.getInstance();
		TessituraRepo repo = TessituraRepo.builder()
				.lowValue(42)
				.highValue(70)
				.build();
		Mu mu1 = new Mu("x");
		mu1.addMuNote(new MuNote(50, 101));
		Mu mu2 = new Mu("x");
		mu2.addMuNote(new MuNote(40, 101));
		List<Mu> muList = new ArrayList<Mu>();
		muList.add(mu1);
		muList.add(mu2);
		muList = x.solveForTessitura(muList, repo);
		assertThat(muList.get(0).getTopPitch()).isEqualTo(50);
		assertThat(muList.get(1).getTopPitch()).isEqualTo(52);
	}
	
	
	@Test
	void when_note_1_and_2_are_in_and_3_is_below_range_and_2_will_be_in_when_transpose_up_an_octave_then_note_2_and_3_are_changed_up_one_octave() throws Exception {
		// because solver favours the latest breakpoint which keeps absolute score the lowest 
		TessituraSolver x = TessituraSolverUsingAtMostOneBreakPoint.getInstance();
		TessituraRepo repo = TessituraRepo.builder()
				.lowValue(42)
				.highValue(70)
				.build();
		Mu mu0 = new Mu("x");
		mu0.addMuNote(new MuNote(50, 101));
		Mu mu1 = new Mu("x");
		mu1.addMuNote(new MuNote(50, 101));
		Mu mu2 = new Mu("x");
		mu2.addMuNote(new MuNote(40, 101));
		List<Mu> muList = new ArrayList<Mu>();
		muList.add(mu0);
		muList.add(mu1);
		muList.add(mu2);
		muList = x.solveForTessitura(muList, repo);
		assertThat(muList.get(0).getTopPitch()).isEqualTo(50);
		assertThat(muList.get(1).getTopPitch()).isEqualTo(50);
		assertThat(muList.get(2).getTopPitch()).isEqualTo(52);
	}
	
	

}
