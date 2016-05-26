package com.ncfxy.FaultLocalization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CompressMatrix {
	
	public static void main(String[] args) throws IOException {
		DealWithOneExperiment deal = new DealWithOneExperiment();
		String basePath = "C:/Users/Administrator/Desktop/邹雨果科研资料/matlab代码/MATLAB/Fault-Localization/data/";
		String programName = "findSecond";
		String version = "v6";
		for(int i = 102;i <= 102;i++){
			programName = "printtokens";
			version = "v" + i;
			Experiment experiment = new Experiment(basePath, programName, version);
			experiment.readInfoFromFile();
			experiment.compressMatrix();
			outputFile(basePath, programName, version, experiment.toString());
			System.out.println(programName + version + ": finished");
		}
		
		
		
	}
	
	public static void outputFile(String basePath, String programName, String version, String outputString) throws IOException{
		File outputFile = new File(basePath + "/" + programName + "/" + version + "/" + "compressed_coverage_matrix.txt");
		OutputStream output = new FileOutputStream(outputFile);
		output.write(outputString.getBytes());
	}
	

}
