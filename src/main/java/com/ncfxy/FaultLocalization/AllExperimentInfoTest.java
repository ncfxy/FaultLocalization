package com.ncfxy.FaultLocalization;

import static org.junit.Assert.*;

import org.junit.Test;

public class AllExperimentInfoTest {

	@Test
	public void test() {
		AllExperimentInfo ex = new AllExperimentInfo();
		ex.findProgramVersions("gzip");
	}

}
