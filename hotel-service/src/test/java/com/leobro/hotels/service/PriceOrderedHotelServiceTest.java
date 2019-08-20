package com.leobro.hotels.service;

import com.leobro.hotels.service.model.Hotel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class PriceOrderedHotelServiceTest {

	private PriceOrderedHotelService service;
	private List<Hotel> hotels;

	@Before
	public void setUp() {
		JsonMapper jsonMapper = Mockito.mock(JsonMapper.class);
		service = new PriceOrderedHotelService(jsonMapper);
		hotels = TestHelper.createHotels();
	}

	private BigDecimal getAmount(List<Hotel> sorted, int hotelIndex) {
		return sorted.get(hotelIndex).getPartners().get(0).getPrices().get(0).getAmount();
	}

	@Test
	public void when_sort_then_sortedByPriceAmount() {
		List<Hotel> sorted = service.sort(hotels);

		assertThat(sorted.size(), is(5));
		assertThat(getAmount(sorted, 0), lessThanOrEqualTo(getAmount(sorted, 1)));
		assertThat(getAmount(sorted, 1), lessThanOrEqualTo(getAmount(sorted, 2)));
		assertThat(getAmount(sorted, 2), lessThanOrEqualTo(getAmount(sorted, 3)));
		assertThat(getAmount(sorted, 3), lessThanOrEqualTo(getAmount(sorted, 4)));
	}
}