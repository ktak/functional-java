package ktak.functionaljava;

import ktak.functionaljava.AATree.Leaf;
import ktak.functionaljava.AATree.Node;

class AATreeSplit<T> implements AATree.Visitor<AATree<T>,T> {
    
    @Override
    public AATree<T> visitLeaf(Leaf<T> leaf) {
        return leaf;
    }
    
    @Override
    public AATree<T> visitNode(final Node<T> node) {
        
        return node.right.visit(new AATree.Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return node;
            }
            
            @Override
            public AATree<T> visitNode(final Node<T> childNode) {
                
                return childNode.right.visit(new AATree.Visitor<AATree<T>,T>() {
                    
                    @Override
                    public AATree<T> visitLeaf(Leaf<T> leaf) {
                        return node;
                    }
                    
                    @Override
                    public AATree<T> visitNode(Node<T> grandChildNode) {
                        
                        if (node.level == grandChildNode.level) {
                            
                            return new Node<T>(
                                    childNode.level+1, childNode.value,
                                    new Node<T>(
                                            node.level, node.value,
                                            node.left,
                                            childNode.left,
                                            node.comparator),
                                    grandChildNode,
                                    node.comparator);
                            
                        }
                        else {
                            
                            return node;
                            
                        }
                        
                    }
                    
                });
                
            }
            
        });
        
    }
    
}
