package com.adflex.internship.model;

import java.util.Hashtable;

/**
 * Created by dangchienhsgs on 12/08/2015.
 */
public class Model extends Hashtable {
    @Override
    public synchronized Object get(Object o) {
        if (this.containsKey(o)) {
            return this.get(o);
        } else {
            return "";
        }
    }
}
