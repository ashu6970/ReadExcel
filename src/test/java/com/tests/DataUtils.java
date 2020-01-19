package com.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class DataUtils {

	static FileOutputStream out;
	static int rowNum=0;
	static XSSFWorkbook workbook;
	static XSSFSheet spreadsheet_1;
	
	/**
	 * Instantiates a new data utils.
	 * @throws FileNotFoundException 
	 */
	static {
		try {
			out = new FileOutputStream(new File("OutputData.xlsx"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		workbook = new XSSFWorkbook(); 
		//Create a blank sheet
		spreadsheet_1 = workbook.createSheet();
		//Create a blank sheet
		
	}

	/** The datafile. */
	static String datafile = "InvoiceData.xlsx";

	/** The testdatafilepath. */
	private static String testdatafilepath = System.getProperty("user.dir") + "\\" + datafile;

	/**
	 * Gets the test data.
	 *
	 * @param sheetname the sheetname
	 * @param id        the id
	 * @param field     the field
	 * @return the test data
	 */
	public static String getTestData(String sheetname, String id, String field) {
		String value = null;
		try {

			Fillo fillo = new Fillo();
			System.out.println("fillo filepath is:" + testdatafilepath);
			Connection connection = fillo.getConnection(testdatafilepath);
			String strQuery = "Select * from " + sheetname + " " + "where Id='" + id + "'";
			Recordset recordset = null;

			recordset = connection.executeQuery(strQuery);

			while (recordset.next()) {
				value = recordset.getField(field);
			}

			recordset.close();
			connection.close();

		}

		catch (Exception e) {
			e.getMessage();
			System.out.println("unable to read data from file"+e.getMessage());
		}
		return value;
	}

	public static void insertData(String sheetName, String columnName, String value) {
		try {
			Fillo fillo = new Fillo();
			Connection connection = fillo.getConnection(testdatafilepath);
			String strQuery = "INSERT INTO " + sheetName + "(" + columnName + ") VALUES(" + value + ");";
			System.out.println("Insert Data Query is " + strQuery);
			connection.executeUpdate(strQuery);

			connection.close();
		} catch (Exception e) {
			e.getMessage();
			System.out.println("unable to insert data in file "+e.getStackTrace());
			System.out.println("unable to insert data in file "+e.getMessage());
		}

	}


	public static void writeExcel(String s) throws Exception {
		rowNum++;
		ArrayList<String> values = new ArrayList<String>(Arrays.asList(s.split(";")));
		System.out.println("values.size="+values.size());
			XSSFRow row = spreadsheet_1.createRow(rowNum);
//			ArrayList<String> arrElement = new ArrayList<String>(Arrays.asList(arrRows.get(rowNum).split(",")));
			for(int j = 0; j < values.size(); j++) {
				XSSFCell cell = row.createCell(j);
				cell.setCellValue(values.get(j));
         
		}
		//Write the workbook in file system
		
		
	}
	
	public static void closeWorkbook() throws IOException {
		workbook.write(out);
		out.close();
		System.out.println("*.xlsx written successfully" );
	}

}
