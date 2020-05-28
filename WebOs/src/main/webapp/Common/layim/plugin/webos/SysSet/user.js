layui.config({
    base: '/Common/layim/layui_exts/' //指定 插件 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'form', 'layer', 'xmSelect', 'laydate', 'common', 'upload', 'strpy'], function (exports) {
    var table = layui.table, form = layui.form, layer = layui.layer, laydate = layui.laydate, common = layui.common, upload = layui.upload, xmSelect = layui.xmSelect,
        strpy = layui.strpy, $ = layui.$,
        tableId = 'tableid';
    var Role = [], kecheng = [];
    common.islogin();
    //获取用户
    table.render({
        elem: '#_User'
        , id: tableId
        , headers: { "Authorization": "bearer " + window.localStorage["_token"] }
        , toolbar: '#toolbarDemo'
        , url: '/api/User/UserList' //数据接口
        , page: true //开启分页
        , cols: [[ //表头
            //{ field: 'ID', title: 'ID', width: 80, sort: true, fixed: 'left' }
            { field: 'Name', title: '姓名' },
            { field: 'Account', title: '账号' },
            {
                field: 'State', title: '状态', sort: true, templet: '#switchTpl', unresize: true

            }
            , { fixed: 'right', title: '操作', toolbar: '#barDemo' }
        ]]
    });
    table.on('toolbar(_User)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'a_search':
                var title = $("#title").val();
                //搜索page设置为0
                table.reload(tableId, {
                    url: '/api/User/UserList?title=' + title
                    , where: {} //设定异步数据接口的额外参数
                });
                $("#title").val(title);
                break;
            case 'a_add': a_add(); break;

        };
    });
    //监听工具条
    table.on('tool(_User)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        var ids = '';   //选中的Id
        $(data).each(function (index, item) {
            ids += item.id + ',';
        });
        if (layEvent === 'del') { //删除
            deleteRole(ids, obj);
        } else if (layEvent === 'detail') {
            layer.open({
                type: 1, title: "查看",
                area: ['70%', '60%'], shade: 0,
                fixed: false, //不固定
                maxmin: true,
                content: $("#_addteacher")[0].innerHTML, success: function (layero) {


                    var m = $('input,select,textarea', $($("form")[0]));
                    $.each(m, function (index, item) {
                        $(item).prop("disabled", true);
                    });

                    //.attr("disabled",false);
                    laydate.render({ elem: '#S_Birth' });
                    init();
                    pingyin();
                    common.post("/api/Teacher/GetTeachercourse", { teacherid: data.ID }, function (res) {
                        form.val("addteacher", data);
                        $("#caozuo").hide();
                        var datajson = JSON.parse(res).data;
                        //表单赋值   
                        if (datajson.length > 0) {
                            var selects = [];
                            $.each(datajson, function (index, item) {
                                selects.push(item.value);
                            });
                     
                            var _courseID = xmSelect.render({
                                el: '#_courseID', filterable: true, radio: true,
                                clickClose: true, data: selects
                            });
                           // formSelects.value('_courseID', selects);

                        }




                        console.log(m);

                    }, function (error) { form.val("addteacher", data); layer.alert("获取课程失败！"); }, "处理中");
                    form.render(); //更新全部

                }, cancel: function () {
                    ////右上角关闭回调
                    $("#adddictionary").hide();

                }
            });
        }
        else if (layEvent === 'edit') { //编辑

            if (!data.ID) return;
            var content;
            // var index = layer.load(1);
            //从桌面打开
            var uplay = layer.open({
                type: 1, title: "编辑用户",
                area: ['70%', '60%'], shade: 0,
                fixed: false, //不固定
                maxmin: false,
                content: $("#_addusers")[0].innerHTML, success: function (layero) {
                    laydate.render({ elem: '#S_Birth' });
                    pingyin();
                    if (Role.length <= 0) {
                        common.get("/api/Users/OrganizeAndRole", {}, function (resp) {
                            resp = JSON.parse(resp);
                            console.log(resp);
                            Role = resp.data;
                            var html = "", htmlrole = "";
                            $(resp.data).each(function (i, item) {
                                if (item.type === "0") {
                                    html += "<option value='" + item.identifier + "'>" + item.Name + "</option>";
                                } else {
                                    htmlrole += "<option value='" + item.ID + "'>" + item.Name + "</option>";
                                }


                            });
                            $('#identifier').html(html);// 下拉菜单里添加元素
                            $('#OrganizeID').html(htmlrole);// 下拉菜单里添加元素
                            form.render();
                            form.val("addteacher", data);
                        }, function (error) { }, "正在获取角色");

                    } else {
                        var html = "", htmlrole = "";
                        $(Role).each(function (i, item) {
                            if (item.type === "0") {
                                html += "<option value='" + item.identifier + "'>" + item.Name + "</option>";
                            } else {
                                htmlrole += "<option value='" + item.ID + "'>" + item.Name + "</option>";
                            }


                        });
                        $('#identifier').html(html);// 下拉菜单里添加元素
                        $('#OrganizeID').html(htmlrole);// 下拉菜单里添加元素
                        form.render();
                        form.val("addteacher", data);
                    }
                    form.render(); //更新全部

                    // 实例化编辑器，隐藏上传空间。
                    uploadEditor = UE.getEditor("uploadEditor", {
                        isShow: false,
                        focus: false,
                        enableAutoSave: false,
                        autoSyncData: false,
                        autoFloatEnabled: false,
                        wordCount: false,
                        sourceEditor: null,
                        scaleEnabled: true,
                        toolbars: [["insertimage"]]
                    });
                    // 自定义按钮绑定触发多图上传和上传附件对话框事件

                    $(".j_upload_img_btn").click(function () {

                        // 监听多图上传和上传附件组件的插入动作
                        uploadEditor.ready(function () {
                            uploadEditor.addListener("beforeInsertImage", _beforeInsertImage);

                        });

                        var dialog = uploadEditor.getDialog("insertimage");
                        dialog.title = '头像上传';
                        dialog.render();
                        dialog.open();
                        dialog.oncancel = function (s, f) {
                            layer.restore(uplay);

                        };
                        layer.min(uplay);
                    });
                    // 多图上传动作
                    function _beforeInsertImage(t, result) {

                        var imageHtml = [];
                        $.each(result, function (index, item, arr) {
                            imageHtml.push(item.src);
                        });
                        if (imageHtml.length > 0) {
                            $(".as-BaiDuImg").val(imageHtml[0]);
                        }

                        var form = layui.form;
                        form.render(); //更新全部
                        layer.restore(uplay);
                    }

                }, cancel: function () {
                    ////右上角关闭回调
                    $("#adddictionary").hide();

                }
            });
        }
    });
    form.on('switch(jihuo)', function (obj) {

        console.log(obj);
        var teacherone = { ID: this.value.split('_')[0], Status: obj.elem.checked ? 1 : 0 };
        common.post('/Common/save', { tab: 'Users', data: JSON.stringify(teacherone) }, function (data) {
            var datajson = JSON.parse(data);
            if (datajson.Success) {
                layer.tips(obj.elem.checked ? "激活成功" : "冻结成功", obj.othis);
                //layer.msg(datajson.msg, {
                //    icon: 1,
                //    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                //}, function () {
                //    //do something
                //});
                // top.layer.closeAll();
                //window.Location.href="";
                //reloadTable();                
            }
            else {
                layer.msg(datajson.msg, {
                    icon: 1,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                }, function () {
                    //do something
                });
            }
        });

        //layer.tips(this.value + ' ' + this.name + '：' + obj.elem.checked, obj.othis);
    });
    form.on('submit(addusers)', function (data) {
        common.post('/api/Users/UsersSave', { data: JSON.stringify(data.field) }, function (data) {
            var datajson = JSON.parse(data);
            if (datajson.Success) {
                layer.msg(datajson.msg, {
                    icon: 1,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                }, function () {
                    //do something
                });
                layer.closeAll();
                reloadTable();

            }
            else {
                layer.msg(datajson.msg, {
                    icon: 1,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                }, function () {
                    //do something
                });
            }
        });

        return false;
    });
    //表格重载
    function reloadTable() {
        //table.reload(tableId, {});
        var title = $("#title").val();
        //搜索page设置为0
        table.reload(tableId, {
            url: '/api/User/UserList?title=' + title
            , where: {} //设定异步数据接口的额外参数
        });
        $("#title").val(title);
    }
    function a_add() {
        var uplay = layer.open({
            type: 1, title: "添加用户",
            area: ['70%', '60%'], shade: 0,
            fixed: false, //不固定
            maxmin: false,
            content: $("#_addusers")[0].innerHTML, success: function (layero) {
                // laydate.render({ elem: '#S_Birth' });
                init();
                pingyin();
                form.render(); //更新全部
                // 实例化编辑器，隐藏上传空间。
                uploadEditor = UE.getEditor("uploadEditor", {
                    isShow: false,
                    focus: false,
                    enableAutoSave: false,
                    autoSyncData: false,
                    autoFloatEnabled: false,
                    wordCount: false,
                    sourceEditor: null,
                    scaleEnabled: true,
                    toolbars: [["insertimage"]]
                });
                // 自定义按钮绑定触发多图上传和上传附件对话框事件

                $(".j_upload_img_btn").click(function () {

                    // 监听多图上传和上传附件组件的插入动作
                    uploadEditor.ready(function () {
                        uploadEditor.addListener("beforeInsertImage", _beforeInsertImage);

                    });

                    var dialog = uploadEditor.getDialog("insertimage");
                    dialog.title = '头像上传';
                    dialog.render();
                    dialog.open();
                    dialog.oncancel = function (s, f) {
                        layer.restore(uplay);

                    };
                    layer.min(uplay);
                });
                // 多图上传动作
                function _beforeInsertImage(t, result) {

                    var imageHtml = [];
                    $.each(result, function (index, item, arr) {
                        imageHtml.push(item.src);
                    });
                    if (imageHtml.length > 0) {
                        $(".as-BaiDuImg").val(imageHtml[0]);
                    }

                    var form = layui.form;
                    form.render(); //更新全部
                    layer.restore(uplay);
                }
            }, cancel: function () {


            }
        });
    }
    function pingyin() {
        $("#Name").blur(function () {
            var _this = $(this);
            var chinaName = _this.val();
            chinaName = strpy(chinaName, "1", 1);
            //给两个文本框赋值    
            $('#Account').val(chinaName);

        });
    }
    function init() {
        //判断角色是否已经加载，没有加载请求服务器否则直接赋值
        if (Role.length <= 0) {
            common.get("/api/Users/OrganizeAndRole", {}, function (resp) {
                resp = JSON.parse(resp);
                console.log(resp);
                Role = resp.data;
                var html = "", htmlrole = "";
                $(resp.data).each(function (i, item) {
                    if (item.type === "0") {
                        html += "<option value='" + item.identifier + "'>" + item.Name + "</option>";
                    } else {
                        htmlrole += "<option value='" + item.ID + "'>" + item.Name + "</option>";
                    }


                });
                $('#identifier').html(html);// 下拉菜单里添加元素
                $('#OrganizeID').html(htmlrole);// 下拉菜单里添加元素
                form.render();

            }, function (error) { }, "正在获取角色");

        } else {
            var html = "", htmlrole = "";
            $(Role).each(function (i, item) {
                if (item.type === "0") {
                    html += "<option value='" + item.identifier + "'>" + item.Name + "</option>";
                } else {
                    htmlrole += "<option value='" + item.ID + "'>" + item.Name + "</option>";
                }


            });
            $('#identifier').html(html);// 下拉菜单里添加元素
            $('#OrganizeID').html(htmlrole);// 下拉菜单里添加元素

            form.render();
        }

        return true;
    }



});