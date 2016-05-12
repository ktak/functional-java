package ktak.immutablejava;

import java.util.Comparator;

public class AATreeMap<K,V> {
    
    private final Comparator<K> cmp;
    private final AATree<Tuple<K,V>> tree;
    
    private static class TreeComparator<K,V> implements Comparator<Tuple<K,V>> {
        
        private final Comparator<K> keyCmp;
        
        public TreeComparator(Comparator<K> keyCmp) {
            this.keyCmp = keyCmp;
        }
        
        @Override
        public int compare(Tuple<K, V> o1, Tuple<K, V> o2) {
            return keyCmp.compare(o1.left, o2.left);
        }
        
    }
    
    public AATreeMap(final Comparator<K> cmp) {
        this.cmp = cmp;
        this.tree = AATree.emptyTree(new TreeComparator<K,V>(cmp));
    }
    
    private AATreeMap(AATree<Tuple<K,V>> tree, Comparator<K> cmp) {
        this.cmp = cmp;
        this.tree = tree;
    }
    
    public static <K,V> AATreeMap<K,V> emptyMap(Comparator<K> comparator) {
        return new AATreeMap<K,V>(comparator);
    }
    
    public Comparator<K> getComparator() {
        return cmp;
    }
    
    public AATreeMap<K,V> insert(K key, V value) {
        
        Tuple<K,V> treeValue = Tuple.create(key, value);
        
        // the comparator only looks at the key
        if (tree.contains(treeValue))
            return new AATreeMap<K,V>(
                    tree.remove(treeValue).insert(treeValue), cmp);
        
        return new AATreeMap<K,V>(tree.insert(treeValue), cmp);
        
    }
    
    public Boolean containsKey(K key) {
        return tree.contains(Tuple.create(key, (V)null));
    }
    
    public Option<V> get(K key) {
        
        return tree.get(Tuple.create(key, (V)null)).visit(
                new Option.Visitor<Option<V>,Tuple<K,V>>() {
                    
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
        return new AATreeMap<K,V>(
                tree.remove(Tuple.create(key, (V)null)), cmp);
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
    
    public <L> AATreeMap<L,V> mapKeys(
            final Function<K,L> f, Comparator<L> cmp) {
        
        return new AATreeMap<L,V>(
                tree.map(new Function<Tuple<K,V>, Tuple<L,V>>() {
                    @Override
                    public Tuple<L,V> apply(Tuple<K,V> x) {
                        return Tuple.create(
                                f.apply(x.left), x.right);
                        }
                    }, new TreeComparator<L,V>(cmp)),
                cmp);
        
    }
    
    public <W> AATreeMap<K,W> mapValues(final Function<V,W> f) {
        
        return new AATreeMap<K,W>(
                tree.map(new Function<Tuple<K,V>, Tuple<K,W>>() {
                    @Override
                    public Tuple<K,W> apply(Tuple<K,V> x) {
                        return Tuple.create(
                                x.left, f.apply(x.right));
                        }
                    }, new TreeComparator<K,W>(cmp)),
                cmp);
        
    }
    
    public <L,W> AATreeMap<L,W> mapKV(
            final Function<Tuple<K,V>,Tuple<L,W>> f,
            Comparator<L> cmp) {
        
        return new AATreeMap<L,W>(
                tree.map(new Function<Tuple<K,V>, Tuple<L,W>>() {
                    @Override
                    public Tuple<L,W> apply(Tuple<K,V> x) {
                        return f.apply(x);
                        }
                    }, new TreeComparator<L,W>(cmp)),
                cmp);
        
    }
    
}
