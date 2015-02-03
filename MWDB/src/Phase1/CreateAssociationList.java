/**
 * @author Sumanth
 *
 */

// Create a list of oneHop neighbors reading the Location Matrix

/**
 *  The LocationMatrix.xlsx file has been changed from .xlsx to 
 *  .xls(Excel 97-2003) format so that it is compatible with the JExcel API. 
 * 
 */
package Phase1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class CreateAssociationList 
{
	public static Map<String,List<String>> getAssociationMap()  throws BiffException, IOException
	{
		String filePath = "C:/Users/Sumanth/Desktop/Fall 2014/MWDB/sampledata_P1_F14/Graphs/LocationMatrix.xls";
		File inputWorkBook = new File(filePath);
		Workbook w;
		w = Workbook.getWorkbook(inputWorkBook);
		Sheet sh = w.getSheet("Sheet1");
		Map<Integer,String> stateMap = new LinkedHashMap<Integer,String>();
		
		//Make an association with row number and the state
		for(int col=1;col<sh.getColumns();col++)
		{
			Cell c = sh.getCell(0,col);
			stateMap.put(col,c.getContents().trim());
		}
		
		Map<String,List<String>> assocMap = new LinkedHashMap<String,List<String>>(); 
		
		//Associate each State with a list of oneHop neighboring states.
		for(int i=1;i<sh.getRows();i++)
		{
			for(int j=1;j<sh.getColumns();j++)
			{
				Cell c = sh.getCell(i,j);
				if(c.getContents().equals("1"))
				{
					if(assocMap.get(stateMap.get(i))==null)
					{
						List<String> a = new ArrayList<String>();
						a.add(stateMap.get(j));
						assocMap.put(stateMap.get(i), a);
					}
					else
					{
						List<String> a = assocMap.get(stateMap.get(i));
						a.add(stateMap.get(j));
						assocMap.put(stateMap.get(i), a);
					}
				}
			}
		}
		
		return assocMap;
		
	
	}

}
