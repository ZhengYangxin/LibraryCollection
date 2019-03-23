package com.zyx.library.simple.binder;


import com.zyx.library.simple.bean.Person;

/**
 * @author pielan
 * @date 10/3/19 下午9:57
 * @dec
 */
public class UserManager implements IUserManager {

    public static UserManager instance = new UserManager();
    private Person person;
    private String name;

    public static UserManager getInstance() {
        return instance;
    }
    @Override
    public Person getPerson() {
        return person;
    }

    @Override
    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
