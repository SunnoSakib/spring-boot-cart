# Sample Spring Boot Cart Application for Ecommerce

This is a sample Spring Boot application that showcases the basic structure and features of an ecommerce cart. The goal is to implement a shopping cart with following use cases via REST API:
- Adding products to the shopping cart
- Removing products from the shopping cart
- Changing the quantity of an item in the shopping cart
- Displaying the shopping cart including the sum of the single items

## Prerequisites

Before running this application, make sure you have the following prerequisites installed:

- Java Development Kit (JDK) version **20** or higher
- Docker

## Application Structure

The application follows a standard Spring Boot project structure, with the main components located in the `src/main/java` directory. The key components include:

- `com.demo.cart`: The main entry point of the application.
- `com.demo.cart.web.controller`: Contains the RESTful API controllers.
- `com.demo.cart.service`: Contains the business logic services.
- `com.demo.cart.repository`: Contains the data repositories.

## Endpoints

### List Carts

Retrieves a paginated list of carts.

- **URL:** `/cart`
- **Method:** `GET`
- **Response:** `Page<Cart>`

### Get Cart by UUID

Retrieves a cart by its UUID.

- **URL:** `/cart/{uuid}`
- **Method:** `GET`
- **Parameters:**
    - `uuid` (path) - The UUID of the cart
- **Response:** `Cart`

### Add Product to Cart

Adds a product to the cart.

- **URL:** `/cart/add-product`
- **Method:** `POST`
- **Request Body:** `AddProductRequest`
  - `cartUUID` - The UUID of the cart. Not required for new request.
  - `customerId` - User id of the customer. Not required for guest user.
  - `productId` - product id which needs to be added in the cart
  - `unit` : amount of the product unit that needs to be added in the cart
- **Response:** `ResponseEntity<?>`

### Update Cart

Updates the quantity of a product in the cart.

- **URL:** `/cart/update-quantity`
- **Method:** `POST`
- **Request Body:** `ChangeProductRequest`
- **Response:** `ResponseEntity<?>`

### Remove Product from Cart

Removes a product from the cart.

- **URL:** `/cart/{uuid}/remove-product/{productId}`
- **Method:** `DELETE`
- **Parameters:**
    - `uuid` (path) - The UUID of the cart
    - `productId` (path) - The ID of the product to remove
- **Response:** `ResponseEntity<ApiResponse>`

## Dependencies

The `CartController` class depends on the following services:

- `CartService` - Service for managing carts
- `ProductService` - Service for managing products
- `CustomerService` - Service for managing customers
- `CartItemService` - Service for managing cart items

Please note that this is a demo, and the project was created as a quick assessment task. Please refactor code in case you want to use it production environment.

## Configuration

The application can be configured using the `application.properties` file located in the `src/main/resources` directory. I have used postgres as database in this project and datasource was configured in `compose.yaml` file.

## API Documentation

The API documentation for this application can be accessed through Swagger UI. Once the application is running, navigate to the following URL in your web browser:

```shell
http://localhost:8080/swagger-ui.html
```


## Contact

If you have any questions or suggestions regarding this sample Spring Boot application, please feel free to contact us at [me@mahmud.dev](mailto:me@mahmud.dev).

## License
This project serves as a sample demonstration and is provided as an open-source resource, allowing you complete freedom to utilize it as needed.