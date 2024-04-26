package com.borntogeek.excel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private Workbook workbook;
	private Sheet sheet;

	private ExcelReader(File excelFile) throws InvalidFormatException, IOException {
		workbook = new XSSFWorkbook(excelFile);
		sheet = workbook.getSheetAt(0);
	}

	public static void main(String[] args) throws InvalidFormatException, IOException {
		File excelFile = Paths.get("").resolve("excel.xlsx").toFile();
		ExcelReader reader = new ExcelReader(excelFile);
		reader.readFromExcelFile();
	}

	private void readFromExcelFile() throws IOException {
		for (Row row : sheet) {
			System.out.println();
			
			for (Cell cell : row) {
				printCellValue(cell);
				System.out.print("\t");
			}
		}
		
		workbook.close();
	}
	
	private void printCellValue(Cell cell) {
		 switch (cell.getCellType()) {
         case STRING:
         	System.out.print(cell.getStringCellValue());
         	break;
         case NUMERIC: 
         	if (DateUtil.isCellDateFormatted(cell)) {
         	 	System.out.print(new SimpleDateFormat("MM-dd-yyyy").format(cell.getDateCellValue()));
         	} else {
         	 	System.out.print((int) cell.getNumericCellValue());
         	}
           
         	break;
         default:
         	break;
     }
	}

}
