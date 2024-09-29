package SJF2;

import java.io.IOException;

import org.cloudbus.cloudsim.examples.power.random.RandomRunner;

public class SjfMain {

	public static void main(String[] args) throws IOException {
		boolean enableOutput = true;
		boolean outputToFile = false;
		String inputFolder = "";
		String outputFolder = "";
		String workload = "random"; // Random workload
		String vmAllocationPolicy = "iqr"; 
		String vmSelectionPolicy = "mc"; 
		String parameter = "1.5"; 

		new RandomRunner(
				enableOutput,
				outputToFile,
				inputFolder,
				outputFolder,
				workload,
				vmAllocationPolicy,
				vmSelectionPolicy,
				parameter);
	}

}
