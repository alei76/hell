package ps.hell.ml.dist.base;

import ps.hell.ml.dist.base.inter.SimilaryFactory;

/**
 * 角距离，是由一定点到两物体之间所量度的夹角
 * 输入为 x y z 的相对坐标，
 * @author Administrator
 *
 */
public class AngularDistance implements SimilaryFactory{



	
	/**
	 * 输入为 x y z 的相对坐标，
	 * @param i
	 * @param j
	 * @return
	 */
    public double measure(double[] i,double[] j) {
    	double theta1 = calcTheta(i);
    	double phi1 = calcPhi(i);
        double theta2 = calcTheta(j);
        double phi2 = calcPhi(j);
        double delTheta = Math.abs(theta1 - theta2);
        double delPhi = Math.abs(phi1 - phi2);
        if (delPhi > Math.PI)
            delPhi = (float)(2.0 * Math.PI - delPhi);
        double dist = delTheta * delTheta + delPhi * delPhi;
        if (dist != 0.0)
            dist = (float)Math.sqrt(dist);
        return dist;
    }

    private static double calcTheta(double[] pos) {
    	double theta = 0.0f;
        double r = Math.sqrt(pos[0] * pos[0] + pos[1] * pos[1]);
        theta = Math.atan2(r, pos[2]);
        return theta;
    }

    private static double calcPhi(double[] pos) {
    	double phi = 0.0f;
        phi = Math.atan2(pos[1], pos[0]);
        if (phi < 0.0)
            phi += 2.0 * Math.PI;
        return phi;
    }

	@Override
	public double getSimilary(double[] userNode1, double[] userNode2,
			double[] weight) {
		// TODO Auto-generated method stub
		return measure(userNode1, userNode2);
	}

	@Override
	public double getSimilary(int[] userNode1, int[] userNode2, double[] weight) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSimilary(String s1, String s2) {
		// TODO Auto-generated method stub
		return 0;
	}


}
