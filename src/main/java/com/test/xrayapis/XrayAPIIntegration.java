/*
 * ThinkPalm, Technologies Pvt Ltd ("COMPANY") CONFIDENTIAL
 * Copyright (c) 2019-2020 [ThinkPalm, Inc.], All Rights Reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

package com.test.xrayapis;

import static com.test.xrayapis.XrayAPIs.TEST_EXECUTION_GET_URL;
import static com.test.xrayapis.XrayAPIs.TEST_RUN_GET_URL;
import static com.test.xrayapis.XrayAPIs.TEST_RUN_STATUS_PUT_URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.http.client.utils.URIBuilder;
import com.test.dto.CreateIssueDTO;
import com.test.dto.ResponseDTO;
import com.test.model.Issue;
import com.test.model.IssueList;
import com.test.model.TestCase;
import com.test.model.TestRun;
import com.test.model.TransitionList;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Xray API integartion
 * 
 * @author nasia.t
 *
 */
public class XrayAPIIntegration {
	private static final ResourceBundle rb = ResourceBundle.getBundle("application");
	private static final String BASE_URL = rb.getString("baseUrl");
	private static final String CREATE_ISSUE_URL = rb.getString("create.issue");
	private static final String CREATE_TEST_EXECUTION_URL = rb.getString("testexecution.get");
	private static final String UPDATE_ISSUEURL = XrayAPIs.UPDATE_ISSUE_URL;
	private static final String JIRA_USERNAME = rb.getString("jira.username");
	private static final String JIRA_PASSWORD = rb.getString("jira.password");
	private static final String CONTENT_TYPE = "application/json";
	private static final String GET_ISSUELIST_URL = XrayAPIs.GET_ISSUE_URL;
	private static final String GET_ISSUE_URL = XrayAPIs.GET_ISSUE;
	private static final String POST_COMMENT_URL = XrayAPIs.COMMENT_URL;
	private static final String TESTCASES = rb.getString("list.of.testcases");

	/**
	 * To get list of test cases linked to the test execution key
	 * 
	 * @param testexecutionkey
	 *        key of test execution
	 *
	 */
	public static List<TestCase> getTestExecution(String testexecutionkey) {
		String exc = TEST_EXECUTION_GET_URL;
		String api = exc.replace("execid", testexecutionkey);
		RestAssured.baseURI = BASE_URL;
		Response response = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD).get(api);
		if (response.getStatusCode() == 200) {
			return Arrays.asList(response.getBody().as(TestCase[].class));
		}
		return Collections.emptyList();
	}

	/**
	 * To update test case status
	 * 
	 * @param testRunId
	 *        id of test run
	 * @param status
	 *        status of test case
	 *
	 */
	public String updateTestCaseStatus(int testRunId, String status) throws URISyntaxException {
		String testRunPutUrl = TEST_RUN_STATUS_PUT_URL;
		String replacedUrl = testRunPutUrl.replace("id", "" + testRunId);
		URIBuilder b = new URIBuilder(BASE_URL + replacedUrl);
		URI u = b.addParameter("status", status).build();
		Response response = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD).put(u);
		return response.getBody().prettyPrint();
	}

	/**
	 * To get test run
	 * 
	 * @param testexecutionkey
	 *        key of test execution
	 * @param testKey
	 *        key of test case
	 */
	public TestRun getTestRun(String testKey, String testexecutionkey) throws URISyntaxException {
		String testRunGetUrl = TEST_RUN_GET_URL;
		URIBuilder b = new URIBuilder(BASE_URL + testRunGetUrl);
		b.addParameter("testIssueKey", testKey);
		b.addParameter("testExecIssueKey", testexecutionkey);
		URI url = b.build();
		Response response = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD).get(url);
		if (response.getStatusCode() == 200)
			return response.getBody().as(TestRun.class);
		return null;
	}

	/**
	 * To create issue in jira Xray
	 * 
	 * @param createIssueDTO
	 *        DTO for create issue
	 *
	 */
	public String createIssue(CreateIssueDTO createIssueDTO) {
		String createIssueUrl = BASE_URL + CREATE_ISSUE_URL;
		String test = String.format(
				"{\"fields\": {\"project\":{\"key\": \"%s\"},\"summary\": \"%s\",\"description\":\"%s\",\"issuetype\": {\"name\": \"%s\"}}}",
				createIssueDTO.getKey(), createIssueDTO.getSummary(), createIssueDTO.getDescription(),
				createIssueDTO.getName());
		RequestSpecification request = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD);
		request.contentType(CONTENT_TYPE);
		request.body(test);
		Response response = request.post(createIssueUrl);
		return response.body().as(ResponseDTO.class).getKey();
	}

	/**
	 * To create test execution in jira Xray
	 * 
	 * @param executionKey
	 *        key of test execution
	 */
	public int postTestExecution(String executionKey) {
		String createExecutionUrl = BASE_URL + CREATE_TEST_EXECUTION_URL;
		String api = createExecutionUrl.replace("execid", executionKey);
		// String test = "{\"add\": [ \"AB-5\", \"AB-6\", \"AB-7\"]}";
		String test = "{\"add\": [ \"" + TESTCASES.replace(",", "\", \"") + "\"]}";
		RequestSpecification request = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD);
		request.contentType(CONTENT_TYPE);
		request.body(test);
		Response response = request.post(api);
		return response.getStatusCode();

	}

	/**
	 * To create bug
	 * 
	 * @param createIssueDTO
	 *        DTO for create issue
	 */
	public ResponseDTO createIssueBug(CreateIssueDTO createIssueDTO) {
		String createIssueUrl = BASE_URL + CREATE_ISSUE_URL;
		// URIBuilder b = new URIBuilder(createIssueUrl);
		// URI u = b.build();
		String test = String.format(
				"{\"fields\":{\"project\":{\"key\":\"%s\" },\"summary\":\"%s\",\"description\":\"%s\",\"issuetype\":{\"name\":\"%s\"}},\"update\":{\"issuelinks\":[{\"add\":{\"type\":{\"name\":\"Blocks\",\"inward\":\"is blocked by\",\"outward\":\"blocks\"},\"outwardIssue\":{\"key\":\"%s\" }}}]}}",
				createIssueDTO.getKey(), createIssueDTO.getSummary(), createIssueDTO.getDescription(),
				createIssueDTO.getName(), createIssueDTO.getTestKey());
		RequestSpecification request = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD);
		request.contentType(CONTENT_TYPE);
		request.body(test);
		Response response = request.post(createIssueUrl);
		return response.body().as(ResponseDTO.class);
	}

	/**
	 * To get transition list
	 * 
	 * @param issueId
	 *        id of issue
	 */
	public TransitionList getTransitionsFromBug(String issueId) {
		String exc = BASE_URL + UPDATE_ISSUEURL;
		String api = exc.replace("issueId", issueId);
		RestAssured.baseURI = BASE_URL;
		Response response = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD).get(api);
		if (response.getStatusCode() == 200) {
			return response.body().as(TransitionList.class);
		}
		return response.body().as(TransitionList.class);
	}

	/**
	 * To post transition
	 * 
	 * @param issueId
	 *        id of issue 
	 *        
	 * @param transitionId
	 *        transition id
	 *        
	 * @param message
	 *        comment to be added
	 *
	 */
	public int postTransitions(String issueId, int transitionId, String message) {
		String exc = BASE_URL + UPDATE_ISSUEURL;
		String api = exc.replace("issueId", issueId);
		String test = String.format(
				"{\"update\":{\"comment\":[{\"add\":{\"body\":\"%s\"}}]},\"transition\":{\"id\":\"%s\"}}", message,
				transitionId);
		RequestSpecification request = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD);
		request.contentType(CONTENT_TYPE);
		request.body(test);
		Response response = request.post(api);
		postComment(issueId, message);
		return response.getStatusCode();
	}

	/**
	 * To get issue list
	 * 
	 * @param projectName 
	 *        name of the project
	 *        
	 * @param transitionId
	 *        transition id
	 *        
	 * @param maxrows
	 *        number of issues
	 *
	 */
	public IssueList getIssueList(String projectName, int maxrows) {
		String exc = BASE_URL + GET_ISSUELIST_URL;
		String api = exc.replace("projectName", projectName) + maxrows;
		IssueList issueList = null;
		RestAssured.baseURI = BASE_URL;
		Response response = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD).get(api);
		if (response.getStatusCode() == 200) {
			issueList = response.body().as(IssueList.class);
		}
		return issueList;
	}

	/**
	 * To get issue details for a particular id
	 * 
	 * @param issueId
	 *        id of issue 
	 */
	public Issue getIssue(String issueId) {
		String exc = BASE_URL + GET_ISSUE_URL;
		String api = exc.replace("issueId", issueId);
		Issue issue = null;
		RestAssured.baseURI = BASE_URL;
		Response response = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD).get(api);
		if (response.getStatusCode() == 200) {
			issue = response.body().as(Issue.class);
		}
		return issue;
	}

	/**
	 * To post comment to a bug
	 * 
	 * @param issueId
	 *        id of issue 
	 *        
	 * @param message
	 *        comment to be added
	 */
	public void postComment(String issueId, String message) {
		String exc = BASE_URL + POST_COMMENT_URL;
		String api = exc.replace("issueId", issueId);
		String test = String.format("{\"body\":\"%s\"}", message);
		RequestSpecification request = RestAssured.given().auth().preemptive().basic(JIRA_USERNAME, JIRA_PASSWORD);
		request.contentType(CONTENT_TYPE);
		request.body(test);
		// Response response = request.post(api);
		request.post(api);
	}
}