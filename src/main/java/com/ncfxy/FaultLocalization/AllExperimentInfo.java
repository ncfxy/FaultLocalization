package com.ncfxy.FaultLocalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ncfxy.FaultLocalization.data.SaveToJSON;

public class AllExperimentInfo {
	
	private String  basePath = "C:/Users/Administrator/Desktop/邹雨果科研资料/matlab代码/MATLAB/Fault-Localization/data/";
	private String[] programNameList = {"gzip"};
	private int[] programNumber = {45};
	
	public void runExperiment(){
		DealWithOneExperiment deal = new DealWithOneExperiment();
		List list = new ArrayList<>();
		for(int i = 0;i < programNameList.length;i++){
			for(int j = 1;j <= programNumber[i];j++){
				try {
					Map map = deal.runOneExperiment(basePath, programNameList[i], "v"+j);
					list.add(map);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			SaveToJSON.saveToJson(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
