package ktak.immutablejava;

public class ListMapCat<A,B> implements List.Visitor<List<B>,A>{
    
    private final Function<A,List<B>> f;
    
    public ListMapCat(Function<A,List<B>> f) {
        this.f = f;
    }
    
    @Override
    public List<B> visitNil() {
        return new List.Nil<>();
    }
    
    @Override
    public List<B> visitCons(A head, List<A> tail) {
        return f.apply(head).append(tail.visit(this));
    }
    
}
