package com.zyx.library.simple.binder;


import com.zyx.library.binderlibrary.annotion.ClassId;
import com.zyx.library.binderlibrary.annotion.Execute;
import com.zyx.library.binderlibrary.annotion.ExecuteMode;
import com.zyx.library.simple.bean.Person;

/**
 * @author pielan
 * @date 10/3/19 下午9:57
 * @dec
 */
@ClassId("com.zyx.library.simple.binder.UserManager")
public interface IUserManager {

    Person getPerson();

    @Execute(excute = ExecuteMode.BASE)
    void setPerson(Person person);

    @Execute(excute = ExecuteMode.LOCAL)
    String getName();

    void setName(String name);
}
