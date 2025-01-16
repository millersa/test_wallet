package ru.miller87;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoadTest {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static final int REQUESTS_PER_SECOND = 500;
    private static final UUID UUID_WALLET_ID = UUID.fromString("a079161c-9b68-4c2f-b8bb-a5dee27e0a43");

    // Создание контейнера PostgreSQL для тестов
    private static PostgreSQLContainer<?> postgresContainer;

    @BeforeAll
    public static void setup() {
        // Запуск контейнера PostgreSQL
        postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.2"))
                .withDatabaseName("test-wallet")
                .withUsername("postgres")
                .withPassword("test!");
        postgresContainer.start();
        System.out.println("PostgreSQL container logs:");
        System.out.println(postgresContainer.getLogs());
        // Настроим RestAssured
        RestAssured.baseURI = BASE_URL;

    }

    @DynamicPropertySource
    public static void dynamicProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        // Динамически добавляем параметры для подключения к базе данных
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    public void setupEach() {
        // Можно дополнительно сбрасывать состояние базы данных перед каждым тестом
    }

    @Test
    public void testGetWalletPerformance() throws InterruptedException {
        // Запускаем тест с 1000 запросами в секунду
        ExecutorService executor = Executors.newFixedThreadPool(REQUESTS_PER_SECOND);
        CountDownLatch latch = new CountDownLatch(REQUESTS_PER_SECOND);
        // Для хранения результатов
        List<Long> responseTimes = new ArrayList<>();
        for (int i = 0; i < REQUESTS_PER_SECOND; i++) {
            executor.submit(() -> {
                try {
                    long startTime = System.currentTimeMillis(); // Засекаем время до запроса

                    RestAssured.given()
                            .contentType(ContentType.JSON)
                            .pathParam("walletId", UUID_WALLET_ID)
                            .when()
                            .get("/wallets/{walletId}")
                            .then()
                            .statusCode(200);

                    long endTime = System.currentTimeMillis(); // Засекаем время после запроса
                    long duration = endTime - startTime; // Время выполнения запроса
                    RestAssured.expect().statusCode(200);
                    synchronized (responseTimes) {
                        responseTimes.add(duration); // Добавляем время в список
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // Дожидаемся завершения всех запросов
        executor.shutdown();
        // Анализируем результаты
        long totalDuration = responseTimes.stream().mapToLong(Long::longValue).sum();
        double averageResponseTime = totalDuration / (double) responseTimes.size();
        long maxResponseTime = responseTimes.stream().mapToLong(Long::longValue).max().orElse(0L);
        long minResponseTime = responseTimes.stream().mapToLong(Long::longValue).min().orElse(0L);

        System.out.println("Total Requests: " + responseTimes.size());
        System.out.println("Average Response Time: " + averageResponseTime + " ms");
        System.out.println("Max Response Time: " + maxResponseTime + " ms");
        System.out.println("Min Response Time: " + minResponseTime + " ms");
        System.out.println("PostgreSQL container logs after test:");
        System.out.println(postgresContainer.getLogs());
    }

    @Test
    public void testUpdateWalletPerformance() throws InterruptedException {
        // Запускаем тест с 1000 запросами в секунду
        ExecutorService executor = Executors.newFixedThreadPool(REQUESTS_PER_SECOND);
        CountDownLatch latch = new CountDownLatch(REQUESTS_PER_SECOND);

        for (int i = 0; i < REQUESTS_PER_SECOND; i++) {
            executor.submit(() -> {
                try {
                    RestAssured.given()
                            .contentType(ContentType.JSON)
                            .body("{\n" +
                                    "  \"walletId\": \"" + UUID_WALLET_ID + "\",\n" +
                                    "  \"operationType\": \"DEPOSIT\",\n" +
                                    "  \"amount\": 1000\n" +
                                    "}")
                            .when()
                            .post("/wallet")
                            .then()
                            .statusCode(200);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // Дожидаемся завершения всех запросов
        executor.shutdown();
    }
}