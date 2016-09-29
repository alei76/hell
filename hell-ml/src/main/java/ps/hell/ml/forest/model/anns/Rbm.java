package ps.hell.ml.forest.model.anns;

import java.util.ArrayList;

import ps.landerbuluse.ml.math.MathBase;
import ps.landerbuluse.ml.math.matrix.Matrix;


/**
 * 受限的波尔茨曼机
 * 测试使用，无调试
 * @author Administrator
 *
 */
public class Rbm {

	public int n_visible=0;
	
	public int n_hidden=0;
	
	public int max_epoch=50;
	
	public int batch_size=110;
	
	public double penalty=2E-4f;
	
	public boolean annel=false;
	
	public double[][] v_bias=null;
	
	public double[][] h_bias=null;
	public double[][] w=null;
	boolean anneal=true;
	
	ArrayList<Double> errors=new ArrayList<Double>(1000);
	/**
	 * 数据源
	 */
	public double[][] x=null;
	
	
	public double[][] hiden_value=null;
	
	
	public double[][] rebuild_value=null;
	public Rbm(int n_visible,int n_hidden,int  max_epoch,int batch_size,
			double penalty,boolean anneal,double[][] w ,
			double[][] v_bias,double[][] h_bias){
		this.n_visible=n_visible;
		this.n_hidden=n_hidden;
		this.max_epoch=max_epoch;
		this.batch_size=batch_size;
		this.penalty=penalty;
		this.annel=anneal;
		this.w=w;
		if(w==null){
			this.w=MathBase.random(n_visible, n_hidden, 0.1d);
		}
		this.v_bias=v_bias;
		if(v_bias==null){
			this.v_bias=MathBase.number(1,n_visible,0d);
		}
		this.h_bias=h_bias;
		if(h_bias==null){
			this.h_bias=MathBase.number(1,n_hidden,0d);
		}
	}
	public Rbm(int n_visible,int n_hidden,int max_epoch){
		this.n_visible=n_visible;
		this.n_hidden=n_hidden;
		this.max_epoch=max_epoch;
		if(w==null){
			this.w=MathBase.random(n_visible, n_hidden, 0.1d);
		}
		if(v_bias==null){
			this.v_bias=MathBase.number(1,n_visible,0d);
		}
		if(h_bias==null){
			this.h_bias=MathBase.number(1,n_hidden,0d);
		}
	}
	
	public double sigmod(double val){
		return MathBase.sigmod(val);
	}
	/**
	 * 正向传播
	 * @param vis
	 * @return
	 */
	public double[][] forward(double[][] vis){
		//获取逆
		vis=MathBase.getInver(vis);
		double[][] pre_simod_input=MathBase.times(vis,this.w);
		return MathBase.sigmod(MathBase.plus(pre_simod_input, this.h_bias));
	}
	/**
	 * 反向传播
	 * @param vis
	 * @return
	 */
	public double[][] backward(double[][] vis){
		double[][] wT=MathBase.transport(w);
		double[][] val=MathBase.plus(vis, MathBase.plus(wT,v_bias));
		return MathBase.sigmod(val);
	}
	
	public ArrayList<double[][]> batch(){
		double eta=0.1f;
		double momentum=0.5f;
		int d=x.length;
		int n=x[0].length;
		int num_batchs=Math.round(n*1f/this.batch_size)+1;
		double[] groups=MathBase.range(0, num_batchs,batch_size);
		groups=MathBase.getData(groups, 0, n);
		double[] perm=MathBase.range(0,n);
		int[] permI=MathBase.getDataRandomInt(perm);
		groups=MathBase.getData(groups,permI);
		ArrayList<double[][]> val=new ArrayList<double[][]>();
		for(int i=0;i<=n;i++){
			//需要崔继抽取x个数据
			val.add(x);
		}
		return val;
	}
	
	
	public void rbmBB(double[][] x){
		this.x=x;
		double eta=0.1f;
		double momentum=0.5f;
		double[][] W=w.clone();
		double[][] b=h_bias.clone();
		double[][] c=v_bias.clone();
		double[][] Wavg=w.clone();
		double[][] bavg=b.clone();
		double[][] cavg=c.clone();
		double[][] Winc=MathBase.number(n_visible,n_hidden,0d);
		double[] binc=MathBase.number(n_hidden,0d);
		double[] cinc=MathBase.number(n_hidden,0d);
		double avgstart=max_epoch-5;
		ArrayList<double[][]> batch_data=batch();
		int num_batch=batch_data.size();
		double oldpenalty=penalty;
		int t=1;
		ArrayList<Object> errors=new ArrayList<Object>();
		for(int epoch:MathBase.rangeInt(0,max_epoch)){
			double err_sum=0f;
			if(anneal){
				penalty=oldpenalty- 0.9f * epoch / max_epoch * oldpenalty;
			}
			for(double batch:MathBase.range(0, num_batch)){
				int num_dims=batch_data.get((int)batch).length;
				int num_cases=batch_data.get((int)batch)[0].length;
				double[][] data=batch_data.get((int)batch);
				//forward
				double[][] ph=forward(data);
				double[][] ph_states=MathBase.random(num_cases,n_hidden, 1d);
				double[][] ph_temp=MathBase.random(num_cases,n_hidden, 1d);
				for(int i=0;i<ph_temp.length;i++){
					for(int j=0;j<ph_temp[0].length;j++){
						if(ph[i][j]>ph_temp[i][j]){
							ph_states[i][j]=1;
						}
					}
				}
				//backward
				double[][] nh_states=ph_states.clone();
				double[][] neg_data=backward(nh_states);
				double[][] neg_data_states=MathBase.number(num_cases,num_dims,0d);
				
				//forward one more time
				neg_data_states=MathBase.transport(neg_data_states);
				double[][] nh=forward(neg_data_states);
				nh_states=MathBase.number(num_cases, n_hidden, 0d);
				double[][] nh_states_temp=MathBase.random(num_cases,n_hidden, 1d);
				for(int i=0;i<nh_states_temp.length;i++){
					for(int j=0;j<nh_states_temp[0].length;j++){
						if(nh[i][j]>nh_states_temp[i][j]){
							nh_states[i][j]=1;
						}
					}
				}
				//update wegith and biases
				double[][] dW=MathBase.minus(MathBase.times(data,ph),MathBase.times(neg_data_states,nh));
				double[] dc=MathBase.minus(MathBase.sum(data,true),MathBase.sum(neg_data_states,true));
				double[] db=MathBase.minus(MathBase.sum(ph,false),MathBase.sum(nh,false));
				double[][] winc_temp=MathBase.minus(MathBase.divide(dW, num_cases),MathBase.times(W,penalty));
				Winc=MathBase.sum(MathBase.times(Winc,momentum),MathBase.times(MathBase.times(data, eta),(winc_temp)));
				binc=MathBase.sum(MathBase.times(binc,momentum),MathBase.times(MathBase.divide(db, num_cases),eta));
				double[] cinc_temp=MathBase.divide(dc, num_cases);
				cinc=MathBase.sum(MathBase.times(cinc, momentum), MathBase.times(cinc_temp, eta));
				W=MathBase.sum(W,Winc);
				b=MathBase.sum(b,binc,true);
				c=MathBase.sum(c,cinc,true);
				this.w=W.clone();
				this.h_bias=b.clone();
				this.v_bias=c.clone();
				if(epoch>avgstart){
					Wavg=MathBase.minus(Wavg,MathBase.times(MathBase.minus(Wavg,W),(1*1d/t)));
					cavg=MathBase.minus(cavg,MathBase.times(MathBase.minus(cavg,c),(1*1d/t)));
					bavg=MathBase.minus(bavg,MathBase.times(MathBase.minus(bavg,b),(1*1d/t)));
					t+=1;
				}else{
					Wavg=W.clone();
					bavg=b.clone();
					cavg=c.clone();
				}
				double error=MathBase.norm(MathBase.minus(data, MathBase.transport(neg_data)));
				err_sum+=error;
				System.out.println("error_sum:"+err_sum+"\tthis_error:"+error);
				errors.add(err_sum);
			}
			this.hiden_value=forward(x);
			int h_row=hiden_value.length;
			int h_col=hiden_value[0].length;
			double[][] hiden_states=MathBase.number(h_row,h_col,0d);
			double[][] hiden_states_temp=MathBase.random(h_row,h_col, 1d);
			for(int i=0;i<hiden_states_temp.length;i++){
				for(int j=0;j<hiden_states_temp[0].length;j++){
					if(hiden_value[i][j]>hiden_states_temp[i][j]){
						hiden_states[i][j]=1;
					}
				}
			}
			rebuild_value=backward(hiden_states);
			this.w=Wavg;
			this.h_bias=b;
			this.v_bias=c;
		}
	}
	
	public double[][] visulize(double[][] X){
		int D=X.length;
		int N=x[0].length;
		int s=(int)Math.sqrt(D);
		double[][] a=null;
		if(s==(int)Math.floor(Math.sqrt(D))){
			int num=(int)Math.ceil(Math.sqrt(N));
			a=MathBase.number(num*s+num+1,num*s+num+1,0d);
			int x=0;
			int y=0;
			for(int i:MathBase.rangeInt(0, N)){
				double[] z_temp=MathBase.getCol(X, i);
				double[][] z=MathBase.reshape(z_temp,s,s,false);
				z=MathBase.transport(z);
				for(int m=0;m<s;m++){
					for(int n=0;n<s;n++){
						a[x*s+1+x+i][y*s+1+y]=z[m][n];
					}
				}
				x=x+1;
				if(x>=num){
					x=0;
					y=y+1;
				}
			}
		}else{
			a=X;
		}
		return a;
	}
	
	public static void main(String[] args) {
		Matrix matrix=new Matrix(200,8);
		Matrix val=matrix.random(200, 8);
		double[] pre=MathBase.number(200,1d);
		for(int i=100;i<200;i++){
			pre[i]=1;
		}
		Matrix dataM=val.addCol(pre);
		//读取数据
		double[][] data=dataM.data;
		data=MathBase.transport(data);
		Rbm rbm= new Rbm(784,100,50);
		rbm.rbmBB(data);
		//orginal data
		double[][] a=rbm.visulize(data);
		
		//rebuild data
		double[][] rebuild_value=MathBase.transport(rbm.rebuild_value);
		double[][] b=rbm.visulize(rebuild_value);
		
		//hidden data
		double[][] hidden_value=MathBase.transport(rbm.hiden_value);
		double[][] c=rbm.visulize(hidden_value);
		
		//weight value(w)
		double[][] w_value=rbm.w;
		double[][] d=rbm.visulize(w_value);
	}
}
