layui.config({
    base: '/Common/layim/layui_exts/' //指定 插件 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'form', 'layer','common'], function (exports) {
    var $ = layui.jquery, form = layui.form; layer = layui.layer, layer = layui.layer, $ = layui.$, common = layui.common;
    common.islogin();
    form.on('submit(_submit)', function (data) {
        if (data.field.newpw !== data.field.pw) {
            //tips层-左
            layer.tips('两次密码不一致', '#pw', {
                tips: [1, '#FF5722']
            });
            return false;
        }

        $.post("/api/User/EditPw", { old: data.field.oldpw, pw: data.field.pw }, function (resp) {
            var res = JSON.parse(resp);
            if (res.Success === true) {
                
                top.layer.closeAll();
                layer.alert("操作成功");
            } else {
                layer.msg(res.msg);
                return false;
            }
        });






        return false;
    });
});