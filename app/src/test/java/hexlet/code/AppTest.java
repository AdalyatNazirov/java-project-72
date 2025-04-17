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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class AppTest {

    private static final String website = "https://example.com";
    private Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
        UrlRepository.removeAll();
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
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url();
            url.setName(website);
            UrlRepository.save(url);
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlsPagWithChecks() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url();
            url.setName(website);
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
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url();
            url.setName(website);

            UrlRepository.save(url);
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
            assertNotNull(response.body());
            assertThat(response.body().string()).contains(url.getName());
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
            var response = client.post(NamedRoutes.urlsPath(), "url=" + website);
            assertThat(response.code()).isEqualTo(200);
            assertNotNull(response.body());
            assertThat(response.body().string()).contains(website);
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
            url.setName(website);
            UrlRepository.save(url);

            OkHttpClient customOkHttpClient = client.getOkHttp().newBuilder().followRedirects(false).build();
            var newClient = new HttpClient(app, customOkHttpClient);

            var response = newClient.post(NamedRoutes.urlsPath(), "url=" + website);

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
}
