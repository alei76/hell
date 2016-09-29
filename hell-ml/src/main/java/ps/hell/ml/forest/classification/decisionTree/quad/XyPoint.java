package ps.hell.ml.forest.classification.decisionTree.quad;

public class XyPoint {

	public double x;
	public double y;
	public XyPoint(double x,double y){
		this.x=x;
		this.y=y;
	}
	@Override
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("{x:").append(x).append(",y:").append(y).append("}");
		return sb.toString();
	}
}
