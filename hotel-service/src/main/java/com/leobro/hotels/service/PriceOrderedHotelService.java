package com.leobro.hotels.service;

import com.leobro.hotels.service.model.Hotel;
import com.leobro.hotels.service.model.Partner;
import com.leobro.hotels.service.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements {@link HotelService} interface and returns results ordered by the prices amount.
 * <p>
 * As the list of hotels is required, but sorting is made by leaves of the structure tree, the tree is
 * "stripped" from leaves to the trunk. That is, each hotel in the output structure has a single partner
 * having a single price. This "stripping" is possible due to reverse dependencies in the original structure.
 */
@Service
public class PriceOrderedHotelService extends UnorderedHotelService {

	@Autowired
	PriceOrderedHotelService(JsonMapper jsonMapper) {
		super(jsonMapper);
	}

	@Override
	public List<Hotel> getHotelsForCity(final String cityName) throws InvalidParameterException {
		return super.getHotelsForCity(cityName);
	}

	@Override
	protected List<Hotel> sort(final List<Hotel> hotels) {
		List<Hotel> sortedHotels = hotels.stream()
				.map(Hotel::getPartners)
				.flatMap(List::stream)
				.map(Partner::getPrices)
				.flatMap(List::stream)
				.sorted(Comparator.comparing(Price::getAmount))
				.map(price -> cloneHotel(price.getPartner().getHotel(),
						clonePartner(price.getPartner(),
								clonePrice(price))))
				.collect(Collectors.toList());

		return sortedHotels;
	}

	/**
	 * The cloning removes reverse dependency from the entity.
	 *
	 * @param thickPartner original partner with the reverse dependency to the hotel,
	 * @param price        cloned price without the reverse dependency the partner.
	 * @return Copy of the original partner without dependency to a hotel.
	 */
	private Partner clonePartner(Partner thickPartner, Price price) {
		Partner partner = new Partner();
		partner.setName(thickPartner.getName());
		partner.setHomePage(thickPartner.getHomePage());
		partner.setPrices(Collections.singletonList(price));
		return partner;
	}
}
