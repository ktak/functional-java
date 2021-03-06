package ktak.immutablejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import ktak.immutablejava.AATree;
import ktak.immutablejava.Option;
import ktak.immutablejava.AATree.Leaf;
import ktak.immutablejava.AATree.Node;

public class AATreeTest {
    
    private static final Comparator<Integer> cmp = new Comparator<Integer>() {
        
        @Override
        public int compare(Integer arg0, Integer arg1) {
            return arg0.compareTo(arg1);
        }
        
    };
    
    @Test
    public void testInsertionSize() {
        
        AATree<Integer> tree = AATree.emptyTree(cmp);
        Long treeSize = 0L;
        
        List<Integer> list = randomIntegerList(1000, 100);
        
        for (Integer val : list) {
            tree = tree.insert(val);
            treeSize++;
            Assert.assertEquals(treeSize, tree.size());
        }
        
    }
    
    @Test
    public void testSortOrder() {
        
        for (int i=0; i < 10; i++) {
            
            List<Integer> randomList = randomIntegerList(100, 25);
            AATree<Integer> tree = AATree.emptyTree(cmp);
            for (Integer random : randomList) {
                tree = tree.insert(random);
            }
            
            Assert.assertTrue(isSorted(sortedListFromTree(tree)));
            
        }
        
    }
    
    @Test
    public void testContains() {
        
        List<Integer> list = consecutiveIntegerList(1000);
        AATree<Integer> tree = AATree.emptyTree(cmp);
        for (Integer uniqueInt : list) {
            tree = tree.insert(uniqueInt);
        }
        
        for (Integer uniqueInt : list) {
            Assert.assertTrue(tree.contains(uniqueInt));
        }
        
    }
    
    @Test
    public void testGet() {
        
        List<Integer> list = consecutiveIntegerList(1000);
        AATree<Integer> tree = AATree.emptyTree(cmp);
        for (Integer uniqueInt : list) {
            tree = tree.insert(uniqueInt);
        }
        
        for (final Integer uniqueInt : list) {
            Assert.assertTrue(
                    tree.get(uniqueInt).visit(
                            new Option.Visitor<Boolean, Integer>() {
                                
                                @Override
                                public Boolean visitNone() {
                                    return false;
                                }
                                
                                @Override
                                public Boolean visitSome(Integer value) {
                                    return value.equals(uniqueInt);
                                }
                
            }));
        }
        
    }
    
    @Test
    public void testRemove() {
        
        AATree<Integer> tree = AATree.emptyTree(cmp);
        List<Integer> list = consecutiveIntegerList(1000);
        Long treeSize = 0L;
        for (Integer uniqueInt : list) {
            tree = tree.insert(uniqueInt);
            treeSize++;
        }
        
        for (Integer uniqueInt : list) {
            tree = tree.remove(uniqueInt);
            treeSize--;
            Assert.assertEquals(treeSize, tree.size());
            Assert.assertFalse(tree.contains(uniqueInt));
        }
        
    }
    
    @Test
    public void testSortedList() {
        
        AATree<Integer> tree = AATree.emptyTree(cmp);
        List<Integer> randomList = randomIntegerList(1000, 100);
        for (Integer random : randomList) {
            tree = tree.insert(random);
        }
        
        Assert.assertTrue(tree.sortedList().visit(new ListSorted(cmp)));
        
    }
    
    @Test
    public void testMap() {
        
        AATree<Integer> tree = AATree.emptyTree(cmp)
                .insert(0).insert(1).insert(2);
        AATree<Integer> mappedTree = tree.map(
                new Function<Integer,Integer>() {
                    
                    @Override
                    public Integer apply(Integer x) {
                        return x+1;
                    }
                    
                },
                cmp);
        
        Assert.assertEquals(Arrays.asList(1,2,3), sortedListFromTree(mappedTree));
        
    }
    
    @Test
    public void testGetMin() {
        
        AATree<Integer> tree = AATree.emptyTree(cmp)
                .insert(0).insert(1).insert(2);
        
        Assert.assertTrue(AATree.emptyTree(cmp).getMin().match(
                new Function<Option.None<Integer>,Boolean>() {
                    @Override
                    public Boolean apply(Option.None<Integer> none) {
                        return true;
                    }
                },
                new Function<Integer,Boolean>() {
                    @Override
                    public Boolean apply(Integer i) {
                        return false;
                    }
                }));
        Assert.assertTrue(AATree.emptyTree(cmp).getMax().match(
                new Function<Option.None<Integer>,Boolean>() {
                    @Override
                    public Boolean apply(Option.None<Integer> none) {
                        return true;
                    }
                },
                new Function<Integer,Boolean>() {
                    @Override
                    public Boolean apply(Integer i) {
                        return false;
                    }
                }));
        
        Assert.assertTrue(tree.getMin().match(
                new Function<Option.None<Integer>,Boolean>() {
                    @Override
                    public Boolean apply(Option.None<Integer> none) {
                        return false;
                    }
                },
                new Function<Integer,Boolean>() {
                    @Override
                    public Boolean apply(Integer i) {
                        return i.equals(0);
                    }
                }));
        Assert.assertTrue(tree.getMax().match(
                new Function<Option.None<Integer>,Boolean>() {
                    @Override
                    public Boolean apply(Option.None<Integer> none) {
                        return false;
                    }
                },
                new Function<Integer,Boolean>() {
                    @Override
                    public Boolean apply(Integer i) {
                        return i.equals(2);
                    }
                }));
        
    }
    
    @Test
    public void testInvariants() {
        
        AATree<Integer> tree = AATree.emptyTree(cmp);
        AATree.Visitor<Boolean,Integer> invariantsVisitor =
                new InvariantsVisitor<Integer>();
        
        List<Integer> randomList = randomIntegerList(1000, 50);
        for (Integer random : randomList) {
            tree = tree.insert(random);
            Assert.assertTrue(tree.visit(invariantsVisitor));
        }
        
        for (int i=0; i < randomList.size() / 2; i++) {
            tree = tree.remove(randomList.get(i));
            Assert.assertTrue(tree.visit(invariantsVisitor));
        }
        
        for (Integer random : randomList) {
            tree = tree.insert(random);
            Assert.assertTrue(tree.visit(invariantsVisitor));
        }
        
    }
    
    private List<Integer> consecutiveIntegerList(int size) {
        
        List<Integer> list = new ArrayList<Integer>();
        for (int i=0; i < size; i++) {
            list.add(i);
        }
        return list;
        
    }
    
    private List<Integer> randomIntegerList(int size, int modulo) {
        
        List<Integer> randomList = new ArrayList<Integer>();
        Random rand = new Random();
        for (int i=0; i < size; i++) {
            randomList.add(rand.nextInt() % modulo);
        }
        return randomList;
        
    }
    
    private boolean isSorted(List<Integer> list) {
        
        if (list.size() < 2)
            return true;
        
        Integer last = list.get(0);
        for (Integer next : list) {
            if (next < last)
                return false;
            last = next;
        }
        
        return true;
        
    }
    
    private class ListSorted implements ktak.immutablejava.List.Visitor<Boolean, Integer> {
        
        private final Comparator<Integer> cmp;
        
        public ListSorted(Comparator<Integer> cmp) { this.cmp = cmp; }
        
        @Override
        public Boolean visitNil() {
            return true;
        }
        
        @Override
        public Boolean visitCons(
                final Integer head1,
                ktak.immutablejava.List<Integer> tail) {
            
            if (!tail.visit(new ktak.immutablejava.List.Visitor<Boolean, Integer>() {
                
                @Override
                public Boolean visitNil() {
                    return true;
                }
                
                @Override
                public Boolean visitCons(
                        Integer head2,
                        ktak.immutablejava.List<Integer> tail) {
                    
                    if (cmp.compare(head1, head2) > 0)
                        return false;
                    
                    return true;
                    
                }
                
            }))
                return false;
            
            return tail.visit(this);
            
        }
        
    }
    
    private <T> List<T> sortedListFromTree(AATree<T> tree) {
        
        return tree.visit(new AATree.Visitor<List<T>,T>() {
            
            @Override
            public List<T> visitLeaf(Leaf<T> leaf) {
                return new ArrayList<T>();
            }
            
            @Override
            public List<T> visitNode(Node<T> node) {
                List<T> list = new ArrayList<T>();
                list.addAll(node.left.visit(this));
                list.add(node.value);
                list.addAll(node.right.visit(this));
                return list;
            }
            
        });
        
    }
    
    /* AATree invariants (source: wikipedia):
     * 1. level of every leaf = 1
     * 2. level of every left child = level of parent - 1
     * 3. level of every right child = level of parent or level of parent - 1
     * 4. level of every right grandchild less than grandparent
     * 5. if level > 1, has two children
     * NOTE: in this implementation, a leaf is a Node with two Leaf children
     */
    private class InvariantsVisitor<T> implements AATree.Visitor<Boolean,T> {

        @Override
        public Boolean visitLeaf(Leaf<T> leaf) {
            return true;
        }

        @Override
        public Boolean visitNode(Node<T> node) {
            return satisfiesInvariant1(node) &&
                    satisfiesInvariant2(node) &&
                    satisfiesInvariant3(node) &&
                    satisfiesInvariant4(node) &&
                    satisfiesInvariant5(node) &&
                    node.left.visit(this) &&
                    node.right.visit(this);
        }
        
    }
    
    private <T> Boolean hasLeftChild(Node<T> node) {
        
        return node.left.visit(new AATree.Visitor<Boolean,T>() {
            
            @Override
            public Boolean visitLeaf(Leaf<T> leaf) {
                return false;
            }
            
            @Override
            public Boolean visitNode(Node<T> node) {
                return true;
            }
            
        });
        
    }
    
    private <T> Boolean hasRightChild(Node<T> node) {
        
        return node.right.visit(new AATree.Visitor<Boolean,T>() {
            
            @Override
            public Boolean visitLeaf(Leaf<T> leaf) {
                return false;
            }
            
            @Override
            public Boolean visitNode(Node<T> node) {
                return true;
            }
            
        });
        
    }
    
    private <T> Boolean hasRightGrandchild(Node<T> node) {
        
        if (!hasRightChild(node))
            return false;
        
        return hasRightChild((Node<T>)node.right);
        
    }
    
    private <T> Boolean satisfiesInvariant1(Node<T> node) {
        
        if (!node.isLeafNode())
            return true;
        
        if (node.level == 1)
            return true;
        
        return false;
        
    }
    
    private <T> Boolean satisfiesInvariant2(Node<T> node) {
        
        if (!hasLeftChild(node))
            return true;
        
        if (((Node<T>)node.left).level == (node.level - 1))
            return true;
        
        return false;
        
    }
    
    private <T> Boolean satisfiesInvariant3(Node<T> node) {
        
        if (!hasRightChild(node))
            return true;
        
        if ((((Node<T>)node.right).level == node.level) ||
                ((Node<T>)node.right).level == (node.level - 1))
            return true;
        
        return false;
        
    }
    
    private <T> Boolean satisfiesInvariant4(Node<T> node) {
        
        if (!hasRightGrandchild(node))
            return true;
        
        return ((Node<T>)((Node<T>)node.right).right).level < node.level;
        
    }
    
    private <T> Boolean satisfiesInvariant5(Node<T> node) {
        
        if (node.level <= 1)
            return true;
        
        return hasLeftChild(node) && hasRightChild(node);
        
    }
    
}
