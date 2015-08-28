package com.singularityclub.shopping.Utils.http;

/**
 * Http地址
 * Created by fenghao on 2015/6/27.
 */
public class HttpUrl {

    //根地址
    public static final String ROOT = "http://dt.tavern.name/front/";

    //上传资料
    public static final String POST_USERINFO = ROOT + "info_entry/index";

    //展示的商品
    public static final String POST_SHOW_PRODUCTION = ROOT + "production/index";

    //一级分类
    public static final String GET_MAIN_CALSSIFY = ROOT + "main_classify/index";
    //一级分类里面的二级分类id
    public static final String POST_SECOND_CALSSIFY_ID = ROOT + "main_classify/main_classify_children";

    //二级分类里面的商品
    public static final String POST_SECOND_CLASSIFY = ROOT + "second_classify/index";

    //关注

    public static final String POST_PRODUCTION_ATTENTION = ROOT + "production/attention";

    //加入购物车
    public static final String POST_ADD_CAR = ROOT + "production/add_in_cart";

    //查看购物车
    public static final String POST_LOOK_CAR = ROOT + "production/show_attention";

    //搜索
    public static final String POST_SEARCH = ROOT + "production/search_product";

    //传shop——id
    public static final String POST_SHOP_ID = ROOT + "info_entry/customer_shop";

    //上传消费行为记录
    public static final String POST_ACTION = ROOT + "production/behavior_record";

    //得到一级主题
    public static final String GET_FIRST_THEME = ROOT + "main_theme/index";

    //得到二级主题
    public static final String POST_SECOND_THEME = ROOT + "main_theme/main_theme_children";

    //得到二级主题里面的商品
    public static final String POST_SECOND_THEME_PRODUCTION = ROOT + "second_theme";
}
