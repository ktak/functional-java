package ktak.immutablejava;

public class Tuple<A,B> {
    
    public final A left;
    public final B right;
    
    public Tuple(A left, B right) {
        this.left = left;
        this.right = right;
    }
    
    public static <A,B> Tuple<A,B> create(A left, B right) {
        return new Tuple<A,B>(left, right);
    }
    
}
