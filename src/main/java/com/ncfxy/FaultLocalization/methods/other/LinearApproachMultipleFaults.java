package com.ncfxy.FaultLocalization.methods.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.ncfxy.FaultLocalization.Experiment;
import com.ncfxy.FaultLocalization.methods.statistics.StatisticsBase;

/**
 * This Method is proposed by A Linear Programming Approach for Automated
 * Localization of Multiple Faults
 * 
 * @author ncfxy
 *
 */
public class LinearApproachMultipleFaults extends StatisticsBase {

	private List<Integer> bestSolution = new ArrayList<Integer>();

	@Override
	public List<Double> getSuspicious(Experiment experiment) {
		init(experiment);
		List<Double> f = new ArrayList<Double>();
		List<Double> p = new ArrayList<Double>();
		for (int i = 0; i < failed.size(); i++) {
			Double tmp = failed.get(i) * 1.0 / totalFailed;
			f.add(tmp);
		}
		for (int i = 0; i < passed.size(); i++) {
			Double tmp = passed.get(i) * 1.0 / totalPassed;
			p.add(tmp);
		}
		Double A = 1.0;
		Double lamude = 1.0;
		while (cal(getParameters(f, p, lamude), experiment) <= lamude - A) {
			A = A * 1.1;
		}
		Double left = 0.0;
		Double right = A;
		while (right - left > 0.00001) {
			lamude = (left + right) / 2;
			Double tmp = cal(getParameters(f, p, lamude), experiment);
			// lamude is too large
			if (tmp <= lamude - A) {
				right = lamude;
			} else {
				left = lamude;
			}
		}
		System.out.println("Linear Approach : ");
		System.out.println("lamude = " + lamude);
		System.out.println("bestSolution : " + bestSolution.toString());
		return null;
	}

	/**
	 * 通过覆盖
	 */
	public String getCoverString(Experiment experiment, List<Integer> list) {
		String result = "";
		List<List<Byte>> failCoverageMatrix = experiment.getFailCoverageMatrix();
		for (List<Byte> failedCase : failCoverageMatrix) {
			boolean cover = false;
			for (int i = 0; i < list.size(); i++) {
				Integer tmp = list.get(i);
				if (failedCase.get(tmp).equals(new Byte("1"))) {
					cover = true;
				} else {
					cover = false;
				}

			}
			if(cover)result += "1";
			else result += "0";
		}
		return result;
	}

	private boolean coverAll(String coverString) {
		for (int i = 0; i < coverString.length(); i++) {
			if (coverString.charAt(i) == '0') {
				return false;
			}
		}
		return true;
	}

	private void copyList(List des, List src) {
		for (int i = 0; i < src.size(); i++) {
			des.add(src.get(i));
		}
	}

	private Double cal(List<Double> parameters, Experiment experiment) {
		Double EndResult = -999999999.0;
		class Temp {
			double value;
			List<Integer> list;
		}
		;
		Map<String, Temp> map = new TreeMap<String, Temp>();
		for (int i = 0; i < parameters.size(); i++) {
			Temp t = new Temp();
			t.list = new ArrayList<Integer>();
			t.list.add(new Integer(i));
			String coverString = getCoverString(experiment, t.list);
			Double result = 0.0;
			for (int j = 0; j < coverString.length(); j++) {
				if ((coverString.charAt(j)) == '1') {
					result += parameters.get(j);
				}
			}
			if (coverAll(coverString)) {
				if (result > EndResult) {
					EndResult = result;
					bestSolution = t.list;
				}
			}
			t.value = result;
			if (map.get(coverString) == null) {
				map.put(coverString, t);
			} else {
				Temp tmp = map.get(coverString);
				if (t.value > tmp.value) {
					map.remove(coverString);
					map.put(coverString, t);
				}
			}
		}

		// 第二次循环时不断加入元素
		for (int i = 1; i < parameters.size(); i++) {
			Map<String, Temp> newMap = new TreeMap<String, Temp>();
			for (Map.Entry<String, Temp> entries : map.entrySet()) {
				newMap.put(entries.getKey(), entries.getValue());
			}
			for (String key : map.keySet()) {
				for (int j = 0; j < parameters.size(); j++) {
					Temp t = new Temp();
					t.list = new ArrayList<Integer>();
					copyList(t.list, map.get(key).list);
					t.list.add(new Integer(j));
					String coverString = getCoverString(experiment, t.list);
					Double result = 0.0;
					for (int k = 0; k < coverString.length(); k++) {
						if ((coverString.charAt(k)) == '1') {
							result += parameters.get(j);
						}
					}
					if (coverAll(coverString)) {
						if (result > EndResult) {
							EndResult = result;
							bestSolution = t.list;
						}
					}
					t.value = result;
					if (map.get(coverString) == null) {
						newMap.put(coverString, t);
					} else {
						Temp tmp = map.get(coverString);
						if (t.value > tmp.value) {
							newMap.put(coverString, t);
						}
					}
				}
			}
			map = newMap;
		}
		return EndResult;
	}

	private List<Double> getParameters(List<Double> f, List<Double> p, Double lamude) {
		List<Double> result = new ArrayList<Double>();
		for (int i = 0; i < f.size(); i++) {
			result.add(f.get(i) - lamude * p.get(i) - lamude * f.get(i));
		}
		return result;
	}

}
