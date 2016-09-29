package ps.hell.util.test;
public class TwoTest {
    public static void main(String[] args) {
        TestBean1 testBean1 = new TestBean1();
        TestBean2 testBean2 = new TestBean2();
        // 这个正常
        testBean2.setTest(testBean1 == null ? new Integer(1) : testBean1.getTest());
        // 这个报空指针
        testBean2.setTest(testBean1 == null ? 1 : testBean1.getTest());
        System.out.println(testBean2.getTest());
    }

    static class TestBean1 {
        private Integer test;

        public Integer getTest() {
            return test;
        }

        public void setTest(Integer test) {
            this.test = test;
        }
    }

    static class TestBean2 {
        private Integer test;

        public Integer getTest() {return test;
        }

        public void setTest(Integer test) {
            this.test = test;
        }
    }
}

         