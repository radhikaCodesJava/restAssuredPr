package lmsApi;

import org.junit.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ApiConfig.config;
import ApiConfig.excelUtil;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

public class deleteRequest extends config {
	
	@Test(dataProvider="programData")
	public void delReq(String id) throws Exception
	{
		Response response= 
							given()
								.auth()
								.basic(userName,pwd)
								.header("Content-Type", "application/json").
								
							when()
								.delete(uri+"/programs/"+id).
							then()
		//***********Status Code Validation********************
								.statusCode(200)
								.extract()
								.response();
		
		//***********Response Body Validation********************
		String body =response.getBody().asString();
		Assert.assertEquals("", body);
		}
	
	@DataProvider(name="programData")
	Object[] getData() throws Exception
	{
		int rowNum = excelUtil.getRowCount(path, "Delete");
		String [] data = new String [rowNum];
		for(int i=1;i<=rowNum;i++)
			
			{
				data[i-1] = excelUtil.GetCellData(path, "Delete", i, 0);
				System.out.println(data[i-1]);
			}
		
		return(data);		
	}

}
