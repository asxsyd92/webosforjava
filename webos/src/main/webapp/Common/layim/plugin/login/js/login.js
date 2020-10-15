layui.use(['layer', 'element', 'jquery'], function () {
    layer = layui.layer; $ = layui.$;
    if (window.localStorage["_token"] !== undefined && window.localStorage["_token"] !== null && window.localStorage["_token"] !== "")
    {
        window.location.href = "/webos.html";
        return false;
    }
 
    $(".submit").click(function () {
        var user = $("#user").val();
        var password = $("#password").val();
        if (user.length <= 0) {
            layer.tips('用户名不能为空', '#user');
            return false;
        }
        if (password.length <= 0) {
            layer.tips('用户名不能为空', '#password');
            return false;
        }
        var lay = layer.msg('正在登陆...', { icon: 16, shade: 0.5, time: 20000000 });
        $.post("/api/ApiLogin/Login", { user: user, pw: password }, function (data) {

            layer.close(lay);
            if (data.success) {
      
                window.localStorage["user"] = data.name;
                window.localStorage["userid"] = data.userid;
                window.localStorage["orname"] = data.orname;
                window.localStorage["orid"] = data.orid;
                window.localStorage["_token"] = data.access_token;
                window.localStorage["account"] = data.account; 
                window.localStorage["picture"] = data.picture;
                window.location.href = "/webos.html";
                //  show_msg('登录成功咯！  正在为您跳转...', "/index.html");
            } else {
                layer.alert(data.msg);
                // show_err_msg(data.msg);
                //  asxsyd92.createCode();
            }
        }).error(function (er) {
            console.log(er);
            layer.alert(er.statusText, { title: "温馨提示：发生异常" });
        });
    });
});