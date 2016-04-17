package ktak.immutablejava;

import java.util.Comparator;

public class AATreeMap<K,V> {
    
    private final AATree<Tuple<K,V>> tree;
    
    public AATreeMap(final Comparator<K> comparator) {
        
        this.tree = AATree.emptyTree(new Comparator<Tuple<K,V>>() {
            
            @Override
            public int compare(Tuple<K,V> o1, Tuple<K,V> o2) {
                return comparator.compare(o1.left, o2.left);
            }
            
        });
        
    }
    
    private AATreeMap(AATree<Tuple<K,V>> tree) { this.tree = tree; }
    
    public static <K,V> AATreeMap<K,V> emptyMap(Comparator<K> comparator) {
        return new AATreeMap<K,V>(comparator);
    }
    
    public AATreeMap<K,V> insert(K key, V value) {
        
        Tuple<K,V> treeValue = Tuple.create(key, value);
        
        // the comparator only looks at the key
        if (tree.contains(treeValue))
            return new AATreeMap<K,V>(tree.remove(treeValue).insert(treeValue));
        
        return new AATreeMap<K,V>(tree.insert(treeValue));
        
    }
    
    public Boolean containsKey(K key) {
        return tree.contains(Tuple.create(key, (V)null));
    }
    
    public Option<V> get(K key) {
        
        return tree.get(Tuple.create(key, (V)null)).visit(new Option.Visitor<Option<V>,Tuple<K,V>>() {
            
            @Override
            public Option<V> visitNone() {
                return Option.none();
            }
            
            @Override
            public Option<V> visitSome(Tuple<K, V> value) {
                return Option.some(value.right);
            }
            
        });
        
    }
    
    public AATreeMap<K,V> remove(K key) {
        return new AATreeMap<K,V>(tree.remove(Tuple.create(key, (V)null)));
    }
    
    public Long size() {
        return tree.size();
    }
    
    public List<K> sortedKeys() {
        
        return tree.sortedList().map(new Function<Tuple<K,V>,K>() {
            
            @Override
            public K apply(Tuple<K, V> x) {
                return x.left;
            }
            
        });
        
    }
    
}
