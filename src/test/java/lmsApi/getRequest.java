package lmsApi;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ApiConfig.config;
import ApiConfig.excelUtil;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import junit.framework.Assert;

public class getRequest extends config{
	@Test
	public void getReq()
	{
		
		given()
			.auth()
			.basic(userName, pwd).
		when()
			.get(uri+"/programs").
		then()
			.assertThat()
		    .body(matchesJsonSchemaInClasspath("lmsGetAllSchema.json"))
		    .statusCode(200)
		    .body("[0]", hasKey("programId"))
		    .body("[0]", hasKey("programName"))
		    .body("[0]", hasKey("programDescription"))
		    .body("[0]", hasKey("online"))
		    .extract()
		    .response()
		    .getBody().asString().contains("programId")
		    ;
		                      
	}
	
	@Test(dataProvider="programData")
	public void getIdReq(String id)
	{
		Response response = given()
								.auth()
								.basic(userName, pwd).
							when()
								.get(uri+"/programs/"+id).
							then()
								.extract()
								.response();
		
		String body = response.getBody().asString();
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		System.out.println(body);
		
		if (body.equalsIgnoreCase("null"))
		{
			System.out.println("id does not exist");
			
		}
		else
		{
			JsonSchemaValidator.matchesJsonSchemaInClasspath("lmsGetSchema.json");
			JsonPath nodes = response.jsonPath();    //check if program id matches
			String id_node = String.valueOf(nodes.get("programId"));
			Assert.assertEquals(id, id_node);
			System.out.println("id exists");
		}
		
		
		
	}

	@DataProvider(name="programData")
	Object[] getData() throws Exception
	{
		int rowNum = excelUtil.getRowCount(path, "Get");
		System.out.println(rowNum);
		String [] data = new String [rowNum];
		for(int i=1;i<=rowNum;i++)
			//for(int j=0;j<cellNum;j++)
			{
				data[i-1] = excelUtil.GetCellData(path, "Get", i, 0);
				System.out.println(data[i-1]);
			}
		
		return(data);		
	}
}
