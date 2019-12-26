/*
 * ThinkPalm, Technologies Pvt Ltd ("COMPANY") CONFIDENTIAL
 * Copyright (c) 2019-2020 [ThinkPalm, Inc.], All Rights Reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

package com.test.xrayapis;

/**
 * Xray API url for integration
 * 
 * @author nasia.t
 *
 */
public class XrayAPIs {
	private XrayAPIs() {

	}

	public static final String TEST_EXECUTION_GET_URL = "/rest/raven/1.0/api/testexec/execid/test?detailed=true";
	public static final String TEST_RUN_STATUS_PUT_URL = "/rest/raven/1.0/api/testrun/id/status";
	public static final String TEST_RUN_GET_URL = "/rest/raven/1.0/api/testrun";
	public static final String UPDATE_ISSUE_URL = "/rest/api/2/issue/issueId/transitions";
	public static final String GET_ISSUE_URL = "/rest/api/latest/search?jql=project=projectName AND issuetype =Bug&fields=id,key,description,summary&maxResults=";
	public static final String GET_ISSUE = "/rest/api/2/issue/issueId?fields=status";
	public static final String COMMENT_URL = "/rest/api/2/issue/issueId/comment";

}
