package ktak.functionaljava;

import org.junit.Test;

import org.junit.Assert;

public class ListTest {
    
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
        
        Eq<Integer> intComparator = new Eq<Integer>() {

            @Override
            public boolean equals(Integer t1, Integer t2) {
                return t1.equals(t2);
            }
            
        };
        
        Assert.assertTrue(new List.Nil<Integer>().equalTo(new List.Nil<Integer>(), intComparator));
        
        Assert.assertTrue(l1.equalTo(l1, intComparator));
        
        Assert.assertTrue(l1.equalTo(l2, intComparator));
        
        Assert.assertFalse(new List.Nil<Integer>().equalTo(l1, intComparator));
        
        Assert.assertFalse(l2.equalTo(new List.Nil<Integer>(), intComparator));
        
        Assert.assertFalse(l1.equalTo(l3, intComparator));
        
        Assert.assertFalse(l2.equalTo(l4, intComparator));
        
    }
    
}
