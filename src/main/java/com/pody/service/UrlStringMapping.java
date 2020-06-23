package com.pody.service;

public class UrlStringMapping {
    //sign up and login
    public static final String URL0001 = "/api/login";//POST
    public static final String URL0002 = "/api/signup/email";//POST
    public static final String URL0003 = "/api/signup/phone";//POST
    //user urls
    public static final String URL0010 = "/api/user/read";//Post
    public static final String URL0011 = "/api/user/update/{id}";//PUT
    public static final String URL0012 = "/api/user/delete/{id}";//DELETE
    public static final String URL0013 = "/api/user/premium/{id}";//GET
    public static final String URL0014 = "/api/user/{id}/image";//POST
    public static final String URL0015 = "/api/user/{id}/channelImage";//POST
    public static final String URL0016 = "/api/user/check/email";//POST
    public static final String URL0017 = "/api/user/check/username";//POST
    public static final String URL0018 = "/api/user/resetPassword";//POST
    public static final String URL0019 = "/api/user/find/resetPassword";//POST
    public static final String URL0020 = "/api/user/list/channels/{till}/{to}";//Post
    public static final String URL0021 = "/api/user/update/isChannel";//Put
    public static final String URL0022 = "/api/user/list/subscriptions/{id}";//Get
    public static final String URL0023 = "/api/user/list/subscriptions/sidenav/{id}";//Get
    public static final String URL0024 = "/api/user/follow/check";//Post
    //search urls
    public static final String URL0100 = "/api/search";//POST
    //podcast urls
    public static final String URL0040 = "/api/podcast/create";//POST
    public static final String URL0041 = "/api/podcast/read/{id}";//GET
    public static final String URL0042 = "/api/podcast/update/{id}";//PUT
    public static final String URL0043 = "/api/podcast/delete/{id}";//DELETE
    public static final String URL0044 = "/api/podcast/{id}/upload";//POST
    public static final String URL0045 = "/api/podcast/view";//POST
    public static final String URL0046 = "/api/podcast/like";//POST
    public static final String URL0047 = "/api/podcast/dislike";//POST
    public static final String URL0048 = "/api/podcast/like/check";//POST
    public static final String URL0049 = "/api/podcast/list/like/{id}/user/{till}/{to}";//GET
    public static final String URL0050 = "/api/podcast/list/history/{id}/user/{till}/{to}";//GET
    public static final String URL0051 = "/api/podcast/list/{id}/user/{till}/{to}";//GET
    public static final String URL0052 = "/api/podcast/list/like/{till}/{to}";//GET لیست محبوب ترین ها
    public static final String URL0053 = "/api/podcast/list/view/{till}/{to}";//GET لیست پر بازدید ترین ها
    public static final String URL0054 = "/api/podcast/list/suggested/{till}/{to}";//GET لیست پیشنهادی
    public static final String URL0055 = "/api/podcast/list/new/{till}/{to}";//GET لیست جدیدترین
    public static final String URL0056 = "/api/podcast/listenLater";//POST
    public static final String URL0057 = "/api/podcast/list/listenLater/{till}/{to}";//POST
    public static final String URL0058 = "/api/podcast/listenLater/check";//POST
    public static final String URL0059 = "/api/podcast/listenLater/delete";//DELETE
    public static final String URL0060 = "";
    public static final String URL0061 = "";
    public static final String URL0062 = "/api/podcast/rss";//POST
    public static final String URL0063 = "/api/podcast/list/trending";//GET
    public static final String URL0064 = "/api/podcast/list/home";//Post
    public static final String URL0065 = "/api/admin/podcast/rss";//Post
    public static final String URL0066 = "/api/podcast/list/followings/{till}/{to}";//Post لیست پادکست پادکسترهای دنبال شده
    public static final String URL0067 = "/api/podcast/list/home/mobile";//Post
    public static final String URL0068 = "/api/podcast/list/home/mobile/infinite/{till}/{to}";//Post

    //category urls
    public static final String URL0110 = "/api/admin/category/create";//POST
    public static final String URL0111 = "/api/admin/category/update/{id}";//PUT
    public static final String URL0112 = "/api/admin/category/delete/{id}";//DELETE
    public static final String URL0113 = "/api/category/tree";//GET
    public static final String URL0114 = "/api/category/list/parents";//GET
    public static final String URL0115 = "/api/category/list/{id}/children";//GET
    public static final String URL0116 = "/api/category/read";//Post
    public static final String URL0117 = "/api/category/list/page";//Post
    public static final String URL0118 = "/api/category/read/infinite/{till}/{to}";//Post
    public static final String URL0119 = "/api/category/info";//Post
    //comment urls
    public static final String URL0140 = "/api/comment/create";//POST
    public static final String URL0141 = "/api/comment/update/{id}";//PUT
    public static final String URL0142 = "/api/admin/comment/delete/{id}";//DELETE
    public static final String URL0143 = "/api/comment/list/{id}/podcast";//POST
    public static final String URL0144 = "/api/comment/like/{id}";//GET
    public static final String URL0145 = "/api/admin/comment/approve/{id}";//GET
    //follow urls
    public static final String URL0160 = "/api/follow/user/create";//POST
    public static final String URL0161 = "/api/follow/user/delete";//DELETE
    public static final String URL0162 = "/api/follow/category/create";//POST
    public static final String URL0163 = "/api/follow/category/delete";//DELETE
    public static final String URL0164 = "/api/follow/hashtag/create";//POST
    public static final String URL0165 = "/api/follow/hashtag/delete";//DELETE
    public static final String URL0166 = "/api/following/{id}/user";//POST
    public static final String URL0167 = "/api/followers/{id}/user";//POST
    //hashtag urls
    public static final String URL0180 = "/api/hashtag/create";//POST
    public static final String URL0181 = "/api/admin/hashtag/delete/{id}";//DELETE

    //news urls
    public static final String URL0200 = "/api/news/create";//POST
    public static final String URL0201 = "/api/news/update/{id}";//PUT
    public static final String URL0202 = "/api/news/delete/{id}";//DELETE
    public static final String URL0203 = "/api/news/list/homePage";//GET
    public static final String URL0204 = "/api/news/list";//GET
}
