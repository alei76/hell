package ps.hell.ml.statistics.base.value.gmm;

import java.util.ArrayList;
import java.util.List;

public class GMMAlgorithm {  
      
    /** 
     *  
    * @Title: GMMCluster  
    * @Description: GMM聚类算法的实现类，返回每条数据的类别(0~k-1) 
    * @return int[] 
    * @throws 
     */  
    public int[] GMMCluster(ArrayList<ArrayList<Double>>dataSet, ArrayList<ArrayList<Double>> pMiu, int dataNum, int k, int dataDimen) {  
        Parameter parameter = iniParameters(dataSet, dataNum, k, dataDimen);  
        double Lpre = -1000000; // 上一次聚类的误差  
        double threshold = 0.0001;  
        while(true) {  
            ArrayList<ArrayList<Double>> px = computeProbablity(dataSet, pMiu, dataNum, k, dataDimen);  
            double[][] pGama = new double[dataNum][k];  
            for(int i = 0; i < dataNum; i++) {  
                for(int j = 0; j < k; j++) {  
                    pGama[i][j] = px.get(i).get(j) * parameter.getpPi().get(j);  
                }  
            }  
              
            double[] sumPGama = GMMUtil.matrixSum(pGama, 2);  
            for(int i = 0; i < dataNum; i++) {  
                for(int j = 0; j < k; j++) {  
                    pGama[i][j] = pGama[i][j] / sumPGama[i];  
                }  
            }  
              
            double[] NK = GMMUtil.matrixSum(pGama, 1); // 第k个高斯生成每个样本的概率的和，所有Nk的总和为N  
              
            // 更新pMiu  
            double[] NKReciprocal = new double[NK.length];  
            for(int i = 0; i < NK.length; i++) {  
                NKReciprocal[i] = 1 / NK[i];  
            }  
            double[][] pMiuTmp = GMMUtil.matrixMultiply(GMMUtil.matrixMultiply(GMMUtil.diag(NKReciprocal), GMMUtil.matrixReverse(pGama)), GMMUtil.toArray(dataSet));  
              
            // 更新pPie  
            double[][] pPie = new double[k][1];  
            for(int i = 0; i < NK.length; i++) {  
                pPie[i][1] = NK[i] / dataNum;  
            }  
              
            // 更新k个pSigma  
            double[][][] pSigmaTmp = new double[dataDimen][dataDimen][k];  
            for(int i = 0; i < k; i++) {  
                double[][] xShift = new double[dataNum][dataDimen];  
                for(int j = 0; j < dataNum; j++) {  
                    for(int l = 0; l < dataDimen; l++) {  
                        xShift[j][l] = pMiuTmp[i][l];  
                    }  
                }  
                  
                double[] pGamaK = new double[dataNum]; // 第k条pGama值  
                for(int j = 0; j < dataNum; j++) {  
                    pGamaK[j] = pGama[j][i];  
                }  
                double[][] diagPGamaK = GMMUtil.diag(pGamaK);  
                  
                double[][] pSigmaK = GMMUtil.matrixMultiply(GMMUtil.matrixReverse(xShift), (GMMUtil.matrixMultiply(diagPGamaK, xShift)));  
                for(int j = 0; j < dataDimen; j++) {  
                    for(int l = 0; l < dataDimen; l++) {  
                        pSigmaTmp[j][l][k] = pSigmaK[j][l] / NK[i];  
                    }  
                }  
            }  
              
            // 判断是否迭代结束  
            double[][] a = GMMUtil.matrixMultiply(GMMUtil.toArray(px), pPie);  
            for(int i = 0; i < dataNum; i++) {  
                a[i][0] = Math.log(a[i][0]);  
            }  
            double L = GMMUtil.matrixSum(a, 1)[0];  
              
            if(L - Lpre < threshold) {  
                break;  
            }  
            Lpre = L;  
        }  
        return null;  
    }  
      
    /** 
     *  
    * @Title: computeProbablity  
    * @Description: 计算每个节点（共n个）属于每个分布（k个）的概率 
    * @return ArrayList<ArrayList<Double>> 
    * @throws 
     */  
    public ArrayList<ArrayList<Double>> computeProbablity(ArrayList<ArrayList<Double>>dataSet, ArrayList<ArrayList<Double>> pMiu, int dataNum, int k, int dataDimen) {  
        double[][] px = new double[dataNum][k]; // 每条数据属于每个分布的概率   
        int[] type = getTypes(dataSet, pMiu, k, dataNum);  
          
        // 计算k个分布的协方差矩阵  
        ArrayList<ArrayList<ArrayList<Double>>> covList = new ArrayList<ArrayList<ArrayList<Double>>>();  
        for(int i = 0; i < k; i++) {  
            ArrayList<ArrayList<Double>> dataSetK = new ArrayList<ArrayList<Double>>();  
            for(int j = 0; j < dataNum; j++) {  
                if(type[k] == i) {  
                    dataSetK.add(dataSet.get(i));  
                }  
            }  
            covList.set(i, GMMUtil.computeCov(dataSetK, dataDimen, dataSetK.size()));  
        }  
          
        // 计算每条数据属于每个分布的概率   
        for(int i = 0; i < dataNum; i++) {  
            for(int j = 0; j < k; j++) {  
                ArrayList<Double> offset = GMMUtil.matrixMinus(dataSet.get(i), pMiu.get(j));  
                ArrayList<ArrayList<Double>> invSigma = covList.get(k);  
                double[] tmp = GMMUtil.matrixSum(GMMUtil.matrixMultiply(GMMUtil.toArray1(offset), GMMUtil.toArray(invSigma)), 2);  
                double coef = Math.pow((2 * Math.PI), -(double)dataDimen / 2d) * Math.sqrt(GMMUtil.computeDet(invSigma, invSigma.size()));  
                px[i][j] = coef * Math.pow(Math.E, -0.5 * tmp[0]);  
            }  
        }  
          
        return GMMUtil.toList(px);  
    }  
      
    /** 
     *  
    * @Title: iniParameters  
    * @Description: 初始化参数Parameter 
    * @return Parameter 
    * @throws 
     */  
    public Parameter iniParameters(ArrayList<ArrayList<Double>> dataSet, int dataNum, int k, int dataDimen) {  
        Parameter res = new Parameter();  
          
        ArrayList<ArrayList<Double>> pMiu = generateCentroids(dataSet, dataNum, k);  
        res.setpMiu(pMiu);  
          
        // 计算每个样本节点与每个中心节点的距离，以此为据对样本节点进行分类计数，进而初始化k个分布的权值  
        ArrayList<Double> pPi = new ArrayList<Double>();  
        int[] type = getTypes(dataSet, pMiu, k, dataNum);  
        int[] typeNum = new int[k];  
        for(int i = 0; i < dataNum; i++) {  
            typeNum[type[i]]++;  
        }  
        for(int i = 0; i < k; i++) {  
            pPi.add((double)(typeNum[i]) / (double)(dataNum));  
        }  
        res.setpPi(pPi);  
          
        // 计算k个分布的k个协方差  
        ArrayList<ArrayList<ArrayList<Double>>> pSigma = new ArrayList<ArrayList<ArrayList<Double>>>();  
        for(int i = 0; i < k; i++) {  
            ArrayList<ArrayList<Double>> tmp = new ArrayList<ArrayList<Double>>();  
            for(int j = 0; j < dataNum; j++) {  
                if(type[j] == i) {  
                    tmp.add(dataSet.get(i));  
                }  
            }  
            pSigma.add(GMMUtil.computeCov(tmp, dataDimen, dataNum));  
        }  
        res.setpSigma(pSigma);  
          
        return res;  
    }  
      
    /** 
     *  
    * @Title: generateCentroids  
    * @Description: 获取随机的k个中心点 
    * @return ArrayList<ArrayList<Double>> 
    * @throws 
     */  
    public ArrayList<ArrayList<Double>> generateCentroids(ArrayList<ArrayList<Double>> data, int dataNum, int k) {  
        ArrayList<ArrayList<Double>> res = null;  
        if(dataNum < k) {  
            return res;  
        } else {  
            res =  new ArrayList<ArrayList<Double>>();  
              
            List<Integer> random = new ArrayList<Integer>();  
            // 随机产生不重复的k个数  
            while(k > 0) {  
                int index = (int)(Math.random() * dataNum);  
                if(!random.contains(index)) {  
                    random.add(index);  
                    k--;  
                    res.add(data.get(index));  
                }  
            }  
        }  
        return res;  
    }  
      
    /** 
     *  
    * @Title: getTypes  
    * @Description: 返回每条数据的类别 
    * @return int[] 
    * @throws 
     */  
    public int[] getTypes(ArrayList<ArrayList<Double>> dataSet, ArrayList<ArrayList<Double>> pMiu, int k, int dataNum) {  
        int[] type = new int[dataNum];  
        for(int j = 0; j < dataNum; j++) {  
            double minDistance = GMMUtil.computeDistance(dataSet.get(j), pMiu.get(0));  
            type[j] = 0; // 0作为该条数据的类别  
            for(int i = 1; i < k; i++) {  
                if(GMMUtil.computeDistance(dataSet.get(j), pMiu.get(0)) < minDistance) {  
                    minDistance = GMMUtil.computeDistance(dataSet.get(j), pMiu.get(0));  
                    type[j] = k;  
                }  
            }  
        }  
        return type;  
    }  
      
    public static void main(String[] args) {  
        ArrayList<Double> pPi = new ArrayList<Double>();  
        System.out.println(pPi.get(0));  
    }  
}  