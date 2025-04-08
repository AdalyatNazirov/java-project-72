package hexlet.code;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    @Test
    void test() throws SQLException, IOException {
        App.main(new String[]{});
        assertTrue(true);
    }
}
