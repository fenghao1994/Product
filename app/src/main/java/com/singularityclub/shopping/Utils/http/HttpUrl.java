package com.singularityclub.shopping.Utils.http;

/**
 * Http地址
 * Created by fenghao on 2015/6/27.
 */
public class HttpUrl {

    //根地址
    public static final String ROOT = "http://qa.tavern.name";

    //注册
    public static final String POST_SIGN_UP = ROOT + "/front/users/sign_up";

    //登陆
    public static final String POST_LOGIN = ROOT + "/front/users/validate";

    //获取最新问题
    public static final String GET_NEW_QUESTION = ROOT + "/front/questions";

    //获取最热问题
    public static final String GET_HOT_QUESTION = ROOT + "/front/questions/question_hottest";

    //获得一个问题的所有答案
    public static final String GET_ALL_ANSWERS = ROOT + "/front/answers?question_id=";

    //获得一个答案的详细信息
    public static final String GET_ONE_ANSWER = ROOT + "/front/answers";

    //新增答案
    public static final String POST_ONE_ANSWER = ROOT + "/front/answers";

    //新增问题
    public static final String POST_ONE_QUESTION = ROOT + "/front/questions";

    //是否可以进行点赞
    public static final String POST_CHECK_PRISE = ROOT + "/front/association/judge_praise";

    //搜索问题
    public static final String POST_SEARCH_QUESTION = ROOT + "/front/questions/search";

    //点赞
    public static final String POST_PRISE = ROOT + "/front/answers/praise";

    //推荐问题
    public static final String GET_RECOMMMENT = ROOT + "/front/questions/recommend_info";

    //推荐问题
    public static final String GET_MYANSWER = ROOT + "/front/answers/user_already_answer";

    //推荐问题
    public static final String GET_QUESTION = ROOT + "/front/questions/user_already_ask";

    //点赞数
    public static final String GET_READ_NUM = ROOT + "/front/answers/answers_praise_num?answer_id=";

    //阅读量
    public static final String POST_READ_NUM = ROOT + "/front/questions/add_read_num";

    //邮箱认证
    public static final String GET_EMAIL_APPROVE = ROOT + "/front/users/school_email_approve";

    //排序
    public static final String GET_RANK = ROOT + "/front/users/get_total_rank";

    //退出登陆
    public static final String POST_LOGIN_OUT = ROOT + "/front/users/sign_out_for_web";

    //得到当前用户
    public static final String GET_CURRENT_USER = ROOT + "/front/users/current";

    //用户认证
    public static final String POST_USER_APPROVE = ROOT + "/front/users/user_approve";

    //通知
    public static final String POST_ALL_NOTICE = ROOT + "/front/notices";

    //编辑个人资料
    public static final String POST_USER_INFO = ROOT + "/front/users/update_only_four_info";
}
