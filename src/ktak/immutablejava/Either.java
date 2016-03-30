package ktak.immutablejava;

public abstract class Either<X,Y> {
    
    public abstract <R> R visit(Visitor<R,X,Y> visitor);
    
    public abstract <Z> Either<Z,Y> mapLeft(Function<X,Z> f);
    public abstract <Z> Either<X,Z> mapRight(Function<Y,Z> f);
    
    public abstract <Z> Z match(Function<X,Z> leftCase, Function<Y,Z> rightCase);
    
    public interface Visitor<R,X,Y> {
        public R visitLeft(X x);
        public R visitRight(Y y);
    }
    
    public static final class Left<X,Y> extends Either<X,Y> {
        
        private final X val;
        
        public Left(X val) { this.val = val; }
        
        @Override
        public <R> R visit(Visitor<R, X, Y> visitor) {
            return visitor.visitLeft(val);
        }
        
        @Override
        public <Z> Either<Z, Y> mapLeft(Function<X, Z> f) {
            return left(f.apply(val));
        }
        
        @Override
        public <Z> Either<X, Z> mapRight(Function<Y, Z> f) {
            return left(val);
        }
        
        @Override
        public <Z> Z match(Function<X, Z> leftCase, Function<Y, Z> rightCase) {
            return leftCase.apply(val);
        }
        
    }
    
    public static final class Right<X,Y> extends Either<X,Y> {
        
        private final Y val;
        
        public Right(Y val) { this.val = val; }
        
        @Override
        public <R> R visit(Visitor<R,X,Y> visitor) {
            return visitor.visitRight(val);
        }
        
        @Override
        public <Z> Either<Z, Y> mapLeft(Function<X, Z> f) {
            return right(val);
        }
        
        @Override
        public <Z> Either<X, Z> mapRight(Function<Y, Z> f) {
            return right(f.apply(val));
        }
        
        @Override
        public <Z> Z match(Function<X, Z> leftCase, Function<Y, Z> rightCase) {
            return rightCase.apply(val);
        }
        
    }
    
    public static <X,Y> Either<X,Y> left(X x) {
        return new Left<X,Y>(x);
    }
    
    public static <X,Y> Either<X,Y> right(Y y) {
        return new Right<X,Y>(y);
    }
    
}
