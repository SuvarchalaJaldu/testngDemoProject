package com.demoPackage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.demoPackage.CustomTestListener.class)
public class UserServiceTest {

	private UserService userService;

	@BeforeClass
	public void beforeClass() {
		// Set up the UserService instance
		userService = new UserService();
	}

	@BeforeMethod()
	public void setUp() {
		System.out.println("Users info at the start of the testcase: \n" + userService.getAllUsers());
	}

	@AfterMethod()
	public void tearDown() {
		System.out.println("Users info at the end of the testcase: \n" + userService.getAllUsers());
	}


	@Test(dataProvider = "userDataProvider", priority = 1)
	public void testCreateUser(User user, boolean expectedResult) {
		
		boolean result = userService.createUser(user);

		// Verify that the result is as expected
		assertEquals(result, expectedResult);
	}

	@DataProvider
	public Object[][] userDataProvider() {
		// Fetch data from Excel sheet and return as Object
		Object[][] data = null;
		try {
			FileInputStream file = new FileInputStream(new File("C:\\Users\\Admin\\Documents\\UserDataDemo\\testdata.xlsx"));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet("Sheet1");

			int rowCount = sheet.getLastRowNum();
			data = new Object[rowCount][2];
			User user;

			for (int i = 0; i < rowCount; i++) {
				XSSFRow row = sheet.getRow(i + 1); // skipping the header row

				String name = row.getCell(0).toString();
				String lastName = row.getCell(1).toString();
				String email = row.getCell(2).toString();
				String password = row.getCell(3).toString();
				boolean expectedResult = Boolean.parseBoolean(row.getCell(4).toString());

				user = new User(name, lastName, email, password);

				data[i][0] = user;
				data[i][1] = expectedResult;
			}

			workbook.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}
	
	@Test(priority = 2, invocationCount=1, retryAnalyzer = CustomRetryAnalyzer.class)
	public void deleteUserData() throws InterruptedException {
		User user1 = new User("John", "Doe", "john.doe1@example.com", "password123");
		userService.createUser(user1);
		userService.deleteUser(user1);

		User result = userService.getUserByEmail("john.doe1@example.com");
		assertNull(result);
	}

	public static class CustomRetryAnalyzer implements IRetryAnalyzer {
		private static final int MAX_RETRY_COUNT = 3;
		private int retryCount = 1; // current retry count

		public boolean retry(ITestResult result) {
			// Retry the test if it failed and the maximum retry count is not reached
			if (result.getStatus() == ITestResult.FAILURE && retryCount < MAX_RETRY_COUNT) {
				retryCount++;
				return true;
			}
			return false;
		}
	}
	
}