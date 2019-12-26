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
import java.util.Random;
import java.util.ResourceBundle;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.test.dto.CustomerAccountsResponseDTO;
import com.test.dto.FinancialMovementsDTO;
import com.test.dto.PaymentResponseDTO;
import com.test.dto.RefundResponseDTO;
import com.test.model.ErrorMessage;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Implements API automation without jira Xray integartion
 * 
 * @author nasia.t
 *
 */
public class DryRun {

	private static final Logger LOGGER = LogManager.getLogger(DryRun.class);
	private static final String RESPONSE = "Response :";
	private static final String STATUS_CODE = "Status Code :";
	private static final ResourceBundle rbb = ResourceBundle.getBundle("application");
	private static final String RESTASSURED_BASE_URL = rbb.getString("restAssuredBaseUrl");
	private static final String UC1_ACCOUNT_NAME = rbb.getString("uc1.account.name");
	private static final String UC1_ACCOUNT_NUMBER = rbb.getString("uc1.account.number");
	private static final String UC1_EMAIL = rbb.getString("uc1.email");
	private static final String UC1_OPERATION_DATE = rbb.getString("uc1.operationDate");
	private static final String UC1_EXTERNAL_PAYMENT_NUMBER = rbb.getString("uc1.externalPaymentNumber");
	private static final String UC1_AMOUNT = rbb.getString("uc1.amount");
	private static final String UC2_ACCOUNT_NAME = rbb.getString("uc2.account.name");
	private static final String UC2_ACCOUNT_NUMBER = rbb.getString("uc2.account.number");
	private static final String UC2_EXTERNAL_PAYMENT_NUMBER = rbb.getString("uc2.externalPaymentNumber");
	private static final String UC2_AMOUNT = rbb.getString("uc2.amount");
	private static CustomerAccountsResponseDTO customerAccountsResponseDTO;
	private static PaymentResponseDTO paymentResponseDTO;
	private static RefundResponseDTO refundResponseDTO;
	private static String paymentUrl = "/payments";
	private static String refundUrl = "/refunds";
	private static String startedDate = "startDate";
	private static String createPaymentDate = "2019-10-14T02:01:01Z";
	private static String constantDate = "2019-10-14T01:01:01Z";
	private static String error = "ERROR";
	private static String gateway = "gateway";
	private static Random random = null;

	/**
	 * test case for use case1 create account and pay with wrong url
	 *
	 */
	@Test(priority = 0)
	public static void uc1CreateAccountAndPayWithWrongURL() {
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
			LOGGER.info("***********USE CASE 1 CREATE ACCOUNT WITH WRONG URL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case1 create account and pay with invalid gateway
	 *
	 */
	@Test(priority = 1)
	public static void uc1CreateAccountAndPayWithInvalidGateway() {
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
			LOGGER.info("***********USE CASE 1 CREATE ACCOUNT WITH INVALID GATEWAY***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case1 create account and pay with account name=null
	 *
	 */
	@Test(priority = 2)
	public static void uc1CreateAccountAndPayWithNullAccountName() {
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case1 create account and pay with account number=null
	 *
	 */
	@Test(priority = 3)
	public static void uc1CreateAccountAndPayWithNullAccountNumber() {
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case1 create account and pay
	 *
	 */
	@Test(priority = 4)
	public static void uc1CreateAccountAndPay() {
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
			LOGGER.info("***********USE CASE 1 CREATE ACCOUNT***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 201);
			assertEquals(customerAccountsResponseDTO.getResult(), "OK");
			assertNotNull(customerAccountsResponseDTO.getCustomerToken());
		}
	}

	
	/**
	 * test case for use case1 payment account with account name=null
	 *
	 */
	@Test(priority = 5)
	public static void uc1CreatePayementWithNullAccountName() {
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case1 create payment with account number=null
	 *
	 */
	@Test(priority = 6)
	public static void uc1CreatePaymentWithNullAccountNumber() {
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case1 create account and pay with customer token=null
	 *
	 */
	@Test(priority = 7)
	public static void uc1CreateAccountAndPayWithNullCustomerToken() {
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case1 create payment with currency as uppercase
	 *
	 */
	@Test(priority = 8)
	public static void uc1CreatePaymentWithCurrencyUpperCase() {
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case1 create payment
	 *
	 */
	@Test(priority = 9)
	public static void uc1CreatePayment() {
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
			LOGGER.info("***********USE CASE 1 CREATE PAYMENT***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 201);
			assertEquals(paymentResponseDTO.getResult(), "OK");
			assertNotNull(paymentResponseDTO.getPaymentNumber());
		}
	}



	/**
	 * test case for use case1 create payment with wrong url
	 *
	 */
	@Test(priority = 10)
	public static void uc1CreatePaymentWithWrongURL() {
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
			LOGGER.info("***********USE CASE 1 CREATE PAYMENT WITH WRONG URL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case2 create payment one shot with wrong url
	 *
	 */
	@Test(priority = 11)
	public static void uc2CreatePaymentOneShotWithWrongURL() {
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
			LOGGER.info("***********USE CASE 2 CREATE PAYMENT ONE SHOT WITH WRONG URL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case2 create payment one shot with larger amount
	 *
	 */
	@Test(priority = 12)
	public static void uc2CreatePaymentOneShotWithLargerAmount() {
		String date = createPaymentDate;
		String operationDate = java.time.LocalDate.now() + date.substring(10);
		random = new Random();
		int randomNumber = random.nextInt(1000);
		int amount = 99999999;
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
			LOGGER.info("***********USE CASE 2 CREATE PAYMENT ONE SHOT WITH AMOUNT VALUE GREATER THAN 6***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 400);
			assertEquals(paymentResponseDTO.getResult(), error);
			assertEquals(paymentResponseDTO.getErrorMessage().getMessage(), "Amount too large");
		}
	}

	/**
	 * test case for use case2 create payment one shot with negative amount
	 *
	 */
	@Test(priority = 13)
	public static void uc2CreatePaymentOneShotWithNegativeAmount() {
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
			LOGGER.info("***********USE CASE 2 CREATE PAYMENT ONE SHOT WITH NEGATIVE AMOUNT***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 400);
			assertEquals(paymentResponseDTO.getResult(), error);
			assertEquals(paymentResponseDTO.getErrorMessage().getMessage(), "Parameter Invalid Integer");
		}
	}

	/**
	 * test case for use case2 create payment one shot
	 *
	 */
	@Test(priority = 14)
	public static void uc2CreatePaymentOneShot() {
		String date = createPaymentDate;
		String operationDate = java.time.LocalDate.now() + date.substring(10);
		random = new Random();
		int randomNumber = random.nextInt(1000);

		Integer amount = random.nextInt(1000);
		Integer externalPaymentNumber = random.nextInt(1000000000);

		RestAssured.baseURI = RESTASSURED_BASE_URL;

		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":%d,\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"source\":\"tok_mastercard\"}",
				UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER, amount.intValue(), randomNumber, operationDate,
				externalPaymentNumber.intValue());

		/*
		 * String requestBody = String.format(
		 * "{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"amount\":%d,\"currency\":\"usd\",\"seed\":%d,\"operationType\":\"INVOICE\",\"operationDate\":\"%s\",\"externalPaymentNumber\":%d,\"source\":\"tok_mastercard\"}",
		 * UC2_ACCOUNT_NAME, UC2_ACCOUNT_NUMBER, Integer.parseInt(UC2_AMOUNT),
		 * randomNumber, operationDate, Integer.parseInt(UC2_EXTERNAL_PAYMENT_NUMBER));
		 */
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
			LOGGER.info("***********USE CASE 2 CREATE PAYMENT ONE SHOT***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 201);
			assertEquals(paymentResponseDTO.getResult(), "OK");
			assertNotNull(paymentResponseDTO.getPaymentNumber());
		}
	}

	/**
	 * test case for use case2 create payment one shot with account name=null
	 *
	 */
	@Test(priority = 15)
	public static void uc2CreatePaymentOneShotWithNullAccountName() {
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case2 create payment one shot with account number=null
	 *
	 */
	@Test(priority = 16)
	public static void uc2CreatePaymentOneShotWithNullAccountNumber() {
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case2 create payment one shot with source=null
	 *
	 */
	@Test(priority = 17)
	public static void uc2CreatePaymentOneShotWithNullSource() {
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case2 amount refund
	 *
	 */
	@Test(priority = 18)
	public static void uc2AmountRefund() {
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
			LOGGER.info("***********USE CASE 2 AMOUNT REFUND***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 201);
			assertEquals(refundResponseDTO.getResult(), "OK");
			assertNotNull(refundResponseDTO.getRefundNumber());
		}

	}

	/**
	 * test case for use case2 amount refund that has alreay refunded
	 *
	 */
	@Test(priority = 19)
	public static void uc2AmountRefundAlreadyRefunded() {
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
			LOGGER.info("***********USE CASE 2 AMOUNT REFUND ALREADY REFUNDED***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 400);
			assertEquals(refundResponseDTO.getResult(), error);
			assertEquals(refundResponseDTO.getErrorMessage().getMessage(), "Charge Already Refunded");
		}

	}

	/**
	 * test case for use case2 amount refund with invalid parameter
	 *
	 */
	@Test(priority = 20)
	public static void uc2AmountRefundWithInvalidPaymentNumber() {
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
			LOGGER.info("***********USE CASE 2 REFUND WITH INVALID PAYMENT NUMBER***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 400);
		}

	}

	/**
	 * test case for use case2 amount refund with wrong url
	 *
	 */
	@Test(priority = 21)
	public static void uc2AmountRefundWithWrongURL() {
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
			LOGGER.info("***********USE CASE 2 REFUND WITH WRONG URL***********");
			LOGGER.info(RESPONSE + response.asString());
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case2 amount refund with account name=null
	 *
	 */
	@Test(priority = 22)
	public static void uc2AmountRefundWithNullAccountName() {
		uc2CreatePaymentOneShot();
		RestAssured.baseURI = RESTASSURED_BASE_URL;
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case2 amount refund with external Payment Number=null
	 *
	 */
	@Test(priority = 23)
	public static void uc2AmountRefundWithNullAccountNumber() {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		uc2CreatePaymentOneShot();
		String requestBody = String.format(
				"{\"accountName\":\"%s\",\"accountNumber\":\"%s\",\"externalPaymentNumber\":0,\"gateway\":\"STRIPE\",\"paymentNumber\":\"%s\"}",
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
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case3 get settlements
	 *
	 */
	@Test(priority = 24)
	public static void uc3GetSettlements() {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		int sizeOfList = 0;
		int sizeOfList1 = 0;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).when().get("/settlements");
			sizeOfList = response.body().path("list.size()");
			LOGGER.info("***********USE CASE 3 GET SETTLEMENTS***********");
			LOGGER.info(sizeOfList + " settlements before hitting payment oneshot");
			uc2CreatePaymentOneShot();
			response = RestAssured.given().contentType(ContentType.JSON).when().get("/settlements");
			sizeOfList1 = response.body().path("list.size()");
			LOGGER.info(sizeOfList1 + " settlements after hitting payment oneshot");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 200);
			assertEquals(sizeOfList1, sizeOfList + 1);
		}
	}

	/**
	 * test case for use case3 get settlements with wrong url
	 *
	 */
	@Test(priority = 25)
	public static void uc3GetSettlementsWithWrongURL() {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		try {
			response = RestAssured.given().contentType(ContentType.JSON).when().get("/fsettlementss");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case4 get financial movements
	 *
	 */
	@Test(priority = 26)
	public static void uc4GetFinancialMovements() {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		int sizeOfList1 = 0;
		int sizeOfList2 = 0;
		try {
			String date = constantDate;
			String startDate = java.time.LocalDate.now() + date.substring(10);
			LOGGER.info("***********USE CASE 4 GET FINANCIAL MOVEMENTS***********");
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
					+ " account-financial-movements before hitting paymentOneShot **************________________***********");
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
			assertEquals(response.getStatusCode(), 200);
			assertEquals(sizeOfList2, sizeOfList1 + 1);
		}
	}

	/**
	 * test case for use case4 get financial movements with wrong url
	 *
	 */
	@Test(priority = 27)
	public static void uc4GetFinancialMovementsWithWrongURL() {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		try {
			String date = constantDate;
			String startDate = java.time.LocalDate.now() + date.substring(10);
			LOGGER.info("***********USE CASE 4 GET FINANCIAL MOVEMENTS WITH WRONG URL***********");
			response = RestAssured.given().pathParams(gateway, "1", "size", "50", startedDate, startDate)
					.contentType(ContentType.JSON).when()
					.get("/account-financial-mrgovementss?gateway={gateway}&size={size}&startDate={startDate}");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 404);
		}
	}

	/**
	 * test case for use case4 get financial movements with invalid size
	 *
	 */
	@Test(priority = 28)
	public static void uc4GetFinancialMovementsWithInvalidSize() {
		RestAssured.baseURI = RESTASSURED_BASE_URL;
		Response response = null;
		try {
			String date = constantDate;
			String startDate = java.time.LocalDate.now() + date.substring(10);
			LOGGER.info("***********USE CASE 4 GET FINANCIAL MOVEMENTS WITH INVALID SIZE***********");
			response = RestAssured.given().pathParams(gateway, "1", "size", "a", startedDate, startDate)
					.contentType(ContentType.JSON).when()
					.get("/account-financial-movements?gateway={gateway}&size={size}&startDate={startDate}");
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		if (response != null) {
			LOGGER.info(STATUS_CODE + response.getStatusCode());
			assertEquals(response.getStatusCode(), 400);
		}
	}

	/**
	 * test case for use case4 get financial movements with invalid page number
	 *
	 */
	@Test(priority = 29)
	public static void uc4GetFinancialMovementsWithInvalidPageNumber() {
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
			assertEquals(response.getStatusCode(), 400);
		}
	}
}