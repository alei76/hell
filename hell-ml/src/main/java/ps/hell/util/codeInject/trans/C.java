package ps.hell.util.codeInject.trans;
public class C {
    public static long timer;
    public void m() throws InterruptedException{
        timer -= System.currentTimeMillis();
        Thread.sleep(100); 
        timer += System.currentTimeMillis();
    }
}