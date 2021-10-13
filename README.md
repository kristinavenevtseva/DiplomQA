# Процедура запуска авто-тестов
1. открыть дипломный проект в репозитории по ссылке: https://github.com/kristinavenevtseva/DiplomQA;
2. выполнить `git clone`;
3. выполнить запуск сервисов `docker-compose up`;
4. для запуска приложения выполнить команду:
    * с MySQL: `java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar aqa-shop.jar`
    * с PostgreSQL: `java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app -jar aqa-shop.jar`
5. запустить тесты:
    * с MySQL: `gradlew test -Dselenide.headless=true -Ddatabase.url=jdbc:mysql://localhost:3306/app -Ddatabase.username=app -Ddatabase.password=pass -Dapp.url=http://localhost:8080`
    * с PostgreSQL: `gradlew test -Dselenide.headless=true -Ddatabase.url=jdbc:postgresql://localhost:5432/app -Ddatabase.username=app -Ddatabase.password=pass -Dapp.url=http://localhost:8080`  
Примечание: при необходимости изменения url приложения, url, имени пользователя или пароля доступа к БД необходимо внести соответствующие изменения в параметры `database.url`, `database.username`, `database.password`, `app.url` при запуске тестов.   
6. для генерации отчета после прохождения тестов выполнить команду: `gradlew allureServe`.
7. по окончанию тестирования выполнить команду: `docker-compose down`.
