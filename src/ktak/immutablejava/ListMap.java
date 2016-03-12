package ktak.immutablejava;

class ListMap<A,B> implements List.Visitor<List<B>,A> {
    
    private final Function<A,B> f;
    
    public ListMap(Function<A,B> f) { this.f = f; }
    
    @Override
    public List<B> visitNil() {
        return new List.Nil<B>();
    }
    
    @Override
    public List<B> visitCons(A head, List<A> tail) {
        return tail.visit(this).cons(f.apply(head));
    }
    
}
