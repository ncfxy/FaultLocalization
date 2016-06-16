package com.ncfxy.FaultLocalization;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.ncfxy.FaultLocalization.data.SaveToJSON;

public class AllExperimentInfo {

	private String basePath = "C:/Users/Administrator/Desktop/邹雨果科研资料/matlab代码/MATLAB/Fault-Localization/data/";
	private String[] programNameList = { "gzip", "grep", "sed", "space", "totinfo", "printtokens2", "printtokens",
			"schedule", "schedule2", "tcas", "replace", "findSecond" };
	private String[] coverageFileName = { "coverage_matrix.txt", "coverage_matrix.txt", "coverage_matrix.txt", "output",
			"output", "output", "output", "output", "output", "output", "output", "coverage_matrix.txt" };

	public List<Integer> findProgramVersions(String programName) {
		File file = new File(basePath + programName);
		List versions = new ArrayList<Integer>();
		if (file.exists() && file.isDirectory()) {
			File[] versionFiles = file.listFiles();
			for (File versionFile : versionFiles) {
				if (versionFile.getName().matches("v[0-9]+") && versionFile.isDirectory()) {
					String versionNumber = versionFile.getName().substring(1);
					try {
						Integer versionInteger = Integer.parseInt(versionNumber);
						versions.add(versionInteger);
					} catch (NumberFormatException e) {

					}
				}
			}
		}
		Collections.sort(versions);
		// System.out.println(versions);
		return versions;
	}

	/**
	 * 运行所有的实验
	 */
	public void runExperiment() {

		List list = new ArrayList<>();// 4，10
		for (int i = 5; i <= 6/* programNameList.length */; i++) {
			ExecutorService pool = Executors.newFixedThreadPool(5);
			List<Integer> versions = findProgramVersions(programNameList[i]);
			List<DealWithOneExperiment> threadList = new ArrayList<DealWithOneExperiment>();
			for (int versionIndex = 0; versionIndex < versions.size(); versionIndex++) {
				// try {
				Map map = null;
				if (coverageFileName[i].equals("output")) {
					DealWithOneExperiment deal = new DealWithOneExperiment(basePath, programNameList[i],
							"v" + versions.get(versionIndex), "output" + versions.get(versionIndex) + ".txt", list);
					// map = deal.runOneExperiment();
//					 deal.start();
					pool.execute(deal);
					threadList.add(deal);
					 //deal.run();

				} else {
					DealWithOneExperiment deal = new DealWithOneExperiment(basePath, programNameList[i],
							"v" + versions.get(versionIndex), coverageFileName[i], list);
					// map = deal.runOneExperiment();
					// deal.start();
					pool.execute(deal);
					threadList.add(deal);
					 //deal.run();
				}
				if (map != null)
					list.add(map);
				// } catch (IOException e) {
				// e.printStackTrace();
				// try {
				// SaveToJSON.saveToJson(list);
				// SaveToJSON.convertFromJsonToCsv();
				// } catch (IOException e1) {
				// e1.printStackTrace();
				// }
				// }
			}
			pool.shutdown();
			try {
				pool.awaitTermination(10000, TimeUnit.DAYS);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int threadIndex = 0; threadIndex < threadList.size(); threadIndex++) {
				try {
					threadList.get(threadIndex).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			SaveToJSON.saveToJson(list);
			SaveToJSON.convertFromJsonToCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
