package com.enation.app.javashop.framework.security.model;

/**
 * Created by kingapex on 2018/3/11.
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/3/11
 */
public class Buyer extends User {


    /**
     * 定义买家的角色
     */
    public Buyer() {
        this.add(Role.BUYER);
    }


}
