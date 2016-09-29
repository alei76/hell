package ps.hell.util.codeInject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 任意注入同级数据的方法
 * @author Administrator
 *
 */
public class AddCounterClassAdapter extends ClassAdapter {
	public String owner=null;
    private boolean isInterface;
    private HashMap<String,String> method=null;
    private String use=null;
    /**
     * @param cv
     * @param method 需要统计的方法
     */
    public AddCounterClassAdapter(ClassVisitor cv,HashSet<String> method) {
        super(cv);
        this.method=new HashMap<String,String>();
        int i=0;
        for(String me:method){
        	i++;
        	this.method.put(me,"auto_timer_"+i);
        }
    }
    @Override
    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
        this.owner=name;
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
    }
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
            String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if(!name.equals("<init>") && !isInterface && mv!=null){
            //为方法添加计时功能
        	use=name;
        	System.out.println(name+"\t"+method.get(name));
        	if(method.get(name)!=null){        		
            mv = new AddTimeMethodAdapter(mv,method.get(name));
        	}
        }
        return mv;
    }
    @Override
    public void visitEnd() {
        //添加字段
        if(!isInterface){
        	for(Entry<String,String> me:method.entrySet()){
            FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC,me.getValue(), "J", null, 1L);
            if(fv!=null){
                fv.visitEnd();
            }
        	}
        }
        cv.visitEnd();
    }
    
    class AddTimeMethodAdapter extends MethodAdapter{
    	private String counterName=null;
        public AddTimeMethodAdapter(MethodVisitor mv,String counterName) {
            super(mv);
            this.counterName=counterName;
        }
        @Override
        public void visitCode() {
//            mv.visitCode();
//            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, counterName, "J");
//            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
//            mv.visitInsn(Opcodes.LSUB);
//            mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, counterName, "J");
        }
        @Override
        public void visitInsn(int opcode) {
        	System.out.println(counterName);
            if((opcode>=Opcodes.IRETURN && opcode<=Opcodes.RETURN) || opcode==Opcodes.ATHROW){
                mv.visitFieldInsn(Opcodes.GETSTATIC, owner, counterName, "J");
                mv.visitLdcInsn(1L);
               // mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
                mv.visitInsn(Opcodes.LADD);
                mv.visitFieldInsn(Opcodes.PUTSTATIC, owner,counterName, "J");
            }
            mv.visitInsn(opcode);
        }
        @Override
        public void visitMaxs(int maxStack, int maxLocal) {
            mv.visitMaxs(maxStack+4, maxLocal);
        }
    }
    
    public static void main(String[] args){
        try {
            ClassReader cr = new ClassReader("com/util/codeInject/C");
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//            ClassAdapter classAdapter = new AddTimeClassAdapter(cw);
            HashSet<String> method=new HashSet<String>();
            method.add("m2");
            ClassAdapter classAdapter = new AddCounterClassAdapter(cw,method);
            //使给定的访问者访问Java类的ClassReader
            cr.accept(classAdapter, ClassReader.SKIP_DEBUG);
            byte[] data = cw.toByteArray();
            File file = new File(System.getProperty("user.dir") + "/src/com/util/codeInject/trans/C.class");
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(data);
            fout.close();
            System.out.println("success!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
