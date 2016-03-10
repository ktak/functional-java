package ktak.immutablejava;

public abstract class List<T> {
    
    public abstract <R> R visit(Visitor<R,T> visitor);
    
    public static interface Visitor<R,T> {
        public R visitNil();
        public R visitCons(T head, List<T> tail);
    }
    
    public static final class Nil<T> extends List<T> {

        @Override
        public <R> R visit(Visitor<R, T> visitor) {
            return visitor.visitNil();
        }
        
    }
    
    private static class Cons<T> extends List<T> {
        
        public final T head;
        public final List<T> tail;
        
        public Cons(T head, List<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public <R> R visit(Visitor<R, T> visitor) {
            return visitor.visitCons(head, tail);
        }
        
    }
    
    public List<T> cons(T value) {
        return new Cons<T>(value, this);
    }
    
    public Long length() {
        return this.visit(new ListLength<T>());
    }
    
    public Boolean equalTo(List<T> other, final Eq<T> eq) {
        return this.visit(new ListEqualTo<T>(other, eq));
    }
    
    public List<T> reverse() {
        return this.visit(new ListReverse<T>());
    }
    
    public List<T> append(List<T> end) {
        return this.visit(new ListAppend<T>(end));
    }
    
}