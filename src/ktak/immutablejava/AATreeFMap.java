package ktak.immutablejava;

import java.util.Comparator;

import ktak.immutablejava.AATree.Leaf;
import ktak.immutablejava.AATree.Node;

class AATreeFMap<T,U> implements AATree.Visitor<AATree<U>,T>{
    
    private final AATree<U> result;
    private final Function<T,U> f;
    
    public AATreeFMap(Function<T,U> f, Comparator<U> cmp) {
        this.result = AATree.emptyTree(cmp);
        this.f = f;
    }
    
    private AATreeFMap(Function<T,U> f, AATree<U> result) {
        this.result = result;
        this.f = f;
    }
    
    @Override
    public AATree<U> visitLeaf(Leaf<T> leaf) {
        return result;
    }
    
    @Override
    public AATree<U> visitNode(Node<T> node) {
        return node.right.visit(
                new AATreeFMap<T,U>(
                        f,
                        node.left.visit(this).insert(f.apply(node.value))));
    }
    
}
