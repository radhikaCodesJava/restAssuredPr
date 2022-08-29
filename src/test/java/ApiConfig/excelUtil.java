package ApiConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excelUtil {
		
		public static FileInputStream fi;
		
		public static XSSFWorkbook wb;
		
		public static XSSFSheet ws;
		
		public static XSSFRow row;
		
		public static XSSFCell cell;
		
		public static FileOutputStream fo;
		
		
		
		public static int getRowCount(String XLFile, String XLSheetName) throws IOException
		{
			fi=new FileInputStream(XLFile);
			wb=new XSSFWorkbook(fi);
			ws=wb.getSheet(XLSheetName);
			int RowCount=ws.getLastRowNum();
			wb.close();
			fi.close();
			return RowCount;
		}
		
		public static int getCellCount(String XLFile, String XLSheetName, int rownum) throws IOException
		{
			fi=new FileInputStream(XLFile);
			wb=new XSSFWorkbook(fi);
			ws=wb.getSheet(XLSheetName);
			row=ws.getRow(rownum);
			int cellCount=row.getLastCellNum();
			wb.close();
			fi.close();
			return cellCount;
		}
		
		
		public static String GetCellData(String XLFile, String XLSheetName, int rownum, int cellnum) throws IOException
		{
			fi=new FileInputStream(XLFile);
			wb=new XSSFWorkbook(fi);
			ws=wb.getSheet(XLSheetName);
		    row=ws.getRow(rownum);
			cell=row.getCell(cellnum);
			String data;
			try {
				DataFormatter formatter=new DataFormatter();
				String cellData=formatter.formatCellValue(cell);
				return cellData;
			} 
			catch (Exception e)
			{
				data="";
			}
			wb.close();
			fi.close();
			return data;
		}
		
		public static void SetCellData(String XLFile, String XLSheetName, int rownum, int cellnum, String data) throws IOException
		{			
			fi=new FileInputStream(XLFile);
			wb=new XSSFWorkbook(fi);
			ws=wb.getSheet(XLSheetName);
		    //row=ws.getRow(rownum);
			row = ws.getRow(rownum++);
		    //cell = row.createCell(colNum++);
			cell=row.createCell(cellnum++);
			cell.setCellValue(data);
			FileOutputStream fo=new FileOutputStream(XLFile);
			wb.write(fo);
			wb.close();
			fi.close();
			fo.close();
		}

}
