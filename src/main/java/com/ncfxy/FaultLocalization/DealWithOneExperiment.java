package com.ncfxy.FaultLocalization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import com.ncfxy.FaultLocalization.methods.FaultLocalizationMethod;
import com.ncfxy.FaultLocalization.methods.statistics.Ochiai;
import com.ncfxy.FaultLocalization.methods.statistics.Tarantula;

public class DealWithOneExperiment {

	private Experiment experiment;

	public void readFileInitializeExperiment(String basePath, String programName, String version) throws IOException {
		experiment = new Experiment(basePath, programName, version);
		experiment.readInfoFromFile();
		FaultLocalizationMethod method = new Tarantula();
		List<Double> list = method.getSuspicious(experiment);
		System.out.println(list.toString());
		method = new Ochiai();
		List<Double> list1 = method.getSuspicious(experiment);
		System.out.println(list.toString());
		for (int i = 0;i < list.size();i++){
			System.out.println(list.get(i)+ ", " + list1.get(i));
		}
	}

}
