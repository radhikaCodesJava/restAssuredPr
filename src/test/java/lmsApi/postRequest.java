package lmsApi;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ApiConfig.config;
import ApiConfig.excelUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.IOException;

public class postRequest extends config {
	
	@Test(dataProvider="programData")
	public void postReq(String id, String onln, String progDesc, String progName ) throws Exception
	{
		boolean online = Boolean.parseBoolean(onln);
		JSONObject param = new JSONObject();
		param.put("online", online);
		param.put("programDescription",progDesc);
		param.put("programId", id);
		param.put("programName", progName);
		
		Response response= 
							given()
								.auth()
								.basic(userName,pwd)
								.header("Content-Type", "application/json")
								.body(param).
							when()
								.post(uri+"/programs").
							then()
		//Status code and schema validation
								.statusCode(200)
								.body(matchesJsonSchemaInClasspath("lmsGetSchema.json"))
								.extract()
								.response();
		
		//Json Body validation
		   JsonPath nodes = response.jsonPath();
		   Assert.assertEquals(nodes.get("online"),online);
		   Assert.assertEquals(nodes.get("programName"),progName);
		   Assert.assertEquals(nodes.get("programDescription"),progDesc);
		   Assert.assertNotNull(nodes.get("programId"));
		   String progId = String.valueOf(nodes.get("programId"));
		   System.out.println("programId ==="+nodes.get("programId"));
		   System.out.println("programName ==="+nodes.get("programName"));
		   System.out.println("programDesc ==="+nodes.get("programDescription"));
		   putData(progId);
		   		
	}
	
	void putData(String progId) throws IOException
	{
		
		int rowNum = excelUtil.getRowCount(path, "Put");
		excelUtil.SetCellData(path, "Put", rowNum, 0, progId);
		
	}
	
	@DataProvider(name="programData")
	Object[][] getData() throws IOException
	{
		
		int rowNum = excelUtil.getRowCount(path, "Post");
		int cellNum = excelUtil.getCellCount(path, "Post", rowNum);
		System.out.println(rowNum + "====" + cellNum);
		String [][] data = new String [rowNum][cellNum];
		for(int i=1;i<=rowNum;i++)
			for(int j=0;j<cellNum;j++)
			{
				data[i-1][j] = excelUtil.GetCellData(path, "Post", i, j);
			}
		
		return(data);
		
	}

}
