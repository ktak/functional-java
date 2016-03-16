package ktak.immutablejava;

class ListEmpty<T> implements List.Visitor<Boolean,T> {
    
    @Override
    public Boolean visitNil() {
        return true;
    }
    
    @Override
    public Boolean visitCons(T head, List<T> tail) {
        return false;
    }
    
}
