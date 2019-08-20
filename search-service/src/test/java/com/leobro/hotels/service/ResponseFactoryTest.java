package com.leobro.hotels.service;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ResponseFactoryTest {

	private static final String PAYLOAD = "Some payload";

	@Test
	public void when_createOkResponse_then_resultOk() {
		ServiceResponse response = ResponseFactory.createOkResponse(PAYLOAD);

		assertThat(response.getResult(), is(ServiceResponse.ResultType.OK));
		assertThat(response.getPayload(), is(PAYLOAD));
	}

	@Test
	public void when_createErrorResponse_then_resultError() {
		ServiceResponse response = ResponseFactory.createErrorResponse(PAYLOAD);

		assertThat(response.getResult(), is(ServiceResponse.ResultType.ERROR));
		assertThat(response.getPayload(), is(PAYLOAD));
	}

	@Test
	public void when_createFatalResponse_then_resultFatal() {
		ServiceResponse response = ResponseFactory.createFatalResponse();

		assertThat(response.getResult(), is(ServiceResponse.ResultType.FATAL));
		assertThat(response.getPayload(), is(ResponseFactory.MESSAGE_ERROR));
	}
}