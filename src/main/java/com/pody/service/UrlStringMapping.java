package com.pody.service;

public class UrlStringMapping {
    //sign up and login
    public static final String URL001 = "/api/signup/email";//POST
    public static final String URL011 = "/api/signup/phone";//POST
    public static final String URL002 = "/api/login";//POST
    //user urls
    public static final String URL003 = "/api/user/read";//Post
    public static final String URL004 = "/api/user/update/{id}";//PUT
    public static final String URL005 = "/api/user/delete/{id}";//DELETE
    public static final String URL006 = "/api/user/premium/{id}";//GET
    public static final String URL007 = "/api/user/{id}/image";//POST
    public static final String URL0077 = "/api/user/{id}/channelImage";//POST
    public static final String URL008 = "/api/user/check/email";//POST
    public static final String URL009 = "/api/user/check/username";//POST
    public static final String URL073 = "/api/user/resetPassword";//POST
    public static final String URL074 = "/api/user/find/resetPassword";//POST
    public static final String URL076 = "/api/user/list/channels";//Post
    public static final String URL078 = "/api/user/update/isChannel";//Put
    public static final String URL081 = "/api/user/list/subscriptions/{id}";//Get
    public static final String URL090 = "/api/user/list/subscriptions/sidenav/{id}";//Get
    public static final String URL086 = "/api/user/follow/check";//Post
    //search urls
    public static final String URL010 = "/api/search";//POST
    //podcast urls
    public static final String URL015 = "/api/podcast/create";//POST
    public static final String URL016 = "/api/podcast/read/{id}";//GET
    public static final String URL017 = "/api/podcast/update/{id}";//PUT
    public static final String URL018 = "/api/podcast/delete/{id}";//DELETE
    public static final String URL019 = "/api/podcast/{id}/upload";//POST
    public static final String URL021 = "/api/podcast/view/{id}";//GET
    public static final String URL022 = "/api/podcast/like/{id}";//GET
    public static final String URL023 = "/api/podcast/list/{id}/user/{till}/{to}";//GET
    public static final String URL024 = "/api/podcast/list/like/{till}/{to}";//GET لیست محبوب ترین ها
    public static final String URL025 = "/api/podcast/list/view/{till}/{to}";//GET لیست پر بازدید ترین ها
    public static final String URL026 = "/api/podcast/list/suggested/{till}/{to}";//GET لیست پیشنهادی
    public static final String URL027 = "/api/podcast/list/new/{till}/{to}";//GET لیست جدیدترین
    public static final String URL028 = "/api/podcast/listenLater";//POST
    public static final String URL087 = "/api/podcast/list/listenLater/{till}/{to}";//POST
    public static final String URL088 = "/api/podcast/listenLater/check";//POST
    public static final String URL089 = "/api/podcast/listenLater/delete";//DELETE
    public static final String URL029 = "/api/podcast/{id}/history";//GET
    public static final String URL071 = "/api/podcast/list/{id}/history";//GET
    public static final String URL072 = "/api/podcast/rss";//POST
    public static final String URL075 = "/api/podcast/list/trending";//GET
    public static final String URL077 = "/api/podcast/list/home";//Post
    public static final String URL082 = "/api/admin/podcast/rss";//Post
    public static final String URL083 = "/api/podcast/list/followings/{till}/{to}";//Post لیست پادکست پادکسترهای دنبال شده
    public static final String URL085 = "/api/podcast/list/home/mobile";//Post
    public static final String URL091 = "/api/podcast/list/home/mobile/infinite/{till}/{to}";//Post

    //category urls
    public static final String URL030 = "/api/admin/category/create";//POST
    public static final String URL031 = "/api/admin/category/update/{id}";//PUT
    public static final String URL032 = "/api/admin/category/delete/{id}";//DELETE
    public static final String URL033 = "/api/category/tree";//GET
    public static final String URL034 = "/api/category/list/parents";//GET
    public static final String URL035 = "/api/category/list/{id}/children";//GET
    public static final String URL079 = "/api/category/read";//Post
    public static final String URL080 = "/api/category/list/page";//Post
    public static final String URL084 = "/api/category/read/infinite/{till}/{to}";//Post
    //comment urls
    public static final String URL036 = "/api/comment/create";//POST
    public static final String URL037 = "/api/comment/update/{id}";//PUT
    public static final String URL038 = "/api/admin/comment/delete/{id}";//DELETE
    public static final String URL039 = "/api/comment/list/{id}/podcast";//POST
    public static final String URL040 = "/api/comment/like/{id}";//GET
    public static final String URL070 = "/api/admin/comment/approve/{id}";//GET
    //follow urls
    public static final String URL041 = "/api/follow/user/create";//POST
    public static final String URL042 = "/api/follow/user/delete";//DELETE
    public static final String URL043 = "/api/follow/category/create";//POST
    public static final String URL044 = "/api/follow/category/delete";//DELETE
    public static final String URL045 = "/api/follow/hashtag/create";//POST
    public static final String URL046 = "/api/follow/hashtag/delete";//DELETE
    public static final String URL047 = "/api/following/{id}/user";//POST
    public static final String URL048 = "/api/followers/{id}/user";//POST
    //hashtag urls
    public static final String URL051 = "/api/hashtag/create";//POST
    public static final String URL052 = "/api/admin/hashtag/delete/{id}";//DELETE

    //news urls
    public static final String URL060 = "/api/news/create";//POST
    public static final String URL061 = "/api/news/update/{id}";//PUT
    public static final String URL062 = "/api/news/delete/{id}";//DELETE
    public static final String URL063 = "/api/news/list/homePage";//GET
    public static final String URL064 = "/api/news/list";//GET
}
