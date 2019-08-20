package com.leobro.hotels.service;

import com.leobro.hotels.service.model.City;
import com.leobro.hotels.service.model.Hotel;
import com.leobro.hotels.service.model.Partner;
import com.leobro.hotels.service.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The class implements {@link HotelService} interface and returns the results without any specific ordering.
 * <p>
 * When created by Spring during the startup of the application, gets a reference to the hotel data from the
 * {@link JsonMapper} and hold them for future requests.
 */
@Service
public class UnorderedHotelService implements HotelService {

	private static final String MESSAGE_UNKNOWN_CITY = "The city is unknown!";

	final List<City> cities;

	@Autowired
	UnorderedHotelService(JsonMapper jsonMapper) {
		cities = jsonMapper.getCities();
	}

	@Override
	public List<Hotel> getHotelsForCity(final String cityName) throws InvalidParameterException {
		List<Hotel> hotels = findCityHotels(cityName);
		return sort(hotels);
	}

	private List<Hotel> findCityHotels(final String cityName) throws InvalidParameterException {
		Optional<City> city = cities.stream()
				.filter(c -> cityName.equalsIgnoreCase(c.getName()))
				.findAny();

		if (city.isPresent()) {
			return city.get().getHotels();
		} else {
			throw new InvalidParameterException(MESSAGE_UNKNOWN_CITY);
		}
	}

	/**
	 * Sorts the passed list of hotels according to the inheritor's specifics.
	 *
	 * @param hotels the list of hotels to be sorted.
	 * @return The sorted list of hotels.
	 */
	protected List<Hotel> sort(final List<Hotel> hotels) {
		return hotels;
	}

	/**
	 * The cloning removes reverse dependency from the entity.
	 *
	 * @param thickPrice original price with the reverse dependency to a partner.
	 * @return Copy of the original price without dependency to a partner.
	 */
	protected Price clonePrice(Price thickPrice) {
		Price price = new Price();
		price.setDescription(thickPrice.getDescription());
		price.setAmount(thickPrice.getAmount());
		price.setFromDate(thickPrice.getFromDate());
		price.setToDate(thickPrice.getToDate());
		return price;
	}

	/**
	 * The cloning removes reverse dependency from the entity.
	 *
	 * @param thickHotel original hotel with the reverse dependency to a city.
	 * @param partner    clone of the original partner.
	 * @return Copy of the original hotel without dependency to a city.
	 */
	protected Hotel cloneHotel(Hotel thickHotel, Partner partner) {
		Hotel hotel = new Hotel();
		hotel.setName(thickHotel.getName());
		hotel.setAddress(thickHotel.getAddress());
		hotel.setPartners(Collections.singletonList(partner));
		return hotel;
	}
}
