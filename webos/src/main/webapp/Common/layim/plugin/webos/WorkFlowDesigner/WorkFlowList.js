layui.config({
    base: '/Common/layim/layui_exts/' //指定 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'form', 'layer'], function (exports) {


    var table = layui.table, form = layui.form, layer = layui.layer,
        $ = layui.$;
    _addindex = null; //var ue;
    table.render({
        elem: '#WorkFlowList'
        , url: '/api/WorkFlow/WorkFlowList?title=&type='
        , toolbar: '#toolbarDemo'
        , title: '用户数据表'
        , totalRow: true
           , height: 'full'
   ,headers: { "Authorization": "bearer " + window.localStorage["_token"] }
        , cols: [[
            { type: 'checkbox', fixed: 'left' }
            , { field: 'Name', title: '标题' }
            , { field: 'keyword', title: '关键词' }
            , { field: 'description', title: '描述', sort: true, totalRow: true }
            , { field: 'author', title: '发布人', width: 80, sort: true }
            , { field: 'tuijian', title: '推荐', width: 80, templet: '#switchTpl', unresize: true }
            , { field: 'isindex', title: '显示首页', width: 90, templet: '#switchTpl1', unresize: true }
            //, { field: 'tuijian', title: '是否推荐', sort: true, totalRow: true }
            //, { field: 'isindex', title: '是否显示首页', sort: true, totalRow: true }
            , { fixed: 'right', title: '操作', toolbar: '#barDemo', }
        ]]
        , page: true
    });
    table.on('toolbar(WorkFlowList)', function (obj) {

        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'a_search':
                var title = $("#title").val();
                //搜索page设置为0
                table.reload('WorkFlowList', {
                    url: '/api/WorkFlow/WorkFlowList?title=' + title + "&page=0"
                    , where: {} //设定异步数据接口的额外参数
                });
                $("#title").val(title);
                break;
            case 'getCheckLength':// add(-1);
                window.location.href = "/Webos/Page/WorkFlowDesigner/index.html";
                //var data = checkStatus.data;
                //layer.msg('选中了：' + data.length + ' 个');
                break;
            case 'isAll':
                layer.msg(checkStatus.isAll ? '全选' : '未全选');
                break;
        }
    });

    //监听行工具事件
    table.on('tool(WorkFlowList)', function (obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            , layEvent = obj.event; //获得 lay-event 对应的值
        if (layEvent === 'detail') {
            layer.open({
                type: 2, shade: 0, title: '查看文章', maxmin: true, //开启最大化最小化按钮
                area: ['90%', '90%'],
                content: '/index.html#/news_view?key=' + data.keyid //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                , success: function (layero) {
                }, cancel: function () {
                    ////右上角关闭回调
                    $("#addinfo").hide();
                    //table.reload('WorkFlowList', {
                    //    url: '/api/WorkFlowList?title=&page=0'
                    //    , where: {} //设定异步数据接口的额外参数
                    //});
                    ////return false 开启该代码可禁止点击该按钮关闭
                }
            });
        } else if (layEvent === 'del') {
            layer.confirm('真的删除行么', function (index) {
                $.post("/api/delinfo", { keyid: data.keyid }, function (resp) {
                    var mysjon = JSON.parse(resp);
                    if (mysjon.Success) {
                        obj.del(); //删除对应行（tr）的DOM结构
                        table.reload('WorkFlowList', {
                            url: '/api/WorkFlowList?title=&page=0'
                            , where: {} //设定异步数据接口的额外参数
                        });
                    }
                });


                layer.close(index);
                //向服务端发送删除指令
            });
        } else if (layEvent === 'edit') {
            window.location.href = "/Webos/Page/WorkFlowDesigner/index.html?key=" + data.ID + "&ss=" + $("#title").val(); //  add(data.keyid);
        }
    });

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

    //监听指定开关
    form.on('switch(switchTest)', function (data) {
    });


        exports('WaitList', {});
});
