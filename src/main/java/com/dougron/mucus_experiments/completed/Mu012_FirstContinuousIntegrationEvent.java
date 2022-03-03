package main.java.com.dougron.mucus_experiments.completed;

import java.util.ArrayList;
import java.util.List;

import main.java.com.dougron.mucus.algorithms.superimposifier.left_to_right.SuFiSu_LeftToRight;
import main.java.com.dougron.mucus.algorithms.superimposifier.left_to_right.SuFi_IntervalModel_Generator;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.MuNote;
import main.java.com.dougron.mucus.mu_framework.ruler.time_signature_list.TimeSignatureListGeneratorFactory;
import main.java.com.dougron.mucus.mucus_output_manager.mucus_lom_injector.MuucusLOMInjector;
import main.java.com.dougron.mucus.mucus_output_manager.musicxml_maker.MuXMLMaker;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;
import test.java.com.dougron.mucus.TestingStuff;
import timed_notes_and_controllers.TimedNote;

/*
 * uses the SuFiSu_LeftToRight to demonstrate superimposed algorithms
 * 
 * this approach superseded by overwritable_vectors method of superimposing
 * 
 * creates musicxml and injects into Live
 * 
 * makeMu() is taken from SuperImposerifier_Mu004, a test class from mucus_junits
 * 
 * 
 */


public class Mu012_FirstContinuousIntegrationEvent
{


	public Mu012_FirstContinuousIntegrationEvent()
	{
		int trackIndex = 0;
		int clipIndex = 0;
		Mu mu = makeMu();
		System.out.println(mu.stateOfMusToString());
		MuucusLOMInjector injector = new MuucusLOMInjector(7800);
		
		injectMuIntoLive(trackIndex, clipIndex, mu, injector);
		makeMusicXML(mu);
	}



	private void makeMusicXML(Mu mu)
	{
		MuXMLMaker.makeXML(mu, TestingStuff.getQuickNastyXMLPathString("Mu012_FirstContinuousIntegrationEvent"));
	}



	private void injectMuIntoLive(int trackIndex, int clipIndex, Mu mu, MuucusLOMInjector injector)
	{
		clearClip(trackIndex, clipIndex, injector);
		setLoopLength(trackIndex, clipIndex, injector, mu);
		injectNotes(trackIndex, clipIndex, injector, mu);
	}
	
	
	
	private void injectNotes(int aTrackIndex, int aClipIndex, MuucusLOMInjector aInjector, Mu aMu)
	{
		List<TimedNote> noteList = makeTimeNoteList(aMu);
		aInjector.replaceNotes(aTrackIndex, aClipIndex, noteList);
	}



	private List<TimedNote> makeTimeNoteList(Mu aMu)
	{
		List<TimedNote> timedNoteList = new ArrayList<TimedNote>();
		for (Mu mu: aMu.getMusWithNotes())
		{
			for (MuNote muNote: mu.getMuNotes())
			{
				timedNoteList.add(
						new TimedNote(
								muNote.getPitch(), muNote.getVelocity(), 
								mu.getGlobalPositionInQuarters(), mu.getLengthInQuarters()
						)
				);
			}
		}
		return timedNoteList;
	}



	private void setLoopLength(int aTrackIndex, int aClipIndex, MuucusLOMInjector aInjector, Mu aMu)
	{
		double lengthInQuarters = aMu.getLengthInQuarters();
		aInjector.setClipLoopEndPosition(aTrackIndex, aClipIndex, lengthInQuarters);
	}



	private void clearClip(int aTrackIndex, int aClipIndex, MuucusLOMInjector aInjector)
	{
		aInjector.deleteClip(aTrackIndex, aClipIndex);	
		aInjector.createClip(aTrackIndex, aClipIndex, 4.0);
	}



	private Mu makeMu()
	{
		double fill = 0.77;
		int startNote = 54;
				
		Mu mu = make4BarFourFourPhrase();
		
		mu.setStartPitch(startNote);
				
		makeSuFiSuAndAddSuFis(fill, mu);
		
		mu.runSuFiSuGenerator();
		return mu;
	}



	private void makeSuFiSuAndAddSuFis(double fill, Mu mu)
	{
		SuFiSu_LeftToRight susu = createSuFiSu(mu);		
		SuFi_IntervalModel_Generator sufi1 = createSufi1(fill, mu, susu);		
		SuFi_IntervalModel_Generator sufi2 = createSufi2(susu);
		
		susu.addSufi(sufi1);
		susu.addSufi(sufi2);
	}
	
	
	
	private static SuFi_IntervalModel_Generator createSufi2(SuFiSu_LeftToRight susu)
	{
		SuFi_IntervalModel_Generator sufi2 = new SuFi_IntervalModel_Generator(5.0, 8.0, susu);
		sufi2.addIntervalModel(-1);
		return sufi2;
	}

	
	
	private static SuFi_IntervalModel_Generator createSufi1(double fill, Mu mu, SuFiSu_LeftToRight susu)
	{
		SuFi_IntervalModel_Generator sufi1 = new SuFi_IntervalModel_Generator(0.0, mu.getLengthInQuarters() * fill, susu);
		sufi1.addIntervalModel(1);
		return sufi1;
	}

	
	
	private static SuFiSu_LeftToRight createSuFiSu(Mu mu)
	{
		SuFiSu_LeftToRight susu = new SuFiSu_LeftToRight(mu);
		mu.setGenerator(susu);
		return susu;
	}
	
	
	
	private static Mu make4BarFourFourPhrase() 
	{
		Mu mu = new Mu("phrase");
		mu.setTimeSignatureGenerator(TimeSignatureListGeneratorFactory.getGenerator(TimeSignature.FOUR_FOUR));
		mu.setLengthInBars(4);
		return mu;
	}
	
	
	
	public static void main(String[] args)
	{
		new Mu012_FirstContinuousIntegrationEvent();
	}
}
