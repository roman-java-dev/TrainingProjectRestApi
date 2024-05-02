# 🙋 CUSTOMERS-ORDERS-APP 📦

![order.jpg](order.jpg)

### ⚡️ Project description
RESTful web app that allows to create and view customers/orders, to update and delete. Orders results support pagination. 
The service allows to download a CSV report file with all records that match the filter criteria (not just one page). 
There is also possible to download a JSON file. At the same time, all valid records from this file are saved in the 
database. The project is covered by integration tests. Does not have UI, interaction happens through Postman.

### 🎯 Endpoints
The web app provides the following endpoints:

<b> Customer Endpoints: </b>

- <b>POST:</b> `/api/customers` - This endpoint will allow to create a new customer (added control of uniqueness of values).
- <b>GET:</b> `/api/customers` - Returns all available records in the database. 
- <b>PUT:</b> `/api/customers/{id}` - Update customer by id (fields validation added).
- <b>DELETE:</b> `/api/customers/{id}` - Delete customer by id. 

<b> Order Endpoints: </b>

- <b>POST:</b> `/api/orders` - This endpoint will allow to create a new order (added validation of required fields, 
its format, etc.).
- <b>GET:</b> `/api/orders/{id}` - Returns detailed order data including data of the customer entity it refers to.
- <b>PUT:</b> `/api/orders/{id}` - Update order by id (fields validation added).
- <b>DELETE:</b> `/api/orders/{id}` - Delete order by id.
- <b>POST:</b> `/api/orders/_list` - Returns a data structure that has a list of elements of the Order entity that 
match the requested page and the total number of pages. Entries in the array have a reduced set of fields of the 
Order entity. In the request, it is possible to optionally specify fields by which records can be filtered.
- <b>POST:</b> `/api/orders/_report` - Generates and offers to download a CSV report file with all records that match 
the filter criteria (not just one page). 
- <b>PUT:</b> `/api/orders/upload` - Accepts a JSON file and stores all valid records from this file in the database. In 
response, it generates JSON, in which it notes the number of successfully imported records, as well as unsuccessfully.
If you don't have your own JSON file, you can use from the [resources](src/test/resources/test_orders.json) folder.


### 🔥 Getting Started
To get started with the project, follow these steps:
1. Clone the repository: git clone https://github.com/roman-java-dev/TrainingProjectRestApi
2. Install Postman for sending requests.
3. Configure the database connection field values in [application.properties](src/main/resources/application.properties). Fill in the 
appropriate values for the following fields: username, password and url path to database (not to the schema, 
it's created automatically).
4. Run the application.
5. Use this URL in Postman to test the app: http://localhost:8080/  
6. <b>NOTICE!</b> Don't modify or delete the file [resources](src/test/resources/test_orders.json) because the correct 
operation of the tests depends on it.