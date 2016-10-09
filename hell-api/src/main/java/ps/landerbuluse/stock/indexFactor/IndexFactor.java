package ps.landerbuluse.stock.indexFactor;

/**
 * Created by Administrator on 2016/10/9.
 */
public interface IndexFactor {

    /**
     * 获取指标值
     * @return
     */
    public float getIndex();

    /**
     * 获取指标名字
     * @return
     */
    public String getName();
}
