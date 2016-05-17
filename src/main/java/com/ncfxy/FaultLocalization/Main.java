package com.ncfxy.FaultLocalization;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		DealWithOneExperiment deal = new DealWithOneExperiment();
		String basePath = "C:/Users/Administrator/Desktop/邹雨果科研资料/matlab代码/MATLAB/Fault-Localization/data/";
		deal.readFileInitializeExperiment(basePath, "findSecond", "v6");
		
	}

}
