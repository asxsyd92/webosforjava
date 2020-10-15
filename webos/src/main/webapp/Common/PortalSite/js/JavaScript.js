﻿window.layui && layui.use(["element", "layer", "form", "util", "flow", "layedit"],
    function () {
        function c() {
            n(".category-toggle").addClass("layui-hide");
            n(".article-category").unbind("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend");
            n(".article-category").removeClass("categoryOut");
            n(".article-category").addClass("categoryIn");
            n(".article-category").addClass("layui-show")
        }
        function o() {
            n(".article-category").on("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend",
                function () {
                    n(".article-category").removeClass("layui-show");
                    n(".category-toggle").removeClass("layui-hide")
                });
            n(".article-category").removeClass("categoryIn");
            n(".article-category").addClass("categoryOut")
        }
        function l() {
            n(".blog-mask").unbind("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend");
            n(".blog-nav-left").unbind("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend");
            n(".blog-mask").removeClass("maskOut");
            n(".blog-mask").addClass("maskIn");
            n(".blog-mask").removeClass("layui-hide");
            n(".blog-mask").addClass("layui-show");
            n(".blog-nav-left").removeClass("leftOut");
            n(".blog-nav-left").addClass("leftIn");
            n(".blog-nav-left").removeClass("layui-hide");
            n(".blog-nav-left").addClass("layui-show")
        }
        function s() {
            n(".blog-mask").on("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend",
                function () {
                    n(".blog-mask").addClass("layui-hide")
                });
            n(".blog-nav-left").on("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend",
                function () {
                    n(".blog-nav-left").addClass("layui-hide")
                });
            n(".blog-mask").removeClass("maskIn");
            n(".blog-mask").addClass("maskOut");
            n(".blog-mask").removeClass("layui-show");
            n(".blog-nav-left").removeClass("leftIn");
            n(".blog-nav-left").addClass("leftOut");
            n(".blog-nav-left").removeClass("layui-show")
        }
        var a = layui.element,
            r = layui.form,
            u = layui.util,
            v = layui.flow,
            n = layui.jquery,
            i = layui.layedit,
            f = layui.device(),
            e = i.build("remarkEditor", {
                height: 150,
                tool: ["face", "|", "left", "center", "right", "|", "link"]
            }),
            t = {
                openBlogHelper: function () {
                    var i, r;
                    f.android || f.ios ? (i = ["100vw", "100vh"], r = 0) : (i = ["375px", "667px"], r = .8);
                    layer.open({
                        type: 2,
                        title: !1,
                        closeBtn: 0,
                        area: i,
                        shade: r,
                        scrollbar: !1,
                        isOutAnim: !1,
                        anim: -1,
                        resize: !1,
                        move: !1,
                        shadeClose: !0,
                        skin: "blogzone animated flipInY",
                        content: "/user/zone",
                        success: function (i, r) {
                            t.closeBlogHelper = function () {
                                n(i).removeClass("flipInY").addClass("flipOutY");
                                setTimeout(function () {
                                    layer.close(r)
                                },
                                    500)
                            }
                        }
                    })
                },
                events: {
                    commentReply: function () {
                        var e = n(this).data("id"),
                            o = n(this).data("targetid"),
                            s = n(this).data("targetname"),
                            r = n(this).parents(".comment-item"),
                            f;
                        n("#commentReplyEritor").remove();
                        n(".comment-item").find(".btnDiv,.layui-layedit").remove();
                        r.append('<textarea id="commentReplyEritor" style="display: none;"><\/textarea><div class="btnDiv" style="margin-left:-60px;padding:10px;"><button style="border-radius:0;" class="layui-btn layui-btn-xs layui-btn-normal">确定<\/button><\/div>');
                        f = i.build("commentReplyEritor", {
                            height: 80,
                            tool: ["face", "|", "link"]
                        });
                        n(".comment-item .layui-layedit-tool").append('<span style="float: right;margin-right:5px;margin-top: 3px;font-size: 12px;color: #ff5722;">@' + s + "<\/span>");
                        n(".comment-item").find(".btnDiv,.layui-layedit").click(function (n) {
                            layui.stope(n)
                        });
                        n("#commentReplyEritor").siblings(".btnDiv").find(".layui-btn").on("click",
                            function () {
                                var s = i.getContent(f),
                                    h;
                                if (s == "" || new RegExp("^[ ]+$").test(s)) {
                                    layer.msg("至少得有一个字吧", {
                                        shift: 6
                                    });
                                    return
                                }
                                h = layer.load(1);
                                n.ajax({
                                    type: "post",
                                    url: "/api/comment/reply",
                                    data: {
                                        remarkId: e,
                                        targetUserId: o,
                                        replyContent: s
                                    },
                                    success: function (i) {
                                        var f, e;
                                        if (layer.close(h), i.code === 1) {
                                            layer.msg("回复成功", {
                                                icon: 6
                                            });
                                            f = i.data;
                                            n("#commentReplyEritor").remove();
                                            n(".comment-item").find(".btnDiv,.layui-layedit").remove();
                                            e = '<div class="comment-reply"><div class="comment-item-left"><div class="useravator" title="{UserName}"><img src="{UserAvatar}" alt="{UserName}"><\/div><\/div><div class="comment-item-right"><div class="content"><span style="color:#01aaed;margin-right:5px">{UserName}：<\/span><span style="color:#ff6a00;margin-right:5px">@{TargetUserName}<\/span>{Content}<\/div><p class="createtime">{CreateTime}<a href="javascript:;" style="margin-left:5px;color:#0094ff;vertical-align:middle;display:none" blog-event="commentReply" data-id="{Id}" data-targetid="{UserId}" data-targetname="{UserName}">回复<\/a><\/p><\/div><\/div>'.replace(/{UserName}/g, f.user.name).replace(/{UserId}/g, f.user.id).replace(/{UserAvatar}/g, f.user.avatar).replace(/{TargetUserName}/g, f.targetUser.name).replace(/{Id}/g, f.commentId).replace(/{Content}/g, f.content).replace(/{CreateTime}/g, u.timeAgo(f.createTime, !1));
                                            r.append(e);
                                            r.find(".comment-reply:last").on("mouseover",
                                                function () {
                                                    n(this).find(".createtime a").show()
                                                }).on("mouseout",
                                                    function () {
                                                        n(this).find(".createtime a").hide()
                                                    });
                                            r.find(".comment-reply:last").find("*[blog-event]").on("click",
                                                function () {
                                                    var i = n(this).attr("blog-event");
                                                    typeof t.events[i] == "function" && t.events[i].call(this)
                                                });
                                            r.find(".comment-reply:last").find(".createtime a").click(function (n) {
                                                layui.stope(n)
                                            })
                                        } else layer.msg(i.msg, {
                                            shift: 6,
                                            icon: 5
                                        })
                                    },
                                    error: function () {
                                        layer.close(h);
                                        layer.msg("程序出错了", {
                                            shift: 6,
                                            icon: 5
                                        })
                                    }
                                })
                            })
                    }
                }
            },
            h;
        window.blog = t;
        n("*[blog-event]").on("click",
            function () {
                var i = n(this).attr("blog-event");
                typeof t.events[i] == "function" && t.events[i].call(this)
            });
        layui.cache.user ? (u.fixbar({
            bar1: "&#xe611;",
            click: function (n) {
                n === "bar1" && t.openBlogHelper()
            }
        }), n.get("/api/user/getunreadmsgcnt",
            function (i) {
                if (i.code === 1 && i.data !== 0) {
                    var u = n(".blog-user"),
                        r;
                    f.android || f.ios ? (r = n('<span style="margin-left:8px;cursor:pointer;" class="blog-msg layui-badge">' + i.data + "<\/span>"), u.append(r)) : (r = n('<span style="margin-right:8px;cursor:pointer;" class="blog-msg layui-badge">' + i.data + "<\/span>"), u.prepend(r));
                    r.on("click", t.openBlogHelper);
                    layer.tips("你有 " + i.data + " 条未读消息", r, {
                        tips: 3,
                        tipsMore: !0,
                        fixed: !0
                    });
                    r.on("mouseenter",
                        function () {
                            layer.closeAll("tips")
                        })
                }
            })) : u.fixbar({});
        n(function () {
            / msie[6 | 7 | 8 | 9] / i.test(navigator.userAgent) || (window.sr = new ScrollReveal({
                reset: !1
            }), sr.reveal(".sr-left", {
                origin: "left",
                scale: 1,
                opacity: .1,
                duration: 1200
            }), sr.reveal(".sr-bottom", {
                scale: 1,
                opacity: .1,
                distance: "60px",
                duration: 1e3
            }), sr.reveal(".sr-listshow", {
                distance: "30px",
                duration: 1e3,
                scale: 1,
                opacity: .1
            }), sr.reveal(".sr-rightmodule", {
                origin: "top",
                distance: "60px",
                duration: 1e3,
                scale: 1,
                opacity: .1
            }));
            layer.photos({
                photos: ".article-left",
                anim: 5
            });
            layer.photos({
                photos: ".article-detail-content",
                anim: 5,
                move: !1
            })
        });
        r.on("submit(formRemark)",
            function (t) {
                if (n(t.elem).hasClass("layui-btn-disabled")) return !1;
                var i = layer.load(1);
                return n.ajax({
                    type: "post",
                    url: "/api/article/remark",
                    data: t.field,
                    success: function (n) {
                        layer.close(i);
                        n.code === 1 ? (layer.msg(n.msg, {
                            icon: 6
                        }), location.reload(!0)) : n.msg != undefined ? layer.msg(n.msg, {
                            icon: 5
                        }) : layer.msg("程序异常，请重试或联系作者", {
                            icon: 5
                        })
                    },
                    error: function (n) {
                        layer.close(i);
                        n.status === 401 ? layer.msg("请先登录", {
                            shift: 6,
                            icon: 5
                        }) : layer.msg("请求异常", {
                            shift: 6,
                            icon: 2
                        })
                    }
                }),
                    !1
            });
        r.on("submit(formComment)",
            function (r) {
                var f = layer.load(1);
                return n.ajax({
                    type: "post",
                    url: "/api/comment/add",
                    data: r.field,
                    success: function (r) {
                        if (layer.close(f), r.code === 1) {
                            layer.msg(r.msg, {
                                icon: 6
                            });
                            var o = r.data,
                                s = '<div class="layui-col-md12"><div class="comment-item sr-bottom animated slideInDown"><div class="comment-item-left"><div class="useravator" title="{UserName}"><img src="{UserAvatar}" alt="{UserName}"><\/div><div class="reply"><button class="layui-btn layui-btn-xs layui-btn-primary" blog-event="commentReply" data-id="{Id}" data-targetid="{UserId}" data-targetname="{UserName}">回复<\/button><\/div><\/div><div class="comment-item-right"><div class="content">{Content}<\/div><p class="createtime">— 来自<span style="padding:0 3px;padding-right:10px;color:#0094ff">{UserName}<\/span>{CreateTime}<\/p><\/div><\/div><\/div>'.replace(/{UserName}/g, o.user.name).replace(/{UserId}/g, o.user.id).replace(/{UserAvatar}/g, o.user.avatar).replace(/{Id}/g, o.id).replace(/{Content}/g, o.content).replace(/{CreateTime}/g, u.timeAgo(o.createTime, !1));
                            n(".commentlist").prepend(s);
                            n("#remarkEditor").val("");
                            n(".blog-editor .layui-layedit").remove();
                            e = i.build("remarkEditor", {
                                height: 150,
                                tool: ["face", "|", "left", "center", "right", "|", "link"]
                            });
                            n(".commentlist").find(".comment-item:first").on("mouseover",
                                function () {
                                    n(this).find(".reply").addClass("layui-show")
                                }).on("mouseout",
                                    function () {
                                        n(this).find(".reply").removeClass("layui-show")
                                    });
                            n(".commentlist").find(".comment-item:first").find("*[blog-event]").on("click",
                                function () {
                                    var i = n(this).attr("blog-event");
                                    typeof t.events[i] == "function" && t.events[i].call(this)
                                });
                            n(".commentlist").find(".comment-item:first").find(".reply").click(function (n) {
                                layui.stope(n)
                            })
                        } else r.msg != undefined ? layer.msg(r.msg, {
                            icon: 5
                        }) : layer.msg("程序异常，请重试或联系作者", {
                            icon: 5
                        })
                    },
                    error: function (n) {
                        layer.close(f);
                        n.status === 401 ? layer.msg("请先登录", {
                            shift: 6,
                            icon: 5
                        }) : layer.msg("请求异常", {
                            shift: 6,
                            icon: 2
                        })
                    }
                }),
                    !1
            });
        r.on("submit(formSearch)",
            function (n) {
                return location.href = "/Article/Search?keywords=" + escape(n.field.keywords),
                    !1
            });
        r.verify({
            content: function (t) {
                if (t = n.trim(i.getText(e)), t == "") return "自少得有一个字吧";
                i.sync(e)
            },
            replyContent: function (n) {
                if (n == "") return "自少得有一个字吧"
            }
        });
        n(".remark-user-avator").on("mouseover",
            function () {
                h = layer.tips("来自【" + n(this).data("name") + "】的评论", this, {
                    tips: 4,
                    time: 1e3
                })
            });
        n(".blog-user img").hover(function () {
            var n = layer.tips("点击退出", ".blog-user img", {
                tips: [3, "#009688"]
            })
        },
            function () {
                layer.closeAll("tips")
            });
        n(".blog-navicon").click(function () {
            var t = new RegExp("layui-hide");
            t.test(n(".blog-nav-left").attr("class")) ? l() : s()
        });
        n(".blog-mask").click(function () {
            s()
        });
    
        n(".category-toggle").click(function (n) {
            n.stopPropagation();
            c()
        });
     
        n(".article-category > a").click(function (n) {
            n.stopPropagation()
        });
        layui.blog = t
    });
Date.prototype.Format = function (n) {
    var t = n;
    return t = t.replace(/yyyy|YYYY/, this.getFullYear()),
        t = t.replace(/yy|YY/, this.getYear() % 100 > 9 ? (this.getYear() % 100).toString() : "0" + this.getYear() % 100),
        t = t.replace(/MM/, this.getMonth() + 1 > 9 ? (this.getMonth() + 1).toString() : "0" + (this.getMonth() + 1)),
        t = t.replace(/M/g, this.getMonth() + 1),
        t = t.replace(/w|W/g, ["日", "一", "二", "三", "四", "五", "六"][this.getDay()]),
        t = t.replace(/dd|DD/, this.getDate() > 9 ? this.getDate().toString() : "0" + this.getDate()),
        t = t.replace(/d|D/g, this.getDate()),
        t = t.replace(/hh|HH/, this.getHours() > 9 ? this.getHours().toString() : "0" + this.getHours()),
        t = t.replace(/h|H/g, this.getHours()),
        t = t.replace(/mm/, this.getMinutes() > 9 ? this.getMinutes().toString() : "0" + this.getMinutes()),
        t = t.replace(/m/g, this.getMinutes()),
        t = t.replace(/ss|SS/, this.getSeconds() > 9 ? this.getSeconds().toString() : "0" + this.getSeconds()),
        t.replace(/s|S/g, this.getSeconds())
};