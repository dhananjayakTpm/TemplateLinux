/*
 * ThinkPalm, Technologies Pvt Ltd ("COMPANY") CONFIDENTIAL
 * Copyright (c) 2019-2020 [ThinkPalm, Inc.], All Rights Reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

package com.test;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.reporters.JUnitXMLReporter;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * Main class used as an entry point in Dockerfile
 * 
 * @author nasia.t
 *
 */
public class AtoBeMainClass {
	
	private static final Logger LOGGER = LogManager.getLogger(AtoBeMainClass.class);

	public static void main(String[] args) {
		if (args.length != 0) {
			String argData = args[0];
			JUnitXMLReporter jux = new JUnitXMLReporter();
			XmlSuite suite = new XmlSuite();
			suite.setName("TestSuite");
			XmlTest test = new XmlTest(suite);
			test.setName("Automation Testing");
			List<XmlClass> classes = new ArrayList<>();
			switch (argData) {
			case "DRY_RUN":
				classes.add(new XmlClass("com.test.DryRun"));
				break;
			case "FULL_RUN":
				classes.add(new XmlClass("com.test.FullRun"));
				break;
			case "RUN":
				classes.add(new XmlClass("com.test.Run"));
				break;
			default:
				LOGGER.info("Wrong argument.");
			}
			test.setXmlClasses(classes);
			List<XmlSuite> suites = new ArrayList<>();
			suites.add(suite);
			TestNG tng = new TestNG();
			tng.addListener((ITestNGListener) jux);
			tng.setXmlSuites(suites);
			tng.run();
		} else {
			LOGGER.info("Please pass an argument to run a test.");
		}
	}
}
