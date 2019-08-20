package com.leobro.hotels.service;

import com.leobro.hotels.service.model.Hotel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class PartnerNameOrderedHotelServiceTest {

	private PartnerNameOrderedHotelService service;
	private List<Hotel> hotels;

	@Before
	public void setUp() {
		JsonMapper jsonMapper = Mockito.mock(JsonMapper.class);
		service = new PartnerNameOrderedHotelService(jsonMapper);
		hotels = TestHelper.createHotels();
	}

	private String getName(List<Hotel> sorted, int hotelIndex) {
		return sorted.get(hotelIndex).getPartners().get(0).getName();
	}

	@Test
	public void when_sort_then_sortedByPartnerName() {
		List<Hotel> sorted = service.sort(hotels);

		assertThat(sorted.size(), is(3));
		assertThat(getName(sorted, 0), lessThanOrEqualTo(getName(sorted, 1)));
		assertThat(getName(sorted, 1), lessThanOrEqualTo(getName(sorted, 2)));
	}
}
