/*
 * ThinkPalm, Technologies Pvt Ltd ("COMPANY") CONFIDENTIAL
 * Copyright (c) 2019-2020 [ThinkPalm, Inc.], All Rights Reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

package com.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import com.test.dto.BugDTO;
import com.test.dto.CreateIssueDTO;
import com.test.dto.CustomerAccountsResponseDTO;
import com.test.dto.FinancialMovementsDTO;
import com.test.dto.PaymentResponseDTO;
import com.test.dto.RefundResponseDTO;
import com.test.dto.ReportDTO;
import com.test.model.ErrorMessage;
import com.test.model.TestCase;
import com.test.model.TestRun;
import com.test.xrayapis.XrayAPIIntegration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * implements api automation with jira integartion
 * 
 * @author nasia.t
 *
 */
public class Run {

	private static final Logger LOGGER = LogManager.getLogger(Run.class);
	static String testExecutionid;
	static XrayAPIIntegration apiIntegration = new XrayAPIIntegration();
	static CreateIssueDTO createIssueDTO = null;
	CreateIssueDTO createBugDTO = null;
	private static final ResourceBundle rb = ResourceBundle.getBundle("application");
	private static final String BASE_URL = rb.getString("baseUrl");
	private static final String RESPONSE = "Response :";
	private static final String STATUS_CODE = "Status Code :";
	private static final String PROJECT_NAME = rb.getString("project.name");
	private static final String XRAY_LINK = rb.getString("xray.link");
	private static final String PROJECT_ID = rb.getString("project.id");
	private static final String RESTASSURED_BASE_URL = rb.getString("restAssuredBaseUrl");
	// private static final String token = rb.getString("token");
	static CustomerAccountsResponseDTO customerAccountsResponseDTO;
	static PaymentResponseDTO paymentResponseDTO;
	static RefundResponseDTO refundResponseDTO;
	private static final String UC1_ACCOUNT_NAME = rb.getString("uc1.account.name");
	private static final String UC1_ACCOUNT_NUMBER = rb.getString("uc1.account.number");
	private static final String UC1_EMAIL = rb.getString("uc1.email");
	private static final String UC1_OPERATION_DATE = rb.getString("uc1.operationDate");
	private static final String UC1_EXTERNAL_PAYMENT_NUMBER = rb.getString("uc1.externalPaymentNumber");
	private static final String UC1_AMOUNT = rb.getString("uc1.amount");
	private static final String UC2_ACCOUNT_NAME = rb.getString("uc2.account.name");
	private static final String UC2_ACCOUNT_NUMBER = rb.getString("uc2.account.number");
	private static final String UC2_EXTERNAL_PAYMENT_NUMBER = rb.getString("uc2.externalPaymentNumber");
	private static final String UC2_AMOUNT = rb.getString("uc2.amount");
	private static final String MAIL_TO = rb.getString("list.to.mail");

	private static final String CREATEACCOUNTANDPAY_RESPONSECODEVALIDATION = rb
			.getString("CreateAccountandPay.Responsecodevalidation");
	private static final String CREATEACCOUNTANDPAY_RESPONSERESULTVALIDATION = rb
			.getString("CreateAccountandPay.Responseresultvalidation");
	private static final String CREATEACCOUNTANDPAY_RESPONSEBODYVALIDATION = rb
			.getString("CreateAccountandPay.Responsebodyvalidation");
	private static final String CREATEACCOUNTANDPAY_INVALIDRESPONSECODEVALIDATION = rb
			.getString("CreateAccountandPay.InvalidResponsecodevalidation");
	private static final String CREATEACCOUNTANDPAY_PAYAPIRESPONSECODEVALIDATION = rb
			.getString("CreateAccountandPay.PayAPIResponsecodevalidation");
	private static final String CREATEACCOUNTANDPAY_PAYAPIRESPONSEBODYVALIDATION = rb
			.getString("CreateAccountandPay.PayAPIResponsebodyvalidation");
	private static final String CREATEACCOUNTANDPAY_PAYAPIINVALIDRESPONSECODEVALIDATION = rb
			.getString("CreateAccountandPay.PayAPIInvalidResponsecodevalidation");
	private static final String CREATEACCOUNTANDPAY_ACCOUNTNAMEASEMPTY = rb
			.getString("CreateAccountandPay.AccountNameAsEmpty");
	private static final String CREATEACCOUNTANDPAY_ACCOUNTNUMBERASEMPTY = rb
			.getString("CreateAccountandPay.AccountNumberAsEmpty");
	private static final String CREATEACCOUNTANDPAY_PAYAPI_ACCOUNTNAMEASEMPTY = rb
			.getString("CreateAccountandPay.PayApi.AccountNameAsEmpty");
	private static final String CREATEACCOUNTANDPAY_PAYAPI_ACCOUNTNUMBERASEMPTY = rb
			.getString("CreateAccountandPay.PayApi.AccountNumberAsEmpty");
	private static final String CREATEACCOUNTANDPAY_PAYAPI_CURRENCYASUPPERCASE = rb
			.getString("CreateAccountandPay.PayApi.CurrencyAsUpperCase");
	private static final String CREATEACCOUNTANDPAY_PAYAPI_CUSTOMERTOKENASEMPTY = rb
			.getString("CreateAccountandPay.PayApi.CustomerTokenAsEmpty");
	private static final String REFUND_PAYAPIRESPONSERESULTVALIDATION = rb
			.getString("Refund.PayAPIResponseresultvalidation");
	private static final String REFUND_PAYAPIRESPONSECODEVALIDATION = rb
			.getString("Refund.PayAPIResponsecodevalidation");
	private static final String REFUND_PAYAPIRESPONSEBODYVALIDATION = rb
			.getString("Refund.payAPIResponsebodyvalidation");
	private static final String REFUND_PAYAPI_ACCOUNTNAMEASEMPTY = rb
			.getString("Refund.PayAPI.AccountNameAsEmpty");
	private static final String REFUND_PAYAPI_ACCOUNTNUMBERASEMPTY = rb
			.getString("Refund.PayAPI.AccountNumberAsEmpty");
	private static final String REFUND_PAYAPI_SOURCEASEMPTY = rb
			.getString("Refund.PayAPI.SourceAsEmpty");
	private static final String REFUND_PAYAPIINVALIDRESPONSECODEVALIDATION = rb
			.getString("Refund.PayAPIInvalidResponsecodevalidation");
	private static final String REFUND_REFUNDAPIRESPONSECODEVALIDATION = rb
			.getString("Refund.RefundAPIResponsecodevalidation");
	private static final String REFUND_REFUNDAPIRESPONSERESULTVALIDATION = rb
			.getString("Refund.RefundAPIResponseresultvalidation");
	private static final String REFUND_REFUNDAPIRESPONSEBODYVALIDATION = rb
			.getString("Refund.RefundAPIResponsebodyvalidation");
	private static final String REFUND_REFUNDAPI_ACCOUNTNAMEASEMPTY= rb
			.getString("Refund.RefundAPI.AccountNameAsEmpty");
	private static final String REFUND_REFUNDAPI_EXTERNALPAYMENTNUMBERASEMPTY= rb
			.getString("Refund.RefundAPI.ExternalPaymentNumberAsEmpty");
	private static final String REFUND_REFUNDAPIINVALIDRESPONSECODEVALIDATION = rb
			.getString("Refund.RefundAPIInvalidResponsecodevalidation");
	private static final String REFUND_REFUNDAPIINVALIDPAYMENTNUMBERVALIDATION = rb
			.getString("Refund.RefundAPIInvalidPaymentNumbervalidation");
	private static final String REFUND_REFUNDAPIALREADYREFUNDEDPAYMENTVALIDATION = rb
			.getString("Refund.RefundAPIAlreadyrefundedpaymentvalidation");
	private static final String GETSETTLEMENTSAPI_RESPONSECODEVALIDATION = rb
			.getString("GetSettlementsAPI.Responsecodevalidation");
	private static final String GETSETTLEMENTSAPI_INVALIDRESPONSECODEVALIDATION = rb
			.getString("GetSettlementsAPI.InvalidResponsecodevalidation");
	private static final String GETSETTLEMENTSAPI_COUNTVALIDATION = rb.getString("GetSettlementsAPI.Countvalidation");
	private static final String GETFINANCIALMOVEMENTSAPI_RESPONSECODEVALIDATION = rb
			.getString("GetfinancialmovementsAPI.Responsecodevalidation");
	private static final String GETFINANCIALMOVEMENTSAPI_INVALIDRESPONSECODEVALIDATION = rb
			.getString("GetfinancialmovementsAPI.InvalidResponsecodevalidation");
	private static final String GETFINANCIALMOVEMENTSAPI_COUNTVALIDATION = rb
			.getString("GetfinancialmovementsAPI.Countvalidation");
	private static final String REFUND_PAYAPIAMOUNTLIMITEXCEEDINGVALIDATION = rb
			.getString("Refund.PayAPIAmountLimitExceedingValidation");
	private static final String REFUND_PAYAPINEGATIVEAMOUNTVALIDATION = rb
			.getString("Refund.PayAPINegativeAmountValidation");
	private static final String GETFINANCIALMOVEMENTSAPI_INVALIDPARAMETERVALIDATION = rb
			.getString("GetfinancialmovementsAPI.Invalidparametervalidation");
	private static final String CREATEACCOUNTANDPAY_CREATEACCOUNTINVALIDGATEWAYVERIFICATION = rb
			.getString("CreateAccountandPay.CreateAccountInvalidgatewayverification");
	private static final String REFUNDPAYAPI_AMOUNTLIMITEXCEEDINGMESSAGEVALIDATION = rb
			.getString("RefundPayAPI.AmountLimitExceedingMessagevalidation");
	private static final String REFUND_REFUNDAPIALREADYREFUNDEDPAYMENTMESSAGEVALIDATION = rb
			.getString("Refund.RefundAPIAlreadyrefundedpaymentmessagevalidation");
	private static final String REFUND_PAYAPINEGATIVEAMOUNTMESSAGEVALIDATION = rb
			.getString("Refund.PayAPINegativeAmountMessageValidation");
	private static final String REFUND_PAYAPINEGATIVEAMOUNTRESPONSEBODYVALIDATION = rb
			.getString("Refund.PayAPINegativeAmountResponsebodyValidation");
	private static final String REFUND_PAYAPIAMOUNTEXCEEDINGRESPONSEBODYVALIDATION = rb
			.getString("Refund.PayAPIAmountExceedingResponsebodyValidation");
	private static final String GETFINANCIALMOVEMENTSAPI_INVALIDPAGE = rb
			.getString("GetfinancialmovementsAPI.InvalidPage");

	private static String paymentUrl = "/payments";
	private static String refundUrl = "/refunds";
	private static String startedDate = "startDate";
	private static String createPaymentDate = "2019-10-14T02:01:01Z";
	private static String constantDate = "2019-10-14T01:01:01Z";
	private static String error = "ERROR";
	private static String gateway = "gateway";
	private static Random random = null;

	/**
	 * test case for creating issue in jira
	 *
	 */
	@Test(priority = 0)
	public static void createIssue() {
		createIssueDTO = new CreateIssueDTO();
		LocalDate date = LocalDate.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDate = date.format(myFormatObj);
		createIssueDTO.setDescription("AtoBe Automated Test Run " + formattedDate);
		createIssueDTO.setKey(PROJECT_ID);
		createIssueDTO.setName("Test Execution");
		createIssueDTO.setSummary("AtoBe Test Run " + formattedDate);
		testExecutionid = apiIntegration.createIssue(createIssueDTO);
		Assert.assertNotNull(testExecutionid);
	}

	/**
	 * test case for linking test cases in jira
	 *
	 */
	@Test(priority = 1)
	public void postTestExecution() {
		int status;
		status = apiIntegration.postTestExecution(testExecutionid);
		assertEquals(200, status);
	}

	/**
	 * test case for use case1 create account and pay for invalid response code
	 * validation
	 *
	 */
	@Test(priority = 2)
	public static void uc1CreateAccountAndPayInvalidResponseCodeValidation() throws URISyntaxException {
		customerAccountsResponseDTO = new CustomerAccountsResponseDTO();
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{%n\"accountName\": \"%s\",%n\"accountNumber\": \"%s\",%n\"source\": \"tok_mastercard\",%n\"email\": \"%s\",%n\"gateway\": \"STRIPE\"%n}",
				UC1_ACCOUNT_NAME, UC1_ACCOUNT_NUMBER, UC1_EMAIL);
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/customer-accountss");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("           USE CASE 1 CREATE ACCOUNT WITH WRONG URL           ");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_INVALIDRESPONSECODEVALIDATION,
					testExecutionid);

			if (404 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (404 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");

			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case1 create account and pay for invalid gateway
	 * verification
	 *
	 */
	@Test(priority = 3)
	public static void uc1CreateAccountAndPayCreateAccountInvalidGatewayVerification() throws URISyntaxException {
		customerAccountsResponseDTO = new CustomerAccountsResponseDTO();
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{%n\"accountName\": \"%s\",%n\"accountNumber\": \"%s\",%n\"source\": \"tok_mastercard\",%n\"email\": \"%s\",%n\"gateway\": \"test\"%n}",
				UC1_ACCOUNT_NAME, UC1_ACCOUNT_NUMBER, UC1_EMAIL);
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/customer-accounts");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("           USE CASE 1 CREATE ACCOUNT WITH INVALID GATEWAY           ");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_CREATEACCOUNTINVALIDGATEWAYVERIFICATION,
					testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");

			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case1 create account and pay with account name=null
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 4)
	public static void uc1CreateAccountAndPayWithNullAccountName() throws URISyntaxException {
		customerAccountsResponseDTO = new CustomerAccountsResponseDTO();
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{%n\"accountName\": \"%s\",%n\"accountNumber\": \"%s\",%n\"source\": \"tok_mastercard\",%n\"email\": \"%s\",%n\"gateway\": \"STRIPE\"%n}",
				null, UC1_ACCOUNT_NUMBER, UC1_EMAIL);
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/customer-accounts");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 1 CREATE ACCOUNT WITH ACCOUNT NAME=NULL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_ACCOUNTNAMEASEMPTY,
					testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
		}
	}
	
	
	/**
	 * test case for use case1 create account and pay with account number=null
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 5)
	public static void uc1CreateAccountAndPayWithNullAccountNumber() throws URISyntaxException {
		customerAccountsResponseDTO = new CustomerAccountsResponseDTO();
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{%n\"accountName\": \"%s\",%n\"accountNumber\": \"%s\",%n\"source\": \"tok_mastercard\",%n\"email\": \"%s\",%n\"gateway\": \"STRIPE\"%n}",
				UC1_ACCOUNT_NAME, null, UC1_EMAIL);
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/customer-accounts");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 1 CREATE ACCOUNT WITH ACCOUNT NUMBER=NULL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_ACCOUNTNUMBERASEMPTY,
					testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
		}
	}
	/**
	 * test case for use case1 create account and pay for response code validation
	 *
	 */
	@Test(priority = 6)
	public static void uc1CreateAccountAndPayResponseCodeValidation() throws URISyntaxException {
		customerAccountsResponseDTO = new CustomerAccountsResponseDTO();
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{%n\"accountName\": \"%s\",%n\"accountNumber\": \"%s\",%n\"source\": \"tok_mastercard\",%n\"email\": \"%s\",%n\"gateway\": \"STRIPE\"%n}",
				UC1_ACCOUNT_NAME, UC1_ACCOUNT_NUMBER, UC1_EMAIL);
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/customer-accounts");
			customerAccountsResponseDTO = new CustomerAccountsResponseDTO();
			customerAccountsResponseDTO
					.setCustomerToken(response.getBody().as(CustomerAccountsResponseDTO.class).getCustomerToken());
			customerAccountsResponseDTO.setResult(response.getBody().as(CustomerAccountsResponseDTO.class).getResult());
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			uc1CreateAccountAndPayResponseCodeValidationWithNotNullResponse(response);
		}
	}

	
	
	/**
	 * use case1 create account and pay response code validation, use case1 create
	 * account and pay response result validation ,use case1 create account and pay
	 * response body validation
	 */
	public static void uc1CreateAccountAndPayResponseCodeValidationWithNotNullResponse(Response response)
			throws URISyntaxException {
		LOGGER.info("           USE CASE 1 CREATE ACCOUNT           ");
		LOGGER.info(RESPONSE + response.asString());
		LOGGER.info(STATUS_CODE + response.getStatusCode());

		TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_RESPONSECODEVALIDATION, testExecutionid);
		if (201 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
		else if (201 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
		assertEquals(response.getStatusCode(), 201);

		TestRun testRun2 = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_RESPONSERESULTVALIDATION, testExecutionid);
		if ("OK".equals(customerAccountsResponseDTO.getResult()) && !"PASS".equals(testRun2.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun2.getId(), "PASS");
		else if (!"OK".equals(customerAccountsResponseDTO.getResult()) && !"FAIL".equals(testRun2.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun2.getId(), "FAIL");
		assertEquals(customerAccountsResponseDTO.getResult(), "OK");

		TestRun testRun3 = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_RESPONSEBODYVALIDATION, testExecutionid);
		if (null != customerAccountsResponseDTO.getCustomerToken() && !"PASS".equals(testRun3.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun3.getId(), "PASS");
		else if (null == customerAccountsResponseDTO.getCustomerToken() && !"FAIL".equals(testRun3.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun3.getId(), "FAIL");
		assertNotNull(customerAccountsResponseDTO.getCustomerToken());
	}

	/**
	 * test case for use case1 create payment with wrong url
	 *
	 */
	@Test(priority = 7)
	public static void uc1CreatePaymentWithWrongURL() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":\"555\",\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"customerToken\":\"%s\"}",
				UC1_ACCOUNT_NAME, UC1_ACCOUNT_NUMBER, Integer.parseInt(UC1_AMOUNT), UC1_OPERATION_DATE,
				Integer.parseInt(UC1_EXTERNAL_PAYMENT_NUMBER), customerAccountsResponseDTO.getCustomerToken());
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/paymentss");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("           USE CASE 1 CREATE PAYMENT WITH WRONG URL           ");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_PAYAPIINVALIDRESPONSECODEVALIDATION,
					testExecutionid);

			if (404 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (404 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");

			assertEquals(response.getStatusCode(), 404);
		}
	}
	/**
	 * test case for use case1 create payment
	 *
	 */
	@Test(priority = 8)
	public static void uc1createPayment() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":\"555\",\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"customerToken\":\"%s\"}",
				UC1_ACCOUNT_NAME, UC1_ACCOUNT_NUMBER, Integer.parseInt(UC1_AMOUNT), UC1_OPERATION_DATE,
				Integer.parseInt(UC1_EXTERNAL_PAYMENT_NUMBER), customerAccountsResponseDTO.getCustomerToken());
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post(paymentUrl);
			paymentResponseDTO = new PaymentResponseDTO();
			paymentResponseDTO.setPaymentNumber(response.getBody().as(PaymentResponseDTO.class).getPaymentNumber());
			paymentResponseDTO.setResult(response.getBody().as(PaymentResponseDTO.class).getResult());
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("           USE CASE 1 CREATE PAYMENT           ");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());

			TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_PAYAPIRESPONSECODEVALIDATION,
					testExecutionid);
			if (201 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (201 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 201);
			assertEquals(paymentResponseDTO.getResult(), "OK");

			TestRun testRun2 = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_PAYAPIRESPONSEBODYVALIDATION,
					testExecutionid);
			if (null != paymentResponseDTO.getPaymentNumber() && !"PASS".equals(testRun2.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun2.getId(), "PASS");
			else if (null == paymentResponseDTO.getPaymentNumber() && !"FAIL".equals(testRun2.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun2.getId(), "FAIL");
			assertNotNull(paymentResponseDTO.getPaymentNumber());
		}
	}

	
	/**
	 * test case for use case1 payment account with account name=null
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 9)
	public static void uc1CreatePayementWithNullAccountName() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":\"555\",\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"customerToken\":\"%s\"}",
				null, UC1_ACCOUNT_NUMBER, Integer.parseInt(UC1_AMOUNT), UC1_OPERATION_DATE,
				Integer.parseInt(UC1_EXTERNAL_PAYMENT_NUMBER), customerAccountsResponseDTO.getCustomerToken());
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/payments");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 1 CREATE PAYMENT WITH ACCOUNT NAME = NULL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_PAYAPI_ACCOUNTNAMEASEMPTY,
					testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");

			assertEquals(response.getStatusCode(), 400);
		}
	}
	/**
	 * test case for use case1 create payment with account number=null
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 10)
	public static void uc1CreatePaymentWithNullAccountNumber() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":\"555\",\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"customerToken\":\"%s\"}",
				UC1_ACCOUNT_NAME, null, Integer.parseInt(UC1_AMOUNT), UC1_OPERATION_DATE,
				Integer.parseInt(UC1_EXTERNAL_PAYMENT_NUMBER), customerAccountsResponseDTO.getCustomerToken());
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post(paymentUrl);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 1 CREATE PAYMENT  WITH ACCOUNT NUMBER = NULL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_PAYAPI_ACCOUNTNUMBERASEMPTY,
					testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");

			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case1 create account and pay with customer token=null
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 11)
	public static void uc1CreateAccountAndPayWithNullCustomerToken() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":\"555\",\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"customerToken\":\"%s\"}",
				UC1_ACCOUNT_NAME, UC1_ACCOUNT_NUMBER, Integer.parseInt(UC1_AMOUNT), UC1_OPERATION_DATE,
				Integer.parseInt(UC1_EXTERNAL_PAYMENT_NUMBER), null);
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/payments");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 1 CREATE PAYMENT WITH CUSTOMER TOKEN=NULL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_PAYAPI_CUSTOMERTOKENASEMPTY,
					testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");

			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case1 create payment with currency as uppercase
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 12)
	public static void uc1CreatePaymentWithCurrencyUpperCase() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"USD\",\"seed\":\"555\",\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"customerToken\":\"%s\"}",
				UC1_ACCOUNT_NAME, UC1_ACCOUNT_NUMBER, Integer.parseInt(UC1_AMOUNT), UC1_OPERATION_DATE,
				Integer.parseInt(UC1_EXTERNAL_PAYMENT_NUMBER), customerAccountsResponseDTO.getCustomerToken());
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/payments");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 1 CREATE PAYMENT WITH CURRENCY AS UPPERCASE***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(CREATEACCOUNTANDPAY_PAYAPI_CURRENCYASUPPERCASE,
					testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");

			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case2 create payment one shot with wrong url
	 *
	 */
	@Test(priority = 13)
	public static void uc2CreatePaymentOneShotWithWrongURL() throws URISyntaxException {
		String date = createPaymentDate;
		String operationDate = java.time.LocalDate.now() + date.substring(10);
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":%d,\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"source\":\"tok_mastercard\"}",
				UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER, Integer.parseInt(UC2_AMOUNT), 12, operationDate,
				Integer.parseInt(UC2_EXTERNAL_PAYMENT_NUMBER));
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/paymentss");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("           USE CASE 2 CREATE PAYMENT ONE SHOT WITH WRONG URL           ");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(REFUND_PAYAPIINVALIDRESPONSECODEVALIDATION, testExecutionid);

			if (404 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (404 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");

			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case2 create payment with larger amount
	 *
	 */
	@Test(priority = 14)
	public static void uc2CreatePaymentOneShotWithLargerAmount() throws URISyntaxException {
		String date = createPaymentDate;
		String operationDate = java.time.LocalDate.now() + date.substring(10);
		random = new Random();
		int randomNumber = random.nextInt(1000);
		int amount = 999999993;
		ErrorMessage errorMessage = new ErrorMessage();
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":%d,\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"source\":\"tok_mastercard\"}",
				UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER, amount, randomNumber, operationDate,
				Integer.parseInt(UC2_EXTERNAL_PAYMENT_NUMBER));
		Response response = null;
		try {

			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post(paymentUrl);
			errorMessage.setMessage(response.getBody().as(PaymentResponseDTO.class).getErrorMessage().getMessage());
			paymentResponseDTO = new PaymentResponseDTO();
			paymentResponseDTO.setPaymentNumber(response.getBody().as(PaymentResponseDTO.class).getPaymentNumber());
			paymentResponseDTO.setResult(response.getBody().as(PaymentResponseDTO.class).getResult());
			paymentResponseDTO.setErrorMessage(errorMessage);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			uc2CreatePaymentOneShotWithLargerAmountWithNotNullResponse(response);
		}
	}

	/**
	 * use case2 refund payment amount exceeding limit validation, use case2 refund
	 * payment amount exceeding limit response body validation, use case2 refund
	 * payment amount exceeding limit message validation
	 *
	 */
	public static void uc2CreatePaymentOneShotWithLargerAmountWithNotNullResponse(Response response)
			throws URISyntaxException {
		String amountLarge = "Amount too large";
		LOGGER.info("           USE CASE 2 CREATE PAYMENT ONE SHOT WITH AMOUNT VALUE GREATER THAN 6           ");
		LOGGER.info(RESPONSE + response.asString());
		LOGGER.info(STATUS_CODE + response.getStatusCode());

		TestRun testRun = apiIntegration.getTestRun(REFUND_PAYAPIAMOUNTLIMITEXCEEDINGVALIDATION, testExecutionid);
		if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
		else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
		assertEquals(response.getStatusCode(), 400);

		TestRun testRun2 = apiIntegration.getTestRun(REFUND_PAYAPIAMOUNTEXCEEDINGRESPONSEBODYVALIDATION,
				testExecutionid);
		if (error.equals(paymentResponseDTO.getResult()) && !"PASS".equals(testRun2.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun2.getId(), "PASS");
		else if (!error.equals(paymentResponseDTO.getResult()) && !"FAIL".equals(testRun2.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun2.getId(), "FAIL");
		assertEquals(paymentResponseDTO.getResult(), error);

		TestRun testRun3 = apiIntegration.getTestRun(REFUNDPAYAPI_AMOUNTLIMITEXCEEDINGMESSAGEVALIDATION,
				testExecutionid);
		if (amountLarge.equals(paymentResponseDTO.getErrorMessage().getMessage())
				&& !"PASS".equals(testRun3.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun3.getId(), "PASS");
		else if (!amountLarge.equals(paymentResponseDTO.getErrorMessage().getMessage())
				&& !"FAIL".equals(testRun3.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun3.getId(), "FAIL");
		assertEquals(paymentResponseDTO.getErrorMessage().getMessage(), amountLarge);

	}

	/**
	 * test case for use case2 create payment one shot with negative amount
	 *
	 */
	@Test(priority = 15)
	public static void uc2CreatePaymentOneShotWithNegativeAmount() throws URISyntaxException {
		String date = createPaymentDate;
		String operationDate = java.time.LocalDate.now() + date.substring(10);
		random = new Random();
		int randomNumber = random.nextInt(1000);
		int amount = -10;
		ErrorMessage errorMessage = new ErrorMessage();
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":%d,\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"source\":\"tok_mastercard\"}",
				UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER, amount, randomNumber, operationDate,
				Integer.parseInt(UC2_EXTERNAL_PAYMENT_NUMBER));
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post(paymentUrl);
			paymentResponseDTO = new PaymentResponseDTO();
			errorMessage.setMessage(response.getBody().as(PaymentResponseDTO.class).getErrorMessage().getMessage());
			paymentResponseDTO.setPaymentNumber(response.getBody().as(PaymentResponseDTO.class).getPaymentNumber());
			paymentResponseDTO.setResult(response.getBody().as(PaymentResponseDTO.class).getResult());
			paymentResponseDTO.setErrorMessage(errorMessage);

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			uc2CreatePaymentOneShotWithNegativeAmountWithNotNullResponse(response);
		}
	}

	/**
	 * use case2 create payment one shot negative amount validation, use case2
	 * create payment one shot negative amount response body validation, use case2
	 * create payment one shot negative amount message valoidation
	 *
	 */
	public static void uc2CreatePaymentOneShotWithNegativeAmountWithNotNullResponse(Response response)
			throws URISyntaxException {
		String parameterInvalid = "Parameter Invalid Integer";
		LOGGER.info("           USE CASE 2 CREATE PAYMENT ONE SHOT WITH NEGATIVE AMOUNT           ");
		LOGGER.info(RESPONSE + response.asString());
		LOGGER.info(STATUS_CODE + response.getStatusCode());

		TestRun testRun = apiIntegration.getTestRun(REFUND_PAYAPINEGATIVEAMOUNTVALIDATION, testExecutionid);
		if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
		else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
		assertEquals(response.getStatusCode(), 400);

		TestRun testRun2 = apiIntegration.getTestRun(REFUND_PAYAPINEGATIVEAMOUNTRESPONSEBODYVALIDATION,
				testExecutionid);
		if (error.equals(paymentResponseDTO.getResult()) && !"PASS".equals(testRun2.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun2.getId(), "PASS");
		else if (!error.equals(paymentResponseDTO.getResult()) && !"FAIL".equals(testRun2.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun2.getId(), "FAIL");
		assertEquals(paymentResponseDTO.getResult(), error);

		TestRun testRun3 = apiIntegration.getTestRun(REFUND_PAYAPINEGATIVEAMOUNTMESSAGEVALIDATION, testExecutionid);
		if (parameterInvalid.equals(paymentResponseDTO.getErrorMessage().getMessage())
				&& !"PASS".equals(testRun3.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun3.getId(), "PASS");
		else if (!parameterInvalid.equals(paymentResponseDTO.getErrorMessage().getMessage())
				&& !"FAIL".equals(testRun3.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun3.getId(), "FAIL");
		assertEquals(paymentResponseDTO.getErrorMessage().getMessage(), parameterInvalid);

	}

	/**
	 * test case for use case2 create payment one shot
	 *
	 */
	@Test(priority = 16)
	public static void uc2CreatePaymentOneShot() throws URISyntaxException {
		String date = createPaymentDate;
		String operationDate = java.time.LocalDate.now() + date.substring(10);
		random = new Random();
		int randomNumber = random.nextInt(1000);

		Integer amount = random.nextInt(1000);
		Integer externalPaymentNumber = random.nextInt(1000000000);

		RestAssured.baseURI = RESTASSURED_BASE_URL;
		/*
		 * String requestBody = String.format(
		 * "{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":%d,\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"source\":\"tok_mastercard\"}",
		 * UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER, Integer.parseInt(UC2_AMOUNT),
		 * randomNumber, operationDate, Integer.parseInt(UC2_EXTERNAL_PAYMENT_NUMBER));
		 */

		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":%d,\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"source\":\"tok_mastercard\"}",
				UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER, amount.intValue(), randomNumber, operationDate,
				externalPaymentNumber.intValue());

		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post(paymentUrl);
			paymentResponseDTO = new PaymentResponseDTO();
			paymentResponseDTO.setPaymentNumber(response.getBody().as(PaymentResponseDTO.class).getPaymentNumber());
			paymentResponseDTO.setResult(response.getBody().as(PaymentResponseDTO.class).getResult());
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			uc2CreatePaymentOneShot(response);
		}
	}

	/**
	 * use case2 create payment one shot refund api response code validation, use
	 * case2 create payment one shot refund api response result validation, use
	 * case2 create payment one shot refund api response body validation
	 *
	 */
	public static void uc2CreatePaymentOneShot(Response response) throws URISyntaxException {
		LOGGER.info("           USE CASE 2 CREATE PAYMENT ONE SHOT           ");
		LOGGER.info(RESPONSE + response.asString());
		LOGGER.info(STATUS_CODE + response.getStatusCode());

		TestRun testRun = apiIntegration.getTestRun(REFUND_PAYAPIRESPONSECODEVALIDATION, testExecutionid);
		if (201 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
		else if (201 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
		assertEquals(response.getStatusCode(), 201);

		TestRun testRun2 = apiIntegration.getTestRun(REFUND_PAYAPIRESPONSERESULTVALIDATION, testExecutionid);
		if ("OK".equals(paymentResponseDTO.getResult()) && !"PASS".equals(testRun2.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun2.getId(), "PASS");
		else if (!"OK".equals(paymentResponseDTO.getResult()) && !"FAIL".equals(testRun2.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun2.getId(), "FAIL");
		assertEquals(paymentResponseDTO.getResult(), "OK");

		TestRun testRun3 = apiIntegration.getTestRun(REFUND_PAYAPIRESPONSEBODYVALIDATION, testExecutionid);
		if (null != paymentResponseDTO.getPaymentNumber() && !"PASS".equals(testRun3.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun3.getId(), "PASS");
		else if (null == paymentResponseDTO.getPaymentNumber() && !"FAIL".equals(testRun3.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun3.getId(), "FAIL");
		assertNotNull(paymentResponseDTO.getPaymentNumber());
	}

	/**
	 * test case for use case2 amount refund
	 *
	 */
	@Test(priority = 17)
	public static void uc2AmountRefund() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"externalPaymentNumber\":0,\"gateway\":\"STRIPE\",\"paymentNumber\":\"%s\"}",
				UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER, paymentResponseDTO.getPaymentNumber());
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post(refundUrl);
			refundResponseDTO = new RefundResponseDTO();
			refundResponseDTO.setResult(response.getBody().as(RefundResponseDTO.class).getResult());
			refundResponseDTO.setRefundNumber(response.getBody().as(RefundResponseDTO.class).getRefundNumber());
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			uc2AmountRefundWithNotNullResponse(response);
		}
	}

	/**
	 * use case2 amount refund response code validation, use case2 amount refund
	 * response result validation, use case2 amount refund response body validation
	 *
	 */
	public static void uc2AmountRefundWithNotNullResponse(Response response) throws URISyntaxException {
		LOGGER.info("           USE CASE 2 AMOUNT REFUND           ");
		LOGGER.info(RESPONSE + response.asString());
		LOGGER.info(STATUS_CODE + response.getStatusCode());

		TestRun testRun = apiIntegration.getTestRun(REFUND_REFUNDAPIRESPONSECODEVALIDATION, testExecutionid);
		if (201 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
		else if (201 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
		assertEquals(response.getStatusCode(), 201);

		TestRun testRun2 = apiIntegration.getTestRun(REFUND_REFUNDAPIRESPONSERESULTVALIDATION, testExecutionid);
		if ("OK".equals(refundResponseDTO.getResult()) && !"PASS".equals(testRun2.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun2.getId(), "PASS");
		else if (!"OK".equals(refundResponseDTO.getResult()) && !"FAIL".equals(testRun2.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun2.getId(), "FAIL");
		assertEquals(refundResponseDTO.getResult(), "OK");

		TestRun testRun3 = apiIntegration.getTestRun(REFUND_REFUNDAPIRESPONSEBODYVALIDATION, testExecutionid);
		if (null != refundResponseDTO.getRefundNumber() && !"PASS".equals(testRun3.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun3.getId(), "PASS");
		else if (null == refundResponseDTO.getRefundNumber() && !"FAIL".equals(testRun3.getStatus()))
			apiIntegration.updateTestCaseStatus(testRun3.getId(), "FAIL");
		assertNotNull(refundResponseDTO.getRefundNumber());
	}

	/**
	 * test case for use case2 amount already refunded
	 *
	 */
	@Test(priority = 18)
	public static void uc2AmountRefundAlreadyRefunded() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		ErrorMessage errorMessage = new ErrorMessage();
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"externalPaymentNumber\":0,\"gateway\":\"STRIPE\",\"paymentNumber\":\"%s\"}",
				UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER, paymentResponseDTO.getPaymentNumber());
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post(refundUrl);
			refundResponseDTO = new RefundResponseDTO();
			refundResponseDTO.setResult(response.getBody().as(RefundResponseDTO.class).getResult());
			errorMessage.setMessage(response.getBody().as(RefundResponseDTO.class).getErrorMessage().getMessage());
			refundResponseDTO.setRefundNumber(response.getBody().as(RefundResponseDTO.class).getRefundNumber());
			refundResponseDTO.setErrorMessage(errorMessage);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			String chargeRefunded = "Charge Already Refunded";
			LOGGER.info("           USE CASE 2 AMOUNT REFUND ALREADY REFUNDED           ");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());

			TestRun testRun = apiIntegration.getTestRun(REFUND_REFUNDAPIALREADYREFUNDEDPAYMENTVALIDATION,
					testExecutionid);
			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
			assertEquals(refundResponseDTO.getResult(), error);

			TestRun testRun2 = apiIntegration.getTestRun(REFUND_REFUNDAPIALREADYREFUNDEDPAYMENTMESSAGEVALIDATION,
					testExecutionid);
			if (chargeRefunded.equals(refundResponseDTO.getErrorMessage().getMessage())
					&& !"PASS".equals(testRun2.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun2.getId(), "PASS");
			else if (!chargeRefunded.equals(refundResponseDTO.getErrorMessage().getMessage())
					&& !"FAIL".equals(testRun2.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun2.getId(), "FAIL");
			assertEquals(refundResponseDTO.getErrorMessage().getMessage(), chargeRefunded);
		}
	}

	/**
	 * test case for use case2 amount refund with invalid payment number
	 *
	 */
	@Test(priority = 19)
	public static void uc2AmountRefundWithInvalidPaymentNumber() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"externalPaymentNumber\":0,\"gateway\":\"STRIPE\",\"paymentNumber\":\"test\"}",
				UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER);

		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post(refundUrl);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("           USE CASE 2 REFUND WITH INVALID PAYMENT NUMBER           ");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(REFUND_REFUNDAPIINVALIDPAYMENTNUMBERVALIDATION,
					testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case2 amount refund with wrong url
	 *
	 */
	@Test(priority = 20)
	public static void uc2AmountRefundWithWrongURL() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"externalPaymentNumber\":0,\"gateway\":\"STRIPE\",\"paymentNumber\":\"test\"}",
				UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER);
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/refundss");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("           USE CASE 2 REFUND WITH WRONG URL           ");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(REFUND_REFUNDAPIINVALIDRESPONSECODEVALIDATION, testExecutionid);

			if (404 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (404 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case2 create payment one shot with account name=null
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 21)
	public static void uc2CreatePaymentOneShotWithNullAccountName() throws URISyntaxException {
		String date = createPaymentDate;
		String operationDate = java.time.LocalDate.now() + date.substring(10);
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":%d,\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"source\":\"tok_mastercard\"}",
				null, UC2_ACCOUNT_NUMBER, Integer.parseInt(UC2_AMOUNT), 12, operationDate,
				Integer.parseInt(UC2_EXTERNAL_PAYMENT_NUMBER));
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/payments");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 2 CREATE PAYMENT ONE SHOT WITH ACCOUNT NAME=NULL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(REFUND_PAYAPI_ACCOUNTNAMEASEMPTY, testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case2 create payment one shot with account number=null
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 22)
	public static void uc2CreatePaymentOneShotWithNullAccountNumber() throws URISyntaxException {
		String date = createPaymentDate;
		String operationDate = java.time.LocalDate.now() + date.substring(10);
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":%d,\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"source\":\"tok_mastercard\"}",
				UC2_ACCOUNT_NAME, null, Integer.parseInt(UC2_AMOUNT), 12, operationDate,
				Integer.parseInt(UC2_EXTERNAL_PAYMENT_NUMBER));
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/payments");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 2 CREATE PAYMENT ONE SHOT WITH ACCOUNT NUMBER=NULL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(REFUND_PAYAPI_ACCOUNTNUMBERASEMPTY, testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case2 create payment one shot with source=null
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 23)
	public static void uc2CreatePaymentOneShotWithNullSource() throws URISyntaxException {
		String date = createPaymentDate;
		String operationDate = java.time.LocalDate.now() + date.substring(10);
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":%d,\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"source\":\"\"}",
				UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER, Integer.parseInt(UC2_AMOUNT), 12, operationDate,
				Integer.parseInt(UC2_EXTERNAL_PAYMENT_NUMBER));
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/payments");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 2 CREATE PAYMENT ONE SHOT WITH SOURCE = NULL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(REFUND_PAYAPI_SOURCEASEMPTY, testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case2 amount refund with account name=null
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 24)
	public static void uc2AmountRefundWithNullAccountName() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		uc2CreatePaymentOneShot();
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"externalPaymentNumber\":0,\"gateway\":\"STRIPE\",\"paymentNumber\":\"%s\"}",
				null, UC2_ACCOUNT_NUMBER,paymentResponseDTO.getPaymentNumber());
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/refunds");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 2 REFUND WITH ACCOUNT NAME=NULL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			
			TestRun testRun = apiIntegration.getTestRun(REFUND_REFUNDAPI_ACCOUNTNAMEASEMPTY, testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case2 amount refund with external Payment Number=null
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 25)
	public static void uc2AmountRefundWithNullAccountNumber() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		uc2CreatePaymentOneShot();
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"externalPaymentNumber\":null,\"gateway\":\"STRIPE\",\"paymentNumber\":\"%s\"}",
				UC2_ACCOUNT_NAME, null,paymentResponseDTO.getPaymentNumber());
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).body(requestBody).post("/refunds");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info("***********USE CASE 2 REFUND WITH EXTERNAL PAYEMENT NUMBER = NULL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(REFUND_REFUNDAPI_EXTERNALPAYMENTNUMBERASEMPTY, testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case3 get settlemets
	 *
	 */
	@Test(priority = 26)
	public static void uc3GetSettlements() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		int sizeOfList = 0;
		int sizeOfList1 = 0;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).when().get("/settlements");
			sizeOfList = response.body().path("list.size()");
			LOGGER.info("           USE CASE 3 GET SETTLEMENTS           ");
			LOGGER.info(sizeOfList + " settlements before hitting payment oneshot");
			uc2CreatePaymentOneShot();
			response = RestAssured.given().contentType(ContentType.JSON).when().get("/settlements");
			sizeOfList1 = response.body().path("list.size()");
			LOGGER.info(sizeOfList1
					+ " settlements after hitting payment oneshot              ________________           ");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info(STATUS_CODE + response.getStatusCode());

			TestRun testRun = apiIntegration.getTestRun(GETSETTLEMENTSAPI_RESPONSECODEVALIDATION, testExecutionid);
			if (200 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (200 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 200);

			TestRun testRun2 = apiIntegration.getTestRun(GETSETTLEMENTSAPI_COUNTVALIDATION, testExecutionid);
			if (sizeOfList1 == sizeOfList + 1 && !"PASS".equals(testRun2.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun2.getId(), "PASS");
			else if (sizeOfList1 != sizeOfList + 1 && !"FAIL".equals(testRun2.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun2.getId(), "FAIL");
			assertEquals(sizeOfList1, sizeOfList + 1);
		}
	}

	/**
	 * test case for use case3 get settlemets with wrong url
	 *
	 */
	@Test(priority = 27)
	public static void uc3GetSettlementsWithWrongURL() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).when().get("/settlementss");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(GETSETTLEMENTSAPI_INVALIDRESPONSECODEVALIDATION,
					testExecutionid);

			if (404 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (404 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case4 get financial movements
	 *
	 */
	@Test(priority = 28)
	public static void uc4GetFinancialMovements() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		int sizeOfList1 = 0;
		int sizeOfList2 = 0;
		try {
			String date = constantDate;
			String startDate = java.time.LocalDate.now() + date.substring(10);
			LOGGER.info("           USE CASE 4 GET FINANCIAL MOVEMENTS           ");
			response = RestAssured.given().pathParams(gateway, "1", startedDate, startDate)
					.contentType(ContentType.JSON).when()
					.get("/account-financial-movements?gateway={gateway}&startDate={startDate}");
			String id = response.getBody().as(FinancialMovementsDTO.class).getAccountFinancialMovements().get(0)
					.getId();
			uc2CreatePaymentOneShot();

			response = RestAssured.given().pathParams(gateway, "1", startedDate, startDate)
					.contentType(ContentType.JSON).when()
					.get("/account-financial-movements?gateway={gateway}&startDate={startDate}&endingBefore=" + id);
			sizeOfList1 = response.body().path("accountFinancialMovements.size()");
			LOGGER.info(sizeOfList1
					+ " account-financial-movements before hitting paymentOneShot               ________________           ");
			uc2CreatePaymentOneShot();

			response = RestAssured.given().pathParams(gateway, "1", startedDate, startDate)
					.contentType(ContentType.JSON).when()
					.get("/account-financial-movements?gateway={gateway}&startDate={startDate}&endingBefore=" + id);
			sizeOfList2 = response.body().path("accountFinancialMovements.size()");
			LOGGER.info(sizeOfList2
					+ " account-financial-movements after hitting paymentOneShot **************________________***********");

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info(STATUS_CODE + response.getStatusCode());

			TestRun testRun = apiIntegration.getTestRun(GETFINANCIALMOVEMENTSAPI_RESPONSECODEVALIDATION,
					testExecutionid);
			if (200 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (200 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 200);

			TestRun testRun2 = apiIntegration.getTestRun(GETFINANCIALMOVEMENTSAPI_COUNTVALIDATION, testExecutionid);
			if (sizeOfList2 == sizeOfList1 + 1 && !"PASS".equals(testRun2.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun2.getId(), "PASS");
			else if (sizeOfList2 != sizeOfList1 + 1 && !"FAIL".equals(testRun2.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun2.getId(), "FAIL");
			assertEquals(sizeOfList2, sizeOfList1 + 1);
		}
	}

	/**
	 * test case for use case4 get financial movements with wrong url
	 *
	 */
	@Test(priority = 29)
	public static void uc4GetFinancialMovementsWithWrongURL() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		try {
			String date = constantDate;
			String startDate = java.time.LocalDate.now() + date.substring(10);
			LOGGER.info("           USE CASE 4 GET FINANCIAL MOVEMENTS WITH WRONG URL           ");

			response = RestAssured.given().pathParams(gateway, "1", "size", "50", startedDate, startDate)
					.contentType(ContentType.JSON).when()
					.get("/account-financial-movementss?gateway={gateway}&size={size}&startDate={startDate}");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(GETFINANCIALMOVEMENTSAPI_INVALIDRESPONSECODEVALIDATION,
					testExecutionid);

			if (404 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (404 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case4 get financial movements with invalid size
	 *
	 */
	@Test(priority = 30)
	public static void uc4GetFinancialMovementsWithInvalidSize() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		try {
			String date = constantDate;
			String startDate = java.time.LocalDate.now() + date.substring(10);
			LOGGER.info("           USE CASE 4 GET FINANCIAL MOVEMENTS WITH INVALID SIZE           ");

			response = RestAssured.given().pathParams(gateway, "1", "size", "a", startedDate, startDate)
					.contentType(ContentType.JSON).when()
					.get("/account-financial-movements?gateway={gateway}&size={size}&startDate={startDate}");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(GETFINANCIALMOVEMENTSAPI_INVALIDPARAMETERVALIDATION,
					testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case4 get financial movements with invalid page number
	 * @throws URISyntaxException 
	 *
	 */
	@Test(priority = 31)
	public static void uc4GetFinancialMovementsWithInvalidPageNumber() throws URISyntaxException {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		try {
			String date = constantDate;
			String startDate = java.time.LocalDate.now() + date.substring(10);
			LOGGER.info("***********USE CASE 4 GET FINANCIAL MOVEMENTS WITH INVALID PAGE NUMBER***********");
			response = RestAssured.given().pathParams(gateway, "1", "size", "50", startedDate, startDate, "page", "a")
					.contentType(ContentType.JSON).when()
					.get("/account-financial-movements?gateway={gateway}&size={size}&startDate={startDate}&page={page}");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			TestRun testRun = apiIntegration.getTestRun(GETFINANCIALMOVEMENTSAPI_INVALIDPAGE,
					testExecutionid);

			if (400 == response.getStatusCode() && !"PASS".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "PASS");
			else if (400 != response.getStatusCode() && !"FAIL".equals(testRun.getStatus()))
				apiIntegration.updateTestCaseStatus(testRun.getId(), "FAIL");
			assertEquals(response.getStatusCode(), 400);
		}
	}
	/**
	 * To send Xray execution report to client
	 *
	 */
	@AfterSuite
	public void afterAllTest() throws URISyntaxException {
		List<TestCase> testExecution = XrayAPIIntegration.getTestExecution(testExecutionid);
		List<BugDTO> bugDTOList = new ArrayList<>();
		testExecution.forEach(a -> {
			BugDTO bugDTO = new BugDTO();
			bugDTO.setTestStatus(a.getStatus());
			bugDTO.setTestCaseId(a.getKey());
			bugDTO.setTestCaseLink(BASE_URL + "/browse/" + a.getKey());
			bugDTOList.add(bugDTO);
		});
		try {
			TestRun response = apiIntegration.getTestRun(testExecution.get(0).getKey(), testExecutionid);
			ReportDTO reportDTO = new ReportDTO();
			reportDTO.setProjectName(PROJECT_NAME);
			reportDTO.setIssueId(testExecutionid);
			reportDTO.setDescription(createIssueDTO.getDescription());
			reportDTO.setSummary(createIssueDTO.getSummary());
			reportDTO.setStartedDate(response.getStartedOn());
			reportDTO.setEndDate(testExecution.get(43).getFinishedOn());
			reportDTO.setJasperBugDTO(bugDTOList);
			reportDTO.setAssignee("assignee");
			reportDTO.setExecutedBy("ThinkPalm");
			reportDTO.setIssueIdLink(BASE_URL + "/browse/" + testExecutionid);
			reportDTO.setXrayLink(
					(BASE_URL + XRAY_LINK).replace("selectedProjectKey=", "selectedProjectKey=" + PROJECT_ID));
			Mail test1 = new Mail();
			VelocityContext context = new VelocityContext();
			int totalTestCases = 0;
			if (!bugDTOList.isEmpty()) {
				totalTestCases = bugDTOList.size();
			}
			int passCount = (int) bugDTOList.stream().filter(a -> a.getTestStatus().equalsIgnoreCase("PASS")).count();
			int failCount = (int) bugDTOList.stream().filter(a -> a.getTestStatus().equalsIgnoreCase("FAIL")).count();
			int bugCount = (int) bugDTOList.stream().filter(a -> a.getBugLink() != null).count();
			List<BugDTO> uc1List = bugDTOList.subList(0,14);
			List<BugDTO> uc2List = bugDTOList.subList(14, 36);
			List<BugDTO> uc3List = bugDTOList.subList(36, 39);
			List<BugDTO> uc4List = bugDTOList.subList(39, 44);

			Date startedDateTime = reportDTO.getStartedDate();
			Instant instant = startedDateTime.toInstant();
			LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String formattedDate = localDateTime.format(myFormatObj);

			Date finishedDateTime = reportDTO.getEndDate();
			Instant finishedInstant = finishedDateTime.toInstant();
			LocalDateTime finishedLocalDateTime = finishedInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
			DateTimeFormatter myFormatObj1 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String finishedDate = finishedLocalDateTime.format(myFormatObj1);

			context.put("projectName", reportDTO.getProjectName());
			context.put("issueId", reportDTO.getIssueId());
			context.put("summary", reportDTO.getSummary());
			context.put("description", reportDTO.getDescription());
			context.put("startedDate", formattedDate + " GMT");
			context.put("endDate", finishedDate + " GMT");
			context.put("executedBy", reportDTO.getExecutedBy());
			context.put("assignee", reportDTO.getAssignee());
			context.put("totalTestCases", totalTestCases);
			context.put("passCount", passCount);
			context.put("failCount", failCount);
			context.put("bugCount", bugCount);
			context.put("xrayLink", reportDTO.getXrayLink());
			context.put("issueLink", reportDTO.getIssueIdLink());
			context.put("uc1List", uc1List);
			context.put("uc2List", uc2List);
			context.put("uc3List", uc3List);
			context.put("uc4List", uc4List);
			test1.sendEmailWithTemplate("A-to-Be Xray Test Execution Report-RUN", Arrays.asList(MAIL_TO),
					"templates/xray_report.vm", context);
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		}
	}

}