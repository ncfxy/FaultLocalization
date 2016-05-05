package com.ncfxy.FaultLocalization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import com.ncfxy.FaultLocalization.methods.FaultLocalizationMethod;
import com.ncfxy.FaultLocalization.methods.Tarantula;

public class DealWithOneExperiment {

	private Experiment experiment;

	public void readFileInitializeExperiment(String basePath, String programName, String version) throws IOException {
		experiment = new Experiment(basePath, programName, version);
		experiment.readInfoFromFile();
		FaultLocalizationMethod method = new Tarantula();
		List<Double> list = method.getSuspicious(experiment);
		System.out.println(list.toString());
	}

}
