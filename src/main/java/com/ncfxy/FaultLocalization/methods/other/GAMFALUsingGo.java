package com.ncfxy.FaultLocalization.methods.other;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ncfxy.FaultLocalization.Experiment;

public class GAMFALUsingGo {

	public List<Integer> runGAMFALUsingGo(List<Integer> deeplyCompressRelation, List<Integer> OchiaiList,
			String filePath, Experiment experiment,int methodIndex) {
		String cmd = "cmd /c go run D:/Github/GA/src/main/main.go"
				+ " D:/Github/GA/src/main/heap.go"
				+ " D:/Github/GA/src/main/fitness.go"
				+ " -file=";
		cmd = cmd + filePath + " -ff=" + methodIndex;
		String res = ExecuteWindowsCommand.execute(cmd);
		// Pattern pattern = Pattern.compile("(?<=result=[)(0\\.[0-9]*)");
		Pattern pattern = Pattern.compile("(?<=result=\\[)([\\s,0-9]+)(?=\\])");

		Matcher matcher = pattern.matcher(res);
		List<Integer> result = new ArrayList<>();
		if (matcher.find()) {
			try {
				String sortedString = matcher.group();
				String[] sortedLineString = sortedString.split(" ");
				for (int i = 0; i < sortedLineString.length; i++) {
					result.add(Integer.parseInt(sortedLineString[i]));
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		List<Integer> newResult = new ArrayList<Integer>();
		for (Integer index : result) {
			newResult.add(deeplyCompressRelation.get(index));
		}
		for (Integer index : OchiaiList) {
			if (!newResult.contains(index)) {
				newResult.add(index);
			}
		}
		for (int i = 0; i < experiment.getIndexOfFaults().size(); i++) {
			if (!newResult.contains(experiment.getIndexOfFaults().get(i))) {
				System.out.println(filePath + "  This version is not satisfied");
			}
		}

		return newResult;
	}

}
