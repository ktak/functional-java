package ktak.immutablejava;

public abstract class Option<T> {
    
    public abstract <R> R visit(Visitor<R,T> visitor);
    
    public interface Visitor<R,T> {
        public R visitNone();
        public R visitSome(T value);
    }
    
    public static final class None<T> extends Option<T> {
        
        @Override
        public <R> R visit(Visitor<R, T> visitor) {
            return visitor.visitNone();
        }
        
    }
    
    public static final class Some<T> extends Option<T> {
        
        private final T val;
        
        public Some(T val) {
            this.val = val;
        }
        
        @Override
        public <R> R visit(Visitor<R, T> visitor) {
            return visitor.visitSome(val);
        }
        
    }
    
    public static <T> Option<T> none() { return new None<T>(); }
    
    public static <T> Option<T> some(T val) { return new Some<T>(val); }
    
}
