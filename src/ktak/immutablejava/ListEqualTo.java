package ktak.immutablejava;

class ListEqualTo<T> implements List.Visitor<Boolean,T> {
    
    private final List<T> other;
    private final Eq<T> eq;
    
    public ListEqualTo(List<T> other, Eq<T> eq) {
        this.other = other;
        this.eq = eq;
    }
    
    
    @Override
    public Boolean visitNil() {
        return other.visit(new List.Visitor<Boolean,T>() {

            @Override
            public Boolean visitNil() {
                return true;
            }

            @Override
            public Boolean visitCons(T head, List<T> tail) {
                return false;
            }
            
        });
    }
    
    @Override
    public Boolean visitCons(final T head, final List<T> tail) {
        return other.visit(new List.Visitor<Boolean,T>() {

            @Override
            public Boolean visitNil() {
                return false;
            }

            @Override
            public Boolean visitCons(T otherHead, List<T> otherTail) {
                return eq.equals(head, otherHead) &&
                        tail.visit(new ListEqualTo<T>(otherTail, eq));
            }
            
        });
    }
    
}
