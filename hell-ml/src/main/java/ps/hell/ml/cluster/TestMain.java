package ps.hell.ml.cluster;

import java.util.ArrayList;
import java.util.Iterator;

public class TestMain {
    public static void main (String args[]){
        ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();

       
        double[] a={2,3};
        double[] b={2,4};
        double[] c={1,4};
        double[] d={1,3};
        double[] e={2,2};
        double[] f={3,2};

        double[] g={8,7};
        double[] h={8,6};
        double[] i={7,7};
        double[] j={7,6};
        double[] k={8,5};

        double[] l={100,2};//孤立点

        double[] m={8,20};
        double[] n={8,19};
        double[] o={7,18};
        double[] p={7,17};
        double[] q={7,20};

        dataPoints.add(new DataPoint(a,"a"));
        dataPoints.add(new DataPoint(b,"b"));
        dataPoints.add(new DataPoint(c,"c"));
        dataPoints.add(new DataPoint(d,"d"));
        dataPoints.add(new DataPoint(e,"e"));
        dataPoints.add(new DataPoint(f,"f"));

        dataPoints.add(new DataPoint(g,"g"));
        dataPoints.add(new DataPoint(h,"h"));
        dataPoints.add(new DataPoint(i,"i"));
        dataPoints.add(new DataPoint(j,"j"));
        dataPoints.add(new DataPoint(k,"k"));

        dataPoints.add(new DataPoint(l,"l"));

        dataPoints.add(new DataPoint(m,"m"));
        dataPoints.add(new DataPoint(n,"n"));
        dataPoints.add(new DataPoint(o,"o"));
        dataPoints.add(new DataPoint(p,"p"));
        dataPoints.add(new DataPoint(q,"q"));

        ClusterAnalysis ca=new ClusterAnalysis(3,0,dataPoints,2);
       double[][] cen={{8,7},{8,6},{7,7}};
       ca.startAnalysis(cen);

       ArrayList<DataPoint>[] v = ca.getClusterOutput();
        for (int ii=0; ii<v.length; ii++){
            ArrayList tempV = v[ii];
            System.out.println("-----------Cluster"+ii+"---------");
            Iterator iter = tempV.iterator();
            while(iter.hasNext()){
                DataPoint dpTemp = (DataPoint)iter.next();
                System.out.println(dpTemp.getPointName());
            }
        }
    }

}