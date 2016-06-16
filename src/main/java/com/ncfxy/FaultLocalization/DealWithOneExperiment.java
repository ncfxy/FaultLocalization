package com.ncfxy.FaultLocalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.text.html.HTMLDocument.Iterator;

import com.ncfxy.FaultLocalization.methods.FaultLocalizationMethod;
import com.ncfxy.FaultLocalization.methods.other.GAMFALUsingGo;
import com.ncfxy.FaultLocalization.methods.other.LinearApproachMultipleFaults;
import com.ncfxy.FaultLocalization.methods.statistics.OP2;
import com.ncfxy.FaultLocalization.methods.statistics.Ochiai;
import com.ncfxy.FaultLocalization.methods.statistics.Tarantula;

public class DealWithOneExperiment extends Thread {
	protected static int index = 1;

	protected Experiment experiment;

	protected String basePath;
	protected String programName;
	protected String version;
	protected String coverageFileName;
	protected List list;

	public DealWithOneExperiment(String basePath, String programName, String version, String coverageFileName,
			List list) {
		super();
		this.basePath = basePath;
		this.programName = programName;
		this.version = version;
		this.coverageFileName = coverageFileName;
		this.list = list;
	}

	@Override
	public void run() {
		Map map = null;
		try {
			map = runOneExperiment();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (map == null) {
			return;
		}
		synchronized (list) {
			list.add(map);
		}

	}

	/**
	 * 运行一次实验（每次实验中会使用不同的方法进行定位）
	 */
	public Map runOneExperiment() throws IOException {
		System.out.println("This is the test " + index++);
		Experiment deepExperiment = new Experiment(basePath, programName, version, coverageFileName);
		experiment = new Experiment(basePath, programName, version, coverageFileName);
		// experiment.readInfoFromFile();
		deepExperiment.readFromCompressedFile();
		experiment.readFromCompressedFile();
		if (experiment.getFailCoverageMatrix().size() == 0)
			System.out.println(programName + "  " + version + "  don't have failed test cases");
		// experiment.compressMatrix();
		// experiment.outputFaultsCoverage("file" + index + ".txt");
		// boolean t = true;
		// if(t)return null;
		if (!experiment.isMultipleFaults()) {
			return null;
		}
		FaultLocalizationMethod method = new Tarantula();
		List<Integer> list = method.getResult(experiment);
		System.out.println(list.toString());
		method = new Ochiai();
		List<Integer> list1 = method.getResult(experiment);
		method = new OP2();
		List<Integer> list2 = method.getResult(experiment);

//		Calendar cal = Calendar.getInstance();
//		cal.setTime(new Date());
//		System.out.println(cal.getTimeInMillis());
//		LinearApproachMultipleFaults method1 = new LinearApproachMultipleFaults();
//		method1.getResult(experiment, list1);
//		cal.setTime(new Date());
//		System.out.println(cal.getTimeInMillis());
		
		Double a = 0.0, b = 0.0;
		List<Double> gamfalResultList;
		deepExperiment.deeplyCompressMatrix();
		List<Integer> deeplyCompressRealtion = deepExperiment.getDeeplyCompressRelation();
		int times = 1;
		for (int i = 0; i < times; i++) {
			GAMFALUsingGo gamfal = new GAMFALUsingGo();
			List<Integer> gamfalResult = gamfal.runGAMFALUsingGo(deeplyCompressRealtion, list1,
					basePath + "/" + programName + "/" + version + "/deeply_compressed_coverage_matrix.txt",experiment,1);
			System.out.println("our gamfal result is :");
			System.out.println(gamfalResult.toString());
			gamfalResultList = evaluateResult(gamfalResult, experiment);
			a += gamfalResultList.get(0);
			b += gamfalResultList.get(1);
		}
		gamfalResultList = new ArrayList<>();
		gamfalResultList.add(a / times);
		gamfalResultList.add(b / times);
		
		a = b = 0.0;
		List<Double> gamfalLinerResultList;
		for (int i = 0; i < times; i++) {
			GAMFALUsingGo gamfal = new GAMFALUsingGo();
			List<Integer> gamfalResult = gamfal.runGAMFALUsingGo(deeplyCompressRealtion, list1,
					basePath + "/" + programName + "/" + version + "/deeply_compressed_coverage_matrix.txt",experiment,2);
			System.out.println("our gamfal result is :");
			System.out.println(gamfalResult.toString());
			gamfalLinerResultList = evaluateResult(gamfalResult, experiment);
			a += gamfalLinerResultList.get(0);
			b += gamfalLinerResultList.get(1);
		}
		gamfalLinerResultList = new ArrayList<>();
		gamfalLinerResultList.add(a / times);
		gamfalLinerResultList.add(b / times);

		Map map = new HashMap<>();

		List<Double> tarantulaResult = evaluateResult(list, experiment);
		map.put("Tarantula", tarantulaResult);
		List<Double> ochiaiResult = evaluateResult(list1, experiment);
		map.put("Ochiai", ochiaiResult);
		List<Double> op2Result = evaluateResult(list2, experiment);
		map.put("OP2", op2Result);

		map.put("GAMFal", gamfalResultList);
		map.put("GAMFalLinear", gamfalLinerResultList);
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

	private List<Double> evaluateResult(List<Integer> sortResult, Experiment experiment) {
		int first = -1, last = -1;
		for (int i = 0; i < sortResult.size(); i++) {
			if (experiment.isFaultLine(sortResult.get(i))) {
				if (first == -1) {
					first = i + 1;
				}
				last = i + 1;
			}
		}
		// System.out.println("The first index is " + first);
		// System.out.println("The last index is " + last);
		// return first * 1.0 / experiment.getNumberOfExecutableEntities();
		List<Double> resultList = new ArrayList<Double>();
		resultList.add(first * 1.0 / experiment.getNumberOfExecutableEntities());
		resultList.add(last * 1.0 / experiment.getNumberOfExecutableEntities());
		return resultList;
	}

}
