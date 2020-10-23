package org.apoos.crdt;

import com.netopyr.wurmloch.crdt.LWWRegister;
import com.netopyr.wurmloch.store.LocalCrdtStore;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LWWRegistertype {
    String type = "Last Writer Wins";

    public static void merge(){
        final LocalCrdtStore crdtStore1 = new LocalCrdtStore("N_1");
        final LocalCrdtStore crdtStore2 = new LocalCrdtStore("N_2");
        crdtStore1.connect(crdtStore2);

        final LWWRegister<String> replica1 = crdtStore1.createLWWRegister("ID_1");
        final LWWRegister<String> replica2 = crdtStore2.<String>findLWWRegister("ID_1").get();

        replica1.set("apple");
        replica2.set("banana");

        assertThat(replica1.get(), is("banana"));
        assertThat(replica2.get(), is(("banana")));

        //disconnect

        crdtStore1.disconnect(crdtStore2);

        replica1.set("strawberry");
        replica2.set("pear");

        assertThat(replica1.get(), is("strawberry"));
        assertThat(replica2.get(), is("pear"));

        crdtStore1.connect(crdtStore2);

        assertThat(replica1.get(), is("pear"));
        assertThat(replica2.get(), is("pear"));
        

    }
}
