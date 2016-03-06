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
    
    public static class Leaf<T> extends AATree<T> {
        
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
    
    public Long size() {
        return this.visit(new AATreeSize<T>());
    }
    
    public Boolean contains(final T value) {
        
        final AATree<T> tree = this;
        
        return tree.visit(new AATree.Visitor<Boolean,T>() {

            @Override
            public Boolean visitLeaf(Leaf<T> leaf) {
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
            public Option<T> visitLeaf(Leaf<T> leaf) {
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
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return new Node<T>(
                        1,
                        value,
                        emptyTree(tree.comparator),
                        emptyTree(tree.comparator),
                        tree.comparator);
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                
                final int comparison = tree.comparator.compare(value, node.value);
                if (comparison < 0) {
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
                    
                    if (isLeafNode(node))
                        return emptyTree(tree.comparator);
                    
                    return node.left.visit(new Visitor<AATree<T>,T>() {
                        
                        @Override
                        public AATree<T> visitLeaf(Leaf<T> leaf) {
                            return rebalanceAfterRemoval(
                                    removeAndReplaceWithSuccessor(node));
                        }
                        
                        @Override
                        public AATree<T> visitNode(Node<T> lnode) {
                            return rebalanceAfterRemoval(
                                    removeAndReplaceWithPredecessor(node));
                        }
                        
                    });
                    
                }
                else if (comparison < 0) {
                    return rebalanceAfterRemoval(new Node<T>(
                            node.level, node.value, node.left.visit(this), node.right,
                            node.comparator));
                }
                else {
                    return rebalanceAfterRemoval(new Node<T>(
                            node.level, node.value, node.left, node.right.visit(this),
                            node.comparator));
                }
                
            }
            
        });
        
    }
    
    private static <T> AATree<T> rebalanceAfterRemoval(AATree<T> tree) {
        
        return tree.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return leaf;
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                return splitRightChild(split(
                        skewRightGrandChild(skewRightChild(skew(
                                decreaseLevel(node))))));
            }
            
        });
        
    }
    
    private static <T> AATree<T> skewRightGrandChild(AATree<T> tree) {
        
        return tree.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return leaf;
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                return new Node<T>(node.level, node.value, node.left, skewRightChild(node.right),
                        node.comparator);
            }
            
        });
        
    }
    
    private static <T> AATree<T> skewRightChild(AATree<T> tree) {
        
        return tree.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return leaf;
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                return new Node<T>(node.level, node.value, node.left, skew(node.right),
                        node.comparator);
            }
            
        });
        
    }
    
    private static <T> AATree<T> splitRightChild(AATree<T> tree) {
        
        return tree.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return leaf;
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                return new Node<T>(node.level, node.value, node.left, split(node.right),
                        node.comparator);
            }
            
        });
        
    }
    
    /* a node is a leaf node if both its children are Leaf instances */
    protected static <T> Boolean isLeafNode(Node<T> node) {
        
        return node.visit(new Visitor<Boolean,T>() {
            
            @Override
            public Boolean visitLeaf(Leaf<T> leaf) {
                return false;
            }
            
            @Override
            public Boolean visitNode(Node<T> node) {
                
                if ((node.left instanceof Leaf) &&
                        (node.right instanceof Leaf)) {
                    return true;
                }
                
                return false;
                
            }
            
        });
        
    }
    
    private static <T> Node<T> removeAndReplaceWithPredecessor(Node<T> node) {
        
        Node<T> predecessor = getRightMost(node.left);
        return new Node<T>(node.level, predecessor.value,
                        removeRightMost(node.left), node.right, node.comparator);
        
    }
    
    private static <T> Node<T> removeAndReplaceWithSuccessor(Node<T> node) {
        
        Node<T> successor = getLeftMost(node.right);
        return new Node<T>(node.level, successor.value,
                        node.left, removeLeftMost(node.right), node.comparator);
        
    }
    
    private static <T> Node<T> getRightMost(final AATree<T> tree) {
        
        return tree.visit(new Visitor<Node<T>,T>() {
            
            @Override
            public Node<T> visitLeaf(Leaf<T> leaf) {
                throw new RuntimeException();
            }
            
            @Override
            public Node<T> visitNode(Node<T> node) {
                
                if (isLeafNode(node))
                    return node;
                
                return node.right.visit(this);
                
            }
            
        });
        
    }
    
    private static <T> Node<T> getLeftMost(final AATree<T> tree) {
        
        return tree.visit(new Visitor<Node<T>,T>() {
            
            @Override
            public Node<T> visitLeaf(Leaf<T> leaf) {
                throw new RuntimeException();
            }
            
            @Override
            public Node<T> visitNode(Node<T> node) {
                
                if (isLeafNode(node))
                    return node;
                
                return node.left.visit(this);
                
            }
            
        });
        
    }
    
    private static <T> AATree<T> removeRightMost(AATree<T> tree) {
        
        return tree.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                throw new RuntimeException();
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                
                if (isLeafNode(node))
                    return emptyTree(node.comparator);
                
                return rebalanceAfterRemoval(new Node<T>(
                        node.level, node.value, node.left, node.right.visit(this),
                        node.comparator));
                
            }
            
        });
        
    }
    
    private static <T> AATree<T> removeLeftMost(AATree<T> tree) {
        
        return tree.visit(new Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                throw new RuntimeException();
            }
            
            @Override
            public AATree<T> visitNode(Node<T> node) {
                
                if (isLeafNode(node))
                    return emptyTree(node.comparator);
                
                return rebalanceAfterRemoval(new Node<T>(
                        node.level, node.value, node.left.visit(this), node.right,
                        node.comparator));
                
            }
            
        });
        
    }
    
    private static <T> AATree<T> decreaseLevel(Node<T> node) {
        
        int correctLevel = minLevelOfChildren(node) + 1;
        if (correctLevel < node.level) {
            AATree<T> right = correctLevel < level(node.right) ?
                    changeLevel(node.right, correctLevel) : node.right;
            return new Node<T>(correctLevel, node.value, node.left, right, node.comparator);
        }
        
        return node;
        
    }
    
    private static <T> int minLevelOfChildren(final Node<T> node) {
        
        return level(node.left) < level(node.right) ? level(node.left) : level(node.right);
        
    }
    
    private static <T> AATree<T> changeLevel(final AATree<T> tree, final int newLevel) {
        
        return tree.visit(new Visitor<AATree<T>,T>() {
            
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
    
    private static <T> int level(AATree<T> tree) {
        
        return tree.visit(new Visitor<Integer,T>() {
            
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
    
    private static <T> AATree<T> skew(final AATree<T> tree) {
        
        return tree.visit(new AATree.Visitor<AATree<T>,T>() {
            
            @Override
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return tree;
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
            public AATree<T> visitLeaf(Leaf<T> leaf) {
                return tree;
            }
            
            @Override
            public AATree<T> visitNode(final Node<T> node) {
                
                return node.right.visit(new AATree.Visitor<AATree<T>,T>() {
                    
                    @Override
                    public AATree<T> visitLeaf(Leaf<T> leaf) {
                        return tree;
                    }
                    
                    @Override
                    public AATree<T> visitNode(final Node<T> childNode) {
                        
                        return childNode.right.visit(new AATree.Visitor<AATree<T>,T>() {
                            
                            @Override
                            public AATree<T> visitLeaf(Leaf<T> leaf) {
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
