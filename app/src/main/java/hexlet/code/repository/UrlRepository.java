package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, url.getName());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var host = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var url = new Url(id, host, createdAt.toLocalDateTime());
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";
        try (var conn = dataSource.getConnection();
             var statement = conn.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            var result = new ArrayList<Url>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var host = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                var url = new Url(id, host, createdAt);
                result.add(url);
            }
            return result;
        }
    }

    public static void removeAll() throws SQLException {
        try (var conn = dataSource.getConnection();
             var statement = conn.createStatement()) {
            statement.execute("DELETE FROM urls");
        }
    }
}
