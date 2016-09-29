package ps.hell.ml.cluster.level.birch;
public class CF {
 
    private int N;
    private double[] LS;
    private double[] SS;
 
    public CF() {
        LS=new double[BIRCH.dimen];
        SS=new double[BIRCH.dimen];
    }
 
    // 根据一个data point instance创建一个Clustering Feature
    public CF(double[] data) {
        int len = data.length;
        this.N = 1;
        this.LS = data;
        this.SS=new double[len];
        for (int i = 0; i < len; i++)
            this.SS[i] = Math.pow(data[i], 2);
    }
     
    //复制构造函数(深复制)
    public CF(CF cf){
        this.N=cf.getN();
        int len=cf.getLS().length;
        this.LS=new double[len];
        this.SS=new double[len];
        for(int i=0;i<len;i++){
            this.LS[i]=cf.getLS()[i];
            this.SS[i]=cf.getSS()[i];
        }
    }
 
    // 采用D2计算两个CF Entry之间的距离
    public double getDistanceTo(CF entry) {
        double dis = 0.0;
        int len = this.LS.length;
        // 采用D2
        for (int i = 0; i < len; i++) {
            dis += this.SS[i] / this.N + entry.getSS()[i] / entry.getN() - 2
                    * this.LS[i] * entry.getLS()[i] / (this.N * entry.getN());
        }
        return Math.sqrt(dis);
    }
     
    //采用D0计算两个簇心之间的欧氏距离
//  public double getDistanceTo(CF entry) {
//      int len=entry.getLS().length;
//      double[] a=new double[len];
//      double[] b=new double[len];
//      for(int i=0;i<len;i++){
//          a[i]=this.getLS()[i]/this.N;
//          b[i]=this.getSS()[i]/this.N;
//      }
//      return calEuraDist(a,b,len);
//  }
 
    // 加上或减去一个CF的值
    public void addCF(CF entry, boolean add) {
        int opt = 1; // 默认为相加
        if (!add) // 如果add为false则为相减
            opt = -1;
        this.N = this.N + entry.getN() * opt;
        int len = this.LS.length;
        for (int i = 0; i < len; i++) {
            this.LS[i] = this.LS[i] + entry.getLS()[i] * opt;
            this.SS[i] = this.SS[i] + entry.getSS()[i] * opt;
        }
    }
 
    //计算两个向量的欧氏距离
    public static double calEuraDist(double[] arr1,double[] arr2,int len){
        double result=0.0;
        for(int i=0;i<len;i++){
            result+=Math.pow(arr1[i]-arr2[i],2.0);
        }
        return Math.sqrt(result);
    }
    public int getN() {
        return N;
    }
 
    public void setN(int n) {
        N = n;
    }
 
    public double[] getLS() {
        return LS;
    }
 
    public void setLS(double[] lS) {
        LS = lS;
    }
 
    public double[] getSS() {
        return SS;
    }
 
    public void setSS(double[] sS) {
        SS = sS;
    }
 
}