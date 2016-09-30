package ps.landerbuluse.stock.user.account;

/**
 * Created by Administrator on 2016/9/30.
 */
public class AccountLogger {
    /**
     * 时间
     */
    String date = null;
    /**
     * 账户总值
     */
    float accountAll =0f;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getAccountAll() {
        return accountAll;
    }

    public void setAccountAll(float accountAll) {
        this.accountAll = accountAll;
    }
}
