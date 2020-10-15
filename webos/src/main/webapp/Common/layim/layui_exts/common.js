/**

 @Name：Asxsyd92Router layui路由扩展v0.01
 @Author：爱上歆随懿恫|http://www.asxsyd92.com
 @Url：http://www.asxsyd92.com
 @License：MIT
*/

layui.define(['jquery'], function (exports) {
    var $ = layui.jquery;
    var util = {
        //获取路由的路径和详细参数
        getParamsUrl: function () {
            var hashDeatail = location.hash.split("?"),
                hashName = hashDeatail[0].split("#")[1],//路由地址
                params = hashDeatail[1] ? hashDeatail[1].split("&") : [],//参数内容
                query = {};
            for (var i = 0; i < params.length; i++) {
                var item = params[i].split("=");
                query[item[0]] = item[1]
            }
            return {
                path: hashName,
                query: query
            }
        },
        getRequest: function () {
            var url = window.location.search; //获取url中"?"符后的字串
            var theRequest = new Object();
            if (url.indexOf("?") != -1) {
                var str = url.substr(1);
                strs = str.split("&");
                for (var i = 0; i < strs.length; i++) {
                    //就是这句的问题
                    theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
                }
            }
            return theRequest;
        },
        getRequestNg: function () {
            var url = window.location.hash; //获取url中"?"符后的字串
            var theRequest = new Object();

            if (url.indexOf("?") != -1) {
                url = "?" + url.split("?")[1];
                var str = url.substr(1);
                strs = str.split("&");
                for (var i = 0; i < strs.length; i++) {
                    //就是这句的问题
                    theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
                }
            }

            return theRequest;
        },
        //生成从minNum到maxNum的随机数
        randomNum: function(start, stop) {
            return parseInt(Math.random() * (stop - start) + start);
        },
        //在json中填加该字段是会被系统替换
        SysOperation: {
            _SYS_DATETIME: "_SYS_DATETIME",//填加系统时间
            _SYS_GETUSERID: "_SYS_GETUSERID",//当前用户ID
            _SYS_GETUSERNAME: "_SYS_GETUSERNAME",//当前用户名
            _SYS_ORGID: "_SYS_ORGID",
            _SYS_ORGNAME:"_SYS_ORGNAME"
        },
        islogin: function () {
            if (window.localStorage["userid"] === null || window.localStorage["userid"] === undefined) {
                top.window.location.href = "/weboslogin.html";
            }
            //初始化表单
            $.ajaxSetup({
                headers: { "Authorization": "bearer " + window.localStorage["_token"] }
            });
        },   
        post: function (reqUrl, jsonData, callback, error, msg) {
            if (msg == null || msg == "" || msg == undefined) { msg = "正在加载...";}
            var lay = top.layer.msg(msg, { icon: 16, shade: 0.5, time: 20000000 });
            $.ajax({
                method: "post",
                data: jsonData,
                url: reqUrl,
                headers: { "Authorization": "bearer " + window.localStorage["_token"] },
                success: function (data) {
                    top.layer.close(lay);
                    callback(data);
                }, error: function (ex) {
                    if (ex.status==403){
                        localStorage.clear();
                        sessionStorage.clear();
                        top.window.location='weboslogin.html';
                    }
                    try{
                        top.layer.close(lay);
                        error(ex);
                    }catch (e) {

                    }

                }
            });
        }, get: function (reqUrl, jsonData, callback, error, msg ) {
            if (msg == null || msg == "" || msg == undefined) { msg = "正在加载..."; }
            var lay = top.layer.msg(msg, { icon: 16, shade: 0.5, time: 20000000 });
            $.ajax({
                method: "get",
                data: jsonData,
                url: reqUrl,
                headers: { "Authorization": "bearer " + window.localStorage["_token"] },
                success: function (data) {
                    top.layer.close(lay);

                    callback(data);


                }, error: function (ex) {
                    if (ex.status==403){
                        localStorage.clear();
                        sessionStorage.clear();
                        top.window.location='weboslogin.html';
                    }

                    try{
                        top.layer.close(lay);
                        error(ex);
                    }catch (e) {

                    }
                }
            });
        }
        /**************************************时间格式化处理************************************/
        , dateFormat: function (fmt, date) { //author: meizz 
            var o = {
                "M+": date.getMonth() + 1,     //月份 
                "d+": date.getDate(),     //日 
                "h+": date.getHours(),     //小时 
                "m+": date.getMinutes(),     //分 
                "s+": date.getSeconds(),     //秒 
                "q+": Math.floor((date.getMonth() + 3) / 3), //季度 
                "S": date.getMilliseconds()    //毫秒 
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt))
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
    }

    exports('common', util);

});