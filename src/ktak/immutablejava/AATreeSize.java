package ktak.immutablejava;

import ktak.immutablejava.AATree.Leaf;
import ktak.immutablejava.AATree.Node;

class AATreeSize<T> implements AATree.Visitor<Long,T> {
    
    @Override
    public Long visitLeaf(Leaf<T> leaf) {
        return 0L;
    }
    
    @Override
    public Long visitNode(Node<T> node) {
        return 1 + node.left.visit(this) + node.right.visit(this);
    }
    
}
