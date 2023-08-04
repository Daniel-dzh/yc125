package SpringTest04;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "SpringTest04")
@PropertySource(value = "classpath:db.properties")
/*
spring启动时，PropertySource类扫描 -->classpath:db.properties
使用键值对形式存储
 */
public class Config {
}
