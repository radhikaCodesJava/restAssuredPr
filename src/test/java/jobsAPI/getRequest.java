package jobsAPI;

import ApiConfig.config;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class getRequest extends config{
	@Test
	
	public void getReq() throws JsonMappingException, JsonProcessingException
	{
		 Response response = given()
	                			.get(jobsUri).
	                		 then()
	                		 	.assertThat()
	     //***********Status Code Validation********************
	                		 	.statusCode(200)
	                		 	.extract()
	                		 	.response();
	                		 	
		 
		 String body = response.getBody().asString();
		 body = body.replaceAll("NaN","0");
		//***********Schema Validation********************
		 assertThat("Json Schema",body,matchesJsonSchemaInClasspath("JobsGetAllSchema.json"));
		 
		//***********Response Body Validation********************
		 ObjectMapper mapper = new ObjectMapper();
		 LinkedHashMap<String, LinkedHashMap<String,LinkedHashMap<String,String>>> map = mapper.readValue(body, new TypeReference<LinkedHashMap<String, LinkedHashMap<String,LinkedHashMap<String,String>>>>(){});
		 LinkedHashMap<String,LinkedHashMap<String,String>> fieldMap = map.get("data");
		 Set<String> FieldNames = fieldMap.keySet();
		 
		 for(String fieldname: FieldNames) {
			 switch(fieldname){
			 case("Job Title"):
				 JobTitle = true;
			 	 break;
			 case("Job Company Name"):
				 JobCompanyName = true;
			 	 break;
			 case("Job Location"):
				 JobLocation = true;
			 	 break;
			 case("Job Type"):
				 JobType = true;
			 	 break;
			 case("Job Posted time"):
				 JobPostedTime = true;
			 	 break;
			 case("Job Description"):
				 JobDescription = true;
			 	 break;
			 case("Job Id"):
				 JobId = true;
			 	 break;
			 default:
				 break;
		 }//end switch
		 
		 }//end for loop	
		 if(JobTitle && JobCompanyName && JobLocation && JobType && JobPostedTime && JobDescription && JobId == true)
		 {
			 responseBody = true;
		 }
		 
		 assertTrue(responseBody);
	}

}
