package com.example.maheshpujala.sillymonks.Utils;

import java.util.Map;

/**
 * Created by maheshpujala on 6/10/16.
 */

public class HelperMethods {

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
