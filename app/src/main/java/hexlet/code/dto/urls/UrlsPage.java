package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.Url;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UrlsPage extends BasePage {
    private final List<Url> urls;

    public UrlsPage(List<Url> urls) {
        this.urls = urls;
    }
}
