package main.java.com.dougron.mucus_experiments.generator_poopline;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import main.java.com.dougron.mucus.algorithms.random_melody_generator.Parameter;

class ParameterRepository_Tests {

	@Test
	void instantiates() {
		ParameterRepository repo = new ParameterRepository();
	}
	
	
	@Test
	void can_put_item_in_map() throws Exception {
		ParameterRepository repo = new ParameterRepository();
		repo.put(Parameter.PHRASE_LENGTH, null);
		assertThat(repo.size()).isEqualTo(1);
	}
	
	
	@Test
	void can_get_from_map() throws Exception {
		ParameterRepository repo = new ParameterRepository();
		repo.put(Parameter.PHRASE_LENGTH, null);
		assertThat(repo.get(Parameter.PHRASE_LENGTH)).isEqualTo(null);
	}

}
