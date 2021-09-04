import Vue from 'vue'
import Router from 'vue-router'
import helloWorld from "../components/helloWorld";
import logIn from "../components/logIn";
import register from "../components/register";
import model from "../components/user/model";
import home from "../components/user/home/home";
import mine from "../components/user/mine/mine";
import wikipedia from "../components/user/wikipedia/wikipedia";
import BBS from "../components/user/BBS/BBS";
import addArticle from "../components/user/BBS/components/article/components/addArticle";
import blogFiles from "../components/user/mine/components/blogFiles/blogFiles";
import friends from "../components/user/mine/components/friends/friends";
import information from "../components/user/mine/components/information/information";
import message from "../components/user/mine/components/message/message";
import questionFiles from "../components/user/mine/components/questionFiles/questionFiles";
import collections from "../components/user/mine/components/collections/collections";
import favorite from "../components/user/mine/components/collections/components/favorite";
import rank from "../components/user/rank/rank";
import adminLogIn from "../components/admin/adminLogIn";
import admin from "../components/admin/admin";
import article from "../components/user/BBS/components/article/article";
import blog from "../components/user/BBS/components/blog/blog";
import addBlog from "../components/user/BBS/components/blog/components/addBlog";
import blogContent from "../components/user/content/blogComponents/blogContent";
import articleContent from "../components/user/content/articleComponents/articleContent";
import adminArticle from "../components/admin/components/adminArticleFunctions/adminArticle";
import adminBlog from "../components/admin/components/adminBlogFunctions/adminBlog";
import adminArticleContent from "../components/admin/components/adminArticleFunctions/components/adminArticleContent";
import adminBlogContent from "../components/admin/components/adminBlogFunctions/components/adminBlogContent";
import following from "../components/user/mine/components/friends/components/following";
import followed from "../components/user/mine/components/friends/components/followed";
Vue.use(Router)

export default new Router({
    mode: "history",
    routes: [{
            path: '/',
            name: 'helloWorld',
            component: helloWorld,
        }, {
            path: '/helloWorld/logIn',
            name: 'logIn',
            component: logIn
        },
        {
            path: '/helloWorld/register',
            name: 'register',
            component: register
        },
        {
            path: '/helloWorld/admin/logIn',
            name: 'adminLogIn',
            component: adminLogIn
        },
        {
            path: '/helloWorld/admin',
            name: 'admin',
            component: admin,

        }, {
            path: '/helloWorld/admin/adminBlog',
            name: 'adminBlog',
            component: adminBlog,
            children: [{
                path: '/helloWorld/admin/adminBlog/adminBlogContent',
                name: 'adminBlogContent',
                component: adminBlogContent,
            }]
        }, {
            path: '/helloWorld/admin/adminArticle',
            name: 'adminArticle',
            component: adminArticle,
            children: [{
                path: '/helloWorld/admin/adminArticle/adminArticleContent',
                name: 'adminArticleContent',
                component: adminArticleContent,
            }]
        },
        {
            path: '/helloWorld/model',
            name: 'model',
            component: model,
            children: [{
                path: '/helloWorld/home',
                name: 'home',
                component: home
            }, {
                path: '/helloWorld/BBS',
                name: 'BBS',
                component: BBS,
                children: [{
                    path: '/helloWorld/BBS/question',
                    name: 'article',
                    component: article
                }, {
                    path: '/helloWorld/BBS/blog',
                    name: 'blog',
                    component: blog
                }, {
                    path: '/helloWorld/BBS/article/addArticle',
                    name: 'addArticle',
                    component: addArticle
                }, {
                    path: '/helloWorld/BBS/blog/addBlog',
                    name: 'addBlog',
                    component: addBlog
                }]
            }, {
                path: '/helloWorld/BBS/articleContent',
                name: 'articleContent',
                component: articleContent
            }, {
                path: '/helloWorld/BBS/blogContent',
                name: 'blogContent',
                component: blogContent
            }, {
                path: '/helloWorld/mine',
                name: 'mine',
                component: mine,
                children: [{
                    path: '/helloWorld/mine/blogFiles',
                    name: 'blogFiles',
                    component: blogFiles
                }, {
                    path: '/helloWorld/mine/friends',
                    name: 'friends',
                    component: friends,
                    children: [{
                        path: '/helloWorld/mine/friends/following',
                        name: 'following',
                        component: following
                    }, {
                        path: '/helloWorld/mine/friends/followed',
                        name: 'followed',
                        component: followed
                    }]
                }, {
                    path: '/helloWorld/mine/information',
                    name: 'information',
                    component: information
                }, {
                    path: '/helloWorld/mine/message',
                    name: 'message',
                    component: message
                }, {
                    path: '/helloWorld/mine/questionFiles',
                    name: 'questionFiles',
                    component: questionFiles
                }, {
                    path: '/helloWorld/mine/collections',
                    name: 'collections',
                    component: collections
                }, {
                    path: '/helloWorld/mine/collections/favorite',
                    name: 'favorite',
                    component: favorite
                }]
            }, {
                path: '/helloWorld/wikipedia',
                name: 'wikipedia',
                component: wikipedia
            }, {
                path: '/helloWorld/rank',
                name: 'rank',
                component: rank
            }]
        }
    ]
})