package com.ncfxy.FaultLocalization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Experiment {

	private String basePath;
	private String programName;
	private String version; // v1、v2、v3
	private String coverageFileName = "coverage_matrix.txt";

	private String versionName;
	private Integer numberOfTestCases;// NOTS;
	private List<Integer> locationsOfExecutableEntities;// LOES
	private Integer numberOfExecutableEntities;// NOES;
	private Integer numberOfFaults; // NOF_
	private List<Integer> locationsOfFaults; // LOFS
	private List<List<Byte>> coverageMatrix;
	private List<List<Byte>> passCoverageMatrix;
	private List<List<Byte>> failCoverageMatrix;
	private List<Boolean> resultVector;

	public Experiment(String basePath, String programName, String version) {
		super();
		this.basePath = basePath;
		this.programName = programName;
		this.version = version;
	}

	public void readInfoFromFile() throws FileNotFoundException {
		File coverageFile = new File(basePath + "/" + programName + "/" + version + "/" + coverageFileName);
		FileInputStream fileInputStream = new FileInputStream(coverageFile);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
		Scanner cin = new Scanner(fileInputStream);
		List<String> lines = new ArrayList<String>();
		while (cin.hasNext()) {
			String tmp = cin.nextLine();
			lines.add(tmp);
		}
		versionName = lines.get(0).substring(7);
		numberOfTestCases = Integer.parseInt(lines.get(1).substring(7));
		String tmp = lines.get(2).substring(7);
		String tmps[] = tmp.split(" ");
		locationsOfExecutableEntities = new ArrayList<Integer>();
		for (String s : tmps) {
			locationsOfExecutableEntities.add(Integer.parseInt(s));
		}
		numberOfExecutableEntities = Integer.parseInt(lines.get(3).substring(7));
		numberOfFaults = Integer.parseInt(lines.get(4).substring(7));
		tmp = lines.get(5).substring(7);
		String tmps2[] = tmp.split(" ");
		locationsOfFaults = new ArrayList<Integer>();
		for (String s : tmps2) {
			locationsOfFaults.add(Integer.parseInt(s));
		}
		coverageMatrix = new ArrayList<List<Byte>>();
		passCoverageMatrix = new ArrayList<List<Byte>>();
		failCoverageMatrix = new ArrayList<List<Byte>>();
		resultVector = new ArrayList<Boolean>();
		for (int i = 0; i < numberOfTestCases; i++) {
			tmp = lines.get(6 + i);
			if (tmp.substring(13, 14).equals("0")) {
				resultVector.add(true);
				String a[] = tmp.substring(16).split(" ");
				ArrayList<Byte> listTmp = new ArrayList<Byte>();
				for (String s : a) {
					listTmp.add(new Byte(s));
				}
				coverageMatrix.add(listTmp);
				passCoverageMatrix.add(listTmp);
			} else {
				resultVector.add(false);
				String a[] = tmp.substring(16).split(" ");
				ArrayList<Byte> listTmp = new ArrayList<Byte>();
				for (String s : a) {
					listTmp.add(new Byte(s));
				}
				coverageMatrix.add(listTmp);
				failCoverageMatrix.add(listTmp);
			}
		}
		//System.out.println(coverageMatrix.toString());
	}

	public String getVersion() {
		return version;
	}

	public String getVersionName() {
		return versionName;
	}

	public Integer getNumberOfTestCases() {
		return numberOfTestCases;
	}

	public List<Integer> getLocationsOfExecutableEntities() {
		return locationsOfExecutableEntities;
	}

	public Integer getNumberOfExecutableEntities() {
		return numberOfExecutableEntities;
	}

	public Integer getNumberOfFaults() {
		return numberOfFaults;
	}

	public List<Integer> getLocationsOfFaults() {
		return locationsOfFaults;
	}

	public List<List<Byte>> getCoverageMatrix() {
		return coverageMatrix;
	}

	public List<List<Byte>> getPassCoverageMatrix() {
		return passCoverageMatrix;
	}

	public List<List<Byte>> getFailCoverageMatrix() {
		return failCoverageMatrix;
	}

	public List<Boolean> getResultVector() {
		return resultVector;
	}

}
