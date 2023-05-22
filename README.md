# RestaurantApi
A restaurant API with order processing imitation and authentication

## How to use
1. Clone repository
2. Run [PostgreSQL in docker](https://hub.docker.com/_/postgres)
3. Build the application with `./gradlew bootJar`
4. Build an application image from `Dockerfile`
    * Example: `docker build -t restaurant_api -f Dockerfile .`
5. Run the app in docker
    * Example: `docker run --restart unless-stopped --name RESTAURANTAPI -d -p EXTERNAL_PORT:8080 -e DB_NAME=... -e DB_HOST=... -e DB_PORT=... -e DB_USER=... -e DB_PASS=... -e JWT_SECRET=... -e ADMIN_USERNAME=... -e ADMIN_EMAIL=... -e ADMIN_PASSWORD=... restaurant_api`
    * Environment variables:
        * `EXTERNAL_PORT (required)` - a port you want to expose for application to be available at
        * `DB_NAME (required)` - name of the database inside the container you ran in step 2 (default is `postgres`)
        * `DB_HOST (required)` - host where the database container is ran (if it's done locally, then it should be `localhost` or `host.docker.internal`)
        * `DB_PORT (required)` - port that is exposed on the host for database connections (usually it's 5432)
        * `DB_USER (required)` - user that you specified upon running database in container
        * `DB_PASS (required)` - password for the user
        * `ADMIN_USERNAME (required)` - admin username to be inserted into database with admin role (used in ADMIN endpoints)
        * `ADMIN_EMAIL (required)` - admin email to be inserted into database with admin role
        * `ADMIN_PASSWORD (required)` - admin password to be inserted into database with admin role
        * `JWT_SECRET (required)` - private key for JWT token generation, validation and claim extraction ([encryption key, at least 256 bits in HEX format](https://allkeysgenerator.com))
6. Use endpoints with Postman, Insomnia etc.

## Endpoints
Postman collection available [here](RestaurantAPI.postman_collection.json).