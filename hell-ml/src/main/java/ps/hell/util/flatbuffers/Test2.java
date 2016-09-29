package ps.hell.util.flatbuffers;

public class Test2 {
	
	public static class TestBean{
		String name="adf";
		int age=34;
		public TestBean(){
			
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
		
	}

	public static void main(String[] args) {
		FlatBufferBuilder builder =new FlatBufferBuilder();
		int val1=builder.createString("test word");
		TestBean bean=new TestBean();
	}
}
