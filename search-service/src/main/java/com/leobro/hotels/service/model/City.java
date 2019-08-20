package com.leobro.hotels.service.model;

import java.util.ArrayList;
import java.util.List;

public class City {

	private String name;
	private int id;
	private List<Hotel> hotels = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Hotel> getHotels() {
		return hotels;
	}

	public void setHotels(List<Hotel> hotels) {
		this.hotels = hotels;
	}
}
