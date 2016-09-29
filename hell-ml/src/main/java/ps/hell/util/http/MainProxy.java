package ps.hell.util.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import ps.hell.util.FileUtil;
import ps.hell.util.JsonUtil;
import ps.hell.util.db2.Redis;

public class MainProxy {

    public static String str = "http://nuodezhongxinbj.fang.com/";
    int page = 0;

    public static LinkedList<IPProxy> proxyList = null;

    private static boolean isTrue = true;

    /**
     * 是否使用redis存储 还是文件存储
     */
    public static boolean useRedis = true;

    public static Redis redis = null;

    public static String proxyKey = "proxyKey";

    /**
     * 设置redis参数
     *
     * @param redis
     */
    public static void setRedis(Redis redis) {
        MainProxy.redis = redis;
    }

    /**
     * 获取代理列表 获取代理
     */
    public static void run(boolean isTrue) {
        MainProxy.isTrue = isTrue;
        if (MainProxy.isTrue) {
            // 使用可以使用的 ip port 代理
            proxyList = getIpTrue(null);
        } else {
            // 获取测试的ip port
            proxyList = getIp(null);
        }
    }

    static {
        // 获取如果为false 则 使用全部的ip地址
        // 为true则为使用有效的测试地址
        boolean getTrueProxy = true;
        run(getTrueProxy);
        // 测试 并
        if (!getTrueProxy) {
            // test();
            // run(!getTrueProxy);
        } else {
        }
    }

    /**
     * 返回的代理 重新填充
     *
     * @param proxy
     */
    public synchronized static void addProxy(IPProxy proxy,
                                             HashMap<String, String> config) {
        if (proxy == null) {
            return;
        }
        if (useRedis) {
            try {
                redis.rpush(config.get(proxyKey), JsonUtil.getJsonStr(proxy));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            proxyList.addLast(proxy);
        }
    }

    /**
     * 从redis中获取 代理ip和port
     *
     * @param config
     * @return
     */
    public static IPProxy get(HashMap<String, String> config) {
        if (useRedis) {
            if (config == null) {
                return null;
            }
            // 从redis中获取代理池
            try {
                String key = config.get(proxyKey);
                if (key == null) {
                    return null;
                }
                String str = redis.lpop(key);
                IPProxy proxy = parseProxy(str);
                return proxy;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
        return get();
    }

    /**
     * 获取porxy 使用简单随机获取 应使用加权获取机制
     *
     * @return 如果返回为null则表示 没有代理地址可用
     */
    public synchronized static IPProxy get() {
        // 如果使用redis
        int count = 0;
        while (true) {
            count++;
            if (proxyList.size() == 0 && count <= 5) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            // 如果代理池中没有了proxy 则等待
            if (proxyList.size() == 0) {
                // System.out.println("没有代理ip了");
                // System.out.println("重新获取代理ip");
                if (MainProxy.isTrue) {
                    // 使用可以使用的 ip port 代理
                    proxyList = getIpTrue(null);
                } else {
                    // 获取测试的ip port
                    proxyList = getIp(null);
                }
            }
            break;
        }
        Random random = new Random();
        if (proxyList.size() <= 0) {
            return null;
        }
        int i = Math.abs(random.nextInt() % proxyList.size());
        IPProxy porxy = proxyList.remove(i);
        return porxy;
    }

    /**
     * 测试代理并 输出到文件中
     *
     * @param inputFile  输入的代理地址
     * @param outPutFile 输出的代理地址
     */
    public static void test(String inputFile, String outPutFile) {
        if (inputFile.equals(outPutFile)) {
            System.out.println("输入输出目录相同");
            System.exit(1);
        }
        FileUtil file3 = new FileUtil(inputFile, "utf-8", false, null);
        ArrayList<String> oldList = file3.readAndClose();
        FileUtil file2 = new FileUtil(outPutFile, "utf-8", false, "other");
        int i = 0;
        while (oldList.size() > 0) {
            String str = oldList.get(i);
            i++;
            if (str.length() < 0) {
                continue;
            }
            IPProxy proxy = (IPProxy) JsonUtil.getDtoFromJsonObjStr(str,
                    IPProxy.class);
            // 测试代理服务
            String url = "http://www.baidu.com";
            // 设置超时时间
            AntGetUrlProxy.limitTime = 1000;
            try {
                String urlCode = AntGetUrlProxy.testDoGet(url, proxy);
            } catch (Exception e) {
                System.out.println("无效代理:" + proxy.getIp());
                continue;
            }
            System.out.println("有效：" + proxy.getIp());
            file2.write(str);
        }
        file2.close();
    }

    /**
     * 获取proxy
     *
     * @param url
     * @return
     */
    public static LinkedList<IPProxy> getIp(String url) {
        LinkedList<IPProxy> list = new LinkedList<IPProxy>();
        // FileUtil2 file=new
        // FileUtil2(System.getProperty("user.dir")+"/data/proxyTruebak.txt","gbk",false);
        // LinkedList<String> list2=file.readAndClose();
        // ArrayList<String> list2 =
        // HdfsFileGet.getFile(ConfigStatic.PROXY_FILE);
        FileUtil fileUtil = new FileUtil(
                "./config/crawler/proxy/proxyTrue.txt", "utf-8", false, null);
        ArrayList<String> list2 = fileUtil.readAndClose();
        int i = 0;
        while (true) {
            if (list2.size() <= 0) {
                break;
            }
            String ip = list2.get(i);
            i++;
            IPProxy proxy = (IPProxy) JsonUtil.getDtoFromJsonObjStr(ip,
                    IPProxy.class);
            list.add(proxy);
        }
        return list;
    }

    /**
     * 获取proxy
     *
     * @param url
     * @return
     */
    public static LinkedList<IPProxy> getIpTrue(String url) {
        LinkedList<IPProxy> list = new LinkedList<IPProxy>();
        // FileUtil2 file=new
        // FileUtil2(System.getProperty("user.dir")+"/data/proxyTrue.txt","utf-8",false);
        // LinkedList<String> list2=file.readAndClose();
        FileUtil fileUtil = new FileUtil(
                "./config/crawler/proxy/proxyTrue.txt", "utf-8", false, null);
        ArrayList<String> list2 = fileUtil.readAndClose();
        int i = 0;
        while (true) {
            if (list2.size() <= 0) {
                break;
            }
            String ip = list2.get(i);
            i++;
            IPProxy proxy = parseProxy(ip);
            if (proxy == null) {
                continue;
            }
            list.add(proxy);
        }
        return list;
    }

    /**
     * 获取数量
     *
     * @param config
     * @return
     */
    public static long getProxySize(HashMap<String, String> config) {
        if (useRedis) {
            try {
                return redis.llen(config.get(proxyKey));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            return proxyList.size();
        }
        return 0l;
    }

    /**
     * 从字符串解析出 proxy的ip和port
     *
     * @param ipString
     * @return
     */
    public static IPProxy parseProxy(String ipString) {
        if (ipString == null) {
            return null;
        }
        if (ipString.startsWith("{")) {
            IPProxy proxy = (IPProxy) JsonUtil.getDtoFromJsonObjStr(ipString,
                    IPProxy.class);
            return proxy;
        } else {
            String[] strList = ipString.split(":");
            if (strList.length == 2) {
                try {
                    IPProxy proxy = new IPProxy();
                    proxy.setIp(strList[0]);
                    proxy.setPort(Integer.parseInt(strList[1]));
                    return proxy;
                } catch (Exception e) {
                }
            }
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        //
        // MainProxy proxy = new MainProxy();
        // proxy.run(true);
        // MainProxy.get();// 获取一个代理ip和port
        // MainProxy.test("输入的proxy文件", "输出并可用的proxy文件");

        // 检测代理是否有效
        // MainProxy.test(System.getProperty("user.dir") +
        // "/data/proxyTrue.txt",
        // System.getProperty("user.dir") + "/data/proxyTrue1.txt");

        Redis redis = new Redis("192.168.1.11", 51900);
        FileUtil file = new FileUtil(System.getProperty("user.dir")
                + "/data/proxyTrue1.txt", "utf-8", false, null);
        ArrayList<String> list = file.readAndClose();
        for (String str : list) {
            redis.rpush("proxyPool", str);
        }
    }
}
