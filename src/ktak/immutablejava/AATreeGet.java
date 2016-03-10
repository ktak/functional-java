package ktak.immutablejava;

import ktak.immutablejava.AATree.Leaf;
import ktak.immutablejava.AATree.Node;

class AATreeGet<T> implements AATree.Visitor<Option<T>,T> {
    
    private final T value;
    
    public AATreeGet(T value) { this.value = value; }
    
    @Override
    public Option<T> visitLeaf(Leaf<T> leaf) {
        return Option.none();
    }
    
    @Override
    public Option<T> visitNode(Node<T> node) {
        
        final int comparison = node.comparator.compare(value, node.value);
        if (comparison == 0) {
            return new Option.Some<T>(node.value);
        }
        else if (comparison < 0) {
            return node.left.visit(this);
        }
        else {
            return node.right.visit(this);
        }
        
    }
    
}
