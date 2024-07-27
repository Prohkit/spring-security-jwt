## Spring security jwt  

Реализация аутентификации и авторизации с использованием Spring Security и JWT.  

Реализовано следующее API:  
`POST /api/v1/auth/register` - регистрация пользователя;  
`POST /api/v1/auth/login` - аутентификация пользователя;  
`POST /api/v1/auth/refresh` - обновление access и refresh токенов;  
`GET /api/admin/hello` - эндпоинт для пользователя с ролью ADMIN;  
`GET /api/user/hello` - эндпоинт для пользователя с ролью USER;  
  
OpenAPI доступен по ссылке `http://localhost:8081`.  
  
В папке postman находится коллекция для импорта в Postman.  
  
### Инструкция по запуску  
  
Для запуска необходимо из корневой директории проекта вызвать команду:  
`docker-compose up`  

Подключиться к базе данных можно со следующими настройками:  
`host: localhost`    
`port: 5435`  
`database: jwtsecuritydb`  
`username: postgres`    
`password: postgres`