package launcher;

import model.Simulator;

public class Launcher {

	public static void main(String[] args) {
		String endpointDBSimulator = "TODO";
		String endpointDBEmergency = "TODO";

		Simulator simulator = new Simulator(endpointDBSimulator, endpointDBEmergency);
		simulator.run();
	}

}
