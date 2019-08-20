package com.leobro.hotels.service;

import com.leobro.hotels.service.model.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

import static com.leobro.hotels.service.ResponseFactory.*;

/**
 * The service abstracts clients from the hotel-service component by making use of the {@link ServiceFactory}
 * abstract factory and {@link HotelService} interface. It also handles exceptions from the hotel-service
 * and sets information about them into the {@link ServiceResponse}.
 */
@Service
public class SearchService {

	private ServiceFactory serviceFactory;

	@Autowired
	SearchService(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	/**
	 * Returns the list of hotels in the specified city sorted according to the specified ordering.
	 *
	 * @param city    the name of the city which hotels are requested,
	 * @param orderBy the name of the ordering type for the hotels.
	 * @return List of hotels in the city ordered as specified.
	 */
	public ServiceResponse getHotels(String city, String orderBy) {
		try {
			HotelService service = serviceFactory.getHotelService(orderBy);
			List<Hotel> hotels = service.getHotelsForCity(city);

			return createOkResponse(hotels);
		} catch (InvalidParameterException e) {
			return createErrorResponse(e.getMessage());
		} catch (Exception e) {
			return createFatalResponse();
		}
	}

	/**
	 * Returns all possible ordering types that can be used in the {@code orderBy} parameter of the
	 * {@code getHotels} method.
	 *
	 * @return The list of ordering names.
	 */
	public ServiceResponse getOrderingTypes() {
		try {
			List<String> types = serviceFactory.getServiceTypes();

			return createOkResponse(types);
		} catch (Exception e) {
			return createFatalResponse();
		}
	}
}
