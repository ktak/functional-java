package ktak.immutablejava;

import org.junit.Test;

import ktak.immutablejava.Eq;
import ktak.immutablejava.List;

import java.util.Comparator;

import org.junit.Assert;

public class ListTest {
    
    private static final Eq<Integer> intEq = new Eq<Integer>() {

        @Override
        public boolean equals(Integer t1, Integer t2) {
            return t1.equals(t2);
        }
        
    };
    
    private static final Eq<String> strEq = new Eq<String>() {
        
        @Override
        public boolean equals(String t1, String t2) {
            return t1.equals(t2);
        }
        
    };
    
    private static final Comparator<Integer> intCmp = new Comparator<Integer>() {
        
        @Override
        public int compare(Integer arg0, Integer arg1) {
            return arg0.compareTo(arg1);
        }
        
    };
    
    @Test
    public void testLength() {
        
        List<Integer> l = new List.Nil<Integer>().cons(1).cons(2).cons(3);
        
        Assert.assertTrue(new List.Nil<Integer>().length().equals(0L));
        
        Assert.assertTrue(l.length().equals(3L));
        
    }
    
    @Test
    public void testEquals() {
        
        List<Integer> l1 = new List.Nil<Integer>().cons(-1).cons(3).cons(9);
        
        List<Integer> l2 = new List.Nil<Integer>().cons(-1).cons(3).cons(9);
        
        List<Integer> l3 = new List.Nil<Integer>().cons(-1).cons(3).cons(9).cons(2);
        
        List<Integer> l4 = new List.Nil<Integer>().cons(9).cons(3).cons(-1);
        
        Assert.assertTrue(new List.Nil<Integer>().equalTo(new List.Nil<Integer>(), intEq));
        
        Assert.assertTrue(l1.equalTo(l1, intEq));
        
        Assert.assertTrue(l1.equalTo(l2, intEq));
        
        Assert.assertFalse(new List.Nil<Integer>().equalTo(l1, intEq));
        
        Assert.assertFalse(l2.equalTo(new List.Nil<Integer>(), intEq));
        
        Assert.assertFalse(l1.equalTo(l3, intEq));
        
        Assert.assertFalse(l2.equalTo(l4, intEq));
        
    }
    
    @Test
    public void testReverse() {
        
        List<Integer> l1 = new List.Nil<Integer>().cons(1).cons(2).cons(3);
        
        List<Integer> l2 = new List.Nil<Integer>().cons(3).cons(2).cons(1);
        
        Assert.assertTrue(new List.Nil<Integer>().equalTo(new List.Nil<Integer>().reverse(), intEq));
        
        Assert.assertTrue(l1.equalTo(l1.reverse().reverse(), intEq));
        
        Assert.assertTrue(l1.equalTo(l2.reverse(), intEq));
        
    }
    
    @Test
    public void testAppend() {
        
        List<Integer> l1 = new List.Nil<Integer>().cons(3).cons(2).cons(1);
        
        List<Integer> l2 = new List.Nil<Integer>().cons(6).cons(5).cons(4);
        
        List<Integer> l3 = new List.Nil<Integer>().cons(6).cons(5).cons(4).cons(3).cons(2).cons(1);
        
        Assert.assertTrue(l3.equalTo(l1.append(l2), intEq));
        
        Assert.assertTrue(l1.equalTo(l1.append(new List.Nil<Integer>()), intEq));
        
    }
    
    @Test
    public void testMap() {
        
        List<Integer> l1 = new List.Nil<Integer>().cons(3).cons(2).cons(1);
        
        Function<Integer,String> f = new Function<Integer,String>() {
            
            @Override
            public String apply(Integer x) {
                return x.toString();
            }
            
        };
        
        List<String> l2 = new List.Nil<String>().cons("3").cons("2").cons("1");
        
        Assert.assertTrue(new List.Nil<Integer>().map(f).equalTo(new List.Nil<String>(), strEq));
        
        Assert.assertTrue(l1.map(f).equalTo(l2, strEq));
        
    }
    
    @Test
    public void testIsEmpty() {
        
        List<Integer> l = new List.Nil<Integer>().cons(1);
        
        Assert.assertTrue(new List.Nil<Integer>().isEmpty());
        
        Assert.assertFalse(l.isEmpty());
        
    }
    
    @Test
    public void testCompareTo() {
        
        Assert.assertEquals(0, new List.Nil<Integer>().compareTo(
                new List.Nil<Integer>(), intCmp));
        
        Assert.assertEquals(-1, new List.Nil<Integer>().compareTo(
                new List.Nil<Integer>().cons(0), intCmp));
        
        Assert.assertEquals(1, new List.Nil<Integer>().cons(0).compareTo(
                new List.Nil<Integer>(), intCmp));
        
        Assert.assertEquals(0, new List.Nil<Integer>().cons(0).compareTo(
                new List.Nil<Integer>().cons(0), intCmp));
        
        Assert.assertEquals(1, new List.Nil<Integer>().cons(3).cons(2).cons(1).compareTo(
                new List.Nil<Integer>().cons(2).cons(1), intCmp));
        
        Assert.assertEquals(0, new List.Nil<Integer>().cons(1).cons(2).cons(3).compareTo(
                new List.Nil<Integer>().cons(1).cons(2).cons(3), intCmp));
        
        Assert.assertEquals(1, new List.Nil<Integer>().cons(1).cons(2).cons(3).compareTo(
                new List.Nil<Integer>().cons(0).cons(2).cons(3), intCmp));
        
    }
    
}
