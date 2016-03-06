package ktak.functionaljava;

import org.junit.Test;

import org.junit.Assert;

public class ListTest {
    
    private static final Eq<Integer> eq = new Eq<Integer>() {

        @Override
        public boolean equals(Integer t1, Integer t2) {
            return t1.equals(t2);
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
        
        Assert.assertTrue(new List.Nil<Integer>().equalTo(new List.Nil<Integer>(), eq));
        
        Assert.assertTrue(l1.equalTo(l1, eq));
        
        Assert.assertTrue(l1.equalTo(l2, eq));
        
        Assert.assertFalse(new List.Nil<Integer>().equalTo(l1, eq));
        
        Assert.assertFalse(l2.equalTo(new List.Nil<Integer>(), eq));
        
        Assert.assertFalse(l1.equalTo(l3, eq));
        
        Assert.assertFalse(l2.equalTo(l4, eq));
        
    }
    
    @Test
    public void testReverse() {
        
        List<Integer> l1 = new List.Nil<Integer>().cons(1).cons(2).cons(3);
        
        List<Integer> l2 = new List.Nil<Integer>().cons(3).cons(2).cons(1);
        
        Assert.assertTrue(new List.Nil<Integer>().equalTo(new List.Nil<Integer>().reverse(), eq));
        
        Assert.assertTrue(l1.equalTo(l1.reverse().reverse(), eq));
        
        Assert.assertTrue(l1.equalTo(l2.reverse(), eq));
        
    }
    
    @Test
    public void testAppend() {
        
        List<Integer> l1 = new List.Nil<Integer>().cons(3).cons(2).cons(1);
        
        List<Integer> l2 = new List.Nil<Integer>().cons(6).cons(5).cons(4);
        
        List<Integer> l3 = new List.Nil<Integer>().cons(6).cons(5).cons(4).cons(3).cons(2).cons(1);
        
        Assert.assertTrue(l3.equalTo(l1.append(l2), eq));
        
        Assert.assertTrue(l1.equalTo(l1.append(new List.Nil<Integer>()), eq));
        
    }
    
}
