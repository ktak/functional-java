package ktak.immutablejava;

class ListLength<T> implements List.Visitor<Long,T> {
    
    @Override
    public Long visitNil() {
        return 0L;
    }
    
    @Override
    public Long visitCons(T head, List<T> tail) {
        return 1 + tail.visit(this);
    }
    
}
