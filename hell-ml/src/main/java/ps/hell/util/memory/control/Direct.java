package ps.hell.util.memory.control;
import java.lang.reflect.Field;  
   

import sun.misc.Unsafe;  
   
//http://blog.csdn.net/fenglibing/article/details/17138079
public class Direct {  
	
//	public int value=333;
//
//    public static void main(String... args) throws NoSuchFieldException, SecurityException {
//        Unsafe unsafe = null;
//        Field field=null;
//        try {
//            field= Unsafe.class.getDeclaredField("theUnsafe");
//            field.setAccessible(true);
//            unsafe = (Unsafe) field.get(null);
////            unsafe = Unsafe.getUnsafe();
//
//        } catch (Exception e) {
//            throw new AssertionError(e);
//        }
//
//        long value = 12345;
//        byte size = 1;
//        //获取分配的地址
//        long allocateMemory = unsafe.allocateMemory(size);
//        System.out.println("allocateMemory"+allocateMemory);
//        unsafe.putAddress(allocateMemory, value);
//        long readValue = unsafe.getAddress(allocateMemory);
//        System.out.println("read value : " + readValue);
//        Direct direct=new Direct();
//       System.out.println("地址:"+  unsafe.objectFieldOffset
//               (direct.getClass().getDeclaredField("value")));
////
////        System.out.println("获取地址的值:"+unsafe.getAddress( unsafe.objectFieldOffset
////                (direct.getClass().getDeclaredField("value"))));
//
//        double val=0.343d;
//        byte size2=8;
//
//        allocateMemory = unsafe.allocateMemory(size);
//        System.out.println("allocateMemory"+allocateMemory);
//        unsafe.putDouble(allocateMemory, val);
//        double val2 = unsafe.getAddress(allocateMemory);
//        System.out.println("read value : " + val2);
//
//
//
//        //密码拷贝
//        String password = new String("l00k@myHor$e");
//        String fake = new String(password.replaceAll(".", "?"));
//        System.out.println(password); // l00k@myHor$e
//        System.out.println(fake); // ????????????
//
//        unsafe.copyMemory(fake, 0L, null, unsafe.(password), sizeOf(password));
//
//        System.out.println(password); // ????????????
//        System.out.println(fake); // ????????????
//
//
//        //反射 实例
//        byte[] classContents = getClassContent();
//        Class c = getUnsafe().defineClass(null, classContents, 0, classContents.length);
//        c.getMethod("a").invoke(c.newInstance(), null);
//
//
//        //创建更大的连续数组
//        long SUPER_SIZE = (long)Integer.MAX_VALUE * 2;
//        SuperArray array = new SuperArray(SUPER_SIZE);
//        System.out.println("Array size:" + array.size()); // 4294967294
//        for (int i = 0; i < 100; i++) {
//            array.set((long)Integer.MAX_VALUE + i, (byte)3);
//            sum += array.get((long)Integer.MAX_VALUE + i);
//        }
//        System.out.println("Sum of 100 elements:" + sum);  // 300
//
//
//
//    }
//
//  //Method to read .class file
//    private static byte[] getClassContent() throws Exception {
//        File f = new File("/home/mishadoff/tmp/A.class");
//        FileInputStream input = new FileInputStream(f);
//        byte[] content = new byte[(int)f.length()];
//        input.read(content);
//        input.close();
//        return content;
//    }
//
//
//    class SuperArray {
//        private final static int BYTE = 1;
//        private long size;
//        private long address;
//
//        public SuperArray(long size) {
//            this.size = size;
//            //得到分配内存的起始地址
//            address = getUnsafe().allocateMemory(size * BYTE);
//        }
//        public void set(long i, byte value) {
//            getUnsafe().putByte(address + i * BYTE, value);
//        }
//        public int get(long idx) {
//            return getUnsafe().getByte(address + idx * BYTE);
//        }
//        public long size() {
//            return size;
//        }
//    }
}  