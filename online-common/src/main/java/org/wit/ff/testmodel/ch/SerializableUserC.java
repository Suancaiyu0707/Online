package org.wit.ff.testmodel.ch;

import java.io.Serializable;

public class SerializableUserC implements Serializable {
    private static final long serialVersionUID = 1L;

    private String noneName;
    private double noneAge ;

    public String getNoneName() {
        return noneName;
    }

    public void setNoneName(String noneName) {
        this.noneName = noneName;
    }

    public double getNoneAge() {
        return noneAge;
    }

    public void setNoneAge(double noneAge) {
        this.noneAge = noneAge;
    }
}
