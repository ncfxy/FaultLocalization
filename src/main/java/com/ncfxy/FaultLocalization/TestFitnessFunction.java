package com.ncfxy.FaultLocalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.ncfxy.FaultLocalization.methods.FaultLocalizationMethod;
import com.ncfxy.FaultLocalization.methods.other.GAMFALUsingGo;
import com.ncfxy.FaultLocalization.methods.statistics.OP2;
import com.ncfxy.FaultLocalization.methods.statistics.Ochiai;
import com.ncfxy.FaultLocalization.methods.statistics.Tarantula;

public class TestFitnessFunction extends DealWithOneExperiment {

	public TestFitnessFunction(String basePath, String programName, String version, String coverageFileName,
			List list) {
		super(basePath, programName, version, coverageFileName, list);
	}

	public void haha() throws IOException {
		System.out.println("This is the test " + index++);
		experiment = new Experiment(basePath, programName, version, coverageFileName);
		// experiment.readInfoFromFile();
		experiment.readFromCompressedFile();
		// experiment.compressMatrix();
		// experiment.outputFaultsCoverage("file" + index + ".txt");
		// boolean t = true;
		// if(t)return null;
		// if (!experiment.isMultipleFaults()) {
		// return;
		// }
		suspiciousList = new Ochiai().getSuspicious(experiment);
		System.out.println(suspiciousList);
		generateCandidate();
		return;
	}

	private List<Double> suspiciousList = null;

	public static void main(String[] args) {

		TestFitnessFunction t = new TestFitnessFunction(
				"C:/Users/Administrator/Desktop/邹雨果科研资料/matlab代码/MATLAB/Fault-Localization/data/", "printtokens", "v202",
				"coverage_matrix.txt", new ArrayList<>());
		t.testPrio();
		try {
			t.haha();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void testPrio() {
		PriorityQueue<Temp> prio = new PriorityQueue<Temp>();
		List<Integer> list = new ArrayList<Integer>();
		prio.add(new Temp(0.4, list));
		prio.add(new Temp(9.0, list));
		prio.add(new Temp(5.5, list));
		prio.add(new Temp(0.8, list));
		while (!prio.isEmpty()) {
			System.out.println(prio.poll().toString());
		}
	}

	public void generateCandidate() {
		int total = experiment.getNumberOfExecutableEntities() - 1;
		System.out.println("total = " + total);
		PriorityQueue<Temp> prio = new PriorityQueue<Temp>();
		// int total = 1000;
		for (int i = 1; i <= total; i++) {
			List list = new ArrayList<Integer>();
			list.add(i - 1);
			AddToPrioQueue(list, prio);
		}
		for (int i = 1; i <= total - 1; i++) {
			for (int j = i + 1; j <= total; j++) {
				List list = new ArrayList<Integer>();
				list.add(i - 1);
				list.add(j - 1);
				AddToPrioQueue(list, prio);
			}
		}
		for (int i = 1; i <= total - 2; i++) {
			for (int j = i + 1; j <= total - 1; j++) {
				for (int k = j + 1; k <= total; k++) {
					List list = new ArrayList<Integer>();
					list.add(i - 1);
					list.add(j - 1);
					list.add(k - 1);
					AddToPrioQueue(list, prio);
				}
			}
			System.out.println(i);
		}

		while (!prio.isEmpty()) {
			System.out.println(prio.poll().toString());
		}
		
		
		return;
	}

	public void AddToPrioQueue(List list, PriorityQueue<Temp> prio) {
		Temp t = new Temp(calculate(list), list);
		prio.add(t);
		if (prio.size() > 1000) {
			prio.poll();
		}
	}

	public double calculate(List<Integer> list) {
		List<List<Byte>> coverageMatrix = experiment.getCoverageMatrix();
		List<List<Byte>> passedMatrix = experiment.getPassCoverageMatrix();
		List<List<Byte>> failedMatrix = experiment.getFailCoverageMatrix();
		double numOfOnesFromPassCases = 0;
		double numOfOnesFromFailedCases = 0;
		int coverFailedCases = 0;
		for (int i = 0; i < passedMatrix.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (passedMatrix.get(i).get(list.get(j)) == 1) {
					numOfOnesFromPassCases++;// += suspiciousList.get(list.get(j));
				}
			}
		}
		for (int i = 0; i < failedMatrix.size(); i++) {
			Boolean cover = false;
			for (int j = 0; j < list.size(); j++) {
				if (failedMatrix.get(i).get(list.get(j)) == 1) {
					cover = true;
					numOfOnesFromFailedCases++;// += suspiciousList.get(list.get(j));
				}
			}
			if (cover)
				coverFailedCases++;
		}
		if (numOfOnesFromFailedCases == 0) {
			return 0.0;
		}
		if (coverFailedCases < failedMatrix.size()) {
			return 0.0;
		}
		numOfOnesFromFailedCases = list.size() * failedMatrix.size() - numOfOnesFromFailedCases;
		Double result = numOfOnesFromFailedCases
				/ Math.sqrt(failedMatrix.size() * (numOfOnesFromFailedCases + numOfOnesFromPassCases));
		result *= Math.pow(0.01, list.size() * 1.0 / 15);
		// result *= Math.log(list.size() * 1.0 / 20) / Math.log(0.02);
		if (Double.isNaN(result))
			return 0.0;
		return result;
	}

	class Temp implements Comparable<Temp> {

		Double value;
		List<Integer> location;

		public Temp(Double value, List<Integer> location) {
			super();
			this.value = value;
			this.location = location;
		}

		@Override
		public int compareTo(Temp o) {
			if (this.value < o.value)
				return -1;
			else if (this.value > o.value)
				return 1;
			return 0;
		}

		@Override
		public String toString() {
			return "{\"value\":\"" + value + "\", \"location\":\"" + location + "\"}";
		}

	}

}
