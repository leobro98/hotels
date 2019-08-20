package com.leobro.hotels.rest;

import com.leobro.hotels.service.SearchService;
import com.leobro.hotels.service.ServiceResponse;
import com.leobro.hotels.service.model.Hotel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HotelControllerTest {

	private static final String ORDERING_URL = "/hotels/ordering";
	private static final String HOTELS_URL = "/hotels/for-city";

	private static final String MESSAGE_ERROR = "Service error";
	private static final String MESSAGE_UNKNOWN = "Unknown city";

	private static final String CITY_PARAMETER = "city";
	private static final String ORDER_BY_PARAMETER = "order-by";
	private static final String NAME_PATH = "$.name";

	private static final List<String> ORDERING_TYPES = Arrays.asList("type1", "type2");
	private static final String SOME_CITY = "some city";
	private static final String SOME_NAME = "Some hotel name";
	private static final String SOME_ORDERING = "ordering";

	@MockBean
	private SearchService service;

	@Autowired
	private MockMvc mvc;

	@Test
	public void when_getOrderingTypes_andException_then_statusServerError() throws Exception {
		given(service.getOrderingTypes())
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.FATAL, MESSAGE_ERROR));
		mvc.perform(
				MockMvcRequestBuilders.get(ORDERING_URL)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(MESSAGE_ERROR));
	}

	@Test
	public void when_getOrderingTypes_then_statusOkAndValuesFromService() throws Exception {
		given(service.getOrderingTypes())
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.OK, ORDERING_TYPES));
		mvc.perform(
				MockMvcRequestBuilders.get(ORDERING_URL)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", is(ORDERING_TYPES)));
	}

	@Test
	public void when_getHotels_andException_then_statusServerError() throws Exception {
		given(service.getHotels(anyString(), anyString()))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.FATAL, MESSAGE_ERROR));
		mvc.perform(
				MockMvcRequestBuilders.get(HOTELS_URL)
						.param(CITY_PARAMETER, SOME_CITY)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError())
				.andExpect(content().string(MESSAGE_ERROR));
	}

	@Test
	public void when_getHotels_andCityUnknown_then_statusBadRequest() throws Exception {
		given(service.getHotels(anyString(), anyString()))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.ERROR, MESSAGE_UNKNOWN));
		mvc.perform(
				MockMvcRequestBuilders.get(HOTELS_URL)
						.param(CITY_PARAMETER, SOME_CITY)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(content().string(MESSAGE_UNKNOWN));
	}

	@Test
	public void when_getHotels_then_statusOkAndValuesFromService() throws Exception {
		given(service.getHotels(anyString(), anyString()))
				.willReturn(new ServiceResponse(
						ServiceResponse.ResultType.OK, createTestHotel()));
		mvc.perform(
				MockMvcRequestBuilders.get(HOTELS_URL)
						.param(CITY_PARAMETER, SOME_CITY)
						.param(ORDER_BY_PARAMETER, SOME_ORDERING)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath(NAME_PATH, is(SOME_NAME)));
	}

	private Hotel createTestHotel() {
		Hotel hotel = new Hotel();
		hotel.setName(SOME_NAME);
		return hotel;
	}
}
