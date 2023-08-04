package SpringTest01;

import SpringTest01.biz.UserBiz;
import SpringTest01.dao.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
        /*
        常用创建容器:
            ClassPathXmlApplicationContext -->根据 类路径 下有一个 xml配置文件 来来生成容器
            FileSystemXmlApplicationContext -->(任意路径)根据 文件系统路径 下的 xml配置文件 来生成容器
            AnnotationConfigApplicationContext -->(读取注解)读取相应包下的 配置注解类 来生成容器
         */
        //首先创建容器
        ApplicationContext container = new AnnotationConfigApplicationContext( Config.class );
        /*
        根据容器中已经创建好了这个类,用键值对形式存取:键(Key):userDaoImpl,值(Val):相应创建的对象,直接读取使用
         */
        UserDao ud = (UserDao) container.getBean("userDaoImpl");
        ud.add("张三");

        UserBiz ub = (UserBiz) container.getBean("userBizImpl");
        ub.add("王五");

//        UserDao userDao = new UserDaolmpl();
//        userDao.add("李四");
//
//        UserBiz userBiz = new UserBizImpl();
//        userBiz.add("赵六");
    }
}
