package net.fachtnaroe.tuuber;

import gnu.math.RealNum;

public class town {
    String name=new String("none");
    Integer population=-1;
    String barony =new String("");
    String civilParish =new String("");
    String poorLawUnion =new String("");
    float acres= -1;

    void set (String n, Integer p, String b, String c, String u, float a) {
        name=n;
        population=p;
        barony=b;
        civilParish=c;
        poorLawUnion=u;
        acres=a;
    }

    town get () {
        return this;
    }
}
