package ktak.immutablejava;

class ListFoldRight<T,U> implements List.Visitor<U,T> {
    
    private final U init;
    private final Function<T,Function<U,U>> f;
    
    public ListFoldRight(U init, Function<T,Function<U,U>> f) {
        this.init = init;
        this.f = f;
    }
    
    @Override
    public U visitNil() {
        return init;
    }
    
    @Override
    public U visitCons(T head, List<T> tail) {
        return f.apply(head).apply(tail.visit(this));
    }
    
}
