package com.demoPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomTestListener implements ITestListener {

	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_RESET = "\u001B[0m";

	UserService userService = new UserService();

     // Runs when a test is started
    public void onTestStart(ITestResult result) {
        System.out.println(ANSI_BLUE + "#### Test started: " + result.getName() + " ####" + ANSI_RESET);
    }

     // Runs when a test is successful
    public void onTestSuccess(ITestResult result) {
        System.out.println(ANSI_GREEN + "#### Test passed: " + result.getName() + " ####" + ANSI_RESET);
    }  

     // Runs when a test is failed
    public void onTestFailure(ITestResult result) {  
        System.out.println(ANSI_RED + "#### Test failed: " + result.getName() + " ####" + ANSI_RESET);

    }

     // Runs when a test is skipped
    public void onTestSkipped(ITestResult result) {
        System.out.println(ANSI_YELLOW + "#### Test skipped: " + result.getName() + "####" + ANSI_RESET);
    }

    public void onExecutionFinish() {
        System.out.println(ANSI_BLUE + "Suite execution finished" + ANSI_RESET);
    }

    public void onFinish(ITestContext context) {
        // Create a new directory for the report
        File reportDirectory = new File("test-output" + File.separator + "custom_report");
        reportDirectory.mkdir();

        try {
            // Create a new HTML file for the report
            File reportFile = new File(reportDirectory, "custom-report.html");
            FileWriter fileWriter = new FileWriter(reportFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write HTML content for the report
            bufferedWriter.write("<html><body>");
            bufferedWriter.write("<h1>Custom TestNG Report</h1>");
            bufferedWriter.write("<p>Report generated on: " + new Date() + "</p>");
            bufferedWriter.write("<table border='1'>");
            bufferedWriter.write("<tr><th>Test Name</th><th>Status</th><th>Duration (ms)</th><th>Exception</th></tr>");

            // Loop through all the test results and write them to the report
            for (ITestResult result : context.getPassedTests().getAllResults()) {
                String testName = result.getName();
                long duration = result.getEndMillis() - result.getStartMillis();

                bufferedWriter.write("<tr>");
                bufferedWriter.write("<td style='color: blue;'>" + testName + "</td>");
                bufferedWriter.write("<td style='color: green;'>PASS</td>");
                bufferedWriter.write("<td>" + duration + "</td>");
                bufferedWriter.write("<td>N/A</td>");
                bufferedWriter.write("</tr>");
            }

            // Loop through all the test results and write them to the report
            for (ITestResult result : context.getSkippedTests().getAllResults()) {
                String testName = result.getName();
                long duration = result.getEndMillis() - result.getStartMillis();

                bufferedWriter.write("<tr>");
                bufferedWriter.write("<td style='color: blue;'>" + testName + "</td>");
                bufferedWriter.write("<td style='color: orange;'>SKIP</td>");
                bufferedWriter.write("<td>" + duration + "</td>");
                bufferedWriter.write("<td>N/A</td>");
                bufferedWriter.write("</tr>");
            }
            
            for (ITestResult result : context.getFailedTests().getAllResults()) {
                String testName = result.getName();
                long duration = result.getEndMillis() - result.getStartMillis();
                String exception = result.getThrowable() != null ? result.getThrowable().toString() : "N/A";

                bufferedWriter.write("<tr>");
                bufferedWriter.write("<td style='color: blue;'>" + testName + "</td>");
                bufferedWriter.write("<td style='color: red;'>FAIL</td>");
                bufferedWriter.write("<td>" + duration + "</td>");
                bufferedWriter.write("<td style='color: red	;'>" + exception + "</td>");
                bufferedWriter.write("</tr>");
            }

            bufferedWriter.write("</table>");
            bufferedWriter.write("</body></html>");

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
