## List
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

System.out.println(list.visit(new ListString())); // cons(3, cons(2, cons(1, nil)))
System.out.println(list_reversed.visit(new ListString())); // cons(1, cons(2, cons(3, nil)))
```
## AATree
```
import ktak.immutablejava.AATree;
```
```
Comparator<Integer> intCmp = new Comparator<Integer>() {
   
   @Override
   public int compare(Integer i1, Integer i2) {
      return i1.compareTo(i2);
   }
   
};

AATree<Integer> tree = AATree.emptyTree(intCmp).insert(3).insert(2).insert(1);

AATree<Integer> mappedTree = tree.map(
   new Function<Integer,Integer>() {
      @Override
      public Integer apply(Integer x) {
         return x+1;
      }
   },
   intCmp);

System.out.println(mappedTree.contains(1)); // false
System.out.println(mappedTree.contains(4)); // true
```
## AATreeMap
```
import ktak.immutablejava.AATreeMap;
```
```
Comparator<Integer> intCmp = new Comparator<Integer>() {
   
   @Override
   public int compare(Integer i1, Integer i2) {
      return i1.compareTo(i2);
   }
   
};

Comparator<String> strCmp = new Comparator<String>() {
   
   @Override
   public int compare(String s1, String s2) {
      return s1.compareTo(s2);
   }
   
};

AATreeMap<Integer,String> map = new AATreeMap<Integer,String>(intCmp)
      .insert(1, "a")
      .insert(2, "b")
      .insert(3, "c");

AATreeMap<String,String> keyMapped = map.mapKeys(
   new Function<Integer,String>() {
      @Override
      public String apply(Integer x) {
         return x.toString();
      }
   },
   strCmp);

System.out.println(keyMapped.containsKey("0")); // false
System.out.println(keyMapped.containsKey("1")); // true
```
## Either
```
import ktak.immutablejava.Either;
```
```
Either<Integer, String> left = Either.left(123);
Either<Integer, String> right = Either.right("abc");

Either.Visitor<String, Integer, String> eitherPrint = new Either.Visitor<String, Integer, String>() {
   
   @Override
   public String visitLeft(Integer x) {
      return x.toString();
   }
   
   @Override
   public String visitRight(String y) {
      return y;
   }
   
};

System.out.println(left.visit(eitherPrint)); // 123
System.out.println(right.visit(eitherPrint)); // abc
```
## Option
```
import ktak.immutablejava.Option;
```
```
Option<String> opt = Option.some("xyz");

Option.Visitor<String, String> optStr = new Option.Visitor<String, String>() {

   @Override
   public String visitNone() {
      return "none";
   }
   
   @Override
   public String visitSome(String value) {
      return value;
   }
   
};

System.out.println(opt.visit(optStr)); // xyz
System.out.println(new Option.None<String>().visit(optStr)); // none
```
