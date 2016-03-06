package ktak.functionaljava;

import ktak.functionaljava.AATree.Leaf;
import ktak.functionaljava.AATree.Node;

class AATreeContains<T> implements AATree.Visitor<Boolean,T> {
    
    private final T value;
    
    public AATreeContains(T value) { this.value = value; }
    
    @Override
    public Boolean visitLeaf(Leaf<T> leaf) {
        return false;
    }
    
    @Override
    public Boolean visitNode(Node<T> node) {
        
        final int comparison = node.comparator.compare(value, node.value);
        if (comparison == 0) {
            return true;
        }
        else if (comparison < 0) {
            return node.left.visit(this);
        }
        else {
            return node.right.visit(this);
        }
        
    }
    
}
