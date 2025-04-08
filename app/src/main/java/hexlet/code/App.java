package hexlet.code;

import io.javalin.Javalin;

public class App {
    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            // Only enable dev logging in development environment
            if (isDevEnvironment()) {
                config.bundledPlugins.enableDevLogging();
            }
        });

        app.get("/", ctx -> ctx.result("Hello World"));

        return app;
    }

    public static void main(String[] args) {
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
}