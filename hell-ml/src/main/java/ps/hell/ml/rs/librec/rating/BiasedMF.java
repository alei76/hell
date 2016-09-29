// Copyright (C) 2014 Guibing Guo
//
// This file is part of LibRec.
//
// LibRec is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// LibRec is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with LibRec. If not, see <http://www.gnu.org/licenses/>.
//

package ps.hell.ml.rs.librec.rating;

import ps.hell.ml.rs.librec.data.DenseMatrix;
import ps.hell.ml.rs.librec.data.DenseVector;
import ps.hell.ml.rs.librec.data.MatrixEntry;
import ps.hell.ml.rs.librec.data.SparseMatrix;
import ps.hell.ml.rs.librec.intf.IterativeRecommender;

/**
 * Biased Matrix Factorization Models. <br/>
 * 偏置矩阵分解模型
 * NOTE: To have more control on learning, you can add additional regularation parameters to user/item biases. For
 * simplicity, we do not do this.
 * 
 * @author guoguibing
 * 
 */
public class BiasedMF extends IterativeRecommender {

	public BiasedMF(SparseMatrix rm, SparseMatrix tm, int fold) {
		super(rm, tm, fold);
	}

	protected void initModel() throws Exception {

		super.initModel();

		userBias = new DenseVector(numUsers);
		itemBias = new DenseVector(numItems);

		// initialize user bias
		userBias.init(initMean, initStd);
		itemBias.init(initMean, initStd);
	}

	@Override
	protected void buildModel() throws Exception {

		for (int iter = 1; iter <= numIters; iter++) {

			loss = 0;
			for (MatrixEntry me : trainMatrix) {

				int u = me.row(); // user
				int j = me.column(); // item
				double ruj = me.get();
				//初始化为全局均值
				double pred = predict(u, j, false);
				//实际值-预测值对应为损失
				double euj = ruj - pred;
				//损失的平方和
				loss += euj * euj;

				// update factors
				double bu = userBias.get(u);
				//预测残差-回归至*bias值
				double sgd = euj - regB * bu;
				//调整用户bias值
				userBias.add(u, lRate * sgd);
				//再加上 bias*bias*regB
				loss += regB * bu * bu;
				//物品 也为相同操作
				double bj = itemBias.get(j);
				sgd = euj - regB * bj;
				itemBias.add(j, lRate * sgd);

				loss += regB * bj * bj;

				for (int f = 0; f < numFactors; f++) {
					//获取用户u对应的第f值
					double puf = P.get(u, f);
					//获取物品j对一个的f值
					double qjf = Q.get(j, f);
					//残差值*qjf值-
					double delta_u = euj * qjf - regU * puf;
					double delta_j = euj * puf - regI * qjf;
					//调整用户对应第f的值
					P.add(u, f, lRate * delta_u);
					//调整物品对应f的值
					Q.add(j, f, lRate * delta_j);
					//累积误差
					loss += regU * puf * puf + regI * qjf * qjf;
				}

			}
			loss *= 0.5;

			if (isConverged(iter))
				break;

		}// end of training

	}

	protected double predict(int u, int j) throws Exception {
		return globalMean + userBias.get(u) + itemBias.get(j) + DenseMatrix.rowMult(P, u, Q, j);
	}

}
