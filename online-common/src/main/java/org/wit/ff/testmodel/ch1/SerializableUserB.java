package org.wit.ff.testmodel.ch1;

import java.io.Serializable;

public class SerializableUserB implements Serializable {
    private static final long serialVersionUID = 1L;

    private String noneName;
    private int noneAge ;

    public String getNoneName() {
        return noneName;
    }

    public void setNoneName(String noneName) {
        this.noneName = noneName;
    }

    public int getNoneAge() {
        return noneAge;
    }

    public void setNoneAge(int noneAge) {
        this.noneAge = noneAge;
    }
}
