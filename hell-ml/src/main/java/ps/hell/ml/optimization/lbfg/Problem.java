package ps.hell.ml.optimization.lbfg;

import ps.hell.math.base.matrix.Matrix;


public class Problem {
    private Matrix X;
    private Matrix Y;
    private Matrix theta;

    public Problem(Matrix X, Matrix Y, Matrix theta) {
        this.X = X;
        this.Y = Y;
        this.theta = theta;
    }

    public Matrix getX() {
        return X;
    }

    public void setX(Matrix x) {
        X = x;
    }

    public Matrix getY() {
        return Y;
    }

    public void setY(Matrix y) {
        Y = y;
    }

    public Matrix getTheta() {
        return theta;
    }

    public void setTheta(Matrix theta) {
        this.theta = theta;
    }
}
