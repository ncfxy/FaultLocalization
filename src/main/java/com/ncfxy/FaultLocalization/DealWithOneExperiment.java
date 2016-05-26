package com.ncfxy.FaultLocalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.text.html.HTMLDocument.Iterator;

import com.ncfxy.FaultLocalization.methods.FaultLocalizationMethod;
import com.ncfxy.FaultLocalization.methods.statistics.Ochiai;
import com.ncfxy.FaultLocalization.methods.statistics.Tarantula;

public class DealWithOneExperiment {

	private Experiment experiment;

	/**
	 * 运行一次实验（每次实验中会使用不同的方法进行定位）
	 */
	public Map runOneExperiment(String basePath, String programName, String version) throws IOException {
		experiment = new Experiment(basePath, programName, version);
		experiment.readInfoFromFile();
		experiment.compressMatrix();
		FaultLocalizationMethod method = new Tarantula();
		List<Double> list = method.getSuspicious(experiment);
		System.out.println(list.toString());
		method = new Ochiai();
		List<Double> list1 = method.getSuspicious(experiment);
		System.out.println(list.toString());

		for (int i = 0; i < list.size(); i++) {
			System.out.println(i + "行 -----Tarantula: " + list.get(i) + ", Ochiai: " + list1.get(i));
		}
		// method = new LinearApproachMultipleFaults();
		// method.getSuspicious(experiment);
		Map map = new HashMap<>();
		
		List<Double> tarantulaResult = evaluateResult(list, experiment);
		map.put("Tarantula", tarantulaResult);
		List<Double> ochiaiResult = evaluateResult(list1, experiment);
		map.put("Ochiai", ochiaiResult);
		return map;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	private class Pair implements Map.Entry<Double, Integer>, Comparable<Pair> {
		Double key;
		Integer value;

		public Pair(Double key, Integer value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public Double getKey() {
			return key;
		}

		@Override
		public Integer getValue() {
			return value;
		}

		@Override
		public Integer setValue(Integer value) {
			return null;
		}

		@Override
		public int compareTo(Pair o) {
			if (this.key < o.getKey()) {
				return 1;
			} else if (this.key > o.getKey()) {
				return -1;
			}
			return 0;
		}

	}

	private List<Double> evaluateResult(List<Double> list, Experiment experiment) {
		List<Pair> newList = new ArrayList<>();
		int first = -1, last = -1;
		for (int i = 0; i < list.size(); i++) {
			newList.add(new Pair(list.get(i), i));
		}
		Collections.sort(newList);
		for (int i = 0; i < newList.size(); i++) {
			if (experiment.isFaultLine(newList.get(i).getValue())) {
				if (first == -1) {
					first = i + 1;
				}
				last = i + 1;
			}
		}
		System.out.println("The first index is " + first);
		System.out.println("The last index is " + last);
//		return first * 1.0 / experiment.getNumberOfExecutableEntities();
		List<Double> resultList = new ArrayList<Double>();
		resultList.add(first * 1.0 / experiment.getNumberOfExecutableEntities());
		resultList.add(last * 1.0 / experiment.getNumberOfExecutableEntities());
		return resultList;
	}

}
