package SpringTest03.system;

/**
 * 测量工具接口
 */
public interface Measure {

    /**
     * 测量
     * @param object    测量对象
     * @return  测量值
     */
    public double doMeasure(Object obj);

}
