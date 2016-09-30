package ps.hell.util.struct;

import java.util.HashMap;

/**
 * C1reated by Administrator on 2016/9/30.
 * key 和 value 互相索引
 */
public class TwoMapPair<K,V> {

    public HashMap<K,V> map = null;
    public HashMap<V,K> mapRev = null;

//    public class KeyMapV{
//        public K key=null;
//        public V value = null;
//        public KeyMapV(K key,V value){
//            this.key = key;
//            this.value = value;
//        }
//
//        public int getHashCode(){
//            int hash = key.hashCode();
//            hash = hash >>2;
//            hash += value.hashCode();
//            return hash%Integer.MAX_VALUE;
//        }
//
//    }

    public void add(K key,V value){
        if(this.map == null){
            this.map = new HashMap<K,V>();
            this.mapRev = new HashMap<V,K>();
        }
//        KeyMapV k = new KeyMapV(key,value);
        this.map.put(key,value);
        this.mapRev.put(value,key);
    }

    public V getByKey(K key){
        return this.map.get(key);
    }

    public K getByValue(V value){
        return this.mapRev.get(value);
    }

}
