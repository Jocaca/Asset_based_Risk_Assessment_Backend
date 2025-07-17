# Asset-based-Risk-Assessment-Platform-Backend

HKU CS project proposal

Frontend project: https://github.com/Lillian-7798/Asset-based-Risk-Assessment-Platform-Frontend
## Dependences
Maven:3.9.10

java: 17

spring-boot:3.2.0

For the rest of the dependence, see pom.xml, please reload the maven before running the project

## Database
Because the project is configured with JPA and hibernate, the database will be **automatically generated** when it is first run. 

Before running, **create a new empty database named 'asset_risk_management'** in your MySQL connection, after which you need to **modify the database connection in application.properties** to your own.
On the first run of the project, an initial user is automatically configured:
> username: admin
> 
> password: admin
> 
> role: admin

You can use this user to change other user's role.

Since there is a static table risktype in this project, after the database is created on the backend for the first time, **execute a buildRiskType.sql** to insert all the contents of the riskType.