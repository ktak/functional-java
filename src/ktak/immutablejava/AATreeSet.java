package ktak.immutablejava;

import java.util.Comparator;

import ktak.immutablejava.AATree.Leaf;
import ktak.immutablejava.AATree.Node;

public class AATreeSet<T> {
    
    private final AATree<T> tree;
    
    public AATreeSet(Comparator<T> comparator) {
        this.tree = AATree.emptyTree(comparator);
    }
    
    private AATreeSet(AATree<T> tree) {
        this.tree = tree;
    }
    
    public static <T> AATreeSet<T> emptySet(Comparator<T> comparator) {
        return new AATreeSet<T>(comparator);
    }
    
    public Comparator<T> getComparator() {
        return tree.comparator;
    }
    
    public AATreeSet<T> insert(T value) {
        
        if (tree.contains(value))
            return this;
        
        return new AATreeSet<T>(tree.insert(value));
        
    }
    
    public boolean contains(T value) {
        return tree.contains(value);
    }
    
    public AATreeSet<T> remove(T value) {
        return new AATreeSet<T>(tree.remove(value));
    }
    
    public Long size() {
        return tree.size();
    }
    
    public List<T> sortedList() {
        return tree.sortedList();
    }
    
    public <U> AATreeSet<U> map(Function<T,U> f, Comparator<U> cmp) {
        return new AATreeSet<U>(tree.map(f, cmp));
    }
    
    public AATreeSet<T> intersection(AATreeSet<T> other) {
        return this.tree.visit(new IntersectionVisitor<T>(other));
    }
    
    private static class IntersectionVisitor<T> implements AATree.Visitor<AATreeSet<T>,T> {
        
        private final AATreeSet<T> other;
        private final AATreeSet<T> intersection;
        
        public IntersectionVisitor(AATreeSet<T> other) {
            this.other = other;
            this.intersection = AATreeSet.emptySet(other.tree.comparator);
        }
        
        private IntersectionVisitor(AATreeSet<T> other, AATreeSet<T> intersection) {
            this.other = other;
            this.intersection = intersection;
        }
        
        @Override
        public AATreeSet<T> visitLeaf(Leaf<T> leaf) {
            return intersection;
        }
        
        @Override
        public AATreeSet<T> visitNode(Node<T> node) {
            
            IntersectionVisitor<T> leftVisitor = other.contains(node.value) ?
                    new IntersectionVisitor<T>(other, intersection.insert(node.value)) :
                    this;
            
            IntersectionVisitor<T> rightVisitor = new IntersectionVisitor<T>(other, node.left.visit(leftVisitor));
            
            return node.right.visit(rightVisitor);
            
        }
        
    }
    
    public AATreeSet<T> union(AATreeSet<T> other) {
        return this.tree.visit(new UnionVisitor<T>(other));
    }
    
    private static class UnionVisitor<T> implements AATree.Visitor<AATreeSet<T>,T> {
        
        private final AATreeSet<T> other;
        private final AATreeSet<T> union;
        
        public UnionVisitor(AATreeSet<T> other) {
            this.other = other;
            this.union = other;
        }
        
        private UnionVisitor(AATreeSet<T> other, AATreeSet<T> union) {
            this.other = other;
            this.union = union;
        }
        
        @Override
        public AATreeSet<T> visitLeaf(Leaf<T> leaf) {
            return union;
        }
        
        @Override
        public AATreeSet<T> visitNode(Node<T> node) {
            
            UnionVisitor<T> leftVisitor = new UnionVisitor<T>(other, union.insert(node.value));
            
            UnionVisitor<T> rightVisitor = new UnionVisitor<T>(other, node.left.visit(leftVisitor));
            
            return node.right.visit(rightVisitor);
            
        }
        
    }
    
    public AATreeSet<T> difference(AATreeSet<T> other) {
        return this.tree.visit(new DifferenceVisitor<T>(other));
    }
    
    private static class DifferenceVisitor<T> implements AATree.Visitor<AATreeSet<T>,T> {
        
        private final AATreeSet<T> other;
        private final AATreeSet<T> difference;
        
        public DifferenceVisitor(AATreeSet<T> other) {
            this.other = other;
            this.difference = AATreeSet.emptySet(other.tree.comparator);
        }
        
        private DifferenceVisitor(AATreeSet<T> other, AATreeSet<T> difference) {
            this.other = other;
            this.difference = difference;
        }
        
        @Override
        public AATreeSet<T> visitLeaf(Leaf<T> leaf) {
            return difference;
        }
        
        @Override
        public AATreeSet<T> visitNode(Node<T> node) {
            
            DifferenceVisitor<T> leftVisitor = other.contains(node.value) ?
                    this :
                    new DifferenceVisitor<T>(other, difference.insert(node.value));
            
            DifferenceVisitor<T> rightVisitor =
                    new DifferenceVisitor<T>(other, node.left.visit(leftVisitor));
            
            return node.right.visit(rightVisitor);
            
        }
        
    }
    
}
