package hexlet.code.repository;

import hexlet.code.model.UrlCheck;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql = """
                INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, urlCheck.getUrlId());
            statement.setInt(2, urlCheck.getStatusCode());
            statement.setString(3, urlCheck.getH1());
            statement.setString(4, urlCheck.getTitle());
            statement.setString(5, urlCheck.getDescription());
            statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> findByUrl(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY id DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            var result = new LinkedList<UrlCheck>();
            while (resultSet.next()) {
                var urlCheck = getUrlCheck(resultSet);
                result.add(urlCheck);
            }
            return result;
        }

    }

    public static Map<Long, UrlCheck> findLatestChecks() throws SQLException {
        var sql = "SELECT DISTINCT ON (url_id) * FROM url_checks ORDER BY url_id DESC, id DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new HashMap<Long, UrlCheck>();
            while (resultSet.next()) {
                var urlCheck = getUrlCheck(resultSet);
                result.put(urlCheck.getUrlId(), getUrlCheck(resultSet));
            }
            return result;
        }
    }

    @NotNull
    private static UrlCheck getUrlCheck(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var urlId = resultSet.getLong("url_id");
        var statusCode = resultSet.getInt("status_code");
        var h1 = resultSet.getString("h1");
        var title = resultSet.getString("title");
        var description = resultSet.getString("description");
        var createdAt = resultSet.getTimestamp("created_at");
        var check = new UrlCheck(id, urlId, statusCode, h1, title, description, createdAt.toLocalDateTime());
        return check;
    }

}
