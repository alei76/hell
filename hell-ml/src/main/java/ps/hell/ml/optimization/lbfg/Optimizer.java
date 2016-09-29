package ps.hell.ml.optimization.lbfg;

import ps.hell.math.base.matrix.Matrix;


public class Optimizer {
    protected Problem problem;

    public Optimizer(Problem problem) {
        this.problem = problem;
    }

    public void update(Matrix theta) {
        this.problem.setTheta(theta);
    }

    protected Matrix getLinearCombination(Matrix theta) {
        return theta.transpose().times(problem.getX()).transpose();
    }

    protected Matrix getLinearCombination() {
        return getLinearCombination(problem.getTheta());
    }

    // Default: Mean Square Loss
    public double getObjectValue(Matrix theta) {
        return getLinearCombination(theta).minus(problem.getY()).sumOfSquares();
    }

    public double getObjectValue() {
        return getObjectValue(problem.getTheta());
    }

    // Default: Gradient of Mean Square Loss
    public Matrix getGradient() {
    	Matrix diff = getLinearCombination().minus(problem.getY());
        return problem.getX().times(diff).divide(problem.getX().colNum);
    }

    public Problem getProblem() {
        return problem;
    }
}
