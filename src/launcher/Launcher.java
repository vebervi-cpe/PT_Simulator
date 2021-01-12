package launcher;

import model.Simulator;

public class Launcher {

	public static void main(String[] args) {
		String serverDBSimulator = "https://nassim-k.fr/apiv2";
		String serverDBEmergency = "https://nassim-k.fr/api";

		Simulator simulator = new Simulator(serverDBSimulator, serverDBEmergency);
		simulator.run();
	}

}
