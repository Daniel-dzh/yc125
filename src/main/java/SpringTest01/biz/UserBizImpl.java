package SpringTest01.biz;

import SpringTest01.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserBizImpl implements UserBiz{
    /*
    将dao层的对象注入到biz DI依赖注入   (将spring容器中托管的userDao的对象传到此处)
    @Resource(name="userDaoImpl")   //由spring容器根据id名为userDaoImpl到容器中找到这个实例并注入
     */
    @Autowired  //根据类型来完成注入,在spring容器中根据类型UserDao接口来找实例
    @Qualifier("userDaoImpl")
    private UserDao userDao;

    public UserBizImpl() {
        System.out.println("UserBizImpl的构造方法...");
    }

    @Override
    public void add(String uname) {
        userDao.add(uname);
        System.out.println("UserBizImpl添加了:"+uname);
    }
    //将dao层的对象注入到biz DI依赖注入(将Spring容器中托管的对象传到此处)
    //@Resource(name="userDAOImpl") //由Spring容器中根据类型UserDao接口
}
