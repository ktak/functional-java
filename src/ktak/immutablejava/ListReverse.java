package ktak.immutablejava;

class ListReverse<T> implements List.Visitor<List<T>,T> {
    
    private final List<T> reversed;
    
    public ListReverse() { this.reversed = new List.Nil<T>(); }
    private ListReverse(List<T> reversed) { this.reversed = reversed; }
    
    @Override
    public List<T> visitNil() {
        return reversed;
    }
    
    @Override
    public List<T> visitCons(T head, List<T> tail) {
        return tail.visit(new ListReverse<T>(reversed.cons(head)));
    }
    
}
