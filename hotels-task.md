# Hotel Service Assignment

## Task 1 - Implement a basic Hotel Service
Implement the interface for the Hotel Service. Please use the attached JSON file as your data source. Your implementation must map the result sets from the data source to corresponding Entities.

The entities encapsulate each other:

```
(Hotel) -[hasMany]-> (Partner) -[hasMany]-> (Price)
```

The JSON file has a similar but not equal structure. Please take a deep look at both structures.

## Task 2 - Build a basic validator for the Partner Entity

Your tasks is to implement a validation for the Partner Entities homepage property. Place the validation in a proper position within the given architecture and ensure the value is valid. It is up to you how you implement it and when to call it within the application's flow.

## Task 3 - Implement a way to get different implementations of the HotelService interface

For example, you can accomplish this by using a simple factory or abstract factory pattern or you can choose the more complex variant using dependency injection via inversion of control. Feel free to choose a different way.

Please choose the variant you feel most comfortable with, not the one that you think might of bringing the most kudos.

You might want to write a second implementation for the interface HotelService, but you don't have to. If you need one, you can think of a "PriceOrderedHotelService" or a "PartnerNameOrderedHotelService", which sort their results after receiving it from the partner service.
