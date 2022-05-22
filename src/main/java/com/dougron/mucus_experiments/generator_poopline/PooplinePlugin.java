package main.java.com.dougron.mucus_experiments.generator_poopline;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;

public interface PooplinePlugin {
	
	public PooplinePackage process(PooplinePackage aPackage);
	
	public void setParent(Poopline aPoopline);

	public boolean isValidated();
	
//	public boolean canSupplyParameter(Parameter aParameter);
	
	public Parameter getRenderParameter();
	public Parameter[] getRequiredParameters();
	public void setExecutedThisCycle(boolean bool);
	public boolean isExecutedThisCycle();
}
