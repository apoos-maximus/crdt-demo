package org.apoos.crdt;

import com.netopyr.wurmloch.crdt.MVRegister;
import com.netopyr.wurmloch.store.CrdtStore;
import com.netopyr.wurmloch.store.LocalCrdtStore;
import javaslang.collection.Array;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class MVRegisterType {
    String type = "MV Register";

    public static void merge(){
       final LocalCrdtStore crdtStore1 = new LocalCrdtStore();
       final LocalCrdtStore crdtStore2 = new LocalCrdtStore();
       crdtStore1.connect(crdtStore2);

       final MVRegister<String> replica1 = crdtStore1.createMVRegister("ID_1");
       final MVRegister<String> replica2 = crdtStore2.<String>findMVRegister("ID_1").get();

       replica1.set("apple");
       replica2.set("banana");

       assertThat(replica1.get(), contains("banana"));
       assertThat(replica2.get(), contains("banana"));

       crdtStore1.disconnect(crdtStore2);

       replica1.set("strawberry");
       replica2.set("pear");

       crdtStore1.connect(crdtStore2);

       assertThat(replica1.get(), containsInAnyOrder("strawberry", "pear"));
       assertThat(replica2.get(), containsInAnyOrder("strawberry", "pear"));

       Array<String> colc = replica1.get();
       for (String ffv: colc) {
          System.out.println(ffv);
       }

       replica2.set("orange");
       assertThat(replica1.get(), contains("orange"));
       assertThat(replica2.get(), contains("orange"));

       Array<String> collection = replica1.get();
       for (String ar: collection) {
          System.out.println(ar);
       }

    }

    public static void manyMerge(){
       final LocalCrdtStore crdtStore1 = new LocalCrdtStore();
       final LocalCrdtStore crdtStore2 = new LocalCrdtStore();
       final LocalCrdtStore crdtStore3 = new LocalCrdtStore();
       crdtStore2.connect(crdtStore1);
       crdtStore2.connect(crdtStore3);

       final MVRegister<String> replica1 = crdtStore1.createMVRegister("ID_1");
       final MVRegister<String> replica2 = crdtStore2.<String>findMVRegister("ID_1").get();
       final MVRegister<String> replica3 = crdtStore3.<String>findMVRegister("ID_1").get();

       // disconnect 2 and 3
       crdtStore2.disconnect(crdtStore3);

       //set some values
       replica1.set("apple");
       replica3.set("banana");

       //stores 1 and 2 contain apple store 3 contains banana
       assertThat(replica1.get(), containsInAnyOrder("apple"));
       assertThat(replica2.get(), containsInAnyOrder("apple"));
       assertThat(replica3.get(), containsInAnyOrder("banana"));

       Array<String> colc = replica1.get();
       System.out.println(colc);
       colc = replica2.get();
       System.out.println(colc);
       colc = replica3.get();
       System.out.println(colc);
       System.out.println("================================");



       crdtStore2.disconnect(crdtStore1);
       crdtStore2.connect(crdtStore3);

       // 1 to strawberry
       replica1.set("strawberry");;

       //strawberry in 1
       // 2 and 3 are synchronized
       assertThat(replica1.get(), containsInAnyOrder("strawberry"));
       assertThat(replica2.get(), containsInAnyOrder("apple", "banana"));
       assertThat(replica3.get(), containsInAnyOrder("apple", "banana"));

       colc = replica1.get();
       System.out.println(colc);
       colc = replica2.get();
       System.out.println(colc);
       colc = replica3.get();
       System.out.println(colc);
       System.out.println("================================");


       //connect all three
       crdtStore2.connect(crdtStore1);

       assertThat(replica1.get(), containsInAnyOrder("strawberry", "banana"));
       assertThat(replica2.get(), containsInAnyOrder("strawberry", "banana"));
       assertThat(replica3.get(), containsInAnyOrder("strawberry", "banana"));

       colc = replica1.get();
       System.out.println(colc);
       colc = replica2.get();
       System.out.println(colc);
       colc = replica3.get();
       System.out.println(colc);
       System.out.println("================================");

    }
}
