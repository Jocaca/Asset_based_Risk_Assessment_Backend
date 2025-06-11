## Dependences
Maven:3.9.10

java: 17

spring-boot:3.2.0

其余见pom.xml

数据库映射使用jpa,不同type的asset继承asserbasicinfo

数据库连接地址在application.properties里面修改

## Use
因为该project配置了Jpa和hibernate,初次运行的时候会自动生成数据库。在初次运行的时候，需要清空和这个prject连接的数据库中的
所有相关表，如使用了：
```
DROP DATABASE IF EXISTS asset_risk_management;
CREATE DATABASE asset_risk_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE asset_risk_management;
......
```
则只需要保留asset_risk_management这个空数据库，保证数据库内没有任何表即可，
随后直接运行该project会自动生成相关表。

只需要在初次运行时进行此操作，后续运行无需对数据库进行处理。