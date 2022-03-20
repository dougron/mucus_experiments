package main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RhythmOffset {

	private int barOffset;
	private int superTactusOffset;
	private int tactusOffset;
	private int subTactusOffset;
	private int subSubTactusOffset;
	
	
	public double getOffsetInQuarters(double aGlobalPositionInQuarters, Mu anyMuFromTheMuNetwork)
	{
		BarsAndBeats startPosition = anyMuFromTheMuNetwork.getGlobalPositionInBarsAndBeats(aGlobalPositionInQuarters);
		return getOffsetInQuarters(startPosition, anyMuFromTheMuNetwork);
	}
	
	
	
	public double getOffsetInQuarters(Mu aMu) 
	{
		BarsAndBeats startPosition = aMu.getGlobalPositionInBarsAndBeats();
		return getOffsetInQuarters(startPosition, aMu);
	}
	
	
	
	public double getOffsetInQuarters(BarsAndBeats startPosition, Mu anyMuFromTheMuNetwork) 
	{
		double startPositionInQuarters = anyMuFromTheMuNetwork.getGlobalPositionInQuarters(startPosition);

		BarsAndBeats globalPositionAfterBarOffset = getGlobalPositionAfterBarOffset(startPosition);
		BarsAndBeats globalPositionAfterSuperTactusOffset = getGlobalPositionAfterSuperTactusOffset(globalPositionAfterBarOffset, anyMuFromTheMuNetwork);		
		BarsAndBeats globalPositionAfterTactusOffset = getGlobalPositionAfterTactusOffset(globalPositionAfterSuperTactusOffset, anyMuFromTheMuNetwork) ;
		BarsAndBeats globalPositionAfterSubTactusOffset = getGlobalPositionAfterSubTactusOffset(globalPositionAfterTactusOffset, anyMuFromTheMuNetwork);
		BarsAndBeats globalPositionAfterSubSubTactusOffset = getGlobalPositionAfterSubSubTactusOffset(globalPositionAfterSubTactusOffset, anyMuFromTheMuNetwork);
		
		double endPositionInQuarters = anyMuFromTheMuNetwork.getGlobalPositionInQuarters(globalPositionAfterSubSubTactusOffset);
		
		return endPositionInQuarters - startPositionInQuarters;
	}
	
	

	private BarsAndBeats getGlobalPositionAfterSubSubTactusOffset(
			BarsAndBeats startPosition, Mu aMu
			)
	{
		if (getSubSubTactusOffset() == 0)
		{
			return startPosition;
		}
		else
		{
			if (getSubSubTactusOffset() > 0)
			{
				return getFinalPositionForPositiveSubSubTactusOffset(startPosition, aMu);
			}
			else
			{
				return getFinalPositionForNegativeSubSubTactusOffset(startPosition, aMu);
			}
		}
	}

	
	
	private BarsAndBeats getFinalPositionForNegativeSubSubTactusOffset(
			BarsAndBeats startPosition, Mu aMu)
	{
		int barIndex = startPosition.getBarPosition();
		TimeSignature ts = getTimeSignature(barIndex, aMu);
		double positionInBar = startPosition.getOffsetInQuarters();
		if (positionInBar == 0.0)
		{
			barIndex--;
			ts = getTimeSignature(barIndex, aMu);
			positionInBar = ts.getLengthInQuarters();
		}
		int count = 0;
		while (count > getSubSubTactusOffset())
		{
			Double[] subSubTactii = ts.getQuartersPositionNBelowSubtactus(1);
			for (int i = subSubTactii.length - 1; i > -1; i--)
			{
				if (count == getSubSubTactusOffset()) break;
				if (subSubTactii[i] < positionInBar)
				{
					positionInBar = subSubTactii[i];
					count--;
				}	
			}
			if (count > getSubSubTactusOffset())
			{
				barIndex--;
				ts = getTimeSignature(barIndex, aMu);
				positionInBar = ts.getLengthInQuarters();
			}
		}	
		return new BarsAndBeats(barIndex, positionInBar);
	}
	
	
	
	private BarsAndBeats getFinalPositionForPositiveSubSubTactusOffset(
			BarsAndBeats startPosition, Mu aMu)
	{
		int barIndex = startPosition.getBarPosition();				
		double positionInBar = startPosition.getOffsetInQuarters();
		int count = 0;
		while (count < getSubSubTactusOffset())
		{
			TimeSignature ts = getTimeSignature(barIndex, aMu);
			for (Double subSubTactii: ts.getQuartersPositionNBelowSubtactus(1))
			{
				if (count == getSubSubTactusOffset()) break;
				if (subSubTactii > positionInBar)
				{
					positionInBar = subSubTactii;
					count++;
				}	
			}
			if (count < getSubSubTactusOffset())
			{
				barIndex++;
				count++;
				positionInBar = 0.0;
			}
		}	
		return new BarsAndBeats(barIndex, positionInBar);
	}
	
	
	
	private BarsAndBeats getGlobalPositionAfterSubTactusOffset(
			BarsAndBeats startPosition, Mu aMu
			)
	{
		if (getSubTactusOffset() == 0)
		{
			return startPosition;
		}
		else
		{
			if (getSubTactusOffset() > 0)
			{
				return getFinalPositionForPositiveSubTactusOffset(startPosition, aMu);
			}
			else
			{
				return getFinalPositionForNegativeSubTactusOffset(startPosition, aMu);
			}
		}
	}
	
	
	
	private BarsAndBeats getFinalPositionForNegativeSubTactusOffset(
			BarsAndBeats startPosition, Mu aMu)
	{
		int barIndex = startPosition.getBarPosition();
		TimeSignature ts = getTimeSignature(barIndex, aMu);
		double positionInBar = startPosition.getOffsetInQuarters();
		if (positionInBar == 0.0)
		{
			barIndex--;
			ts = getTimeSignature(barIndex, aMu);
			positionInBar = ts.getLengthInQuarters();
		}
		int count = 0;
		while (count > getSubTactusOffset())
		{
			Double[] subTactii = ts.getSubTactusAsQuartersPositions();
			for (int i = subTactii.length - 1; i > -1; i--)
			{
				if (count == getSubTactusOffset()) break;
				if (subTactii[i] < positionInBar)
				{
					positionInBar = subTactii[i];
					count--;
				}	
			}
			if (count > getSubTactusOffset())
			{
				barIndex--;
				ts = getTimeSignature(barIndex, aMu);
				positionInBar = ts.getLengthInQuarters();
			}
		}	
		return new BarsAndBeats(barIndex, positionInBar);
	}
	
	
	
	private BarsAndBeats getFinalPositionForPositiveSubTactusOffset(
			BarsAndBeats startPosition, Mu aMu)
	{
		int barIndex = startPosition.getBarPosition();				
		double positionInBar = startPosition.getOffsetInQuarters();
		int count = 0;
		while (count < getSubTactusOffset())
		{
			TimeSignature ts = getTimeSignature(barIndex, aMu);
			for (Double subTactus: ts.getSubTactusAsQuartersPositions())
			{
				if (count == getSubTactusOffset()) break;
				if (subTactus > positionInBar)
				{
					positionInBar = subTactus;
					count++;
				}	
			}
			if (count < getSubTactusOffset())
			{
				barIndex++;
				count++;
				positionInBar = 0.0;
			}
		}	
		return new BarsAndBeats(barIndex, positionInBar);
	}
	


	
	

	private BarsAndBeats getGlobalPositionAfterTactusOffset(
			BarsAndBeats startPosition,
			Mu aMu
			)
	{
		if (getTactusOffset() == 0)
		{
			return startPosition;
		}
		else
		{
			if (getTactusOffset() > 0)
			{
				return getPositionForSubTactusCountForPositiveTactusOffset(startPosition, aMu);
			}
			else
			{
				return getPositionForSubTactusCountForNegativeTactusOffset(startPosition, aMu);
			}
		}
	}
	
	



	private BarsAndBeats getPositionForSubTactusCountForNegativeTactusOffset(
			BarsAndBeats startPosition, Mu aMu)
	{
		int barIndex = startPosition.getBarPosition();
		TimeSignature ts = getTimeSignature(barIndex, aMu);
		double positionInBar = startPosition.getOffsetInQuarters();
		if (positionInBar == 0.0)
		{
			barIndex--;
			ts = getTimeSignature(barIndex, aMu);
			positionInBar = ts.getLengthInQuarters();
		}
		int count = 0;
		while (count > getTactusOffset())
		{
			Double[] tactii = ts.getTactusAsQuartersPositions();
			for (int i = tactii.length - 1; i > -1; i--)
			{
				if (count == getTactusOffset()) break;
				if (tactii[i] < positionInBar)
				{
					positionInBar = tactii[i];
					count--;
				}	
			}
			if (count > getTactusOffset())
			{
				barIndex--;
				ts = getTimeSignature(barIndex, aMu);
				positionInBar = ts.getLengthInQuarters();
			}
		}	
		return new BarsAndBeats(barIndex, positionInBar);
	}
	
	
	
	private BarsAndBeats getPositionForSubTactusCountForPositiveTactusOffset(
			BarsAndBeats startPosition, Mu aMu)
	{
		int barIndex = startPosition.getBarPosition();				
		double positionInBar = startPosition.getOffsetInQuarters();
		int count = 0;
		while (count < getTactusOffset())
		{
			TimeSignature ts = getTimeSignature(barIndex, aMu);
			for (Double tactus: ts.getTactusAsQuartersPositions())
			{
				if (count == getTactusOffset()) break;
				if (tactus > positionInBar)
				{
					positionInBar = tactus;
					count++;
				}	
			}
			if (count < getTactusOffset())
			{
				barIndex++;
				count++;
				positionInBar = 0.0;
			}
		}	
		return new BarsAndBeats(barIndex, positionInBar);
	}
	


	private BarsAndBeats getGlobalPositionAfterSuperTactusOffset(
			BarsAndBeats startPosition, 
			Mu aMu
			)
	{
		if (getSuperTactusOffset() == 0)
		{
			return startPosition;
		}
		else
		{
			if (getSuperTactusOffset() > 0)
			{
				return getPositionForTactusCountForPositiveSuperTactusOffset(startPosition, aMu);
			}
			else
			{
				return getPositionForTactusCountForNegativeSuperTactusOffset(startPosition, aMu);
			}
		}
	}
	
	
	
	private BarsAndBeats getPositionForTactusCountForPositiveSuperTactusOffset(BarsAndBeats startPosition, Mu aMu)
	{
		int barIndex = startPosition.getBarPosition();				
		double positionInBar = startPosition.getOffsetInQuarters();
		int count = 0;
		while (count < getSuperTactusOffset())
		{
			TimeSignature ts = getTimeSignature(barIndex, aMu);
			for (Double superTatcus: ts.getSuperTactusAsQuartersPositions())
			{
				if (count == getSuperTactusOffset()) break;
				if (superTatcus > positionInBar)
				{
					positionInBar = superTatcus;
					count++;
				}	
			}
			if (count < getSuperTactusOffset())
			{
				barIndex++;
				count++;
				positionInBar = 0.0;
			}
		}	
		return new BarsAndBeats(barIndex, positionInBar);
	}

	
	
	
	private BarsAndBeats getPositionForTactusCountForNegativeSuperTactusOffset(BarsAndBeats startPosition, Mu aMu)
	{
		int barIndex = startPosition.getBarPosition();
		TimeSignature ts = getTimeSignature(barIndex, aMu);
		double positionInBar = startPosition.getOffsetInQuarters();
		if (positionInBar == 0.0)
		{
			barIndex--;
			ts = getTimeSignature(barIndex, aMu);
			positionInBar = ts.getLengthInQuarters();
		}
		int count = 0;
		while (count > getSuperTactusOffset())
		{
			Double[] superTactii = ts.getSuperTactusAsQuartersPositions();
			for (int i = superTactii.length - 1; i > -1; i--)
			{
				if (count == getSuperTactusOffset()) break;
				if (superTactii[i] < positionInBar)
				{
					positionInBar = superTactii[i];
					count--;
				}	
			}
			if (count > getSuperTactusOffset())
			{
				barIndex--;
				ts = getTimeSignature(barIndex, aMu);
				positionInBar = ts.getLengthInQuarters();
			}
		}	
		return new BarsAndBeats(barIndex, positionInBar);
	}
	
	

	public TimeSignature getTimeSignature(int aPositionInBars, Mu aMu)
	{
		Mu parent = aMu;
		while (true)
		{
			if (parent.getParent() == null)
			{
				break;
			}
			parent = parent.getParent();
		}
		
		parent.checkForNullRulerAndCreate();
		return parent.getTimeSignature(aPositionInBars);
	}
	
	
	
	private BarsAndBeats getGlobalPositionAfterBarOffset(BarsAndBeats startPosition)
	{
		if (getBarOffset() == 0)
		{
			return startPosition;
		}
		else
		{
			int barPosition = startPosition.getBarPosition();
			if (getBarOffset() < 0 && startPosition.getOffsetInQuarters() > 0.0)
			{
				barPosition++;
			}
			barPosition += getBarOffset();
			return new BarsAndBeats(barPosition, 0.0);
		}
	}



}
