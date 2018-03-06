
// learning2

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

public class Brain {

	int hiddenNodes = 5;
	BasicNetwork network;

	Brain() {

		network = new BasicNetwork();

		// input layer, has 10 nodes (9 for localmap, 1 node for fish direction.

		// so input layer is
		network.addLayer(new BasicLayer(null, false,10));
		// hidden layer
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, hiddenNodes));
		// output layer, has 3 outputs: direction, speed, state of fish
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
		// finalises the structure
		network.getStructure().finalizeStructure();
		// resets the weights to random values between -1 and 1
		network.reset();
	}
	
	Brain (Brain parent) {
		
		this.network = (BasicNetwork) parent.network.clone();
		
		for (int i = 0; i < hiddenNodes; i++) {
			
			double w = network.getWeight(1, i, 0);
			System.out.println(w);
			
			// adjusts the weight by 10% randomly.
			network.setWeight(1, i, 0, w + w*(0.1*((Math.random() * 2) - 1)));
			
		}
		
	}

}