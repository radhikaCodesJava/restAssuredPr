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

public class deleteRequest extends config{
	@Test(dataProvider="jobsData")
	public void delReq(String id) throws JsonMappingException, JsonProcessingException
	{
		 Response response = given()
				 				.param("Job Id",id)
	                			.
	                		when()
	                		 	.delete(jobsUri).
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
		 //System.out.println(body);
		 assertThat("Json Schema",body,matchesJsonSchemaInClasspath("JobsGetAllSchema.json"));
		
		 //***********Response body Validation********************
		 ObjectMapper mapper = new ObjectMapper();
		 LinkedHashMap<String, LinkedHashMap<String,LinkedHashMap<String,String>>> map = 
				 mapper.readValue(body, new TypeReference<LinkedHashMap<String, LinkedHashMap<String,LinkedHashMap<String,String>>>>(){});
		 
		 LinkedHashMap<String,LinkedHashMap<String,String>> fieldMap = map.get("data");
		 
		 LinkedHashMap<String,String> JobIdMap = fieldMap.get("Job Id");
		 		 		 
		 //*******Fetch the key of the last record to assert post************
		 Set<String> keys = JobIdMap.keySet();
		 String lastId = "";
		 for(String key: keys)
		 {
			lastId = key;
		 }
		 
		 System.out.println(lastId);
		 
		 Assert.assertNotEquals(JobIdMap.get(lastId),id);
		  			
	}
	@Test(dataProvider="jobsData")
	
	public void delNonExistingId(String id)
	{
		 Response response = given()
				 				.param("Job Id",id)
	                			.
	                		when()
	                		 	.delete(jobsUri).
	                		then()
	                		 	.assertThat()
	     //***********Status Code Validation********************
	                		 	.statusCode(404)
	                		 	.log().all()
	                		 	.extract()
	                		 	.response();
	                		 	
		 //***********Response body Validation********************
		 String body = response.getBody().asString();
		 System.out.println(body); 
		 Assert.assertEquals(body.contains("\"message\": \"'"+id+"' Job  not found.\""),true);	  			
	}
	@DataProvider(name="jobsData")
	Object[] getData() throws IOException
	{
		
		int rowNum = excelUtil.getRowCount(pathJobs, "Delete");
		int cellNum = excelUtil.getCellCount(pathJobs, "Delete", rowNum);
		System.out.println(rowNum + "====" + cellNum);
		String [] data = new String [rowNum];
		for(int i=1;i<=rowNum;i++)
		{	
			
		data[i-1] = excelUtil.GetCellData(pathJobs, "Delete", i, 0);
		}		
			
		
		return(data);
		
	}
}
