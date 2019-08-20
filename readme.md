# Hotel Search Service

This is the test assignment accomplishment for implementing the hotel search service. The assignment itself can be found in the `hotels-task.md` file. The interface to be implemented is `HotelService` in `search-service` module and the data source file is `rest/src/main/resources/datasource.json`.

## Architecture

The application consists of three components: `rest`, `search-service` and `hotel-service` and is built with Spring Boot. The core component is `search-service`, two other components are plug-ins for it.

### Rest

`rest` is the "main" application partition and packages other components together with itself into the executable `jar` file. As it is the main component, it holds references to all other components in the application. It has also direct reference to the `SearchService` class in the `search-service` component.

### Search Service

The `search-service` component contains definitions of business objects (`City`, `Hotel`, `Partner` and `Price`) and the `HotelService` interface. It contains also abstract factory `ServiceFactory`.

The `ServiceFactory` has the method `getServiceTypes()` which returns a list of strings – the names of all possible ordering of results, which correspond to implementations of the `HotelService` interface. It has also `getService(String)` method which returns one of the `HotelService` implementations according to the passed ordering. The `SearchService` uses this implementation to get the list of hotels with the needed ordering.

### Hotel Service

The `hotel-service` component contains implementations of the `HotelService` interface. It also contains concrete factory `OrderedServiceFactory` which inherits abstract `ServiceFactory`. The concrete factory knows the implementations of the `HotelService` and can return an instance of one of them according to the specified ordering as well as the list of implemented orderings.

Thus, the `hotel-service` component can be developed and deployed independently of the `search-service`. For example, if this component is packaged separately, new implementations of the `HotelService` for new types of ordering can be added, and the component can be deployed without changing and rebuilding of the `search-service`.

### Mapping JSON

On the application startup, an instance of the `JsonMapper` class is created as a Spring bean. At the class constructing, it loads the JSON from the data source file and maps it to business objects. The whole hierarchy of the objects is stored in the list of `City` objects. Then the bean is injected into implementations of the `HotelService` interface, and its `cities` field becomes available to them as a source data for serving requests.

### Validation

The validation of the partner's home page works during the mapping of the JSON data to the `Partner` entity. As this property supposedly is not very important to the user, it is left blank if the source data for it is invalid, and the warning is written to the log file for future checking.

## Building the application

To be sure that you have the correct version of Maven installed and set up, you can use Maven Wrapper contained in this project. Open the project folder (`hotels`). On Linux, you can run

```
./mvnw clean package
```

On Windows, you can run

```
mvnw.cmd clean package
```

A normal Maven build will be executed with the one important change that if the user doesn't have the necessary version of Maven specified in `.mvn/wrapper/maven-wrapper.properties` it will be downloaded for the user first, installed and then used.

Subsequent uses of `mvn/mvnw.cmd` use the previously downloaded, specific version as needed.

## Running the application

The application can be run from any IDE like Eclipse or IntelliJ IDEA like a normal Java application. As it is packaged as an executable `jar`, you can also run it directly from a command line. Build the application, open the application folder (`hotels`) and type

```
java -jar rest/target/rest-0.0.1-SNAPSHOT.jar
```

Alternatively, the packaging can be changed to `war` to allow the application deployment into a standalone Web server like Tomcat.

The working application is available on the address `http://localhost:8080/hotels/`. You can try, for example, to make a request

```
http://localhost:8080/hotels/ordering
```

to get all available types of ordering, or

```
http://localhost:8080/hotels/for-city?city=düsseldorf&order-by=by-price
```

to get the list of hotels ordered by the room price.
