package ps.hell.ml.cluster.level.birch;
public abstract class TreeNode extends CF {
 
    private TreeNode parent;
 
    public TreeNode() {
         
    }
     
    public TreeNode(double[] data) {
        super(data);
    }
 
    public TreeNode getParent() {
        return parent;
    }
 
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
     
    public void addCFUpToRoot(CF cf){
        TreeNode node=this;
        while(node!=null){
            node.addCF(cf, true);
            node=node.getParent();
        }
    }
     
    abstract void split();
     
    abstract void absorbSubCluster(MinCluster cluster);
}