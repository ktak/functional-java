package ktak.immutablejava;

import ktak.immutablejava.AATree.Leaf;
import ktak.immutablejava.AATree.Node;

class AATreeGetMin<T> implements AATree.Visitor<Option<T>,T> {
    
    @Override
    public Option<T> visitLeaf(Leaf<T> leaf) {
        return Option.none();
    }
    
    @Override
    public Option<T> visitNode(Node<T> node) {
        return node.isLeafNode() ?
                Option.some(node.value) :
                node.left.visit(this);
    }
    
}
