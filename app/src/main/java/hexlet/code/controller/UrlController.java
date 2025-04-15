package hexlet.code.controller;

import hexlet.code.dto.urls.BuildUrlPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {
    public static void build(Context ctx) {
        var page = new BuildUrlPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/build.jte", model("page", page));
    }

    public static void create(Context ctx) {
        try {
            var urlString = ctx.formParamAsClass("url", String.class)
                    .check(value -> !value.isEmpty(), "URL не должен быть пустым")
                    .get();

            if (!isValidURL(urlString)) {
                setFlashAndRedirect(ctx, "Некорректный URL", "danger", NamedRoutes.rootPath());
                return;
            }

            URI uri = URI.create(urlString);
            URI baseUri = new URI(uri.getScheme(), uri.getAuthority(), null, null, null);

            var url = new Url();
            url.setName(baseUri.toString());
            UrlRepository.save(url);

            setFlashAndRedirect(ctx, "Страница успешно добавлена", "success", NamedRoutes.urlsPath());

        } catch (SQLException e) {
            handleSqlException(ctx, e);
        } catch (ValidationException e) {
            handleValidationException(ctx, e);
        } catch (URISyntaxException e) {
            setFlashAndRedirect(ctx, "Ошибка при обработке URL", "danger", NamedRoutes.rootPath());
        }
    }

    public static void list(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/list.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).orElseThrow(() -> new NotFoundResponse("Product not found"));

        var page = new UrlPage(url);

        ctx.render("urls/show.jte", model("page", page));
    }

    private static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void setFlashAndRedirect(Context ctx, String message, String type, String path) {
        ctx.sessionAttribute("flash", message);
        ctx.sessionAttribute("flash-type", type);
        ctx.redirect(path);
    }

    private static void handleSqlException(Context ctx, SQLException e) {
        if (e.getErrorCode() == 23505) {
            setFlashAndRedirect(ctx, "Страница уже существует", "warning", NamedRoutes.urlsPath());
        } else {
            var url = ctx.formParam("url");
            var page = new BuildUrlPage();
            page.setUrl(url);
            ctx.render("products/build.jte", model("page", page));
        }
    }

    private static void handleValidationException(Context ctx, ValidationException e) {
        var url = ctx.formParam("url");
        var page = new BuildUrlPage(url, e.getErrors());
        ctx.render("products/build.jte", model("page", page)).status(422);
    }
}
