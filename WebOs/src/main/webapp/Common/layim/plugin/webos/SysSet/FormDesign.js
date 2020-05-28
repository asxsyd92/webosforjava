layui.config({
    base: '/Common/layim/layui_exts/' //指定 插件 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'form', 'layer', 'laydate', 'tree', 'util', 'common'], function (exports) {
    table = layui.table; var $ = layui.jquery, util = layui.util, tree = layui.tree, form = layui.form; layer = layui.layer, layer = layui.layer, laydate = layui.laydate, common = layui.common;
    //初始化表单
    tableId = 'ratableid';
    common.islogin();
    //用于刷新表必须定义
    appid = $(window.frameElement).attr('tab-id');
    //第一个实例
    table.render({
        elem: '#formdesign'
        , id: tableId
        , toolbar: '#toolbarDemo'
        , url: '/Common/GetCommonList?tab=SysFormDesign' //数据接口
        , page: true //开启分页
        , cols: [[ //表头
            //{ field: 'ID', title: 'ID', width: 80, sort: true, fixed: 'left' }
            { field: 'Title', title: '名称' },
            { field: 'DateTime', title: '填加时间' },
            { field: 'Tab', title: '存储表明' }
            ,
            { field: 'Url', title: '访问路径' }
            , { fixed: 'right', title: '操作', toolbar: '#barDemo' }
        ]]
    });
    //监听工具条
    table.on('tool(formdesign)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        var ids = '';   //选中的Id
        $(data).each(function (index, item) {
            ids += item.id + ',';
        });
        if (layEvent === 'del') { //删除
            deleteSysFormDesign(ids, obj);
        }
   
        else if (layEvent === 'edit') { //编辑
            if (!data.ID) return;
            top.xadmin.add_tab("编辑" + data.Title, 'webos//Page/FormDesign/FormDesign.html?tabid='+data.Tab);
           // top.xadmin.open('编辑', '/webos/Page/SysSet/addSysFormDesign.html?id=' + data.ID + '&appid=' + appid, 400, 500);
        }


    });
    table.on('toolbar(formdesign)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'a_search':
                var title = $("#title").val();
                //搜索page设置为0
                table.reload(tableId, {
                    url: '/Common/GetCommonList?tab=SysFormDesign&title=' + title + "&page=1"
                    , where: {} //设定异步数据接口的额外参数
                });
                $("#title").val(title);
                break;
            case 'a_add':// add(-1);
                top.xadmin.open('添加角色', '/webos/Page/SysSet/addSysFormDesign.html?appid=' + appid, 500, 600);
                break;

        };
    });
    function deleteSysFormDesign(ids, obj) {
        var msg = obj ? '确认删除【' + obj.data.Name + '】吗？' : '确认删除选中数据吗？';
        top.layer.confirm(msg, { icon: 3, title: '删除' }, function (index) {
            top.layer.closeAll();
            //向服务端发送删除指令
            //向服务端发送删除指令
            common.post("/api/FormDesign/FormDel", { key: obj.data.ID, url: obj.data.Url }, function (resp) {
                var mysjon = JSON.parse(resp);
                if (mysjon.Success) {
                    //刷新表格
                    reloadTable();
                }
            //$.post("/Common/Del", { key: obj.data.ID, table: "SysFormDesign" }, function (resp) {
            //    var mysjon = JSON.parse(resp);
            //    if (mysjon.Success) {
            //        //刷新表格
            //        reloadTable();
            //    }
            })

        });
    }
    function reloadTable() {
        //table.reload(tableId, {});
        var title = $("#title").val();
        //搜索page设置为0
        table.reload(tableId, {
            url: '/Common/GetCommonList?tab=SysFormDesign&title=' + title + "&page=1"
            , where: {} //设定异步数据接口的额外参数
        });
        $("#title").val(title);
    }
    if (common.getRequest().key !== null && common.getRequest().key !== undefined) {
        var lay = layer.msg('获取中...', { icon: 16, shade: 0.5, time: 20000000 });
        $.get("/api/Users/GetSetMenu", { v: common.getRequest().key }, function (resp) {
            var my = JSON.parse(resp);  //权限设置
            //开启复选框
            console.log(my);
            tree.render({
                elem: '#settree'
                , data: my.data
                , id: '_settree'
                , showCheckbox: true
            });
            var _ids = [];

            $(my.s_data).each(function (i, it) {
                _ids.push(it.MenuID);
            });

            console.log(_ids);
            tree.setChecked('_settree', _ids); //批量勾选 id 为 2、3 的节点
            layer.close(lay);
        });
        //按钮事件
        util.event('lay-demo', {
            getChecked: function (othis) {
                var checkedData = tree.getChecked('_settree'); //获取选中节点的数据
                var ids = "";
                $(checkedData).each(function (index, item) {
                    ids += item.ID + ",";
                    if (item.children.length > 0) {
                        $(item.children).each(function (i, it) {
                            ids += it.ID + ",";
                        });

                    }
                });
                if (ids === "") {
                    return false;
                }
                ids = ids.substring(0, ids.length - 1);
                var lay = layer.msg('正在设置...', { icon: 16, shade: 0.5, time: 20000000 });
                $.post("/api/Users/SetSysFormDesign", { key: common.getRequest().key, list: ids }, function (resp) {
                    var my = JSON.parse(resp);  //权限设置
                    //开启复选框

                    layer.close(lay);
                    layer.msg(my.msg);
                    top.layer.closeAll();
                    //刷新表
                    var frame = top.frames;
                    for (var i = 0; i < frame.length; i++) {
                        if (common.getRequest().appid === frame[i].appid) {
                            frame[i].table.reload(frame[i].tableId, {});
                        }
                    }
                });
                layer.alert(JSON.stringify(ids), { shade: 0 });
                console.log(ids);
            }
            , setChecked: function () {
                tree.setChecked('_settree', [12, 16]); //勾选指定节点
            }
            , reload: function () {
                //重载实例
                tree.reload('_settree', {

                });

            }
        });
    }
    if (common.getRequest().id !== null && common.getRequest().id !== undefined) {
        $.post("/Common/GetByID", { key: common.getRequest().id, table: 'SysFormDesign' }, function (data) {
            var datajson = JSON.parse(data);
            //表单赋值   
            form.val('asSysFormDesign', datajson.data);
        });
    }
    form.on('submit(_submit)', function (data) {
        $.post("/Common/save", { tab: "SysFormDesign", data: JSON.stringify(data.field) }, function (resp) {
            var res = JSON.parse(resp);
            if (res.Success === true) {
                layer.msg("操作成功");
                top.layer.closeAll();
                //刷新表
                var frame = top.frames;
                for (var i = 0; i < frame.length; i++) {
                    if (common.getRequest().appid === frame[i].appid) {
                        frame[i].table.reload(frame[i].tableId, {});
                    }
                }
            } else {
                layer.msg(res.msg);
                return false;
            }
        });






        return false;
    });
    exports('FormDesign', {});
});