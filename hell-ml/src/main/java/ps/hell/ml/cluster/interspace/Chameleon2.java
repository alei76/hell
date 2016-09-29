package ps.hell.ml.cluster.interspace;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Vector;

import ps.hell.math.base.distance.EuclideanDistance;
import ps.hell.math.base.distance.Metric;
import ps.hell.math.base.MathBase;
import ps.hell.math.base.matrix.Matrix;

/**
 * 变色龙 
 * @author Administrator
 *
 */
public class Chameleon2 {
    double[][] W; // weight矩阵(方阵)
    byte[][] Conn; // 连接矩阵(方阵)
    Vector<Vector<Integer>> clusters;
    double MI; // 综合指数
    int k=0;
    Metric<double[]> distMethod=new EuclideanDistance();
 
    double[][] objects=null;
    // 构造函数，初始化变量
    public Chameleon2(int k,double[][] data, double mi) {
    	this.k=k;
    	this.objects=data;
        W = new double[data.length][];
        for (int i = 0; i < data.length; i++) {
            W[i] = new double[data.length];
        }
        Conn = new byte[data.length][];
        for (int i = 0; i < data.length; i++) {
            // 由于是无向图，连接矩阵是对称的，所以我们只用矩阵的下三角以节约空间
            Conn[i] = new byte[i + 1];
        }
        clusters = new Vector<Vector<Integer>>();
        MI = mi;
    }
    public void run(){
    	exec();
    }
 
    // 构造weight矩阵。根据两点间距离的倒数计算两点的相似度，作为连接权重
    public void exec() {
        for (int i = 0; i < W.length; i++) {
            W[i][i] = 1.0f;
            for (int j = i + 1; j < W[i].length; j++) {
                double dist =distMethod.d(objects[i], objects[j]);
                W[i][j] = W[j][i] = 1 / (1 + dist);
            }
        }
        buildSmallCluster();
    }
 
    // 把weight矩阵写入文件，下次计算相同实例时免得重新计算
    public void writeWeightToFile(File file) {
        DataOutputStream fout;
        try {
            fout = new DataOutputStream(
                    new DeflaterOutputStream(new FileOutputStream(file)));
            fout.writeInt(W.length);    //先把方阵的边长写入文件
            for(int i=0;i<W.length;i++)
                for(int j=0;j<W.length;j++)
                    fout.writeDouble(W[i][j]);
            fout.close();
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found!");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
     
    // 从文件读入weight矩阵
    public void readWeightFromFile(File file){
        DataInputStream fin;
        try {
            fin = new DataInputStream(
                    new InflaterInputStream(new FileInputStream(file)));
            fin.readInt();  //第一个数字是方阵的边长，略过
            for(int i=0;i<W.length;i++)
                for(int j=0;j<W.length;j++)
                    W[i][j]=fin.readFloat();
            fin.close();
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found!");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
 
    // CHAMELEON第一阶段，按照K最邻近建立较小的子簇
    public void buildSmallCluster() {
        PriorityQueue<Entry<Integer, Double>> pq = new PriorityQueue<Entry<Integer, Double>>(k, new Comparator<Entry<Integer, Double>>() {
                    @Override
					public int compare(Entry<Integer, Double> arg0,
                            Entry<Integer, Double> arg1) {
                        return arg0.getValue().compareTo(arg1.getValue());
                    }
                });
        for (int i = 0; i < W.length; i++) {
            pq.clear();
            int j = 0;
            HashMap<Integer, Double> map = new HashMap<Integer, Double>();
            // 找到与object距离最小（亦即相似度最大）的K个点
            for (; j < k; j++) {
                map.clear();
                map.put(j, W[i][j]);
                pq.add(map.entrySet().iterator().next());
            }
            for (; j < W[i].length; j++) {
                if (W[i][j] > pq.peek().getValue()) {
                    pq.poll();
                    map.clear();
                    map.put(j, W[i][j]);
                    pq.add(map.entrySet().iterator().next());
                }
            }
            for (j = 0; j < k; j++) {
                Entry<Integer, Double> entry = pq.poll();
                if (i > entry.getKey())
                    Conn[i][entry.getKey()] = 1;
                else if (i < entry.getKey())
                    Conn[entry.getKey()][i] = 1;
                // 对角线上的设为0
            }
        }
        // 根据连接矩阵构造连通子图
        boolean[] visited = new boolean[W.length];
        boolean allvisited = false;
        while (!allvisited) {
            allvisited = true;
            L1: for (int i = 0; i < W.length; i++) {
                if (visited[i])
                    continue;
                allvisited = false;
                // 搜寻第i列，以图发现新子图的第一个点
                for (int j = i + 1; j < W.length; j++) {
                    // 发现了新子图的第一个点
                    if (Conn[j][i] == 1) {
                        Vector<Integer> cluster = new Vector<Integer>();
                        Queue<Integer> queue = new LinkedList<Integer>();
                        queue.add(i);
                        queue.add(j);
                        while (!queue.isEmpty()) {
                            int ele = queue.poll();
                            cluster.add(ele);
                            visited[ele] = true;
                            // 遍历第ele列
                            for (int k = ele + 1; k < W.length; k++) {
                                if (visited[k])
                                    continue;
                                if (Conn[k][ele] == 1 && !queue.contains(k)) {
                                    queue.add(k);
                                }
                            }
                            // 遍历第ele行
                            for (int k = 0; k < ele; k++) {
                                if (visited[k])
                                    continue;
                                if (Conn[ele][k] == 1 && !queue.contains(k))
                                    queue.add(k);
                            }
                        }
                        clusters.add(cluster);
                        break L1;
                    }
                }
            }
        }
    }
 
    // 打印子簇
    public void printClusters() {
        for (int i = 0; i < clusters.size(); i++) {
            System.out.print("以下数据点属于第" + i + "簇：");
            Iterator<Integer> iter = clusters.get(i).iterator();
            while (iter.hasNext()) {
                System.out.print(iter.next() + ",");
            }
            System.out.println();
        }
    }
 
    // CHAMELEON第二阶段，合并相对互联度RI和相对紧密度RC都较高的簇
    public void cluster() {
        int len = clusters.size();
        float[] EC1 = new float[len];
        for (int i = 0; i < len; i++) {
            Vector<Integer> vec = clusters.get(i);
            for (int j = 0; j < vec.size(); j++) {
                for (int k = 0; k < vec.size(); k++) {
                    EC1[i] += W[vec.get(j)][vec.get(k)];
                }
            }
        }
        boolean end = true;
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                Vector<Integer> vec1 = clusters.get(i);
                Vector<Integer> vec2 = clusters.get(j);
                float EC = 0.0f;
                float RI = 0.0f;
                float SEC = 0.0f;
                float RC = 0.0f;
                for (int k = 0; k < vec1.size(); k++) {
                    for (int m = 0; m < vec2.size(); m++) {
                        EC += W[vec1.get(k)][vec2.get(m)];
                    }
                }
                RI = 2 * EC / (EC1[i] + EC1[j]);
                RC = (vec1.size() + vec2.size()) * EC
                        / (vec2.size() * EC1[i] + vec1.size() * EC1[j]);
                // 以RI*RC作为综合指数
                System.out.println("RI:"+RI+"\tRC:"+RC);
                if (RI * RC > MI) {
                    mergeClusters(i, j);
                    end = false;
                    break;
                }
            }
        }
        // 递归合并子簇
        if (!end)
            cluster();
    }
 
    // 把簇b合并到簇a里面去
    public void mergeClusters(int a, int b) {
        Iterator<Integer> iter = clusters.get(b).iterator();
        while (iter.hasNext()) {
            clusters.get(a).add(iter.next());
        }
        clusters.remove(b);
    }
 
    public static void main(String[] args) {
       Matrix matrix=Matrix.random(50,10);
       double[][] data=matrix.data;
       data=MathBase.times(data,10);
//      datasource.readMatrix(new File("/home/orisun/test/text.normalized.mat"));
//      datasource.readRLabel(new File("/home/orisun/test/text.rlabel"));
//      File wfile=new File("/home/orisun/test/test_weight");
//      if(!wfile.exists()){
//          try {
//              wfile.createNewFile();
//          } catch (IOException e) {
//              e.printStackTrace();
//          }
//      }
        //综合指数0.1
        Chameleon2 cham = new Chameleon2(4,data, 0.1);
//      cham.readWeightFromFile(wfile);
        cham.run();
//      cham.writeWeightToFile(wfile);
        System.out.println("==============第一阶段后的分类结果==============");
        cham.printClusters();
        cham.cluster();
        System.out.println("==============第二阶段后的分类结果==============");
        cham.printClusters();
    }
}