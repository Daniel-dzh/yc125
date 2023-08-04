package SpringTest03.system;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class Container<T> {

    private List<T> objs = new ArrayList<T>();

    @Resource(name = "bmiMeasure")
    private Measure measure;
    @Resource(name = "bmiFilter")
    private ContainerFilter filter;

    private T max;
    private T min;
    private double avg;
    private double sum;

    /**
     * 添加对象的方法
     * @param t
     */
    public void add(T t){
        //判断数据是否合格，调用数据筛选方法实现
        if (filter != null){
            if (filter.doFilter( t ) == false ){
                return;
            }
        }
        //添加到 objs
        objs.add( t );
        //判断大小,记录max,min,计算avg -->调用Measure实现
        if ( objs.size() ==1 ){
            max = t;
            min = t;
        }else {
            double val = this.measure.doMeasure( t );
            double maxval = this.measure.doMeasure( max );
            double minval = this.measure.doMeasure( min );
            if ( val>maxval ){
                max = t;
            }
            if ( val<minval ){
                min = t;
            }
        }
        sum += measure.doMeasure( t );
        avg = sum/objs.size();
    }

    public int size(){
        return objs.size();
    }


    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    /**
     * 系统复位,清空所有数据
     */
    public void clearAll(){
        objs = new ArrayList<T>();
        measure = null;
        filter = null;
        max = null;
        min = null;
        avg = 0;
    }
}
