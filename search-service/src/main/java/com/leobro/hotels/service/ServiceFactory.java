package com.leobro.hotels.service;

import java.util.List;

/**
 * Abstract factory to get the service of a desired type. It is used at the compile time by the classes
 * of this component. At the run time its usages are substituted by auto-wired instances of a concrete
 * factory. In order to let consuming components know the implemented service types, the factory includes
 * {@code getServiceTypes()} method.
 */
abstract class ServiceFactory {

	/**
	 * Returns all possible values for {@code serviceType} parameter of {@code getServiceTypes()} method.
	 * @return names by which implemented services can be retrieved.
	 */
	abstract List<String> getServiceTypes();

	/**
	 * Returns one of the implemented services according to {@code serviceType}.
	 * @param serviceType name of an implemented service.
	 * @return the service implementing the desired ordering.
	 */
	abstract HotelService getHotelService(String serviceType);
}
