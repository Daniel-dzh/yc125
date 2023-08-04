package SpringTest03.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //带所有参的构造方法
@NoArgsConstructor  //带无参的构造方法
public class Student {

    private String name;
    private double height;
    private double weight;

}
