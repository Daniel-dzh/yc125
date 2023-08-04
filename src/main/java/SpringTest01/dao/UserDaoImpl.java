package SpringTest01.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/*
@Repository 标识这是一个dao层的类,由spring托管
@Component  标识这是一个部件类，由spring托管
 */
@Repository
@Scope
@Lazy
public class UserDaoImpl implements UserDao {  //UserDao接口的实现类

    public UserDaoImpl() {
        System.out.println("UserDaoImpl的构造方法");
    }

    @Override
    public void add(String uname) {
        System.out.println("UserDaoImpl添加了:"+ uname);
    }
}
