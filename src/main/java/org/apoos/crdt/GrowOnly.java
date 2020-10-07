package org.apoos.crdt;

import com.netopyr.wurmloch.crdt.GSet;
import com.netopyr.wurmloch.store.LocalCrdtStore;

import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class GrowOnly {
    String type = "Grow Only";

    public void merge(){
        LocalCrdtStore crdtStore1 = new LocalCrdtStore();
        LocalCrdtStore crdtStore2 = new LocalCrdtStore();
        crdtStore1.connect(crdtStore2);

        GSet<String> replica1 = crdtStore1.createGSet("ID_1");
        GSet<String> replica2 = crdtStore2.<String>findGSet("ID_1").get();

        replica1.add("apple");
        replica2.add("banana");
        assertThat(replica1, containsInAnyOrder("apple","banana"));
        assertThat(replica2,containsInAnyOrder("apple", "banana"));

        crdtStore1.disconnect(crdtStore2);

        replica1.add("strawberry");
        replica2.add("pear");
        assertThat(replica1, containsInAnyOrder("apple", "banana", "strawberry"));
        assertThat(replica2, containsInAnyOrder("apple", "banana", "pear"));

        crdtStore1.connect(crdtStore2);
        assertThat(replica1, containsInAnyOrder("apple", "banana", "strawberry", "pear"));
        assertThat(replica2, containsInAnyOrder("apple", "banana", "strawberry", "pear"));
    }


}
