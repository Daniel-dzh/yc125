package SpringTest04.jdbcTemplate;

import SpringTest04.datasource.MyDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcTemplate<T> {

    private DataSource dataSource;  //数据源

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //查询的模板方法
    public List<?> executeQuery( String sql, RowMapper<?> rowMapper,Object...params){
        List<Object> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            //连接池中获取连接
             con = dataSource.getConnection();
             //创建语句对象PrepareStatement
            pstmt = con.prepareStatement(sql);
            //设置参数
            setParams(pstmt,params);
            //查询
            rs = pstmt.executeQuery();
            //循环ResultSet
            int i = 0;
            while (rs.next()){
                T t = (T)rowMapper.mapper(rs,i);
                i++;
                list.add(t);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }finally {
            if (rs !=null){
                try {
                    rs.close();
                } catch ( SQLException throwables ) {
                    throwables.printStackTrace();
                }
            }
            if (pstmt != null){
                try {
                    pstmt.close();
                } catch ( SQLException throwables ) {
                    throwables.printStackTrace();
                }
            }
            ((MyDataSource)dataSource).returnConnection( con );
        }
        return list;
    }

    private void setParams(PreparedStatement stmt, Object...params) throws SQLException {
        if (null== params || params.length <=0){
            return;
        }
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i+1,params[i]);  //？从1开始第一个参数i+1
        }
    }
}
