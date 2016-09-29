package ps.hell.ml.cluster;

import java.util.ArrayList;

public class DataPoint {
    private double dimension[]; //样本点的维度
    private String pointName; //样本点名字
    private Cluster cluster; //类簇
    private double euDt;//样本点到质点的距离

    public DataPoint(double dimension[], String pointName) {
        this.dimension = dimension;
        this.pointName = pointName;
        this.cluster = null;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }


   /**
    * 计算平方和
    * @return
    */
    public double calEuclideanDistanceSum() {
        double sum=0.0;
        Cluster cluster= getCluster();
        ArrayList<DataPoint> dataPoints=cluster.getDataPoints();

        for(int i=0;i<dataPoints.size();i++){
            double[] dims=dataPoints.get(i).getDimensioin();
            for(int j=0;j<dims.length;j++){
                 double temp=Math.pow((dims[j]-this.dimension[j]),2);
                 sum=sum+temp;
            }
        }

        return Math.sqrt(sum);
    }

   
    public double testEuclideanDistance(Medoid c) {
        double sum=0.0;
        double[] cDim=c.getDimensioin();

        for(int i=0;i<dimension.length;i++){
           double temp=Math.pow((dimension[i]-cDim[i]),2);
           sum=sum+temp;
        }

        return Math.sqrt(sum);
    }

    public double[] getDimensioin() {
        return this.dimension;
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public double getCurrentEuDt() {
        return this.euDt;
    }

    public String getPointName() {
        return this.pointName;
    }
}