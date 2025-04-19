package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlCheck {

    private Long id;
    private Long urlId;
    private Integer statusCode;
    private String h1;
    private String title;
    private String description;
    private LocalDateTime createdAt;

    /**
     * Return formatted date time string for createdAt field.
     *
     * @return formatted date time string for createdAt field, example: 2022-01-01 00:00:00
     */
    public String getFormattedCreatedAt() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
