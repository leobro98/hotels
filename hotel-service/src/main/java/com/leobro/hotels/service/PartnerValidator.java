package com.leobro.hotels.service;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validator for the {@link com.leobro.hotels.service.model.Partner} entity.
 */
class PartnerValidator {

	private static Logger LOG = LoggerFactory.getLogger(PartnerValidator.class);

	/**
	 * Validates the {@code homePage} property.
	 *
	 * @param homePage the home page address to be validated.
	 * @return The passed home page address if it is valid; otherwise, the empty string.
	 */
	static String validateHomePage(String homePage) {
		String[] schemes = {"http", "https"};
		UrlValidator validator = new UrlValidator(schemes);

		if (validator.isValid(homePage)) {
			return homePage;
		} else {
			LOG.warn("Invalid HomePage: " + homePage);
			return "";
		}
	}
}
