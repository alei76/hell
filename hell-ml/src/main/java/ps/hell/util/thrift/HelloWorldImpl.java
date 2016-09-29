package ps.hell.util.thrift;
import org.apache.thrift.TException;
 
/**
 * blog http://www.micmiu.com
 *
 * @author Michael
 *
 */
public class HelloWorldImpl implements HelloWorldService.Iface {
 
	public HelloWorldImpl() {
	}
 
	@Override
	public String sayHello(String username) throws TException {
		return "Hi," + username + " welcome to my blog www.micmiu.com";
	}
 
}