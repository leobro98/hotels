package com.leobro.hotels.rest;

import com.leobro.hotels.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller of the REST Web service. This is the first class hit by the client's request.
 * It returns results as JSON or XML string depending on the HTTP Accept header of the request.
 */
@RestController
@RequestMapping(path = "/hotels")
public class HotelController {

	private final SearchService service;

	@Autowired
	public HotelController(SearchService service) {
		this.service = service;
	}

	/**
	 * Returns the list of hotels, containing the room prices, in the given city. The results are sorted
	 * with the specified ordering type. If the ordering type is omitted, the results are not sorted.
	 * @param city    the name of the city, which hotels should be returned. If the city is not known to the
	 *                system, status 400 (Bad Request) is returned.
	 * @param orderBy the ordering type for the results. If the type is not known to the system or missing,
	 *                the results are not sorted.
	 * @return The list of hotels in the passed city sorted according to the passed ordering type.
	 */
	@GetMapping("/for-city")
	public ResponseEntity<?> getHotels(@RequestParam(name = "city") String city,
									   @RequestParam(name = "order-by", defaultValue = "none") String orderBy) {
		return ResponseFactory.createResponse(
				service.getHotels(city, orderBy));
	}

	/**
	 * Returns the list of names for all implemented types of results ordering. The names can be used
	 * in requests to {@code getHotels} method.
	 *
	 * @return The list of all ordering types.
	 */
	@GetMapping("/ordering")
	public ResponseEntity<?> getOrderingTypes() {
		return ResponseFactory.createResponse(
				service.getOrderingTypes());
	}
}
