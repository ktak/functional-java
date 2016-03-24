## Lists
```
import ktak.immutablejava.List;
```
```
List<Integer> list = new List.Nil<Integer>().cons(1).cons(2).cons(3);

class ListReverse implements List.Visitor<List<Integer>, Integer> {

   private final List<Integer> reversed_list;
  
   public ListReverse() { this.reversed_list = new List.Nil<Integer>(); }
   public ListReverse(List<Integer> reversed_list) { this.reversed_list = reversed_list; }
  
   @Override
   public List<Integer> visitNil() { return reversed_list; }
  
   @Override
   public List<Integer> visitCons(Integer head, List<Integer> tail) {
     return tail.visit(new ListReverse(reversed_list.cons(head)));
   }

}

List<Integer> list_reversed = list.visit(new ListReverse());

class ListString implements List.Visitor<String, Integer> {

   @Override
   public String visitNil() { return "nil"; }
  
   @Override
   public String visitCons(Integer head, List<Integer> tail) {
     return "cons(" + head + ", " + tail.visit(this) + ")";
   }

}

System.out.println(list.visit(new ListString()));
System.out.println(list_reversed.visit(new ListString()));
```
