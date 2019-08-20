package com.leobro.hotels.service;

/**
 * Creates the response from the service to its client.
 */
class ResponseFactory {

	static final String MESSAGE_ERROR = "Service error";

	/**
	 * Creates the response of successful processing.
	 *
	 * @param payload the content of the response.
	 * @return The service response from the successful operation.
	 */
	static ServiceResponse createOkResponse(Object payload) {
		return new ServiceResponse(ServiceResponse.ResultType.OK, payload);
	}

	/**
	 * Creates the response of a handled error, usually caused by wrong input data.
	 *
	 * @param errorMessage the explanation of the error.
	 * @return The service response without a useful results caused by a wrong request.
	 */
	static ServiceResponse createErrorResponse(String errorMessage) {
		return new ServiceResponse(ServiceResponse.ResultType.ERROR, errorMessage);
	}

	/**
	 * Creates the response about some unexpected error in the service.
	 *
	 * @return The service response telling about an error on the service side.
	 */
	static ServiceResponse createFatalResponse() {
		return new ServiceResponse(ServiceResponse.ResultType.FATAL, MESSAGE_ERROR);
	}
}
