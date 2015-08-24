package com.singularityclub.shopping.preferences;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by fenghao on 2015/8/24.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface UserInfo {

    @DefaultInt(-1)
    int id();

    /**
     * 购买商品的数量
     * @return
     */
    @DefaultInt(0)
    int shopNumber();


}
