package com.leobro.hotels.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leobro.hotels.service.model.City;
import com.leobro.hotels.service.model.Hotel;
import com.leobro.hotels.service.model.Partner;
import com.leobro.hotels.service.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Loads JSON from the file in the {@code resources} folder and maps the data to data objects.
 */
@Component
public class JsonMapper {

	private static final String CITY_NAME = "city";
	private static final String CITY_ID = "id";
	private static final String CITY_HOTELS = "hotels";

	private static final String HOTEL_NAME = "name";
	private static final String HOTEL_ADDRESS = "adr";
	private static final String HOTEL_PARTNERS = "partners";

	private static final String PARTNER_NAME = "name";
	private static final String PARTNER_URL = "url";
	private static final String PARTNER_PRICES = "prices";

	private static final String PRICE_DESCRIPTION = "description";
	private static final String PRICE_AMOUNT = "amount";
	private static final String PRICE_DATE_FROM = "from";
	private static final String PRICE_DATE_TO = "to";

	private final List<City> cities;

	@Autowired
	JsonMapper(@Value("${data.source.file}") String jsonFileName) throws IOException {
		cities = importJson(jsonFileName);
	}

	/**
	 * Loads JSON from the file with the specified name from {@code resources} folder
	 * and maps the data to data objects.
	 *
	 * @param jsonFileName the name of the file with data in the {@code resources} folder.
	 * @return List of {@link City} objects containing the whole hierarchy of data from the file.
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	private List<City> importJson(String jsonFileName) throws IOException {
		String json = readFileAsString(jsonFileName);
		JsonNode node = new ObjectMapper().readTree(json);

		// The real structure of data regarding cities is unknown - are there several cities in one file
		// or there is one city per file. If there are several ones in one file, how are they structured,
		// e.g. as Map or as array. Therefore, the simplest approach is chosen here - just one city.
		return Collections.singletonList(mapCity(node));
	}

	/**
	 * Reads whole content of the file from the {@code resources} folder into a string.
	 *
	 * @param fileName the name of the input file.
	 * @return The string containing the contents of the file.
	 * @throws IOException if a low-level I/O problem (unexpected end-of-input, network error) occurs.
	 */
	protected String readFileAsString(String fileName) throws IOException {
		ClassPathResource resource = new ClassPathResource(fileName);
		byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
		return new String(bytes, StandardCharsets.UTF_8);
	}

	private City mapCity(JsonNode node) {
		City city = new City();

		city.setName(node.get(CITY_NAME).asText());
		city.setId(node.get(CITY_ID).asInt());
		city.setHotels(mapHotels(node, city));

		return city;
	}

	private List<Hotel> mapHotels(JsonNode node, City city) {
		List<Hotel> hotels = new ArrayList<>();
		Map<String, Object> mapOfHotels = new ObjectMapper()
				.convertValue(node.get(CITY_HOTELS),
						new TypeReference<Map<String, Object>>() {});

		for (String key : mapOfHotels.keySet()) {
			Map<?, ?> hotelMap = (Map<?, ?>) mapOfHotels.get(key);

			Hotel hotel = new Hotel();
			hotel.setCity(city);
			hotel.setName((String) hotelMap.get(HOTEL_NAME));
			hotel.setAddress((String) hotelMap.get(HOTEL_ADDRESS));
			hotel.setPartners(mapPartners(hotelMap, hotel));

			hotels.add(hotel);
		}
		return hotels;
	}

	private List<Partner> mapPartners(Map<?, ?> hotelMap, Hotel hotel) {
		ArrayList<Partner> partners = new ArrayList<>();
		Map<?, ?> mapOfPartners = (Map<?, ?>) hotelMap.get(HOTEL_PARTNERS);

		for (Object key : mapOfPartners.keySet()) {
			Map<?, ?> partnerMap = (Map<?, ?>) mapOfPartners.get(key);

			Partner partner = new Partner();
			partner.setHotel(hotel);
			partner.setName((String) partnerMap.get(PARTNER_NAME));
			partner.setHomePage(PartnerValidator.validateHomePage((String) partnerMap.get(PARTNER_URL)));
			partner.setPrices(mapPrices(partnerMap, partner));

			partners.add(partner);
		}
		return partners;
	}

	private List<Price> mapPrices(Map<?, ?> partnerMap, Partner partner) {
		ArrayList<Price> prices = new ArrayList<>();
		Map<?, ?> mapOfPrices = (Map<?, ?>) partnerMap.get(PARTNER_PRICES);

		for (Object key : mapOfPrices.keySet()) {
			Map<?, ?> priceMap = (Map<?, ?>) mapOfPrices.get(key);

			Price price = new Price();
			price.setPartner(partner);
			price.setDescription((String) priceMap.get(PRICE_DESCRIPTION));
			price.setAmount(BigDecimal.valueOf(new Long((Integer) priceMap.get(PRICE_AMOUNT))));
			price.setFromDate(LocalDate.parse((String) priceMap.get(PRICE_DATE_FROM)));
			price.setToDate(LocalDate.parse((String) priceMap.get(PRICE_DATE_TO)));

			prices.add(price);
		}
		return prices;
	}

	List<City> getCities() {
		return cities;
	}
}
