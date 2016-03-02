package ktak.functionaljava;

public abstract class List<T> {
    
    public abstract <R> R visit(Visitor<R,T> visitor);
    
    public static interface Visitor<R,T> {
        public R visitNil();
        public R visitCons(T head, List<T> tail);
    }
    
    public static class Nil<T> extends List<T> {

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
    
    public static <T> Long length(List<T> list) {
        
        return list.visit(new Visitor<Long,T>() {

            @Override
            public Long visitNil() {
                return 0L;
            }

            @Override
            public Long visitCons(T head, List<T> tail) {
                return 1 + tail.visit(this);
            }
            
        });
        
    }
    
    public static <T> Boolean equals(
            List<T> list1, final List<T> list2, final Eq<T> eq) {
        
        return list1.visit(new Visitor<Boolean,T>() {

            @Override
            public Boolean visitNil() {
                return list2.visit(new Visitor<Boolean,T>() {

                    @Override
                    public Boolean visitNil() {
                        return true;
                    }

                    @Override
                    public Boolean visitCons(T head, List<T> tail) {
                        return false;
                    }
                    
                });
            }

            @Override
            public Boolean visitCons(final T head1, final List<T> tail1) {
                return list2.visit(new Visitor<Boolean,T>() {

                    @Override
                    public Boolean visitNil() {
                        return false;
                    }

                    @Override
                    public Boolean visitCons(T head2, List<T> tail2) {
                        if (!eq.equals(head1, head2))
                            return false;
                        return List.equals(tail1, tail2, eq);
                    }
                    
                });
            }
            
        });
        
    }
    
}
