package SpringTest01;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
/*@Configuration
    标识此类是一个配置类(这个类也会被spring创建),声明容器运行时的一些配置信息
 */
@ComponentScan(basePackages = {"SpringTest01"})
/*@ComponentScan
    扫描的路径,这个路径所有带有@Component,@Repository,@Service,@Controller这样的注解都会被spring托管
 */
public class Config {

}
