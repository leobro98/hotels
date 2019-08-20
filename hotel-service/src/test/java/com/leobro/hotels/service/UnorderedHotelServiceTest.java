package com.leobro.hotels.service;

import com.leobro.hotels.service.model.City;
import com.leobro.hotels.service.model.Hotel;
import com.leobro.hotels.service.model.Partner;
import com.leobro.hotels.service.model.Price;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class UnorderedHotelServiceTest {

	private static final String UNKNOWN_CITY = "München";
	private static final String CITY = "Hamburg";
	private static final int CITY_ID = 1;
	private static final String ANOTHER_CITY = "Berlin";
	private static final int ANOTHER_CITY_ID = 2;

	private static final String HOTEL_NAME_PREFIX = "Test Hotel of ";
	private static final String HOTEL_ADDRESS_PREFIX = "Test Address of ";
	private static final String PARTNER_NAME_PREFIX = "Test Partner of ";

	private UnorderedHotelService service;
	private JsonMapper jsonMapper;

	@Before
	public void setUp() {
		jsonMapper = Mockito.mock(JsonMapper.class);
		Mockito.when(jsonMapper.getCities()).thenReturn(createCities());

		service = new UnorderedHotelService(jsonMapper);
	}

	private List<City> createCities() {
		ArrayList<City> cities = new ArrayList<>();
		cities.add(createCity(CITY, CITY_ID));
		cities.add(createCity(ANOTHER_CITY, ANOTHER_CITY_ID));
		return cities;
	}

	private City createCity(String name, int id) {
		City city = new City();
		city.setName(name);
		city.setId(id);
		city.setHotels(createHotels(city));
		return city;
	}

	private List<Hotel> createHotels(City city) {
		ArrayList<Hotel> hotels = new ArrayList<>();

		Hotel hotel = new Hotel();
		hotel.setCity(city);
		hotel.setName(HOTEL_NAME_PREFIX + city.getName());
		hotel.setAddress(HOTEL_ADDRESS_PREFIX + city.getName());
		hotel.setPartners(createPartners(hotel));

		hotels.add(hotel);
		return hotels;
	}

	private List<Partner> createPartners(Hotel hotel) {
		ArrayList<Partner> partners = new ArrayList<>();

		Partner partner = new Partner();
		partner.setHotel(hotel);
		partner.setName(PARTNER_NAME_PREFIX + hotel.getName());
		partner.setPrices(createPrices(partner));

		partners.add(partner);
		return partners;
	}

	private List<Price> createPrices(Partner partner) {
		ArrayList<Price> prices = new ArrayList<>();

		Price price = new Price();
		price.setPartner(partner);
		price.setAmount(BigDecimal.ONE);

		prices.add(price);
		return prices;
	}

	@Test
	public void when_constructed_then_callsJsonMapper() {
		Mockito.verify(jsonMapper).getCities();
	}

	@Test
	public void when_constructed_then_citiesAreFilled() {
		assertThat(service.cities.size(), greaterThan(0));
	}

	@Test(expected = InvalidParameterException.class)
	public void when_getHotelsForCity_andCityIsNotKnown_then_throwsException() {
		service.getHotelsForCity(UNKNOWN_CITY);
	}

	@Test
	public void when_getHotelsForCity_andCityIsKnown_then_returnsHotelsOfTheCity() {
		List<Hotel> hotels = service.getHotelsForCity(CITY);

		assertThat(hotels.size(), greaterThan(0));
		assertNotNull(hotels.get(0));
		assertThat(hotels.get(0).getName(), containsString(CITY));
		assertThat(hotels.get(0).getAddress(), containsString(CITY));
	}

	@Test
	public void when_getHotelsForCity_andCityIsKnown_then_returnsHotelsPartnersPrices() {
		List<Hotel> hotels = service.getHotelsForCity(CITY);

		assertNotNull(hotels.get(0));
		assertNotNull(hotels.get(0).getPartners().get(0));
		assertNotNull(hotels.get(0).getPartners().get(0).getPrices().get(0));
	}
}
