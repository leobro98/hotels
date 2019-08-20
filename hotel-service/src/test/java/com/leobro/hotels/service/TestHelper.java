package com.leobro.hotels.service;

import com.leobro.hotels.service.model.City;
import com.leobro.hotels.service.model.Hotel;
import com.leobro.hotels.service.model.Partner;
import com.leobro.hotels.service.model.Price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class TestHelper {
	private static final String CITY = "Hamburg";
	private static final int CITY_ID = 1;

	private static final String HOTEL_NAME_1 = "Good Hotel of ";
	private static final String HOTEL_ADDRESS_1 = "Good Address of ";
	private static final String HOTEL_NAME_2 = "Superior Hotel of ";
	private static final String HOTEL_ADDRESS_2 = "Super Address of ";

	private static final String PARTNER_NAME_1 = "C Partner of ";
	private static final String PARTNER_NAME_2 = "A Partner of ";
	private static final String PARTNER_NAME_3 = "B Partner of ";

	static List<Hotel> createHotels() {
		City city = createCity();
		ArrayList<Hotel> hotels = new ArrayList<>();

		Hotel hotel1 = createHotel(city, HOTEL_NAME_1, HOTEL_ADDRESS_1);
		Partner partner1 = createPartner(hotel1, PARTNER_NAME_1);
		partner1.getPrices().add(createPrice(partner1, BigDecimal.valueOf(3)));
		partner1.getPrices().add(createPrice(partner1, BigDecimal.valueOf(1)));
		Partner partner2 = createPartner(hotel1, PARTNER_NAME_2);
		partner2.getPrices().add(createPrice(partner2, BigDecimal.valueOf(5)));
		hotel1.getPartners().add(partner1);
		hotel1.getPartners().add(partner2);
		hotels.add(hotel1);

		Hotel hotel2 = createHotel(city, HOTEL_NAME_2, HOTEL_ADDRESS_2);
		Partner partner3 = createPartner(hotel2, PARTNER_NAME_3);
		partner3.getPrices().add(createPrice(partner3, BigDecimal.valueOf(2)));
		partner3.getPrices().add(createPrice(partner3, BigDecimal.valueOf(4)));
		hotel2.getPartners().add(partner3);
		hotels.add(hotel2);

		return hotels;
	}

	private static City createCity() {
		City city = new City();
		city.setName(CITY);
		city.setId(CITY_ID);
		return city;
	}

	private static Hotel createHotel(City city, String hotelName, String hotelAddress) {
		Hotel hotel = new Hotel();
		hotel.setCity(city);
		hotel.setName(hotelName + city.getName());
		hotel.setAddress(hotelAddress + city.getName());
		return hotel;
	}

	private static Partner createPartner(Hotel hotel, String partnerName) {
		Partner partner = new Partner();
		partner.setHotel(hotel);
		partner.setName(partnerName + hotel.getName());
		return partner;
	}

	private static Price createPrice(Partner partner, BigDecimal amount) {
		Price price = new Price();
		price.setPartner(partner);
		price.setAmount(amount);
		return price;
	}
}
