package jobsAPI;

import ApiConfig.config;
import ApiConfig.excelUtil;

import static io.restassured.module.jsv.JsonSchemaValidator.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class putRequest extends config{
	@Test(dataProvider="jobsData")
	public void putReq(String id,String name,String title) throws JsonMappingException, JsonProcessingException
	{
		 Response response = given()
				 				.param("Job Id",id)
	                			.param("Job Title",title)
	                			.
	                		when()
	                		 	.put(jobsUri).
	                		 then()
	                		 	.assertThat()
	     //***********Status code Validation********************
	                		 	.statusCode(200)
	                		 	.log().all()
	                		 	.extract()
	                		 	.response();
		 //***********Schema Validation********************           		 	
		 String body = response.getBody().asString();
		 body = body.replaceAll("NaN","0");
		 assertThat("Json Schema",body,matchesJsonSchemaInClasspath("JobsGetAllSchema.json"));
		 	
		 //***********Response body Validation********************
		 ObjectMapper mapper = new ObjectMapper();
		 LinkedHashMap<String, LinkedHashMap<String,LinkedHashMap<String,String>>> map = 
				 mapper.readValue(body, new TypeReference<LinkedHashMap<String, LinkedHashMap<String,LinkedHashMap<String,String>>>>(){});
		 
		 LinkedHashMap<String,LinkedHashMap<String,String>> fieldMap = map.get("data");
		 
		 LinkedHashMap<String,String> JobIdMap = fieldMap.get("Job Id");
		 LinkedHashMap<String,String> JobTitleMap = fieldMap.get("Job Title");
		 
		 //*******Fetch the key of the last record to assert post************
		 Set<String> keys = JobIdMap.keySet();
		 String lastId = "";
		 for(String key: keys)
		 {
			lastId = key;
		 }
		 
		 System.out.println(lastId);
		 
		 Assert.assertEquals(JobIdMap.get(lastId),id);
		 Assert.assertEquals(JobTitleMap.get(lastId),title);
		 			
	}
	
	@Test(dataProvider="jobsData1")
	public void putNonExistingId(String id, String name, String title)
	{
		 Response response = given()
     			.param("Job Title",title)
     			.param("Job Id",id)
     			.
     		when()
     		 	.put(jobsUri).
     		 then()
     		 	.assertThat()
        //***********Status code Validation********************
     		 	.statusCode(404)
     		 	.log().all()
     		 	.extract()
     		 	.response();
     		 	
		//***********Response body Validation********************
		String body = response.getBody().asString();
		System.out.println(body); 
		Assert.assertEquals(body.contains("\"message\": \"'"+id+"' job not found.\""),true);
		
	
	}

	@DataProvider(name="jobsData")
	Object[][] getData() throws IOException
	{
		
		int rowNum = excelUtil.getRowCount(pathJobs, "Put");
		int cellNum = excelUtil.getCellCount(pathJobs, "Put", rowNum);
		System.out.println(rowNum + "====" + cellNum);
		String [][] data = new String [rowNum][cellNum];
		for(int i=1;i<=rowNum;i++)
			for(int j=0;j<cellNum;j++)
			{
				data[i-1][j] = excelUtil.GetCellData(pathJobs, "Put", i, j);
				
			}
		
		return(data);
		
	}
	
	@DataProvider(name="jobsData1")
	Object[][] getData1() throws IOException
	{
		
		int rowNum = excelUtil.getRowCount(pathJobs, "Put non-existing Id");
		int cellNum = excelUtil.getCellCount(pathJobs, "Put non-existing Id", rowNum);
		System.out.println(rowNum + "====" + cellNum);
		String [][] data = new String [rowNum][cellNum];
		for(int i=1;i<=rowNum;i++)
			for(int j=0;j<cellNum;j++)
			{
				data[i-1][j] = excelUtil.GetCellData(pathJobs, "Put non-existing Id", i, j);
				
			}
		
		return(data);
		
	}
}
