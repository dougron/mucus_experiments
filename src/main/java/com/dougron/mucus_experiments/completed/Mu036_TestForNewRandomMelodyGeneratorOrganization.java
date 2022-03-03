package main.java.com.dougron.mucus_experiments.completed;

import java.util.Random;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMG_002;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RMRandomNumberContainer;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyGenerator;
import main.java.com.dougron.mucus.algorithms.random_melody_generator.RandomMelodyParameterObject;

/*
 * this is to test the software architecture for RandomMelodyGenerator, with RandomMelodyGenerator becoming abstract
 * and subclasses becoming singletons
 * 
 */


public class Mu036_TestForNewRandomMelodyGeneratorOrganization
{
	
	
	
	public Mu036_TestForNewRandomMelodyGeneratorOrganization ()
	{
//		testOfGneralAbstractClassAndSingletonSubclass();
		RandomMelodyGenerator rmg = RMG_002.getInstance();
		for (int i: rmg.getPhraseLengthOptions())
		{
			System.out.println(i);
		}
		Random rnd = new Random();
		RMRandomNumberContainer rndContainer = rmg.getRandomNumberContainer(rnd);
		RandomMelodyParameterObject po = rmg.getParameterObject(rndContainer, rnd);
		System.out.println(po.toString());
	}



	public void testOfGneralAbstractClassAndSingletonSubclass ()
	{
		Abstraction imp = Implementation.getInstance();
		Abstraction imp2 = Implementation2.getInstance();
		System.out.println(imp.getValue());
		System.out.println(imp2.getValue());
		imp.process();
		System.out.println(imp.getValue());
		System.out.println(imp2.getValue());
		imp.setValue(1001);
		System.out.println(imp.getValue());
		System.out.println(imp2.getValue());
	}
	
	

	public static void main (String[] args)
	{
		new Mu036_TestForNewRandomMelodyGeneratorOrganization();

	}

}


class Implementation extends Abstraction
{
	
	
	private static Abstraction instance;


	private Implementation ()
	{
		setValue(101);
	}
	
	
	public static Abstraction getInstance()
	{
		if (instance == null) instance = new Implementation();
		return instance;
	}
}



class Implementation2 extends Abstraction
{
	
	
	private static Abstraction instance;


	private Implementation2 ()
	{
		setValue(202);
	}
	
	
	public static Abstraction getInstance()
	{
		if (instance == null) instance = new Implementation2();
		return instance;
	}
}



abstract class Abstraction
{
	
	private int value;
	
	public void process()
	{
		value += 1;
	}

	public int getValue ()
	{
		return value;
	}

	public void setValue (int value)
	{
		this.value = value;
	}
}