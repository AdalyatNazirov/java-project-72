package hexlet.code;


import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.HttpClient;
import io.javalin.testtools.JavalinTest;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class AppTest {

    private static final String WEBSITE_NAME = "https://example.com";
    private static MockWebServer mockServer;
    private Javalin app;

    @BeforeAll
    public static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        MockResponse mockedResponse = new MockResponse()
                .setBody(readFixture("index.html"));
        mockServer.enqueue(mockedResponse);
        mockServer.start();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    private static String readFixture(String filename) throws IOException {
        Path path = Paths.get("src/test/java/resources/fixtures", filename)
                .toAbsolutePath()
                .normalize();
        return Files.readString(path);
    }

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
        UrlRepository.removeAll();
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Shutdown both servers after each test
        app.stop();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            // Create a new client that doesn't follow redirects
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertNotNull(response.body());
            assertThat(response.body().string().length()).isGreaterThan(0);
        });
    }

    @Test
    public void testUrlsPageWithNoChecks() throws SQLException, IOException {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url();
            url.setName(WEBSITE_NAME);
            UrlRepository.save(url);
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlsPageWithChecks() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url();
            url.setName(WEBSITE_NAME);
            UrlRepository.save(url);

            var urlCheck = new UrlCheck();
            urlCheck.setUrlId(url.getId());
            urlCheck.setStatusCode(200);
            UrlCheckRepository.save(urlCheck);
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
            assertNotNull(response.body());
            assertThat(response.body().string()).contains(urlCheck.getStatusCode().toString());
        });
    }

    @Test
    public void testUrlPageWithNoChecks() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url();
            url.setName(WEBSITE_NAME);

            UrlRepository.save(url);
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertNotNull(response.body());
            var body = response.body().string();
            assertThat(body).contains(url.getName());
            assertThat(body).contains("No checks performed yet");
        });
    }

    @Test
    public void testUrlPageReturn404IfUrlNotExist() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(1L));
            assertThat(response.code()).isEqualTo(404);
            assertNotNull(response.body());
        });
    }

    @Test
    public void testPostUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post(NamedRoutes.urlsPath(), "url=" + WEBSITE_NAME);
            assertThat(response.code()).isEqualTo(200);
            assertNotNull(response.body());
            assertThat(response.body().string()).contains(WEBSITE_NAME);
        });
    }

    @Test
    public void testPostUrlPageFailsIfUrlIsNotSpecified() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post(NamedRoutes.urlsPath(), "url=");
            assertThat(response.code()).isEqualTo(422);
            assertNotNull(response.body());
            assertThat(response.body().string()).contains("URL не должен быть пустым");
        });
    }

    @Test
    public void testPostUrlPageRedirectIfAlreadyExist() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url();
            url.setName(WEBSITE_NAME);
            UrlRepository.save(url);

            OkHttpClient customOkHttpClient = client.getOkHttp().newBuilder().followRedirects(false).build();
            var newClient = new HttpClient(app, customOkHttpClient);

            var response = newClient.post(NamedRoutes.urlsPath(), "url=" + WEBSITE_NAME);

            assertThat(response.code()).isEqualTo(302);
            assertNotNull(response.body());
            assertThat(response.body().string()).isEqualTo("Redirected");
            assertThat(response.header("Location")).contains(NamedRoutes.urlsPath());

        });
    }

    @Test
    public void testPostUrlPageFailsIfUrlIsInvalid() {
        JavalinTest.test(app, (server, client) -> {
            OkHttpClient customOkHttpClient = client.getOkHttp().newBuilder().followRedirects(false).build();
            var newClient = new HttpClient(app, customOkHttpClient);

            var response = newClient.post(NamedRoutes.urlsPath(), "url=bad_url");

            assertThat(response.code()).isEqualTo(302);
            assertNotNull(response.body());
            assertThat(response.body().string()).isEqualTo("Redirected");
            assertThat(response.header("Location")).contains(NamedRoutes.rootPath());
        });
    }

    @Test
    public void testUrlCheckPage() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url();
            url.setName(mockServer.url("/").toString());
            UrlRepository.save(url);

            var response = client.post(NamedRoutes.urlCheckPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertNotNull(response.body());
            var body = response.body().string();
            assertThat(body).contains("Simple Page Title");
            assertThat(body).contains("Simple Page Header");
            assertThat(body).contains("A simple HTML page with title, h1, and meta description");
        });
    }
}
