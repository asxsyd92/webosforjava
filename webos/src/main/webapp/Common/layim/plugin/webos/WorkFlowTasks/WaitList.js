layui.config({
    base: '/Common/layim/layui_exts/' //指定 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'form', 'layer','common'], function (exports) {


    var table = layui.table, form = layui.form, layer = layui.layer, common = layui.common,
        $ = layui.$,
        tableId = 'tableid';
    common.islogin();
    //表格渲染
    table.render({
        id: tableId,
        elem: '#WaitList'
        , url: '/api/WorkFlowTasks/WaitList?title=&type='
        , totalRow: true
        , height: 'full'
        ,  headers: { "Authorization": "bearer " + window.localStorage["_token"] },
        //height: 'full-65', //自适应高度
        //size: '',   //表格尺寸，可选值sm lg
        //skin: '',   //边框风格，可选值line row nob
        //even:true,  //隔行变色
        page: true,
        limits: [8, 16, 24, 32, 40, 48, 56], toolbar: '#toolbarDemo',
        limit: 8,
        cols: [[
            { type: 'checkbox', fixed: 'left' }
            //, { field: 'ID', title: 'ID',  fixed: 'left', unresize: true, sort: true, totalRowText: '合计' }
            , { field: 'Title', title: '标题' }
            //, {
            //    field: 'email', title: '邮箱', width: 150, edit: 'text', templet: function (res) {
            //        return '<em>' + res.email + '</em>'
            //    }
            //}
            , { field: 'StepName', title: '步骤', sort: true, totalRow: true }
            , { field: 'ReceiveTime', title: '接受时间', edit: 'text', sort: true }
            , { field: 'SenderName', title: '发送人', sort: true, totalRow: true }
            , { field: 'Flow', title: '流程', sort: true, totalRow: true }
            //, { field: 'sign', title: '签名' }
            //, { field: 'city', title: '城市', width: 100 }
            //, { field: 'ip', title: 'IP', width: 120 }
            //, { field: 'joinTime', title: '加入时间', width: 120 }
            , { fixed: 'right', title: '操作', toolbar: '#barDemo', }
        ]]
    });
    //监听工具条
    table.on('tool(WaitList)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
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
            var query = "";
            query = query + "&taskid=" + data.ID + "&groupid=" + data.GroupID;
            top.window.xadmin.add_tab(data.Title, '/webos/Page/from/Debug/' + data.Urls.toString().trim() + '.html?key=' + data.InstanceID + "&flowid=" + data.FlowID + "&stepid=" + data.StepID + query+"&prevew=true");

        }
        else if (layEvent === 'edit') { //编辑
            console.log(data);
            if (!data.ID) return;
            var content;
            // var index = layer.load(1);
            //从桌面打开
             query = "";
            query = query + "&taskid=" + data.ID + "&groupid=" + data.GroupID;
            top.window.xadmin.add_tab(data.Title, '/webos/Page/from/Debug/' + data.Urls.toString().trim() + '.html?key=' + data.InstanceID + "&flowid=" + data.FlowID + "&stepid=" + data.StepID + query);
        }
    });
    table.on('toolbar(WaitList)', function (obj) {

        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'a_search':
                var title = $("#title").val();
                //搜索page设置为0
                reloadTable();
                $("#title").val(title);
                break;
        }
    });
    //表格重载
    function reloadTable() {
        table.reload(tableId, {});
    }

    //打开添加页面
    function addRole() {

    }
    //删除角色
    function deleteRole(ids, obj) {
        var msg = obj ? '确认删除角色【' + obj.data.Title + '】吗？' : '确认删除选中数据吗？';
        layer.confirm(msg, { icon: 3, title: '删除系统角色' }, function (index) {
            layer.close(index);
            //向服务端发送删除指令
            //向服务端发送删除指令
            $.post("/api/Form/DelFormData", { key: obj.data.InstanceID, table: obj.data.t_Table }, function (resp) {
                var mysjon = JSON.parse(resp);
                if (mysjon.Success) {
                    //刷新表格
                    if (obj) {
                        layer.msg('删除成功', {
                            icon: 1,
                            time: 2000
                        });
                        obj.del(); //删除对应行（tr）的DOM结构
                    } else {
                        layer.msg('向服务端发送删除指令后刷新表格即可', {
                            time: 100
                        });
                        reloadTable();  //直接刷新表格
                    }
                }
            })

        });
    }
    //绑定按钮事件
    $('#addRole').on('click', addRole);
    $('#deleteRole').on('click', function () {
        var checkStatus = table.checkStatus(tableId);
        var checkCount = checkStatus.data.length;
        if (checkCount < 1) {
            layer.msg('请选择一条数据', {
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




    exports('WaitList', {});
});
