layui.config({
    base: '/Common/layim/layui_exts/' //指定 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'form', 'layer','common'], function (exports) {


    var table = layui.table, form = layui.form, layer = layui.layer, common = layui.common,
        $ = layui.$,
        tableId = 'tableid';
    common.islogin();
    _addindex = null; //var ue;
    table.render({
        elem: '#CompletedList'
        , url: '/api/WorkFlowTasks/CompletedList?title=&type='
        , toolbar: '#toolbarDemo'
        , title: '用户数据表'
        , totalRow: true
        , height: 'full'
        , headers: { "Authorization": "bearer " + window.localStorage["_token"] }
        , cols: [[
            { type: 'checkbox', fixed: 'left' },
            //{ field: 'ID', title: 'ID', sort: true, fixed: 'left' }
            { field: 'Title', title: '标题', }
            , { field: 'StepName', title: '步骤', }
            , { field: 'SenderName', title: '发送人', }
            , { field: 'ReceiveTime', title: '时间', }
            //, { field: 'SenderName', title: '发送人', templet: '#switchTpl', unresize: true }
            , { fixed: 'right', align: 'center', toolbar: '#barDemo' }
        ]]
        , page: true
    });
    table.on('toolbar(CompletedList)', function (obj) {

        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'a_search':
                var title = $("#title").val();
                //搜索page设置为0
                table.reload('CompletedList', {
                    url: '/api/WorkFlowTasks/CompletedList?title=' + title + "&page=0"
                    , where: {} //设定异步数据接口的额外参数
                });
                $("#title").val(title);
                break;

        };
    });

    //监听行工具事件
    table.on('tool(CompletedList)', function (obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            , layEvent = obj.event; //获得 lay-event 对应的值
        if (layEvent === 'detail') {
            var query = "";
            query = query + "&taskid=" + data.ID + "&groupid=" + data.GroupID;
            top.window.xadmin.add_tab(data.Title, '/webos/Page/from/Debug/' + data.Urls.toString().trim() + '.html?key=' + data.InstanceID + "&flowid=" + data.FlowID + "&stepid=" + data.StepID + query + "&prevew=true");

        } else if (layEvent === 'del') {
            layer.confirm('真的删除行么', function (index) {
                $.post("/api/delinfo", { keyid: data.keyid }, function (resp) {
                    var mysjon = JSON.parse(resp);
                    if (mysjon.Success) {
                        obj.del(); //删除对应行（tr）的DOM结构
                        reloadTable();
                    }
                })


                layer.close(index);
                //向服务端发送删除指令
            });
        } 
    });

    //表格重载
    function reloadTable() {
        table.reload(tableId, {});
    }
    //监听指定开关
    form.on('switch(switchTest)', function (data) {
    });


    exports('CompletedList', {});
});
