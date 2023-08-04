package org.ycframework.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ycframework.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class YcAnnotationConfigApplicationContext implements YcApplicationContext{
    private Logger logger = LoggerFactory.getLogger( YcAnnotationConfigApplicationContext.class );
    //存每个待托管的Bean的定义信息
    private Map<String,YcBeanDefinition> beanDefinitionMap = new HashMap<>();
    //存每个实例化后的beam
    private Map<String,Object> beanMap = new HashMap<>();
    //存系统属性，db.properties
    private Properties pros;

    /**
     * YcAnnotation配置应用上下文
     * @param configClasses
     */
    public YcAnnotationConfigApplicationContext( Class...configClasses) {
        try {
            //读取文件系统的属性，然后将其存储
            pros = System.getProperties();
            List<String> toScanPackagePath = new ArrayList<>();
            for (Class cls : configClasses) {
                if ( cls.isAnnotationPresent(YcConfiguration.class)==false){
                    continue;
            }
                String[] basePackages = null;
                //扫描配置类上的 @YcComponentScan注解，读取要扫描的包
                if ( cls.isAnnotationPresent(YcComponentScan.class)){
                    //如果true，则说明此配置类上有@YcComponentScan，则读取basePackages
                    YcComponentScan ycComponentScan = (YcComponentScan) cls.getAnnotation(YcComponentScan.class);
                    basePackages = ycComponentScan.basePackages();

                    if ( basePackages==null || basePackages.length<=0 ){
                        basePackages = new String[1];
                        basePackages[0] = cls.getPackage().getName();
                    }

                    logger.info( cls.getName()+"类上有@YcComponentScan注解，他要扫描的路径是:"+ basePackages[0]  );
                }
                //将这些包中的带有IOC注解的类加载到一个 Map 中存储起来
                //开始扫描这些basePackages包下的bean,并加载包装成 BeanDefinition 对象,存储到beanDefinition
                recursionLoadBeanDefinition( basePackages );
            }
            //循环 beanDefinitionMap,创建 bean(是否为懒加载,或者是单例),存储到 beanMap
            createBean();
            //循环所有托管的beanMap中的bean，看属性和方法是否有@AutoWired,@Resource,@Value....考虑DI注入
            doDi();
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        }
    }

    /**
     * 开始扫描这些basePackages包下的bean，并加载包装成 BeanDefinition 对象，储存到beanDefinitionMap中
     */
    private void doDi() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        //循环的是beanMap，这是托管Bean
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            String beanId = entry.getKey();
            Object beanObj = entry.getValue();
            //情况一:属性上有 @YcResource注解的情况
            Field[] fields = beanObj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent( YcResource.class)){
                    YcResource ycResource = field.getAnnotation( YcResource.class);
                    String toDiBeanId = ycResource.name();
                    //从 beanMap中找，是否为lazy，singleton
                    Object obj = getToDiBean( toDiBeanId);
                    //注入
                    field.setAccessible( true ); //因为属性是private的，所以要将他accessible设为true
                    field.set( beanObj, obj );   // userBizImpl。userDao = userDaoImpl
                }
            }
        }
    }

    private Object getToDiBean(String toDiBeanId) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if ( !beanMap.containsKey(toDiBeanId)){
            return beanMap.get( toDiBeanId);
        }
        //判断beanMap中没有此bean是否因为lazy
        if ( ! beanDefinitionMap.containsKey(toDiBeanId)){
            throw new RuntimeException("spring容器中没有加载此class:"+toDiBeanId);
        }
        YcBeanDefinition bd = beanDefinitionMap.get( toDiBeanId );
        if ( bd.isLazy() ){
            //是因为懒，所以没有托管
            String classpath = bd.getClassInfo();
            Object beanObj = Class.forName( classpath ).newInstance();
            beanMap.put( toDiBeanId, beanObj );
            return beanObj;
        }
        //是否因为prototype
        if ( bd.getScope().equalsIgnoreCase("prototype")){
            //是因为懒所以没托管
            String classpath = bd.getClassInfo();
            Object beanObj = Class.forName( classpath ).newInstance();
            //beanMap.put( toDiBean, beanObj ); 原型模式下，每次getBean创建一次bean，所以beanMap不存
            return beanObj;
        }
        return null;
    }

    /**
     * 创建Bean的实例对象
     * 循环·beanDefinitionMap，创建bean(是否为懒加载，单例)，存到beanMap中
     */
    private void createBean() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (Map.Entry<String, YcBeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanId = entry.getKey();
            YcBeanDefinition ybd = entry.getValue();
            if (!ybd.isLazy() || !ybd.isPrimary() || ! ybd.getScope().equalsIgnoreCase("protocol")){
                String classInfo = ybd.getClassInfo();
                Object obj = Class.forName( classInfo ).newInstance();
                beanMap.put( beanId, obj );
                logger.trace("spring容器托管了:"+beanId+"==》"+ classInfo);
            }
        }

    }

    /**
     * 递归加载Bean定义
     * 循环beanDefinitionMap，创建bean(是否为懒加载，是单例)，存到beanMap中去
     * @param basePackages  基础包数组--》存储基础包
     */
    private void recursionLoadBeanDefinition(String[] basePackages) {

        for (String basePackage : basePackages) {
            //将包名中的 . 替换成路径分隔符 /
            String packagePath = basePackage.replace("\\", "/");
            //创建Enumeration 集合,用来存储 URL：每个资源的路径
            Enumeration<URL> files = null;
            try{
                files = Thread.currentThread().getContextClassLoader().getResources( packagePath);
                //循环这个files，看是否是我们要加载的资源
                while (files.hasMoreElements()){
                    URL url = files.nextElement();
                    //查找此包下的类     com/yc-->全路径  com/yc-->包名
                    findPackageClasses(url.getFile(), basePackage);
                }
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查找包类
     * @param packagePath   包路径
     * @param basePackage   基础包
     */
    private void findPackageClasses(String packagePath, String basePackage) {
        //路径异常的处理，前面有/,则表明是文件，则去除他
        if ( packagePath.startsWith("/")){
            packagePath = packagePath.substring(1);
        }
        File file  = new File( packagePath);
        //只取后缀名为.class的字节码
        //写法一
        /*
        File[] classFiles = file.listFiles( );
        */

        //写法二：lambda写法
        File[] classFiles = file.listFiles( (pathname)->{
            if ( pathname.getName().endsWith(".class") || pathname.isDirectory()){
                return true;
            }
            return false;
        });
        //循环此classFiles
        if ( classFiles==null || classFiles.length<=0 ){
            return;
        }
        for (File cf : classFiles) {
            if ( cf.isDirectory() ){
                //继续递归
                logger.trace("递归:"+ cf.getAbsolutePath()+",它对应的包名为："+(basePackage+"."+cf.getName()));
                findPackageClasses( cf.getAbsolutePath(), basePackage+"."+cf.getName());
            }else {
                //是class文件，则取出文件，判断此文件对应的class中是否有@Component注解
                URLClassLoader uc = new URLClassLoader( new URL[]{});
                Class cls = null;
                try{
                    cls = uc.loadClass(basePackage+"."+cf.getName().replaceAll(".class",""));
                    if (    cls.isAnnotationPresent(YcComponent.class)
                            ||cls.isAnnotationPresent(YcConfiguration.class)
                            ||cls.isAnnotationPresent(YcController.class)
                            ||cls.isAnnotationPresent(YcRepository.class)
                            ||cls.isAnnotationPresent(YcService.class)
                    ){
                        logger.info("加载到一个待托管的的类"+cls.getName());
                        YcBeanDefinition bd = new YcBeanDefinition();
                        if (cls.isAnnotationPresent( YcLazy.class))
                            bd.setLazy(true);
                        if ( cls.isAnnotationPresent( YcScope.class )){
                            YcScope ycScope = (YcScope) cls.getAnnotation( YcScope.class);
                            String scope = ycScope.value();
                            bd.setScope( scope );
                        }
                        //classInfo中保存这个待托管的类的包路径
                        bd.setClassInfo( basePackage+"."+cf.getName().replaceAll(".class",""));
                        //存储到 beanDefinitionMap 中
                        String beanId = getBeanId( cls );
                        this.beanDefinitionMap.put(beanId,bd);
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 获取BeanId
     * 获取一个待托管的BeanId
     * @param cls  直接用类名(首字母小写)
     * @return
     */
    private String getBeanId(Class cls) {
        YcComponent ycComponent = (YcComponent) cls.getAnnotation(YcComponent.class);
        YcController ycController = (YcController) cls.getAnnotation(YcController.class);
        YcService ycService = (YcService) cls.getAnnotation(YcService.class);
        YcRepository ycRepository = (YcRepository) cls.getAnnotation(YcRepository.class);
        YcConfiguration ycConfiguration = (YcConfiguration) cls.getAnnotation(YcConfiguration.class);

        if (ycConfiguration != null){
            return cls.getSimpleName();   //全路径名
        }
        String beanId = null;
        if (ycComponent != null){
            beanId = ycComponent.value();
        }else if (ycController != null){
            beanId = ycController.value();
        }else if (ycService != null){
            beanId = ycService.value();
        }else if (ycRepository != null){
            beanId = ycRepository.value();
        }else if (ycConfiguration != null){
            beanId = ycConfiguration.value();
        }
        if ( beanId==null || "".equalsIgnoreCase( beanId )){
            String typeName = cls.getSimpleName();
            beanId = typeName.substring(0,1).toLowerCase()+ typeName.substring(1);
        }
        return beanId;
    }

    @Override
    public Object getBean(String beanId) {
        YcBeanDefinition bd = this.beanDefinitionMap.get(beanId);

        if ( bd==null ){
            throw new RuntimeException("容器中没有加载此bean");
        }

        String scope = bd.getScope();

        if ("prototype".equalsIgnoreCase( scope )){
            Object obj = null;
            try{
                obj = Class.forName( bd.getClassInfo()).newInstance();
                //这种原型模式创建的bean不能保存到beanMap中
                return obj;
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        if ( this.beanMap.containsKey( beanId)){
            return this.beanMap.get( beanId );
        }

        if ( bd.isLazy() ){
            Object obj = null;
            try {
                obj = Class.forName( bd.getClassInfo() ).newInstance();
                //懒加载的bean是要保存的
                this.beanMap.put( beanId, obj);
                return obj;
            } catch ( IllegalAccessException e ) {
                e.printStackTrace();
            } catch ( InstantiationException e ) {
                e.printStackTrace();
            } catch ( ClassNotFoundException e ) {
                e.printStackTrace();
            }
            return obj;
        }

        return null;
    }
}
