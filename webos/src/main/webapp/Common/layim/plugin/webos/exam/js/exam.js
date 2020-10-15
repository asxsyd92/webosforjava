layui.config({
    base: '/Common/layim/layui_exts/' //指定  路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'form', 'common'], function (exports) {

    var T = "";
    var table = layui.table, form = layui.form,
        $ = layui.$, common = layui.common,
        tableId = 'tableid'; common.islogin();
    var listtask = "";
    //表格渲染
    table.render({
        height: 'full',
        id: tableId,
        elem: '#role',
        url: '/api/Question/GetCommonList?title=&type=' + T + '&desc=AddTime desc',
        headers: { "Authorization": "bearer " + window.localStorage["_token"] },

        //size: '',   //表格尺寸，可选值sm lg
        //skin: '',   //边框风格，可选值line row nob
        //even:true,  //隔行变色
        page: true,
        limits: [8, 16, 24, 32, 40, 48, 56], toolbar: '#toolbarDemo',
        limit: 15,
        cols: [[
            { fixed: 'right', title: '操作', toolbar: '#barDemo' }
            , { field: 'Question', title: '题目' }
            , { field: 'ParentName', title: '分类标识' }
            , { field: 'Addtime', title: '时间' }


        ]], done: function () {

        }
    });
    //监听工具条
    table.on('tool(roletable)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值
        if (layEvent === 'del') { //删除
            deleteRole(data, obj);
        }
        else if (layEvent === 'edit') { //编辑
            console.log(data);
            if (!data.ID) return;
            var content;
            var table = common.getRequest().table;
            window.location.href = '/webos/page/from/Debug/' + table + '.html?key=' + data.ID;
        }
    });
    table.on('toolbar(roletable)', function (obj) {
        switch (obj.event) {
            case 'a_search':
                var title = $("#title").val();
                var tp = $("#selectqs").val();
                //搜索page设置为0
                table.reload(tableId, {
                    url: '/api/Question/GetCommonList?title=' + title + "&type=" + tp + "&desc=AddTime desc"
                    , where: {} //设定异步数据接口的额外参数
                });
                $("#title").val(title);
                setTimeout(function () {
                    $("#selectqs").val(tp);
                }, 2000);

                break;
            case "a_daoru":
                var uplay = layer.open({
                    type: 1, title: "导入数据",
                    area: ['50%', '25%'], shade: 0,
                    fixed: false, //不固定
                    maxmin: false,
                    content: $("#_addusers")[0].innerHTML, success: function (layero) {
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
                            toolbars: [["attachment"]]
                        });
                        // 自定义按钮绑定触发多图上传和上传附件对话框事件

                        $(".j_upload_img_btn").click(function () {

                            // 监听多图上传和上传附件组件的插入动作
                            uploadEditor.ready(function () {
                                uploadEditor.addListener("afterUpfile", _afterUpfile);
                  
                            });

                            var dialog = uploadEditor.getDialog("attachment");
                            dialog.title = '文件上传';
                            dialog.render();
                            dialog.open();
                            dialog.oncancel = function (s, f) {
                                layer.restore(uplay);

                            };
                            layer.min(uplay);
                        });
                        //// 附件上传
                        function _afterUpfile(t, result) {
                            var fileHtml = [];
                            $.each(result, function (index, item, arr) {
                                fileHtml.push(item.url);
                            });
                            $(".as-BaiDuFile").val(fileHtml.join(","));
                            var form = layui.form;
                            form.render(); //更新全部


                            // document.getElementById('upload_file_wrap').innerHTML = fileHtml;
                        }

                    }, cancel: function () {
                        ////右上角关闭回调
                        $("#adddictionary").hide();

                    }
                });
                break;
            case 'a_map':
                var checkStatus = table.checkStatus(tableId);
                var checkCount = checkStatus.data.length;
                if (checkCount < 1) {
                    top.layer.msg('请选择一条数据', {
                        time: 2000
                    });
                    return false;
                }
                var ids = '';
                $(checkStatus.data).each(function (index, item) {
                    if (item.t_Table.toUpperCase() === "A_Article".toUpperCase()) {
                        ids += item.InstanceID + ',';
                    }

                });
                if (ids === '') {
                    top.layer.msg('选择文章中没有可以推送的技术文章，只能推送文章类型的数据！', {
                        time: 2000
                    });
                    return;
                }
                ids = ids.substring(0, ids.length - 1);
                var lay = top.layer.msg('正在生成地图...', { icon: 16, shade: 0.5, time: 20000000 });
                $.post('/PortalSite/WriteMap', { data: ids }, function (data) {
                    top.layer.close(lay);
                    layer.alert(data, { title: '温馨提示' });

                });

                break;
        }
    });
    //表格重载
    function reloadTable() {
        table.reload(tableId, {});
    }

    //打开添加页面
    function addRole() {
        top.winui.window.msg("自行脑补画面", {
            icon: 2,
            time: 2000
        });
    }
    //删除角色
    function deleteRole(ids, obj) {
        var msg = obj ? '确认删除【' + obj.data.Title + '】吗？' : '确认删除选中数据吗？';
        top.layer.confirm(msg, { icon: 3, title: '删除' + obj.data.Title }, function (index) {
            layer.close(index);
            //向服务端发送删除指令
            //向服务端发送删除指令
            common.post("/api/Question/Del", { key: obj.data.ID }, function (resp) {
                var mysjon = JSON.parse(resp);
                if (mysjon.Success) {
                    //刷新表格
                    if (obj) {
                        top.layer.msg('删除成功', {
                            icon: 1,
                            time: 2000
                        });
                        obj.del(); //删除对应行（tr）的DOM结构
                    } else {
                        top.layer.msg('向服务端发送删除指令后刷新表格即可', {
                            time: 100
                        });
                        reloadTable();  //直接刷新表格
                    }
                }
            })

        });
    }
    //绑定按钮事件

    $('#deleteRole').on('click', function () {
        var checkStatus = table.checkStatus(tableId);
        var checkCount = checkStatus.data.length;
        if (checkCount < 1) {
            top.winui.window.msg('请选择一条数据', {
                time: 2000
            });
            return false;
        }
        var ids = '';
        $(checkStatus.data).each(function (index, item) {
            ids += item.id + ',';
        });
        deleteRole(ids);
    });
    $('#reloadTable').on('click', reloadTable);
    //自定义验证规则
    form.verify({
        title: function (value) {
            if (value.length < 3) {
                return '标题至少得5个字符啊';
            }
        }
        , pass: [
            /^[\S]{6,12}$/
            , '密码必须6到12位，且不能出现空格'
        ]
        , content: function (value) {
            layedit.sync(editIndex);
        }
    });
    exports('exam', {});
});
