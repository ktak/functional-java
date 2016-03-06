package ktak.functionaljava;

import ktak.functionaljava.AATree.Leaf;
import ktak.functionaljava.AATree.Node;

class AATreeSkew<T> implements AATree.Visitor<AATree<T>,T> {
    
    @Override
    public AATree<T> visitLeaf(Leaf<T> leaf) {
        return leaf;
    }
    
    @Override
    public AATree<T> visitNode(final Node<T> node) {
        
        return node.left.visit(new AATree.Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return node;
            }
            
            @Override
            public AATree<T> visitNode(Node<T> childNode) {
                
                if (node.level == childNode.level) {
                    return new Node<T>(
                            childNode.level, childNode.value,
                            childNode.left,
                            new Node<T>(
                                    node.level, node.value,
                                    childNode.right,
                                    node.right));
                }
                else {
                    return node;
                }
                
            }
            
        });
        
    }
    
}
