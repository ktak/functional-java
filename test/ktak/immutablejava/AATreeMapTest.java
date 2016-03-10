package ktak.immutablejava;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import ktak.immutablejava.AATreeMap;
import ktak.immutablejava.Option;

import org.junit.Assert;

public class AATreeMapTest {
    
private static final Comparator<Integer> cmp = new Comparator<Integer>() {
        
        @Override
        public int compare(Integer arg0, Integer arg1) {
            return arg0.compareTo(arg1);
        }
        
    };
    
    @Test
    public void testSize() {
        
        AATreeMap<Integer,Integer> map = AATreeMap.emptyMap(cmp);
        
        List<Integer> list = consecutiveIntegerList(100);
        Long mapSize = 0L;
        
        for (Integer unique : list) {
            map = map.insert(unique, unique);
            mapSize++;
            Assert.assertEquals(mapSize, map.size());
        }
        
        for (Integer unique : list) {
            map = map.insert(unique, unique);
            Assert.assertEquals(mapSize, map.size());
        }
        
    }
    
    @Test
    public void testContains() {
        
        AATreeMap<Integer,Integer> map = AATreeMap.emptyMap(cmp);
        
        List<Integer> randomList = randomIntegerList(1000, 900);
        for (Integer random : randomList) {
            map = map.insert(random, random);
            Assert.assertTrue(map.containsKey(random));
        }
        
        for (Integer random : randomList) {
            Assert.assertTrue(map.containsKey(random));
        }
        
    }
    
    @Test
    public void testGet() {
        
        AATreeMap<Integer,Integer> map = AATreeMap.emptyMap(cmp);
        
        List<Integer> list = consecutiveIntegerList(1000);
        List<Integer> randomList = randomIntegerList(1000, 1000);
        for (int i=0; i < list.size(); i++) {
            map = map.insert(list.get(i), randomList.get(i));
        }
        
        for (int i=0; i < list.size(); i++) {
            final Integer random = randomList.get(i);
            Assert.assertTrue(map.get(list.get(i)).visit(
                    new Option.Visitor<Boolean,Integer>() {
                        
                        @Override
                        public Boolean visitNone() {
                            return false;
                        }
                        
                        @Override
                        public Boolean visitSome(Integer value) {
                            return value.equals(random);
                        }
                        
                    }));
        }
        
    }
    
    @Test
    public void testRemove() {
        
        AATreeMap<Integer,Integer> map = AATreeMap.emptyMap(cmp);
        
        List<Integer> list = consecutiveIntegerList(1000);
        for (Integer unique : list) {
            map = map.insert(unique, unique);
        }
        
        for (Integer unique : list) {
            map = map.remove(unique);
            Assert.assertFalse(map.containsKey(unique));
        }
        
    }
    
    private List<Integer> consecutiveIntegerList(int size) {
        
        List<Integer> list = new ArrayList<Integer>();
        for (int i=0; i < size; i++) {
            list.add(i);
        }
        return list;
        
    }
    
    private List<Integer> randomIntegerList(int size, int modulo) {
        
        List<Integer> randomList = new ArrayList<Integer>();
        Random rand = new Random();
        for (int i=0; i < size; i++) {
            randomList.add(rand.nextInt() % modulo);
        }
        return randomList;
        
    }
    
}
