package ps.hell.ml.statistics.base.value.gmm;

import java.util.ArrayList;

public class Parameter {  
    private ArrayList<ArrayList<Double>> pMiu; // 均值参数k个分布的中心点，每个中心点d维  
    private ArrayList<Double> pPi; // k个GMM的权值  
    private ArrayList<ArrayList<ArrayList<Double>>> pSigma; // k类GMM的协方差矩阵,d*d*k  
      
    public ArrayList<ArrayList<Double>> getpMiu() {  
        return pMiu;  
    }  
    public void setpMiu(ArrayList<ArrayList<Double>> pMiu) {  
        this.pMiu = pMiu;  
    }  
    public ArrayList<Double> getpPi() {  
        return pPi;  
    }  
    public void setpPi(ArrayList<Double> pPi) {  
        this.pPi = pPi;  
    }  
    public ArrayList<ArrayList<ArrayList<Double>>> getpSigma() {  
        return pSigma;  
    }  
    public void setpSigma(ArrayList<ArrayList<ArrayList<Double>>> pSigma) {  
        this.pSigma = pSigma;  
    }  
} 