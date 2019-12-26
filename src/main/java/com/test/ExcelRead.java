/*
 * ThinkPalm, Technologies Pvt Ltd ("COMPANY") CONFIDENTIAL
 * Copyright (c) 2019-2020 [ThinkPalm, Inc.], All Rights Reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * To read mail credentials from the excel file
 * 
 * @author nasia.t
 *
 */
public class ExcelRead {
	
	private static final Logger LOGGER = LogManager.getLogger(ExcelRead.class);
	private FileInputStream fis = null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private String xlFilePath;
	DataFormatter formatter = new DataFormatter();

	public ExcelRead() {
		this.xlFilePath = "MailCredentials.xlsx";
		try {
			fis = new FileInputStream(new File(xlFilePath));
		} catch (FileNotFoundException e) {
			LOGGER.info(e.getMessage());
		}
		try {
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet("Credentials");
			fis.close();
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		}
	}

	public String getUserName() {
		for (Row singleRow : sheet) {
			for (Cell singleCell : singleRow) {
				String text = formatter.formatCellValue(singleCell);
				if (singleCell.getColumnIndex() == 0 && !text.equalsIgnoreCase("username")) {
					break;
				}
				if (singleCell.getColumnIndex() == 1) {
					return text;
				}
			}
		}
		return null;
	}

	public String getPassword() {
		for (Row singleRow : sheet) {
			for (Cell singleCell : singleRow) {
				String text = formatter.formatCellValue(singleCell);
				if (singleCell.getColumnIndex() == 0 && !text.equalsIgnoreCase("password")) {
					break;
				}
				if (singleCell.getColumnIndex() == 1) {
					return text;
				}
			}
		}
		return null;
	}
}
