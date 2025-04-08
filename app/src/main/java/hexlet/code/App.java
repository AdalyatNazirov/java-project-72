package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public class App {

    private static final String SQL_SCHEMA_FILE = "schema.sql";

    public static Javalin getApp() throws IOException, SQLException {

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getConnectionString());

        var dataSource = new HikariDataSource(hikariConfig);
        var sql = readResourceFile();

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        Javalin app = Javalin.create(config -> {
            // Only enable dev logging in development environment
            if (isDevEnvironment()) {
                config.bundledPlugins.enableDevLogging();
            }
        });

        app.get("/", ctx -> ctx.result("Hello World"));

        return app;
    }

    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(getPort());
    }

    private static boolean isDevEnvironment() {
        String env = System.getenv().getOrDefault("ENV", "dev");
        return !env.equalsIgnoreCase("production");
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    private static String getConnectionString() {
        return System.getenv().getOrDefault(
                "JDBC_DATABASE_URL",
                "jdbc:h2:mem:project;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;");
    }

    private static String readResourceFile() throws IOException {
        try (var inputStream = App.class.getClassLoader().getResourceAsStream(SQL_SCHEMA_FILE)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }
}