//package ps.hell.ml.groupAi.ga;
//
//import java.util.ArrayList;
//
//import ps.hell.util.ShellExec;
//
//import com.ml.recommandSystem.svdFeather.SvdFeather;
//import com.ml.recommandSystem.svdFeather.SvdFeatherGetRmse;
//
//public class GA_OuterExec implements OuterExecInter{
//
//	public static String dir="/home/apps/tianlie_exec/svdfeather/svd/demo/basicMF/svddatabase";
//
//	public void GA_OuterExec()
//	{
//
//	}
//	@Override
//	public ModelValue get(double[] inputList,ArrayList<String> inputStrList) {
//		// TODO Auto-generated method stub
//		//此处设置对应的调整参数值
//		//inputStrList 0 存储的为对应的线程号;
//		//目前只调节学习率
////		System.out.println(inputStrList.get(0));
//		System.out.println(inputList[0]);
//		SvdFeather svdFeather=new SvdFeather(null,dir+inputStrList.get(0)+"/");
//		svdFeather.setConfigParam("learning_rate",inputList[0]);
//		svdFeather.run();
//	//	System.exit(1);
//		//执行调度svd程序
//		System.out.println(dir+inputStrList.get(0)+"/run.sh");
//		ShellExec.execShellAndPrint(dir+inputStrList.get(0)+"/run.sh");
//		//然后执行获取emse值
//		return SvdFeatherGetRmse.get(dir+inputStrList.get(0));
//		//return null;
//	}
//}
