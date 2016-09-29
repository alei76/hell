package ps.hell.ml.forest.classification.decisionTree.quad;

import java.util.ArrayList;

public class INDArray {

	public ArrayList<XyPoint> data=new ArrayList<XyPoint>();
	
	public INDArray(){
		
	}
	
	public void addData(XyPoint val){
		this.data.add(val);
	}
	
	public int rows(){
		return data.size();
	}
	
	public XyPoint mean(){
		double valX=0;
		double valY=0;
		for(int i=0;i<data.size();i++){
			XyPoint v=data.get(i);
			valX+=v.x;
			valY+=v.y;
		}
		return new XyPoint(valX/data.size(),valY/data.size());
	}
	public XyPoint minX(){
		double valx=Double.MAX_VALUE;
		XyPoint re=null;
		for(int i=0;i<data.size();i++){
			XyPoint v=data.get(i);
			if(v.x<valx){
				re=v;
			}
			valx=v.x;
		}
		return re;
	}
	public XyPoint maxX(){
		double valx=-Double.MAX_VALUE;
		XyPoint re=null;
		for(int i=0;i<data.size();i++){
			XyPoint v=data.get(i);
			if(v.x>valx){
				re=v;
			}
			valx=v.x;
		}
		return re;
	}
	public XyPoint minY(){
		double valy=Double.MAX_VALUE;
		XyPoint re=null;
		for(int i=0;i<data.size();i++){
			XyPoint v=data.get(i);
			if(v.y<valy){
				re=v;
			}
			valy=v.y;
		}
		return re;
	}
	public XyPoint maxY(){
		double valy=-Double.MAX_VALUE;
		XyPoint re=null;
		for(int i=0;i<data.size();i++){
			XyPoint v=data.get(i);
			if(v.y>valy){
				re=v;
			}
			valy=v.y;
		}
		return re;
	}
	
}
