package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import io.javalin.validation.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildUrlPage extends BasePage {
    private String url;
    private Map<String, List<ValidationError<Object>>> errors;
}
