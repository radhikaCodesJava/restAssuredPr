package jobsAPI;

import ApiConfig.config;
import ApiConfig.excelUtil;

import static io.restassured.module.jsv.JsonSchemaValidator.*;

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

public class postRequest extends config{
	@Test(dataProvider="jobsData")
	public void postReq(String title, String name, String location, String type, String time, String id, String desc) throws JsonMappingException, JsonProcessingException
	{
		 Response response = given()
	                			.param("Job Title",title)
	                			.param("Job Company Name",name)
	                			.param("Job Location",location)
	                			.param("Job Type",type)
	                			.param("Job Posted time",time)
	                			.param("Job Id",id)
	                			.param("Job Description",desc).
	                		when()
	                		 	.post(jobsUri).
	                		 then()
	                		 	.assertThat()
	     //***********Status Code Validation********************
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
		 LinkedHashMap<String,String> JobCompanyMap = fieldMap.get("Job Company Name");
		 LinkedHashMap<String,String> JobLocationMap = fieldMap.get("Job Location");
		 LinkedHashMap<String,String> JobTypeMap = fieldMap.get("Job Type");
		 LinkedHashMap<String,String> JobTimeMap = fieldMap.get("Job Posted time");
		 LinkedHashMap<String,String> JobDescMap = fieldMap.get("Job Description");
		 		 
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
		 Assert.assertEquals(JobCompanyMap.get(lastId),name);
		 Assert.assertEquals(JobLocationMap.get(lastId),location);
		 Assert.assertEquals(JobTypeMap.get(lastId),type);
		 Assert.assertEquals(JobTimeMap.get(lastId),time);
		 Assert.assertEquals(JobDescMap.get(lastId),desc);
		 		 				
	}
	
	@Test(dataProvider="jobsData")
	public void postExistingId(String title, String name, String location, String type, String time, String id, String desc)
	{
		 Response response = given()
     			.param("Job Title",title)
     			.param("Job Company Name",name)
     			.param("Job Location",location)
     			.param("Job Type",type)
     			.param("Job Posted time",time)
     			.param("Job Id",id)
     			.param("Job Description",desc).
     		when()
     		 	.post(jobsUri).
     		 then()
     		 	.assertThat()
       //***********Status Code Validation********************
     		 	.statusCode(409)
     		 	.log().all()
     		 	.extract()
     		 	.response();
     		 	
		//***********Response Body Validation********************
		String body = response.getBody().asString();
		System.out.println(body); 
		Assert.assertEquals(body.contains("\"message\": \"'"+id+"' already exists.\""),true);
		
	
	}

	@DataProvider(name="jobsData")
	Object[][] getData() throws IOException
	{
		
		int rowNum = excelUtil.getRowCount(pathJobs, "Post");
		int cellNum = excelUtil.getCellCount(pathJobs, "Post", rowNum);
		System.out.println(rowNum + "====" + cellNum);
		String [][] data = new String [rowNum][cellNum];
		for(int i=1;i<=rowNum;i++)
			for(int j=0;j<cellNum;j++)
			{
				data[i-1][j] = excelUtil.GetCellData(pathJobs, "Post", i, j);
				
			}
		
		return(data);
		
	}
}
