package com.leobro.hotels.service;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PartnerValidatorTest {

	private static final String CORRECT_ADDRESS = "http://www.test.org";
	private static final String EMPTY = "";

	@Test
	public void when_homePage_IsValid_then_returnsOriginal() {
		String homePage = PartnerValidator.validateHomePage(CORRECT_ADDRESS);

		assertThat(homePage, is(CORRECT_ADDRESS));
	}

	@Test
	public void when_homePage_containsWrongSchema_then_returnsEmpty() {
		String homePage = PartnerValidator.validateHomePage("ftp://www.test");

		assertThat(homePage, is(EMPTY));
	}

	@Test
	public void when_homePage_containsSpaces_then_returnsEmpty() {
		String homePage = PartnerValidator.validateHomePage("ftp://www.test com");

		assertThat(homePage, is(EMPTY));
	}

	@Test
	public void when_homePage_containsOrphanComa_then_returnsEmpty() {
		String homePage = PartnerValidator.validateHomePage("http://.com");
		assertThat(homePage, is(EMPTY));

		homePage = PartnerValidator.validateHomePage("http://com.");
		assertThat(homePage, is(EMPTY));
	}

	@Test
	public void when_homePage_containsNoDomain_then_returnsEmpty() {
		String homePage = PartnerValidator.validateHomePage("http://");
		assertThat(homePage, is(EMPTY));

		homePage = PartnerValidator.validateHomePage("http://..");
		assertThat(homePage, is(EMPTY));
	}

	@Test
	public void when_homePage_containsWrongSemicolon_then_returnsEmpty() {
		String homePage = PartnerValidator.validateHomePage("http://www:test.com");

		assertThat(homePage, is(EMPTY));
	}
	@Test
	public void when_homePage_containsWrongSeparator_then_returnsEmpty() {
		String homePage = PartnerValidator.validateHomePage("http:/test.com");
		assertThat(homePage, is(EMPTY));

		homePage = PartnerValidator.validateHomePage("http:test.com");
		assertThat(homePage, is(EMPTY));
	}
}
