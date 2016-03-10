package ktak.immutablejava;

import ktak.immutablejava.AATree.Leaf;
import ktak.immutablejava.AATree.Node;
import ktak.immutablejava.AATree.Visitor;

class AATreeRemove<T> implements AATree.Visitor<AATree<T>,T> {
    
    private final T value;
    
    public AATreeRemove(T value) { this.value = value; }
    
    @Override
    public AATree<T> visitLeaf(Leaf<T> leaf) {
        return leaf;
    }
    
    @Override
    public AATree<T> visitNode(final Node<T> node) {
        
        final int comparison = node.comparator.compare(value, node.value);
        if (comparison == 0) {
            
            if (node.isLeafNode())
                return AATree.emptyTree(node.comparator);
            
            return node.left.visit(new Visitor<AATree<T>,T>() {
                
                @Override
                public AATree<T> visitLeaf(Leaf<T> leaf) {
                    return node.removeAndReplaceWithSuccessor()
                            .rebalanceAfterRemoval();
                }
                
                @Override
                public AATree<T> visitNode(Node<T> lnode) {
                    return node.removeAndReplaceWithPredecessor()
                            .rebalanceAfterRemoval();
                }
                
            });
            
        }
        else if (comparison < 0) {
            return new Node<T>(
                    node.level, node.value, node.left.visit(this), node.right)
                    .rebalanceAfterRemoval();
        }
        else {
            return new Node<T>(
                    node.level, node.value, node.left, node.right.visit(this))
                    .rebalanceAfterRemoval();
        }
        
    }
    
}
