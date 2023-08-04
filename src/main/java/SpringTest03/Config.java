package SpringTest03;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan(basePackages = {"SpringTest03"})
@ComponentScan(basePackages = {"SpringTest03.system", "SpringTest03.user"})
public class Config {
}
