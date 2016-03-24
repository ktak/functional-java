package ktak.immutablejava;

import ktak.immutablejava.AATree.Leaf;
import ktak.immutablejava.AATree.Node;

class AATreeSortedList<T> implements AATree.Visitor<List<T>,T> {
    
    private final List<T> tail;
    
    public AATreeSortedList() { this.tail = new List.Nil<T>(); }
    private AATreeSortedList(List<T> tail) { this.tail = tail; }
    
    @Override
    public List<T> visitLeaf(Leaf<T> leaf) {
        return tail;
    }
    
    @Override
    public List<T> visitNode(Node<T> node) {
        return node.left.visit(
                new AATreeSortedList<T>(node.right.visit(this).cons(node.value)));
    }
    
}
