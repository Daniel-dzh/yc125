package SpringTest04.jdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {

    /**
     * 将第i行的ResulSet转换成 T 对象，这个具体实现由用户决定和完成
     * @param rs
     * @param i
     * @return
     */
    public T mapper(ResultSet rs,int i) throws SQLException;
}
