package org.apoos.crdt;

import com.netopyr.wurmloch.crdt.RGA;
import com.netopyr.wurmloch.store.LocalCrdtStore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class RGAType {
    public static void merge(){
        final LocalCrdtStore crdtStore1 = new LocalCrdtStore();
        final LocalCrdtStore crdtStore2 = new LocalCrdtStore();
        crdtStore1.connect(crdtStore2);

        final RGA<String> replica1 = crdtStore1.createRGA("ID_1");
        final RGA<String> replica2 = crdtStore2.<String>findRGA("ID_1").get();

        replica1.add("apple");
        replica2.add("banana");

        assertThat(replica1, contains("apple", "banana"));
        assertThat(replica1, contains("apple", "banana"));
        System.out.println(replica1.toString());
        System.out.println(replica2.toString());
        System.out.println("============================");

        crdtStore1.disconnect(crdtStore2);

        replica1.remove("banana");
        replica2.add(1,"strawberry");

        assertThat(replica1, contains("apple"));
        assertThat(replica2, contains("apple","strawberry","banana"));
        System.out.println(replica1.toString());
        System.out.println(replica2.toString());
        System.out.println("================================");

        crdtStore1.connect(crdtStore2);
        assertThat(replica1, contains("apple", "strawberry"));
        assertThat(replica2, contains("apple", "strawberry"));
        System.out.println(replica1.toString());
        System.out.println(replica2.toString());
        System.out.println("==================================");

        crdtStore1.disconnect(crdtStore2);
        replica2.add(0,"lizard");
        replica1.add(0,"frog");
        System.out.println(replica1.toString());
        System.out.println(replica2.toString());

        crdtStore1.connect(crdtStore2);
        System.out.println("-------------------------------------------");
        System.out.println(replica1.toString());
        System.out.println(replica2.toString());
        System.out.println("=====================================");
    }
}
