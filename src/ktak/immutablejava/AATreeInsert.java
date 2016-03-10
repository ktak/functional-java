package ktak.immutablejava;

import ktak.immutablejava.AATree.Leaf;
import ktak.immutablejava.AATree.Node;

class AATreeInsert<T> implements AATree.Visitor<AATree<T>,T> {
    
    private final T value;
    
    public AATreeInsert(T value) { this.value = value; }
    
    @Override
    public AATree<T> visitLeaf(Leaf<T> leaf) {
        return new Node<T>(
                1,
                value,
                AATree.emptyTree(leaf.comparator),
                AATree.emptyTree(leaf.comparator));
    }
    
    @Override
    public AATree<T> visitNode(Node<T> node) {
        
        final int comparison = node.comparator.compare(value, node.value);
        if (comparison < 0) {
            return new Node<T>(
                    node.level, node.value, node.left.visit(this), node.right)
                    .skew().split();
        }
        else {
            return new Node<T>(
                    node.level, node.value, node.left, node.right.visit(this))
                    .skew().split();
        }
        
    }
    
}
