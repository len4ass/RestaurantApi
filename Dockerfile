FROM openjdk:17
ADD build/libs/RestaurantApi.jar RestaurantApi.jar
ENTRYPOINT ["java", "-jar", "/RestaurantApi.jar"]