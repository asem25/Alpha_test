# Alpha Test Application

## Описание проекта
**Alpha Test Application** — это RESTful приложение для управления клиентами и их контактами. Приложение предоставляет возможность выполнения CRUD операций для клиентов и их контактов, а также взаимодействия через документированный API (Swagger).

## Функционал
- Управление клиентами и их контактами (CRUD операции).
- Автоматическая обработка исключений (ExceptionHandler).
- Документированный API с использованием Swagger.
- Запуск приложения через Docker.
- Покрытие тестами с использованием JUnit и Mockito.

## Технологический стек
- Java 21
- Spring Boot 3.4.1
- Hibernate
- PostgreSQL
- Swagger/OpenAPI
- Docker/Docker Compose
- JUnit 5
- Mockito

## Запуск приложения

### 1. Требования
- Docker версии 20.10 или выше
- Docker Compose версии 1.29 или выше

### 2. Сборка и запуск

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/asem25/Alpha_test.git
   cd alpha_test
   ```

2. Соберите Docker-образ и запустите приложение с помощью Docker Compose:
   ```bash
   docker-compose up --build
   ```

3. Приложение будет доступно по адресу: `http://localhost:8080`

4. Swagger UI доступен по адресу: `http://localhost:8080/swagger-ui/index.html#/`

### 3. Остановка контейнеров
Для остановки контейнеров выполните:
```bash
docker-compose down
```

## Тестирование

### Запуск тестов
Для запуска модульных тестов выполните команду:
```bash
mvn test
```

После выполнения команды `mvn test` вы увидите отчет с результатами тестов в терминале. Он содержит следующую информацию:
- Общее количество тестов, которые были выполнены.
- Количество успешно пройденных, пропущенных или завершившихся с ошибками тестов.

Пример вывода:
```
[INFO] Results:
[INFO]
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS
```

### Технологии для тестирования
- **JUnit 5**: для написания и запуска тестов.
- **Mockito**: для создания и использования моков в тестах.

## Структура проекта
```
alpha_test/
├── src/main/java/ru/semavin/alpha_test/
│   ├── controllers/      # REST контроллеры
│   ├── models/           # Сущности (Client, Contact)
│   ├── repositories/     # Репозитории JPA
│   ├── services/         # Бизнес-логика
│   ├── AlphaTestApplication.java  # Точка входа
├── src/test/java/ru/semavin/alpha_test/
│   ├── ClientServiceTest.java  # Тесты для ClientService
│   ├── ContactServiceTest.java # Тесты для ContactService
│   ├── ClientControllerTest.java # Тесты для ClientController
├── src/main/resources/
│   ├── application.properties  # Настройки приложения
├── Dockerfile
├── docker-compose.yml
├── README.md
```

## Маршруты API

| Метод | URL                  | Описание                          |
|-------|----------------------|-----------------------------------|
| GET   | /api/clients         | Получить список всех клиентов    |
| POST  | /api/clients         | Создать нового клиента           |
| GET   | /api/clients/{id}    | Получить информацию о клиенте    |
| PUT   | /api/clients/{id}    | Обновить информацию о клиенте    |
| DELETE| /api/clients/{id}    | Удалить клиента                  |
| GET   | /api/contacts        | Получить список всех контактов   |
| POST  | /api/contacts        | Создать новый контакт            |
| GET   | /api/contacts/{id}   | Получить информацию о контакте   |
| PUT   | /api/contacts/{id}   | Обновить информацию о контакте   |
| DELETE| /api/contacts/{id}   | Удалить контакт                  |


## Автор
Разработано в рамках тестового задания.
 Связаться: asemavin250604@gmail.com.
