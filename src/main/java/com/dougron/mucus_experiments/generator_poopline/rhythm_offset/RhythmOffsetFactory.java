package main.java.com.dougron.mucus_experiments.generator_poopline.rhythm_offset;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus.mu_framework.data_types.BarsAndBeats;
import main.java.com.dougron.mucus.mu_framework.data_types.RelativeRhythmicPosition;
import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;

public class RhythmOffsetFactory
{

	public static RhythmOffset getRhythmOffset(Mu from, Mu to)
	{
		BarsAndBeats globalStartPosition = from.getGlobalPositionInBarsAndBeats();
		BarsAndBeats globalEndPosition = to.getGlobalPositionInBarsAndBeats();
		return getRhythmOffset(globalStartPosition, globalEndPosition, from);
	}
	
	
	public static RhythmOffset getRhythmOffset(
			BarsAndBeats aGlobalStartPosition, 
			BarsAndBeats aGlobalEndPosition,
			Mu aMuFromTheNetwork
			)
	{
		int barOffset = getRelativeBarPosition(aGlobalStartPosition, aGlobalEndPosition);		
		BarsAndBeats positionForSuperTactusCalculation 
		= getPositionForSuperTactusCalculation(aGlobalStartPosition, barOffset);
		
		int superTactusOffset 
		= getRelativeSuperTactusOffset(positionForSuperTactusCalculation, aGlobalEndPosition, aMuFromTheNetwork);		
		BarsAndBeats positionForTactusCalculation 
		= getPositionForTactusCalculation(aGlobalEndPosition, positionForSuperTactusCalculation, superTactusOffset, aMuFromTheNetwork);
		
		int tactusOffset 
		= getRelativeTactusOffset(positionForTactusCalculation, aGlobalEndPosition, aMuFromTheNetwork);
		BarsAndBeats positionForSubTactusCalculation 
		= getPositionForSubTactusCalulation(aGlobalEndPosition, positionForTactusCalculation, tactusOffset, aMuFromTheNetwork);
		
		int subTactusOffset 
		= getRelativeSubTactusOffset(positionForSubTactusCalculation, aGlobalEndPosition, aMuFromTheNetwork);
		BarsAndBeats positionForSubSubTactusCalculation 
		= getPositionForSubSubTactusCalulation(aGlobalEndPosition, positionForSubTactusCalculation, subTactusOffset, aMuFromTheNetwork);
		
		int subSubTactusOffset 
		= getRelativeSubSubTactusOffset(positionForSubSubTactusCalculation, aGlobalEndPosition, aMuFromTheNetwork);
		
		return new RhythmOffset(barOffset, superTactusOffset, tactusOffset, subTactusOffset, subSubTactusOffset);
	}
	
	
	
	private static int getRelativeSubSubTactusOffset(
			BarsAndBeats aGlobalStartPosition, 
			BarsAndBeats aGlobalEndPosition, 
			Mu aMuFromTheNetwork)
	// assume they are in the same bar
	{
		TimeSignature ts = aMuFromTheNetwork.getTimeSignatureFromGlobalBarPosition(aGlobalEndPosition.getBarPosition());
		double startPos = aGlobalStartPosition.getOffsetInQuarters();
		double endPos = aGlobalEndPosition.getOffsetInQuarters();		
		int count = 0;
		if (aGlobalStartPosition.getBarPosition() > aGlobalEndPosition.getBarPosition()) 
		{
			startPos += ts.getLengthInQuarters();
		}
		if (startPos < endPos)
		{
			for (Double d: ts.getSubSubTactusAsQuartersPositions())
			{
				if (d > startPos && d <= endPos) count++;
			}
		}
		else
		{
//			if (startPos == 0.0 && endPos > 0.0) startPos = ts.getLengthInQuarters();
			for (Double d: ts.getSubSubTactusAsQuartersPositions())
			{
				if (d < startPos && d >= endPos) count--;
			}
		}
		return count;
	}
	
	
	
	private static BarsAndBeats getPositionForSubSubTactusCalulation(
			BarsAndBeats aGlobalEndPosition,
			BarsAndBeats positionForSubTactusCalculation, 
			int subTactusOffset,
			Mu aMuFromTheNetwork
			)
	{
		BarsAndBeats positionForSubSubTactusCalculation = positionForSubTactusCalculation.getDeepCopy();;
		TimeSignature ts = aMuFromTheNetwork.getTimeSignature(aGlobalEndPosition.getBarPosition());
		
		if (subTactusOffset > 0)
		{
			positionForSubSubTactusCalculation = getPositionForSubSubTactusCalculationForEndGreaterThanBeginning
					(
							positionForSubTactusCalculation, 
							subTactusOffset, 
							ts
							);
		}
		else if (subTactusOffset < 0)
		{
			positionForSubSubTactusCalculation = getPositionForSubSubTactusCalculationForEndBeforeBeginning
					(
							positionForSubTactusCalculation, 
							subTactusOffset, 
							ts
							);
		}
		return positionForSubSubTactusCalculation;
	}
	
	
	
	private static BarsAndBeats getPositionForSubSubTactusCalculationForEndBeforeBeginning(
			BarsAndBeats positionForSubTactusCalculation, 
			int subTactusOffset, 
			TimeSignature ts
			)
	{
		Double[] tactii = ts.getSubTactusAsQuartersPositions();
		double position = 0.0;
		int count = 0;
		double positionInBar = positionForSubTactusCalculation.getOffsetInQuarters();
		int barIndex = positionForSubTactusCalculation.getBarPosition();
		if (positionInBar == 0.0)
		{
			positionInBar = ts.getLengthInQuarters(); 
			barIndex--;
		}
		for (int i = tactii.length - 1; i > -1; i--)
		{
			if (count == subTactusOffset)
			{
				return new BarsAndBeats(barIndex, position);
			}
			else
			{
				if (tactii[i] < positionInBar)
				{
					position = tactii[i];
					count--;
				}
			}
		}
		return positionForSubTactusCalculation;
	}



	private static BarsAndBeats getPositionForSubSubTactusCalculationForEndGreaterThanBeginning(
			BarsAndBeats positionForSubTactusCalculation, 
			int subTactusOffset, 
			TimeSignature ts
			)
	{
		Double[] tactii = ts.getSubTactusAsQuartersPositions();
		double position = 0.0;
		int count = 0;
		for (Double tactus: tactii)
		{
			if (count == subTactusOffset)
			{
				return new BarsAndBeats(positionForSubTactusCalculation.getBarPosition(), position);
			}
			else
			{
				if (tactus > positionForSubTactusCalculation.getOffsetInQuarters())
				{
					position = tactus;
					count++;
				}
			}
		}
		if (count == subTactusOffset)
		{
			return new BarsAndBeats(positionForSubTactusCalculation.getBarPosition(), position);
		}
		return positionForSubTactusCalculation;
	}
	
	
	
	private static int getRelativeSubTactusOffset(
			BarsAndBeats aGlobalStartPosition, 
			BarsAndBeats aGlobalEndPosition, 
			Mu aMuFromTheNetwork)
	// assume they are in the same bar
	{
		TimeSignature ts = aMuFromTheNetwork.getTimeSignatureFromGlobalBarPosition(aGlobalEndPosition.getBarPosition());
		double startPos = aGlobalStartPosition.getOffsetInQuarters();
		double endPos = aGlobalEndPosition.getOffsetInQuarters();		
		int count = 0;
		if (aGlobalStartPosition.getBarPosition() > aGlobalEndPosition.getBarPosition()) 
		{
			startPos += ts.getLengthInQuarters();
		}
		if (startPos < endPos)
		{
			for (Double d: ts.getSubTactusAsQuartersPositions())
			{
				if (d > startPos && d <= endPos) count++;
			}
		}
		else
		{
//			if (startPos == 0.0 && endPos > 0.0) startPos = ts.getLengthInQuarters();
			for (Double d: ts.getSubTactusAsQuartersPositions())
			{
				if (d < startPos && d >= endPos) count--;
			}
		}
		return count;
	}
	
	
	
	private static BarsAndBeats getPositionForSubTactusCalulation(
			BarsAndBeats aGlobalEndPosition,
			BarsAndBeats positionForTactusCalculation, 
			int tactusOffset,
			Mu aMuFromTheNetwork
			)
	{
		BarsAndBeats positionForSubTactusCalculation = positionForTactusCalculation.getDeepCopy();;
		TimeSignature ts = aMuFromTheNetwork.getTimeSignature(aGlobalEndPosition.getBarPosition());
		
		if (tactusOffset > 0)
		{
			positionForSubTactusCalculation = getPositionForSubTactusCalculationForEndGreaterThanBeginning
					(
							positionForTactusCalculation, 
							tactusOffset, 
							ts
							);
		}
		else if (tactusOffset < 0)
		{
			positionForSubTactusCalculation = getPositionForSubTactusCalculationForEndBeforeBeginning
					(
							positionForTactusCalculation, 
							tactusOffset, 
							ts
							);
		}
		return positionForSubTactusCalculation;
	}



	private static BarsAndBeats getPositionForSubTactusCalculationForEndBeforeBeginning(
			BarsAndBeats positionForTactusCalculation, int tactusOffset, TimeSignature ts)
	{
		Double[] tactii = ts.getTactusAsQuartersPositions();
		double position = 0.0;
		int count = 0;
		double positionInBar = positionForTactusCalculation.getOffsetInQuarters();
		int barIndex = positionForTactusCalculation.getBarPosition();
		if (positionInBar == 0.0)
		{
			positionInBar = ts.getLengthInQuarters(); 
			barIndex--;
		}
		for (int i = tactii.length - 1; i > -1; i--)
		{
			if (count == tactusOffset)
			{
				return new BarsAndBeats(barIndex, position);
			}
			else
			{
				if (tactii[i] < positionInBar)
				{
					position = tactii[i];
					count--;
				}
			}
		}
		return positionForTactusCalculation;
	}



	private static BarsAndBeats getPositionForSubTactusCalculationForEndGreaterThanBeginning(
			BarsAndBeats positionForTactusCalculation, int tactusOffset, TimeSignature ts)
	{
		Double[] tactii = ts.getTactusAsQuartersPositions();
		double position = 0.0;
		int count = 0;
		for (Double tactus: tactii)
		{
			if (count == tactusOffset)
			{
				return new BarsAndBeats(positionForTactusCalculation.getBarPosition(), position);
			}
			else
			{
				if (tactus > positionForTactusCalculation.getOffsetInQuarters())
				{
					position = tactus;
					count++;
				}
			}
		}
		if (count == tactusOffset)
		{
			return new BarsAndBeats(positionForTactusCalculation.getBarPosition(), position);
		}
		return positionForTactusCalculation;
	}
	
	
	
	private static int getRelativeTactusOffset(
			BarsAndBeats aGlobalStartPosition, 
			BarsAndBeats aGlobalEndPosition,
			Mu aMuFromTheNetwork
			)
	// assume they are in the same bar
	{
		TimeSignature ts = aMuFromTheNetwork.getTimeSignatureFromGlobalBarPosition(aGlobalEndPosition.getBarPosition());
		double startPos = aGlobalStartPosition.getOffsetInQuarters();
		if (aGlobalStartPosition.getBarPosition() > aGlobalEndPosition.getBarPosition()) 
		{
			startPos += ts.getLengthInQuarters();
		}
		double endPos = aGlobalEndPosition.getOffsetInQuarters();
		int count = 0;
		if (startPos < endPos)
		{
			for (Double d: ts.getTactusAsQuartersPositions())
			{
				if (d > startPos && d <= endPos) count++;
			}
		}
		else
		{
			for (Double d: ts.getTactusAsQuartersPositions())
			{
				if (d < startPos && d >= endPos) count--;
			}
		}
		return count;
	}
	
	
	
	private static BarsAndBeats getPositionForTactusCalculation(
			BarsAndBeats aGlobalEndPosition,
			BarsAndBeats positionForSuperTactusCalculation, 
			int superTactusOffset,
			Mu aMuFromTheNetwork
			)
	{
		BarsAndBeats positionForTactusCalculation = positionForSuperTactusCalculation.getDeepCopy();;
		TimeSignature ts = aMuFromTheNetwork.getTimeSignatureFromGlobalBarPosition(aGlobalEndPosition.getBarPosition());
		
		if (superTactusOffset > 0)
		{
			positionForTactusCalculation = getPositionForTactusCalculationForEndGreaterThanBeginning
					(
							positionForSuperTactusCalculation, 
							superTactusOffset,  
							ts
							);
		}
		else if (superTactusOffset < 0)
		{
			positionForTactusCalculation = getPositionForTactusCalculationForEndBeforeBeginning(
					positionForSuperTactusCalculation, 
					superTactusOffset,
					ts);
		}
		return positionForTactusCalculation;
	}
	
	
	
	private static BarsAndBeats getPositionForTactusCalculationForEndBeforeBeginning(
			BarsAndBeats positionForSuperTactusCalculation, 
			int superTactusOffset,
			TimeSignature ts)
	{
		Double[] superTactii = ts.getSuperTactusAsQuartersPositions();
		double position = 0.0;
		int count = 0;
		double positionInBar = positionForSuperTactusCalculation.getOffsetInQuarters();
		int barIndex = positionForSuperTactusCalculation.getBarPosition();
		if (positionInBar == 0.0)
		{
			positionInBar = ts.getLengthInQuarters(); 
			barIndex--;
		}
		for (int i = superTactii.length - 1; i > -1; i--)
		{
			if (count == superTactusOffset)
			{
				return new BarsAndBeats(barIndex, position);
			}
			else
			{
				if (superTactii[i] < positionInBar)
				{
					position = superTactii[i];
					count--;
				}	
			}
		}
		return positionForSuperTactusCalculation;
	}



	private static BarsAndBeats getPositionForTactusCalculationForEndGreaterThanBeginning(
			BarsAndBeats positionForSuperTactusCalculation, 
			int superTactusOffset,
			TimeSignature ts)
	{
		double position = 0.0;
		int count = 0;
		for (Double superTactus: ts.getSuperTactusAsQuartersPositions())
		{
			if (count == superTactusOffset)
			{
				return new BarsAndBeats(positionForSuperTactusCalculation.getBarPosition(), position);
			}
			else
			{
				if (superTactus > positionForSuperTactusCalculation.getOffsetInQuarters())
				{
					position = superTactus;
					count++;
				}	
			}
		}
		if (count == superTactusOffset)
		{
			return new BarsAndBeats(positionForSuperTactusCalculation.getBarPosition(), position);
		}
		return positionForSuperTactusCalculation;
	}
	
	
	
	private static int getRelativeSuperTactusOffset(
			BarsAndBeats aGlobalStartPosition, 
			BarsAndBeats aGlobalEndPosition,
			Mu aMuFromTheNetwork
			)
	{
		TimeSignature ts = aMuFromTheNetwork.getTimeSignatureFromGlobalBarPosition(aGlobalEndPosition.getBarPosition());

		if (aGlobalStartPosition.getBarPosition() == aGlobalEndPosition.getBarPosition())
		{
			return getRelativeSuperTactusCountInTheSameBar(aGlobalStartPosition, aGlobalEndPosition, ts);
		}
		else if (aGlobalStartPosition.getBarPosition() < aGlobalEndPosition.getBarPosition())
		{
			return countSuperTactusFromBeginningOfOtherBar(aGlobalEndPosition, ts);
		}
		else if (aGlobalStartPosition.getBarPosition() > aGlobalEndPosition.getBarPosition())
		{
			return countSuperTactusFromEndOfOtherBar(aGlobalEndPosition, ts);
		}
		return 0;
	}
	
	
	
	private static int countSuperTactusFromEndOfOtherBar(
			BarsAndBeats otherGlobalPositionInBarsAndBeats, 
			TimeSignature ts
			)
	{
		Double[] superTactii = ts.getSuperTactusAsQuartersPositions();
		int count = 0;
		for (int i = superTactii.length - 1; i >= 0; i--)
		{
			if (otherGlobalPositionInBarsAndBeats.getOffsetInQuarters() > superTactii[i])
			{
				break;
			}
			else
			{
				count--;
			}
		}
		return count;
	}



	private static int countSuperTactusFromBeginningOfOtherBar(
			BarsAndBeats otherGlobalPositionInBarsAndBeats,
			TimeSignature ts
			)
	{
		int count = -1;
		for (Double d: ts.getSuperTactusAsQuartersPositions())
		{
			if (otherGlobalPositionInBarsAndBeats.getOffsetInQuarters() >= d) count++; else break;
		}
		return count;
	}



	private static int getRelativeSuperTactusCountInTheSameBar(
			BarsAndBeats thisGlobalPositionInBarsAndBeats,
			BarsAndBeats otherGlobalPositionInBarsAndBeats, 
			TimeSignature ts
			)
	{
		int count = 0;
		int increment = 1;
		double hi;
		double low = thisGlobalPositionInBarsAndBeats.getOffsetInQuarters();
		if (otherGlobalPositionInBarsAndBeats.getOffsetInQuarters() < low)
		{
			low = otherGlobalPositionInBarsAndBeats.getOffsetInQuarters();
			hi = thisGlobalPositionInBarsAndBeats.getOffsetInQuarters();
			increment = -1;
		}
		else
		{
			hi = otherGlobalPositionInBarsAndBeats.getOffsetInQuarters();
		}
		for (Double d: ts.getSuperTactusAsQuartersPositions())
		{
			if (d > low && d <= hi) count += increment;
		}
		return count;
	}


	private static BarsAndBeats getPositionForSuperTactusCalculation(BarsAndBeats aGlobalStartPosition, int barOffset)
	{
		BarsAndBeats positionForSuperTactusCalculation;
		if (barOffset == 0)
		{
			positionForSuperTactusCalculation = aGlobalStartPosition.getDeepCopy();
		}
		else 
		{
			if (barOffset < 0 && aGlobalStartPosition.getOffsetInQuarters() > 0.0) barOffset++;
			positionForSuperTactusCalculation 
			= new BarsAndBeats(aGlobalStartPosition.getBarPosition() + barOffset, 0.0);
		}
		return positionForSuperTactusCalculation;
	}
	
	
	private static int getRelativeBarPosition(BarsAndBeats aGlobalStartPosition, BarsAndBeats aGlobalEndPosition)
	{
		if (
				aGlobalStartPosition.getBarPosition() == aGlobalEndPosition.getBarPosition()
//				&& aGlobalStartPosition.getOffsetInQuarters() > 0.0
				&& aGlobalEndPosition.getOffsetInQuarters() > 0.0
				) 
		{
			return 0;
		}
		if (aGlobalStartPosition.getBarPosition() < aGlobalEndPosition.getBarPosition())
		{
			return relativeBarPositionForOtherAfterThis(aGlobalStartPosition, aGlobalEndPosition);
		}
		else
		{
			return relativeBarPositionForOtherBeforeThis(aGlobalStartPosition, aGlobalEndPosition);
		}
	}
	


	private static int relativeBarPositionForOtherBeforeThis(
			BarsAndBeats aGlobalStartPosition, 
			BarsAndBeats aGlobalEndPosition
			)
	{
		int pos = aGlobalEndPosition.getBarPosition() - aGlobalStartPosition.getBarPosition();
		if (aGlobalStartPosition.getOffsetInQuarters() == 0.0) pos++;
		if (aGlobalEndPosition.getOffsetInQuarters() == 0.0) pos--;
		return pos;
	}



	private static int relativeBarPositionForOtherAfterThis(
			BarsAndBeats aGlobalStartPosition, 
			BarsAndBeats aGlobalEndPosition
			)
	{
		int pos = aGlobalEndPosition.getBarPosition() - aGlobalStartPosition.getBarPosition();
		return pos;
	}

}
