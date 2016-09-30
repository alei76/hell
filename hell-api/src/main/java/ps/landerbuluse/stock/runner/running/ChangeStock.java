package ps.landerbuluse.stock.runner.running;

/**
 * Created by Administrator on 2016/9/30.
 * 股票变更方法
 */
public interface ChangeStock {

    /**
     * 添加开盘策略
     */
    public void addOpeningStrategy();

    /**
     * 添加盘中策略
     */
    public void addSessionStrategy();

    /**
     * 添加收盘策略
     */
    public void addCloseStrategy();
}
