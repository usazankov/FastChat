# FastChat

Учебный Android-проект 2018 года — клиент чата (мессенджера), работающий по собственному протоколу поверх TCP-сокета. Реализованы сетевой API-клиент, слой данных и доменный слой; графического интерфейса и сервера в репозитории нет (адрес сервера зашит в коде — см. `apifastchat/src/main/java/com/project/apifastchat/net/TcpClient.java`).

Протокол: JSON-сообщения, передаваемые по TCP с 4-байтовым префиксом длины (см. `apifastchat/src/main/java/com/project/apifastchat/net/TcpClient.java`).

## Возможности

Команды клиента, реализованные запросами (`apifastchat/src/main/java/com/project/apifastchat/requests/`):
- авторизация — `AuthRequest` (`c_auth_req`);
- проверка соединения — `CheckConnectRequest` (`c_check_connect`);
- отправка сообщения — `MessageRequest` (`c_send_message`);
- получение списка пользователей — `UserListRequest` (`c_get_users`);
- получение информации о пользователе — `UserInfoRequest` (`c_get_user_info`).

События, присылаемые сервером (описаны в `Consts.java`): подключение/отключение пользователя, новое сообщение, обновлённый список пользователей.

Сетевой менеджер (`NetworkManager.java`):
- выполняет запросы в виде RxJava-`Observable`, сопоставляя ответ с запросом по `id_msg`; таймауты соединения и ответа — по 30 секунд;
- автоподключение/переподключение по таймеру и периодическая проверка связи (`c_check_connect` каждые 5 секунд при установленном соединении);
- подписки на события сервера и смену состояния соединения (online/offline).

## Технологии

Версии — из `build.gradle` модулей:
- Java, Android Support Library (`appcompat-v7` 28.0.0);
- RxJava 2.0.2, RxAndroid 2.0.1;
- Gson 2.8.5;
- jsonschema2pojo (gradle-плагин) — генерация POJO-сущностей из JSON-схем при сборке;
- StorO 1.2.0 — дисковый кэш (слой `data`);
- Android Gradle Plugin 3.1.3, Gradle 4.4 (wrapper); compileSdk 28, minSdk 22, targetSdk 28.

## Архитектура и модули

Многослойная архитектура (чистая архитектура; часть базовых классов слоя `data` взята из открытого проекта Fernando Cejas — файлы с заголовком Apache License 2.0). В сборку (`settings.gradle`) входят `:apifastchat`, `:data`, `:domain`.

- `apifastchat` — Android-библиотека, API-клиент протокола чата: TCP-клиент, `NetworkManager`, построители запросов, JSON-мапперы на Gson, «stores» (обёртки запросов: аутентификация, пользователи, сообщения, события сервера), сущности — генерируются из JSON-схем каталога `apifastchat/schemes/jsonschemas`.
- `domain` — доменный слой: интерфейсы репозиториев (`IUserRepository` и др.), базовый класс use-case'ов (`AUseCase`), исполнители потоков; содержит незавершённые заготовки (пустые `UserActions`, `MessageActions`).
- `data` — слой данных: `UserDataRepository`, фабрика источников «cloud/disk» (`UserDataStoreFactory`), дисковый кэш (`StoroCacheManager`, `FileManager`), `JobExecutor`.
- `app` — заготовка приложения: исключена из `settings.gradle` (коммит «Убрал проект app»), в манифесте нет активити — готового UI не существует.

## Тесты

- Модульные (JUnit 4.12): `apifastchat` — `AuthRespJsonMapperTest`, `AuthMsgTest`; `data` — `FileManagerTest` (на базе Robolectric 3.1.1). В тестах применяются Mockito 1.9.5 и AssertJ 1.7.1.
- Инструментальные (`androidTest`, `apifastchat`): `AuthTest`, `UsersTest`, `MessageTest`, `CheckConnectTest`, `NetworkManagerTest`, `StoresTest` — интеграционные, подключаются к реальному серверу чата по TCP (общий базовый класс `CommonTcp`), без доступного сервера не пройдут.

## Сборка и запуск

Сборка — gradle-wrapper'ом (`gradlew` / `gradlew.bat`, Gradle 4.4). APK проект не собирает: модуль `app` исключён из сборки, презентационный слой не реализован. Инструментальным тестам нужен запущенный сервер по адресу, зашитому в `TcpClient`/`ConnectConf`.

## Связанные проекты

- [Chat](https://github.com/usazankov/Chat) — клиент-серверный чат на Qt/C++ того же автора: тот же протокол (команды `c_auth_req`, `c_check_connect`, `c_send_message`, `c_get_users`, события `e_message`, `e_users_list` и др., кадр «4 байта длины + JSON», порт 1977). Проекты разрабатывались параллельно, в декабре 2018.

## Статус

Личный учебно-экспериментальный проект: 26 коммитов за октябрь–декабрь 2018 года (последний — 27.12.2018), после чего разработка прекратилась. UI-слой не был начат, часть доменного слоя — заготовки.
