package ktak.immutablejava;

import java.util.Comparator;

class ListCompareTo<T> implements List.Visitor<Integer,T> {
    
    private final List<T> other;
    private final Comparator<T> cmp;
    
    public ListCompareTo(List<T> other, Comparator<T> cmp) {
        this.other = other;
        this.cmp = cmp;
    }
    
    @Override
    public Integer visitNil() {
        
        return other.visit(new List.Visitor<Integer,T>() {
            
            @Override
            public Integer visitNil() {
                return 0;
            }
            
            @Override
            public Integer visitCons(T head, List<T> tail) {
                return -1;
            }
            
        });
        
    }
    
    @Override
    public Integer visitCons(final T head, final List<T> tail) {
        
        return other.visit(new List.Visitor<Integer,T>() {
            
            @Override
            public Integer visitNil() {
                return 1;
            }
            
            @Override
            public Integer visitCons(T otherHead, List<T> otherTail) {
                
                int cmpResult = cmp.compare(head, otherHead);
                
                if (cmpResult != 0)
                    return cmpResult;
                
                return tail.visit(new ListCompareTo<T>(otherTail, cmp));
                
            }
        });
        
    }
    
}
