package org.apoos.main;

import org.apoos.crdt.GrowOnly;

public class Run {
    public static void main(String[] args) {
        GrowOnly gon = new GrowOnly();
        gon.merge();
    }
}
