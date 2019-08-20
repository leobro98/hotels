package com.leobro.hotels.rest;

import com.leobro.hotels.service.ServiceResponse;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ResponseFactoryTest {

	private static final String PAYLOAD = "I am payload";

	@Test
	public void when_resultOk_then_status200() {
		ServiceResponse response = new ServiceResponse(ServiceResponse.ResultType.OK, PAYLOAD);

		ResponseEntity<?> entity = ResponseFactory.createResponse(response);

		assertThat(entity.getStatusCode(), is(HttpStatus.OK));
		assertThat(entity.getBody(), is(PAYLOAD));
	}

	@Test
	public void when_resultError_then_status400() {
		ServiceResponse response = new ServiceResponse(ServiceResponse.ResultType.ERROR, PAYLOAD);

		ResponseEntity<?> entity = ResponseFactory.createResponse(response);

		assertThat(entity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(entity.getBody(), is(PAYLOAD));
	}

	@Test
	public void when_resultFatal_then_status500() {
		ServiceResponse response = new ServiceResponse(ServiceResponse.ResultType.FATAL, PAYLOAD);

		ResponseEntity<?> entity = ResponseFactory.createResponse(response);

		assertThat(entity.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
		assertThat((entity.getBody()), is(PAYLOAD));
	}
}