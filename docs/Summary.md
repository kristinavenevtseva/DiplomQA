# Отчёт о проведённой автоматизации

Было запланировано произвести автоматизацию тестирования
комплексного сервиса, взаимодействующего с СУБД и API Банка.
А именно, автоматизировать проверку функций оплаты и покупки 
в кредит, корректности внесения приложением информации в БД. 
Запланированные работы выполнены в полном объеме.

#### Сработавшие риски
1) Автоматизация большого количества тестовых сценариев заняла значительное время.
2) Отсутствие удобной системы атрибутов от разработчиков существенно увеличило время подбора селекторов и соответственно автоматизации.
3) Симулятор банковских сервисов написан на Node.js, что потребовало его предварительной упаковки в контейнер.
4) Заявленная поддержка двух баз данных привела к увеличению времени тестирования.

#### Общий итог по времени 
1) Подготовка тестовых данных (план/факт), часы: 4/8
2) Настройка окружения (план/факт), часы: 1/3
3) Написание авто-тестов (план/факт), часы: 40/60
4) Подготовка отчета (план/факт), часы: 1/1
* Расхождение по времени от плана произошло по причине не корректной оценки 
трудозатрат.
