package com.leobro.hotels.service;

import com.leobro.hotels.service.model.Hotel;
import com.leobro.hotels.service.model.Partner;
import com.leobro.hotels.service.model.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements {@link HotelService} interface and returns results ordered by the name of the partner.
 * <p>
 * As the list of hotels is required, but sorting is made by the second level of the structure tree, the tree is
 * "stripped" from partners to hotels. That is, each hotel in the output structure has a single partner, but
 * hotels in the list are not unique. This "stripping" is possible due to reverse dependencies in the original
 * structure.
 */
@Service
public class PartnerNameOrderedHotelService extends UnorderedHotelService {

	@Autowired
	PartnerNameOrderedHotelService(JsonMapper jsonMapper) {
		super(jsonMapper);
	}

	@Override
	public List<Hotel> getHotelsForCity(String cityName) throws InvalidParameterException {
		return super.getHotelsForCity(cityName);
	}

	@Override
	protected List<Hotel> sort(List<Hotel> hotels) {
		List<Hotel> sortedHotels = hotels.stream()
				.map(Hotel::getPartners)
				.flatMap(List::stream)
				.sorted(Comparator.comparing(Partner::getName))
				.map(partner -> cloneHotel(partner.getHotel(),
						clonePartner(partner)))
				.collect(Collectors.toList());

		return sortedHotels;
	}

	/**
	 * The cloning removes reverse dependencies from the entities.
	 *
	 * @param thickPartner original partner with the reverse dependency to a hotel.
	 * @return Copy of the original partner without dependency to a hotel.
	 */
	private Partner clonePartner(Partner thickPartner) {
		Partner partner = new Partner();
		partner.setName(thickPartner.getName());
		partner.setHomePage(thickPartner.getHomePage());

		for (Price price : thickPartner.getPrices()) {
			partner.getPrices().add(clonePrice(price));
		}
		return partner;
	}
}
