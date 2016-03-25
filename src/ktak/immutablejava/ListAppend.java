package ktak.immutablejava;

class ListAppend<T> implements List.Visitor<List<T>,T> {
    
    private final List<T> l2;
    
    public ListAppend(List<T> l2) { this.l2 = l2; }
    
    @Override
    public List<T> visitNil() {
        return l2;
    }
    
    @Override
    public List<T> visitCons(T head, List<T> tail) {
        return tail.visit(this).cons(head);
    }
    
}
