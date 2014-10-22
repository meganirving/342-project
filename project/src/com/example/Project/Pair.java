package com.example.Project;

/**
 * Created by Megan on 22/10/2014.
 */
public class Pair<L,R> {
    private L l;
    private R r;

    // constructors
    public Pair(L newL, R newR) {
        l = newL; r = newR;
    }

    // get/set
    public L getL() { return l; }
    public R getR() { return r; }
    public void setL(L newL) { l = newL; }
    public void setR(R newR) { r = newR; }
}
