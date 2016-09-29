package ps.hell.ml.cluster.level.birch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
 
public class BIRCH {
 
    public static final int dimen=4;
    LeafNode leafNodeHead=new LeafNode();
    int point_num=0;        //point instance计数
     
    //逐条扫描数据库，建立B-树
    public TreeNode buildBTree(String filename){
        //先建立一个叶子节点
        LeafNode leaf=new LeafNode();
        TreeNode root=leaf;
 
        //把叶子节点加入存储叶子节点的双向链表
        leafNodeHead.setNext(leaf);
        leaf.setPre(leafNodeHead);
        //打开文件，从文件中读取原始数据
        File file = new File(filename);
        if(!file.exists()){
            System.out.println("Data File Not Exists.");
            System.exit(2);
        }
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            String line=null;
            while((line=br.readLine())!=null && line.trim()!=""){
                point_num++;
                String[] cont=line.split("[,|\\s+]");
                //读入point instance
                double[] data=new double[dimen];
                for(int i=0;i<data.length;i++){
                    data[i]=Double.parseDouble(cont[i]);
                }
                String mark=String.valueOf(point_num)+cont[data.length];
                //根据一个point instance创建一个MinCluster
                CF cf=new CF(data);
                MinCluster subCluster=new MinCluster();
                subCluster.setCf(cf);
                subCluster.getInst_marks().add(mark);
                //把新到的point instance插入树中
                root.absorbSubCluster(subCluster);
                //要始终保证root是树的根节点
                while(root.getParent()!=null){
                    root=root.getParent();
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
     
    //打印B-树的所有叶子节点
    public void printLeaf(LeafNode header){
        //point_num清0
        point_num=0;
        while(header.getNext()!=null){
            System.out.println("\n一个叶子节点:");
            header=header.getNext();
            for(MinCluster cluster:header.getChildren()){
                System.out.println("\n一个最小簇:");
                for(String mark:cluster.getInst_marks()){
                    point_num++;
                    System.out.print(mark+"\t");
                }
            }
        }
    }
     
    //打印指定根节点的子树
    public void printTree(TreeNode root){
        if(!root.getClass().getName().equals("birch.LeafNode")){
            NonleafNode nonleaf=(NonleafNode)root;
            for(TreeNode child:nonleaf.getChildren()){
                printTree(child);
            }
        }
        else{
            System.out.println("\n一个叶子节点:");
            LeafNode leaf=(LeafNode)root;
            for(MinCluster cluster:leaf.getChildren()){
                System.out.println("\n一个最小簇:");
                for(String mark:cluster.getInst_marks()){
                    System.out.print(mark+"\t");
                    point_num++;
                }
            }
        }
    }
     
    public static void main(String[] args) {
        BIRCH birch=new BIRCH();
        TreeNode root=birch.buildBTree("/home/orisun/test/iris.shuffled");
        birch.point_num=0;
        birch.printTree(root);
        System.out.println();
        //birch.printLeaf(birch.leafNodeHead);
        //确认被分类的point instance和扫描数据库时录入的point instance的个数是一致的
        System.out.println(birch.point_num);
    }
}