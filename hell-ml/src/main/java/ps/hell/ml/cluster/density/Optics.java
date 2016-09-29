package ps.hell.ml.cluster.density;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Optics {
	
	 class ComparatorDp implements Comparator<DataPoint>{
	        @Override
			public int compare(DataPoint arg0, DataPoint arg1) {
	            double temp=arg0.getReachableDistance()-arg1.getReachableDistance();
	            int a = 0;
	            if (temp < 0) {
	                a = -1;
	            } else {
	                a = 1;
	            }
	            return a;
	        }
	    }

	   
	    public List<DataPoint> startAnalysis(List<DataPoint> dataPoints,
	            double radius, int ObjectNum) {
	        List<DataPoint> dpList = new ArrayList<DataPoint>();
	        List<DataPoint> dpQue = new ArrayList<DataPoint>();

	        int total = 0;
	        while (total < dataPoints.size()) {
	            if (isContainedInList(dataPoints.get(total), dpList) == -1 ) {
	            	//如果 不为核心点
	                List<DataPoint> tmpDpList = isKeyAndReturnObjects(dataPoints.get(total),
	                        dataPoints, radius, ObjectNum);
	                if(tmpDpList != null && tmpDpList.size() > 0){
	                	//如果存在核心点
	                    DataPoint newDataPoint=new DataPoint(dataPoints.get(total));
	                    //将该点放入队列中
	                   dpQue.add(newDataPoint);
	                }
	            }
	            while (!dpQue.isEmpty()) {
	                DataPoint tempDpfromQ = dpQue.remove(0);
	                DataPoint newDataPoint=new DataPoint(tempDpfromQ);
	                dpList.add(newDataPoint);
	                List<DataPoint> tempDpList = isKeyAndReturnObjects(tempDpfromQ,
	                        dataPoints, radius, ObjectNum);
	                System.out.println(newDataPoint.getName()+":"+newDataPoint.getReachableDistance());
	                if (tempDpList != null && tempDpList.size() > 0) {
	                    for (int i = 0; i < tempDpList.size(); i++) {
	                        DataPoint tempDpfromList = tempDpList.get(i);
	                        int indexInList = isContainedInList(tempDpfromList,
	                                dpList);
	                        int indexInQ = isContainedInList(tempDpfromList, dpQue);
	                        if (indexInList == -1) {
	                        	//如果不包含名
	                            if (indexInQ > -1) {
	                                int index = -1;
	                                for (DataPoint dataPoint : dpQue) {
	                                    index++;
	                                    //获取最小的可达距离
	                                    if (index == indexInQ) {
	                                        if (dataPoint.getReachableDistance() > tempDpfromList
	                                                .getReachableDistance()) {
	                                            dataPoint
	                                                    .setReachableDistance(tempDpfromList
	                                                            .getReachableDistance());
	                                        }
	                                    }
	                                }
	                            } else {
	                                dpQue.add(new DataPoint(tempDpfromList));
	                            }
	                        }
	                    }

	                    // TODO：对Q进行重新排序
	                    Collections.sort(dpQue, new ComparatorDp());
	                }
	            }
	            System.out.println("------");
	            total++;

	        }

	        return dpList;
	    }

	   
	    public void displayDataPoints(List<DataPoint> dps){
	        for(DataPoint dp: dps){
	            System.out.println(dp.getName()+":"+dp.getReachableDistance());
	        }
	    }

	   /**
	    * 是否包含名
	    * @param dp
	    * @param dpList
	    * @return
	    */
	    private int isContainedInList(DataPoint dp, List<DataPoint> dpList) {
	        int index = -1;
	        for (DataPoint dataPoint : dpList) {
	            index++;
	            if (dataPoint.getName().equals(dp.getName())) {
	                return index;
	            }
	        }
	        return -1;
	    }

	   /**
	    * 获取是否为核心点及其对应的可达点
	    * @param dataPoint
	    * @param dataPoints
	    * @param radius
	    * @param ObjectNum
	    * @return
	    */
	   private List<DataPoint> isKeyAndReturnObjects(DataPoint dataPoint,List<DataPoint> dataPoints,double radius,int ObjectNum){
	       List<DataPoint> arrivableObjects=new ArrayList<DataPoint>(); //用来存储所有直接密度可达对象
	       List<Double> distances=new ArrayList<Double>(); //欧几里得距离
	       double coreDistance; //核心距离

	        for (int i = 0; i < dataPoints.size(); i++) {
	            DataPoint dp = dataPoints.get(i);
	            //获取欧几里得距离
	            double distance = getDistance(dataPoint, dp);
	            if (distance <= radius) {
	                distances.add(distance);
	                arrivableObjects.add(dp);
	            }
	        }

	       if(arrivableObjects.size()>=ObjectNum){
	           List<Double> newDistances=new ArrayList<Double>(distances);
	           Collections.sort(distances);
	           coreDistance=distances.get(ObjectNum-1);
	           //获取最大可达距离 
	           for(int j=0;j<arrivableObjects.size();j++){
	                if (coreDistance > newDistances.get(j)) {
	                	//如果为覆盖点则不变
	                    if(newDistances.get(j)==0){
	                    	//设置可达距离
	                        dataPoint.setReachableDistance(coreDistance);
	                    }
	                    arrivableObjects.get(j).setReachableDistance(coreDistance);
	                }else{
	                    arrivableObjects.get(j).setReachableDistance(newDistances.get(j));
	                }
	           }
	           return arrivableObjects;
	       }

	       return null;
	   }

	   
	    private double getDistance(DataPoint dp1,DataPoint dp2){
	        double distance=0.0;
	        double[] dim1=dp1.getDimensioin();
	        double[] dim2=dp2.getDimensioin();
	        if(dim1.length==dim2.length){
	            for(int i=0;i<dim1.length;i++){
	                double temp=Math.pow((dim1[i]-dim2[i]), 2);
	                distance=distance+temp;
	            }
	            distance=Math.pow(distance, 0.5);
	            return distance;
	        }
	        return distance;
	    }

	    public static void main(String[] args){
	         ArrayList<DataPoint> dpoints = new ArrayList<DataPoint>();
	        
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
	         double[] q={8,21};

	         dpoints.add(new DataPoint(a,"a"));
	         dpoints.add(new DataPoint(g,"g"));
	         dpoints.add(new DataPoint(b,"b"));
	         dpoints.add(new DataPoint(c,"c"));
	         dpoints.add(new DataPoint(d,"d"));
	         dpoints.add(new DataPoint(e,"e"));
	         dpoints.add(new DataPoint(f,"f"));

	    
	         dpoints.add(new DataPoint(h,"h"));
	         dpoints.add(new DataPoint(i,"i"));
	         dpoints.add(new DataPoint(j,"j"));
	         dpoints.add(new DataPoint(k,"k"));

	         dpoints.add(new DataPoint(l,"l"));

	         dpoints.add(new DataPoint(m,"m"));
	         dpoints.add(new DataPoint(n,"n"));
	         dpoints.add(new DataPoint(o,"o"));
	         dpoints.add(new DataPoint(p,"p"));
	         dpoints.add(new DataPoint(q,"q"));

	         Optics ca=new Optics();
	         List<DataPoint> dps=ca.startAnalysis(dpoints, 2, 4);
	         ca.displayDataPoints(dps);
	    }
}
 class DataPoint {
    private String name; // 样本点名
    private double dimensioin[]; // 样本点的维度
    private double coreDistance; //核心距离，如果该点不是核心对象，则距离为-1
    private double reachableDistance; //可达距离

    public DataPoint(){
    }

    public DataPoint(DataPoint e){
        this.name=e.name;
        this.dimensioin=e.dimensioin;
        this.coreDistance=e.coreDistance;
        this.reachableDistance=e.reachableDistance;
    }

    public DataPoint(double dimensioin[],String name){
        this.name=name;
        this.dimensioin=dimensioin;
        this.coreDistance=-1;
        this.reachableDistance=-1;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double[] getDimensioin() {
        return dimensioin;
    }
    public void setDimensioin(double[] dimensioin) {
        this.dimensioin = dimensioin;
    }
    public double getCoreDistance() {
        return coreDistance;
    }
    public void setCoreDistance(double coreDistance) {
        this.coreDistance = coreDistance;
    }
    public double getReachableDistance() {
        return reachableDistance;
    }
    public void setReachableDistance(double reachableDistance) {
        this.reachableDistance = reachableDistance;
    }
}

