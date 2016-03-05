package ktak.functionaljava;

import java.util.Comparator;

public abstract class AATree<T> {
    
    private final Comparator<T> comparator;
    
    private AATree(Comparator<T> comparator) {
        this.comparator = comparator;
    }
    
    public static interface Visitor<R,T> {
        public R visitLeaf();
        public R visitNode(Node<T> node);
    }
    
    public abstract <R> R visit(Visitor<R,T> visitor);
    
    protected static class Leaf<T> extends AATree<T> {
        
        public Leaf(Comparator<T> comparator) {
            super(comparator);
        }

        @Override
        public <R> R visit(Visitor<R,T> visitor) {
            return visitor.visitLeaf();
        }
        
    }
    
    public static <T> AATree<T> emptyTree(Comparator<T> comparator) {
        return new Leaf<T>(comparator);
    }
    
    public static class Node<T> extends AATree<T> {
        
        protected final int level;
        public final T value;
        public final AATree<T> left;
        public final AATree<T> right;
        
        private Node(int level, T value, AATree<T> left, AATree<T> right,
                Comparator<T> comparator) {
            
            super(comparator);
            
            this.level = level;
            this.value = value;
            this.left = left;
            this.right = right;
            
        }

        @Override
        public <R> R visit(Visitor<R,T> visitor) {
            return visitor.visitNode(this);
        }
        
    }
    
    public static <T> Integer size(AATree<T> tree) {
        
        return tree.visit(new AATree.Visitor<Integer,T>() {

            @Override
            public Integer visitLeaf() {
                return 0;
            }

            @Override
            public Integer visitNode(Node<T> node) {
                return 1 + node.left.visit(this) + node.right.visit(this);
            }
            
        });
        
    }
    
    public Boolean contains(final T value) {
        
        final AATree<T> tree = this;
        
        return tree.visit(new AATree.Visitor<Boolean,T>() {

            @Override
            public Boolean visitLeaf() {
                return false;
            }

            @Override
            public Boolean visitNode(Node<T> node) {
                
                final int comparison = tree.comparator.compare(value, node.value);
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
            
        });
        
    }
    
    public Option<T> get(final T value) {
        
        final AATree<T> tree = this;
        
        return tree.visit(new AATree.Visitor<Option<T>,T>() {
            
            @Override
            public Option<T> visitLeaf() {
                return new Option.None<T>();
            }
            
            @Override
            public Option<T> visitNode(Node<T> node) {
                
                final int comparison = tree.comparator.compare(value, node.value);
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
            
        });
        
    }
    
    public AATree<T> insert(final T value) {
        
        final AATree<T> tree = this;
        
        return tree.visit(new AATree.Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf() {
                return new Node<T>(
                        1,
                        value,
                        new Leaf<T>(tree.comparator),
                        new Leaf<T>(tree.comparator),
                        tree.comparator);
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                
                final int comparison = tree.comparator.compare(value, node.value);
                if (comparison == 0) {
                    return new Node<T>(node.level, value, node.left, node.right,
                            tree.comparator);
                }
                else if (comparison < 0) {
                    return split(skew(new Node<T>(
                            node.level, node.value, node.left.visit(this), node.right,
                            tree.comparator)));
                }
                else {
                    return split(skew(new Node<T>(
                            node.level, node.value, node.left, node.right.visit(this),
                            tree.comparator)));
                }
                
            }
            
        });
        
    }
    
    private static <T> AATree<T> skew(final AATree<T> tree) {
        
        return tree.visit(new AATree.Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf() {
                return tree;
            }
            
            @Override
            public AATree<T> visitNode(final Node<T> node) {
                
                return node.left.visit(new AATree.Visitor<AATree<T>,T>() {
                    
                    @Override
                    public AATree<T> visitLeaf() {
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
                                            node.right,
                                            tree.comparator),
                                    tree.comparator);
                        }
                        else {
                            return node;
                        }
                        
                    }
                    
                });
                
            }
            
        });
        
    }
    
    private static <T> AATree<T> split(final AATree<T> tree) {
        
        return tree.visit(new AATree.Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf() {
                return tree;
            }
            
            @Override
            public AATree<T> visitNode(final Node<T> node) {
                
                return node.right.visit(new AATree.Visitor<AATree<T>,T>() {
                    
                    @Override
                    public AATree<T> visitLeaf() {
                        return tree;
                    }
                    
                    @Override
                    public AATree<T> visitNode(final Node<T> childNode) {
                        
                        return childNode.right.visit(new AATree.Visitor<AATree<T>,T>() {
                            
                            @Override
                            public AATree<T> visitLeaf() {
                                return tree;
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
                                                    tree.comparator),
                                            grandChildNode,
                                            tree.comparator);
                                    
                                }
                                else {
                                    
                                    return tree;
                                    
                                }
                                
                            }
                            
                        });
                        
                    }
                    
                });
                
            }
            
        });
        
    }
    
}
