package ktak.functionaljava;

import org.junit.Test;

import org.junit.Assert;

public class ListTest {
    
    @Test
    public void testLength() {
        
        List<Integer> l = new List.Nil<Integer>().cons(1).cons(2).cons(3);
        
        Assert.assertTrue(List.length(new List.Nil<Integer>()).equals(0L));
        
        Assert.assertTrue(List.length(l).equals(3L));
        
    }
    
    @Test
    public void testEquals() {
        
        List<Integer> l1 = new List.Nil<Integer>().cons(-1).cons(3).cons(9);
        
        List<Integer> l2 = new List.Nil<Integer>().cons(-1).cons(3).cons(9);
        
        List<Integer> l3 = new List.Nil<Integer>().cons(-1).cons(3).cons(9).cons(2);
        
        List<Integer> l4 = new List.Nil<Integer>().cons(9).cons(3).cons(-1);
        
        Eq<Integer> intComparator = new Eq<Integer>() {

            @Override
            public boolean equals(Integer t1, Integer t2) {
                return t1.equals(t2);
            }
            
        };
        
        Assert.assertTrue(List.equals(new List.Nil<Integer>(), new List.Nil<Integer>(), intComparator));
        
        Assert.assertTrue(List.equals(l1, l1, intComparator));
        
        Assert.assertTrue(List.equals(l1, l2, intComparator));
        
        Assert.assertFalse(List.equals(new List.Nil<Integer>(), l1, intComparator));
        
        Assert.assertFalse(List.equals(l2, new List.Nil<Integer>(), intComparator));
        
        Assert.assertFalse(List.equals(l1, l3, intComparator));
        
        Assert.assertFalse(List.equals(l2, l4, intComparator));
        
    }
    
}
