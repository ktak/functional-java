package ktak.immutablejava;

import java.util.Comparator;

import org.junit.Assert;
import org.junit.Test;

public class AATreeSetTest {
    
    private static final Comparator<Integer> cmp = new Comparator<Integer>() {
        
        @Override
        public int compare(Integer arg0, Integer arg1) {
            return arg0.compareTo(arg1);
        }
        
    };
    
    private static final Eq<Integer> eq = new Eq<Integer>() {
        
        @Override
        public boolean equals(Integer t1, Integer t2) {
            return t1.equals(t2);
        }
        
    };
    
    @Test
    public void testSize() {
        
        AATreeSet<Integer> set = AATreeSet.emptySet(cmp).insert(0).insert(1).insert(2);
        
        Assert.assertEquals((Long)0L, AATreeSet.emptySet(cmp).size());
        Assert.assertEquals((Long)3L, set.size());
        Assert.assertEquals((Long)3L, set.insert(2).size());
        
    }
    
    @Test
    public void testContains() {
        
        AATreeSet<Integer> set = AATreeSet.emptySet(cmp).insert(0).insert(1).insert(9);
        
        Assert.assertTrue(set.contains(0));
        Assert.assertTrue(set.contains(1));
        Assert.assertTrue(set.contains(9));
        
        Assert.assertFalse(set.contains(3));
        
    }
    
    @Test
    public void testRemove() {
        
        AATreeSet<Integer> set = AATreeSet.emptySet(cmp).insert(0).insert(1)
                .insert(Integer.MAX_VALUE).insert(Integer.MIN_VALUE);
        
        Assert.assertTrue(set.contains(Integer.MAX_VALUE));
        Assert.assertTrue(set.contains(Integer.MIN_VALUE));
        
        Assert.assertFalse(set.remove(Integer.MAX_VALUE).contains(Integer.MAX_VALUE));
        
    }
    
    @Test
    public void testIntersection() {
        
        AATreeSet<Integer> set1 = AATreeSet.emptySet(cmp).insert(0).insert(1)
                .insert(Integer.MAX_VALUE).insert(Integer.MIN_VALUE);
        
        AATreeSet<Integer> set2 = AATreeSet.emptySet(cmp)
                .insert(Integer.MAX_VALUE).insert(Integer.MIN_VALUE);
        
        AATreeSet<Integer> set3 = AATreeSet.emptySet(cmp).insert(0).insert(2)
                .insert(Integer.MAX_VALUE);
        
        Assert.assertTrue(AATreeSet.emptySet(cmp).sortedList()
                .equalTo(new List.Nil<Integer>(), eq));
        
        Assert.assertTrue(set1.intersection(set1).sortedList()
                .equalTo(set1.sortedList(), eq));
        
        Assert.assertTrue(set1.intersection(AATreeSet.emptySet(cmp)).sortedList()
                .equalTo(new List.Nil<Integer>(), eq));
        
        Assert.assertTrue(set2.intersection(set1).sortedList()
                .equalTo(set2.sortedList(), eq));
        
        Assert.assertTrue(set1.intersection(set2).sortedList()
                .equalTo(set2.sortedList(), eq));
        
        Assert.assertTrue(set3.intersection(set1).sortedList()
                .equalTo(new List.Nil<Integer>().cons(Integer.MAX_VALUE).cons(0), eq));
        
    }
    
    @Test
    public void testUnion() {
        
        AATreeSet<Integer> set1 = AATreeSet.emptySet(cmp).insert(0).insert(2)
                .insert(4).insert(6);
        
        AATreeSet<Integer> set2 = AATreeSet.emptySet(cmp).insert(1).insert(3)
                .insert(5).insert(7);
        
        Assert.assertTrue(AATreeSet.emptySet(cmp).union(AATreeSet.emptySet(cmp)).sortedList()
                .equalTo(new List.Nil<Integer>(), eq));
        
        Assert.assertTrue(AATreeSet.emptySet(cmp).union(set1).sortedList()
                .equalTo(set1.sortedList(), eq));
        
        Assert.assertTrue(set1.union(AATreeSet.emptySet(cmp)).sortedList()
                .equalTo(set1.sortedList(), eq));
        
        Assert.assertTrue(set1.union(set2).sortedList()
                .equalTo(new List.Nil<Integer>().cons(7).cons(6).cons(5).cons(4)
                        .cons(3).cons(2).cons(1).cons(0), eq));
        
    }
    
    @Test
    public void testDifference() {
        
        AATreeSet<Integer> set1 = AATreeSet.emptySet(cmp).insert(0).insert(1)
                .insert(2).insert(3).insert(4).insert(5).insert(6).insert(7);
        
        AATreeSet<Integer> set2 = AATreeSet.emptySet(cmp).insert(1).insert(3)
                .insert(5).insert(7).insert(9).insert(11);
        
        Assert.assertTrue(AATreeSet.emptySet(cmp).difference(AATreeSet.emptySet(cmp)).sortedList()
                .equalTo(new List.Nil<Integer>(), eq));
        
        Assert.assertTrue(set1.difference(AATreeSet.emptySet(cmp)).sortedList()
                .equalTo(set1.sortedList(), eq));
        
        Assert.assertTrue(set1.difference(set2).sortedList()
                .equalTo(new List.Nil<Integer>().cons(6).cons(4).cons(2).cons(0), eq));
        
        Assert.assertTrue(set2.difference(set1).sortedList()
                .equalTo(new List.Nil<Integer>().cons(11).cons(9), eq));
        
    }
    
}
