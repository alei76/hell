package ps.hell.ml.optimization.lbfg;

import ps.hell.math.base.matrix.Matrix;


/*
 * Limited-memory Broyden-Fletcher-Goldfarb-Shanno (L-BFGS)
 * 
 */
public class LBFGS extends BFGS {

    public static Matrix train(Optimizer opt, int Iter, int m, double e) {
        int dim = opt.getProblem().getTheta().rowNum;
        Matrix s = new Matrix(dim, m);
        Matrix y = s.copy();
        Matrix rho = new Matrix(m,m);
        Matrix g = opt.getGradient();
        Matrix x = opt.getProblem().getTheta();
        for (int k = 0; k < Iter; k++) {
            int current = k % m;
//            Matrix ma=new Matrix();
            Matrix d = getHdotg(g, s, y, rho, k, m).times(-1);
            double lambda = backtrackLineSearch(opt, d, 0.5, 0.0001);
            s.putColumn(current, d.times(lambda));
            Matrix newx = x.add(s.getCo(current));
            opt.update(newx);
            Matrix newg = opt.getGradient();
            if (newg.sub(g).norm2() < e) {
                return opt.getProblem().getTheta();
            }
            y.putColumn(current, newg.sub(g));
            double ys = y.getColumn(current).transpose().mmul(s.getColumn(current)).get(0);
            rho.put(current, ys == 0 ? 0 : (1 / ys));
            g = newg.dup();
            x = newx.dup();
            System.out.println(k + " - " + opt.getObjectValue() + " - " + newg.norm2());
        }

        return opt.getProblem().getTheta();
    }

    private static Matrix getHdotg(Matrix g, Matrix s,
    		Matrix y, Matrix rho, int k, int m) {
        int delta = (k <= m ? 0 : k - m);
        int L = (k <= m ? k : m);
        Matrix alpha = new Matrix(L);
        Matrix beta = alpha.dup();
        Matrix q = new Matrix(g.getLength(), L + 1);
        q.putColumn(L, g);
        // backward
        for (int i = L - 1; i >= 0; i--) {
            int j = (i + delta) % m;
            alpha.put(i, s.getColumn(j).transpose().mmul(q.getColumn(i + 1)).mul(rho.get(j)).get(0));
            q.putColumn(i, q.getColumn(i + 1).sub(y.getColumn(j).mul(alpha.get(i))));
        }

        // forward
        Matrix z = q.dup();
        for (int i = 0; i < L; i++) {
            int j = (i + delta) % m;
            beta.put(j, y.getColumn(j).transpose().mmul(z.getColumn(i)).mul(rho.get(j)).get(0));
            z.putColumn(i + 1, z.getColumn(i).add(s.getColumn(j).mul(alpha.get(i) - beta.get(i))));
        }
        return z.getColumn(L);
    }
}
