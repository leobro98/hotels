package com.leobro.hotels.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SearchServiceTest {

	private static final String CITY = "Some City";
	private static final String ORDER_BY = "some order";

	private SearchService service;
	private ServiceFactory factory;
	private HotelService hotelService;

	@Before
	public void setUp() {
		hotelService = Mockito.mock(HotelService.class);
		factory = Mockito.mock(ServiceFactory.class);
		Mockito.when(factory.getHotelService(Mockito.anyString())).thenReturn(hotelService);
		service = new SearchService(factory);
	}

	@Test
	public void when_getHotels_then_factoryAndHotelServiceAreCalled() {
		service.getHotels(CITY, ORDER_BY);

		Mockito.verify(factory).getHotelService(ORDER_BY);
		Mockito.verify(hotelService).getHotelsForCity(CITY);
	}

	@Test
	public void when_getOrderingTypes_then_factoryIsCalled() {
		service.getOrderingTypes();

		Mockito.verify(factory).getServiceTypes();
	}
}
