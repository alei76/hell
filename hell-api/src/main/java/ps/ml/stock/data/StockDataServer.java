package ps.ml.stock.data;

import org.apache.commons.codec.EncoderException;

import java.io.IOException;

/**
 * Created by Administrator on 2016/9/23.
 */
public interface StockDataServer {


    public Object getData(String stockId,String start,String end) throws IOException, EncoderException;

    public void test() throws IOException, EncoderException;
}
