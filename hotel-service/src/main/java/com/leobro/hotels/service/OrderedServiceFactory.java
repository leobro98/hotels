package com.leobro.hotels.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Concrete factory to be used in run-time in place of the abstract factory. It knows all implementations of
 * the {@link HotelService} interface and returns one of them depending on the passed type.
 * <p>
 * Properly speaking, it is not a factory, as it doesn't create the services. However, I didn't want to create
 * a service and parse JSON at every request, so I've passed this work to Spring. Autowired beans are all of
 * the same {@link HotelService} type, but are distinguished by their names.
 */
@Component
class OrderedServiceFactory extends ServiceFactory {

	private static final String ORDER_BY_NONE = "none";
	private static final String ORDER_BY_PARTNER = "by-partner";
	private static final String ORDER_BY_PRICE = "by-price";

	private HotelService unorderedService;
	private HotelService partnerOrderedService;
	private HotelService priceOrderedService;

	@Autowired
	public OrderedServiceFactory(@Qualifier("unorderedHotelService") HotelService unorderedService,
								 @Qualifier("partnerNameOrderedHotelService") HotelService partnerOrderedService,
								 @Qualifier("priceOrderedHotelService") HotelService priceOrderedService) {
		this.unorderedService = unorderedService;
		this.partnerOrderedService = partnerOrderedService;
		this.priceOrderedService = priceOrderedService;
	}

	@Override
	List<String> getServiceTypes() {
		return Arrays.asList(ORDER_BY_NONE, ORDER_BY_PARTNER, ORDER_BY_PRICE);
	}

	@Override
	HotelService getHotelService(String serviceType) {
		switch (serviceType) {
			case ORDER_BY_PARTNER:
				return partnerOrderedService;
			case ORDER_BY_PRICE:
				return priceOrderedService;
			case ORDER_BY_NONE:
			default:
				return unorderedService;
		}
	}
}
