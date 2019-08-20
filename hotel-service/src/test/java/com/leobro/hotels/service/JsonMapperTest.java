package com.leobro.hotels.service;

import com.leobro.hotels.service.model.City;
import com.leobro.hotels.service.model.Hotel;
import com.leobro.hotels.service.model.Partner;
import com.leobro.hotels.service.model.Price;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class JsonMapperTest {

	// the file is in another module, so relative path to it should be given
	private static final String JSON_FILE_NAME = "../rest/src/main/resources/datasource.json";

	private JsonMapper mapper;

	@Before
	public void setUp() throws IOException {
		mapper = new JsonMapper(JSON_FILE_NAME){
			// the file can not be read as a resource because it is in another module
			@Override
			public String readFileAsString(String fileName) throws IOException {
				return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
			}
		};
	}

	@Test
	public void when_getCities_then_cityIsMapped() {
		List<City> cities = mapper.getCities();

		assertThat(cities.size(), greaterThan(0));
		assertNotNull(cities.get(0));
		assertThat(cities.get(0).getName(), is("Düsseldorf"));
	}

	@Test
	public void when_getCities_then_hotelsAreMapped() {
		List<City> cities = mapper.getCities();

		List<Hotel> hotels = cities.get(0).getHotels();
		assertNotNull(hotels);
		assertThat(hotels.size(), greaterThan(0));
		assertNotNull(hotels.get(0));
		assertThat(hotels.get(0).getName(), is("Hilton Düsseldorf"));
		assertThat(hotels.get(0).getAddress(), is("Georg-Glock-Straße 20, 40474 Düsseldorf"));
	}

	@Test
	public void when_getCities_then_partnersAreMapped() {
		List<City> cities = mapper.getCities();

		List<Partner> partners = cities.get(0).getHotels().get(0).getPartners();
		assertNotNull(partners);
		assertThat(partners.size(), greaterThan(0));
		assertNotNull(partners.get(0));
		assertThat(partners.get(0).getName(), is("Booking.com"));
		assertThat(partners.get(0).getHomePage(), is("http://www.booking.com"));
	}

	@Test
	public void when_getCities_then_pricesAreMapped() {
		List<City> cities = mapper.getCities();

		List<Price> prices = cities.get(0).getHotels().get(0).getPartners().get(0).getPrices();
		assertNotNull(prices);
		assertThat(prices.size(), greaterThan(0));
		assertThat(prices.get(0).getDescription(), is("Single Room"));
		assertThat(prices.get(0).getAmount().intValue(), is(125));
		assertThat(prices.get(0).getFromDate().toString(), is("2012-10-12"));
		assertThat(prices.get(0).getToDate().toString(), is("2012-10-13"));
	}
}
