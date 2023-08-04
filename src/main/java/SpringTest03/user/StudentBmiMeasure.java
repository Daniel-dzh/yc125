package SpringTest03.user;

import org.springframework.stereotype.Component;
import SpringTest03.system.Measure;

@Component("bmiMeasure")
public class StudentBmiMeasure implements Measure {

    @Override
    public double doMeasure(Object obj) {
        if ( obj==null ){
            throw new RuntimeException("待数据异常");
        }
        if ( !(obj instanceof Student )){
            throw new RuntimeException("待数据异常");
        }
        Student s = (Student) obj;
        return s.getWeight()/(s.getHeight() * s.getHeight());
    }
}
