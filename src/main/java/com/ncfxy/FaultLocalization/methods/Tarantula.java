package com.ncfxy.FaultLocalization.methods;

import java.util.ArrayList;
import java.util.List;

import com.ncfxy.FaultLocalization.Experiment;

public class Tarantula implements FaultLocalizationMethod {

	public List<Double> getSuspicious(Experiment experiment) {
		// TODO Auto-generated method stub
		int totalFailed = experiment.getFailCoverageMatrix().size();
		int totalPassed = experiment.getPassCoverageMatrix().size();
		List<Integer> failed = new ArrayList<Integer>();
		List<Integer> passed = new ArrayList<Integer>();
		List<Double> result = new ArrayList<Double>();
		for (int i = 0; i < experiment.getNumberOfExecutableEntities(); i++) {
			failed.add(new Integer(0));
			passed.add(new Integer(0));
		}
		for (int i = 0; i < experiment.getFailCoverageMatrix().size(); i++) {
			List<Byte> list = experiment.getFailCoverageMatrix().get(i);
			for (int j = 0; j < list.size(); j++) {
				failed.set(j, failed.get(j) + list.get(j));
			}
		}
		for (int i = 0; i < experiment.getPassCoverageMatrix().size(); i++) {
			List<Byte> list = experiment.getPassCoverageMatrix().get(i);
			for (int j = 0; j < list.size(); j++) {
				passed.set(j, passed.get(j) + list.get(j));
			}
		}
		for (int i = 0; i < failed.size(); i++) {
			Double haha = (failed.get(i) * 1.0 / totalFailed)
					/ ((passed.get(i) * 1.0 / totalPassed) + (failed.get(i) / totalFailed));
			if(haha.isNaN() || haha.isInfinite()){
				haha = 0.0;
			}
			result.add(haha);
		}

		return result;
	}

}
