package dao;
import java.sql.Connection;
import javax.sql.DataSource;
public class Dao {
    private DataSource ds;

    protected Connection getConnection() {
        return ds.getConnection();
    }
}
