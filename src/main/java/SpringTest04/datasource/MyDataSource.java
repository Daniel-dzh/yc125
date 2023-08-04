package SpringTest04.datasource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

@Component
@Data
public class MyDataSource implements DataSource {

    private ConcurrentLinkedQueue<MyConnection> pool;
    //配置后注入，结合属性文件完成注入操作
    //从属性文件中获取

    @Value("${coreSize}")
    private int coreSize;

    @Value("${mysql.username}")
    private String username;

    @Value("${mysql.password}")
    private String passsword;

    @Value("${url}")
    private String url;


    public MyDataSource() {
        System.out.println("MyDataSource的构造方法....");
    }

    @PostConstruct
    public void init() throws SQLException {
        System.out.println("MyDataSource的init..");
        pool = new ConcurrentLinkedQueue<MyConnection>();
        for (int i = 0; i < coreSize; i++) {
            MyConnection mc = new MyConnection();
            mc.status = false;
            mc.con = DriverManager.getConnection(url,username,passsword);
            pool.add( mc );
        }
    }

    class MyConnection{
        Connection con;
        boolean status; // ture在用，false空闲
    }

    @Override
    public Connection getConnection() throws SQLException {
        do {
            MyConnection mc = this.pool.poll();
            if ( mc==null ){
                return null;
            }
            if ( !mc.status ){
                return mc.con;
            }
        }while (true);
    }

    public void returnConnection(Connection con){
        MyConnection mc = new MyConnection();
        mc.status = false;
        mc.con = con;
        this.pool.add( mc );
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
