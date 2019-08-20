package com.leobro.hotels.service;

import com.leobro.hotels.service.model.Hotel;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * The implementation is responsible for resolving the hotel of the city from the
 * given city name. The second responsibility is to sort the returning result from the partner
 * service in whatever way.
 * <p>
 * This breaks with the rule of the separation of concerns, but for this test case we want to
 * keep it simple.
 */
public interface HotelService {

	/**
	 * Returns the list of hotels in the specified city.
	 *
	 * @param cityName name of the city to search for.
	 * @return The list of hotels in the given city.
	 * @throws InvalidParameterException if the city name is unknown.
	 */
	List<Hotel> getHotelsForCity(final String cityName) throws InvalidParameterException;
}
