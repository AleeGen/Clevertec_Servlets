
<h1 align="center">Cash Receipt</h1>

<h4 align="center">The application generates and displays a receipt of products on request </h4>


          Java '17'     |     Gradle '7.4.2'      |     Servlets     |     PostgreSQL      |      Liquibase
---
### Stack:

- `jakarta.servlet:jakarta.servlet-api:5.0.0`
- `org.apache.logging.log4j:log4j-core:2.20.0`
- `org.liquibase:liquibase-core:4.20.0`
- `org.projectlombok:lombok:1.18.26`
- `org.postgresql:postgresql`
- `com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2`
- `com.itextpdf:kernel:7.2.5`
- `com.itextpdf:layout:7.2.5`
- `com.itextpdf:io:7.2.5`
---

### Properties of "resources\application.yml": 

- "is_init_db" for tracking changes in the database
- "pagination" to change pagination settings

### Used example entity (All CRUD operations are supported):

> Format request "products?id=P" or "cards?card=NNNN"
> 
> P is the product ID, NNNN is the four-digit card number
> 
> With additional parameters, you can specify the number of displayed elements on the page, as well as its number: "&page=N&pageSize=N

- ##### Get: http://localhost:8080/products?page=2&pageSize=5
![image](https://user-images.githubusercontent.com/100039077/230886803-a03e4d6e-9358-496e-9359-56e413d6b7e8.png)

### Used example cash receipt:

> Format request "cashReceipt?id=P&card=NNNN"
> 
> The card may be missing

- ##### Get: http://localhost:8080/cashReceipt?id=1&id=1&id=2&card=3333
![image](https://user-images.githubusercontent.com/100039077/224175445-efaa885e-6931-4839-946c-0862cb7cb217.png)
- ##### Get pdf format:
![image](https://user-images.githubusercontent.com/100039077/226687806-2689f851-8837-4ed3-83dd-99b425be6871.png)

