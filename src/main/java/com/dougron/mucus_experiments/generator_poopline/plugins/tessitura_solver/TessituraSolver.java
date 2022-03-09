package main.java.com.dougron.mucus_experiments.generator_poopline.plugins.tessitura_solver;

import java.util.List;

import main.java.com.dougron.mucus.mu_framework.Mu;
import main.java.com.dougron.mucus_experiments.generator_poopline.plugins.plugin_repos.TessituraRepo;

public interface TessituraSolver {

	public List<Mu> solveForTessitura(List<Mu> muList, TessituraRepo repo);
}
