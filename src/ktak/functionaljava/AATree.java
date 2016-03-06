package ktak.functionaljava;

import java.util.Comparator;

public abstract class AATree<T> {
    
    protected final Comparator<T> comparator;
    
    private AATree(Comparator<T> comparator) {
        this.comparator = comparator;
    }
    
    public static interface Visitor<R,T> {
        public R visitLeaf(Leaf<T> leaf);
        public R visitNode(Node<T> node);
    }
    
    public abstract <R> R visit(Visitor<R,T> visitor);
    
    public static final class Leaf<T> extends AATree<T> {
        
        public Leaf(Comparator<T> comparator) {
            super(comparator);
        }

        @Override
        public <R> R visit(Visitor<R,T> visitor) {
            return visitor.visitLeaf(this);
        }
        
    }
    
    public static <T> AATree<T> emptyTree(Comparator<T> comparator) {
        return new Leaf<T>(comparator);
    }
    
    public static final class Node<T> extends AATree<T> {
        
        protected final int level;
        public final T value;
        public final AATree<T> left;
        public final AATree<T> right;
        
        Node(int level, T value, AATree<T> left, AATree<T> right,
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
        
        private int minLevelOfChildren() {
            return left.level() < right.level() ? left.level() : right.level();
        }
        
        /* a node is a leaf node if both its children are Leaf instances */
        protected Boolean isLeafNode() {
            
            if ((left instanceof Leaf) &&
                    (right instanceof Leaf)) {
                return true;
            }
            
            return false;
            
        }
        
        private Node<T> decreaseLevel() {
            
            int correctLevel = minLevelOfChildren() + 1;
            if (correctLevel < level) {
                AATree<T> rightChild = correctLevel < right.level() ?
                        right.changeLevel(correctLevel) : right;
                return new Node<T>(correctLevel, value, left, rightChild, comparator);
            }
            
            return this;
            
        }
        
        protected Node<T> removeAndReplaceWithSuccessor() {
            
            Node<T> successor = right.getLeftMost();
            return new Node<T>(level, successor.value,
                    left, right.removeLeftMost(), comparator);
            
        }
        
        protected Node<T> removeAndReplaceWithPredecessor() {
            
            Node<T> predecessor = this.left.getRightMost();
            return new Node<T>(this.level, predecessor.value,
                    this.left.removeRightMost(), this.right, this.comparator);
            
        }
        
    }
    
    public Long size() {
        return this.visit(new AATreeSize<T>());
    }
    
    public Boolean contains(T value) {
        return this.visit(new AATreeContains<T>(value));
    }
    
    public Option<T> get(final T value) {
        return this.visit(new AATreeGet<T>(value));
    }
    
    public AATree<T> insert(T value) {
        return this.visit(new AATreeInsert<T>(value));
    }
    
    public AATree<T> remove(final T value) {
        
        final AATree<T> tree = this;
        
        return tree.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return leaf;
            }
            
            @Override
            public AATree<T> visitNode(final Node<T> node) {
                
                final int comparison = tree.comparator.compare(value, node.value);
                if (comparison == 0) {
                    
                    if (node.isLeafNode())
                        return emptyTree(tree.comparator);
                    
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
                            node.level, node.value, node.left.visit(this), node.right,
                            node.comparator).rebalanceAfterRemoval();
                }
                else {
                    return new Node<T>(
                            node.level, node.value, node.left, node.right.visit(this),
                            node.comparator).rebalanceAfterRemoval();
                }
                
            }
            
        });
        
    }
    
    protected AATree<T> skew() {
        return this.visit(new AATreeSkew<T>());
    }
    
    protected AATree<T> split() {
        return this.visit(new AATreeSplit<T>());
    }
    
    protected AATree<T> rebalanceAfterRemoval() {
        
        return this.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return leaf;
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                return node.decreaseLevel()
                        .skew().skewRightChild().skewRightGrandChild()
                        .split().splitRightChild();
            }
            
        });
        
    }
    
    private AATree<T> skewRightGrandChild() {
        
        return this.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return leaf;
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                return new Node<T>(node.level, node.value, node.left, node.right.skewRightChild(),
                        node.comparator);
            }
            
        });
        
    }
    
    private AATree<T> skewRightChild() {
        
        return this.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return leaf;
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                return new Node<T>(node.level, node.value, node.left, node.right.skew(),
                        node.comparator);
            }
            
        });
        
    }
    
    private AATree<T> splitRightChild() {
        
        return this.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return leaf;
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                return new Node<T>(node.level, node.value, node.left, node.right.split(),
                        node.comparator);
            }
            
        });
        
    }
    
    private Node<T> getRightMost() {
        
        return this.visit(new Visitor<Node<T>,T>() {
            
            @Override
            public Node<T> visitLeaf(Leaf<T> leaf) {
                throw new RuntimeException();
            }
            
            @Override
            public Node<T> visitNode(Node<T> node) {
                
                if (node.isLeafNode())
                    return node;
                
                return node.right.visit(this);
                
            }
            
        });
        
    }
    
    private Node<T> getLeftMost() {
        
        return this.visit(new Visitor<Node<T>,T>() {
            
            @Override
            public Node<T> visitLeaf(Leaf<T> leaf) {
                throw new RuntimeException();
            }
            
            @Override
            public Node<T> visitNode(Node<T> node) {
                
                if (node.isLeafNode())
                    return node;
                
                return node.left.visit(this);
                
            }
            
        });
        
    }
    
    private AATree<T> removeRightMost() {
        
        return this.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                throw new RuntimeException();
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                
                if (node.isLeafNode())
                    return emptyTree(node.comparator);
                
                return new Node<T>(
                        node.level, node.value, node.left, node.right.visit(this),
                        node.comparator).rebalanceAfterRemoval();
                
            }
            
        });
        
    }
    
    private AATree<T> removeLeftMost() {
        
        return this.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                throw new RuntimeException();
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                
                if (node.isLeafNode())
                    return emptyTree(node.comparator);
                
                return new Node<T>(
                        node.level, node.value, node.left.visit(this), node.right,
                        node.comparator).rebalanceAfterRemoval();
                
            }
            
        });
        
    }
    
    private AATree<T> changeLevel(final int newLevel) {
        
        return this.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return leaf;
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                return new Node<T>(
                        newLevel, node.value, node.left, node.right, node.comparator);
            }
            
        });
        
    }
    
    private int level() {
        
        return this.visit(new Visitor<Integer,T>() {
            
            @Override
            public Integer visitLeaf(Leaf<T> leaf) {
                return 0;
            }
            
            @Override
            public Integer visitNode(Node<T> node) {
                return node.level;
            }
            
        });
        
    }
    
}
