package com.zyx.library.simple.bean;

/**
 * @author pielan
 * @date 10/3/19 下午9:58
 * @dec
 */
public class Person {
    private String name;
    private String say;

    public Person(String name, String say) {
        this.name = name;
        this.say = say;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSay() {
        return say;
    }

    public void setSay(String say) {
        this.say = say;
    }
}
