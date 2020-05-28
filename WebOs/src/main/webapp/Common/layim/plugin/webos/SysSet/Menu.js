layui.config({
    base: '/Common/layim/layui_exts/' //指定 插件 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'form', 'layer', 'laytpl', 'iconPicker','colorpicker', 'laydate', 'tree', 'util', 'common', 'treetable'], function (exports) {
    form = layui.form, layer = layui.layer, colorpicker = layui.colorpicker, iconPicker = layui.iconPicker; var treeTable = layui.treetable, laytpl = layui.laytpl,  common = layui.common; $ = layui.$;
    common.islogin();
    var layout = [
        { name: '菜单名称', treeNodes: true, headerClass: 'value_col', colClass: 'value_col', style: '' },
        {
            name: '操作',
            headerClass: 'value_col',
            colClass: 'value_col',
            style: 'width: 20%',
            render: function (row) {
                return "<a class='layui-btn layui-btn-sm' onclick='a_edit(" + row + ")'><i class='layui-icon'>&#xe642;</i> 编辑</a><a class='layui-btn layui-btn-danger layui-btn-sm' onclick='a_del(" + row + ")'><i class='layui-icon'>&#xe640;</i> 删除</a>"; //列渲染
            }
        },
    ];

    var lay = layer.msg('正在获取菜单请稍等...', { icon: 16, shade: 0.5, time: 20000000 });
    $.get("/api/Users/GetToMenu", {}, function (resp) {
        layer.close(lay);
        resp = JSON.parse(resp);
        var arr = new Object();
        arr.ParentID = "00000000-0000-0000-0000-000000000000";
        arr.Title = "根目录"; arr.name = "根目录";
        var ree = new Array(); ree.push(arr);
        console.log(ree.concat(resp.data));
        var datas = { //数据
            "title": "Layui常用模块"
            , "list": ree.concat(resp.data)
        };
        var getTpl = QXS.innerHTML,
            view = document.getElementById('ParentID');
        laytpl(getTpl).render(datas, function (html) {
            view.innerHTML = html;
        });
     
        var tree1 = layui.treetable({
            elem: '#tree-table', //传入元素选择器
            checkbox: true,
            nodes: resp.data,
            layout: layout
        });
        iconPicker.render({
            // 选择器，推荐使用input
            elem: '#icon',
            // fa 图标接口
            url: "/Common/bootstrap/css/variables.less",
            // 是否开启搜索：true/false，默认true
            search: true,
            // 是否开启分页：true/false，默认true
            page: true,
            // 每页显示数量，默认12
            limit: 12,
            // 点击回调
            click: function (data) {
                console.log(data);
            },
            // 渲染成功后的回调
            success: function (d) {
                console.log(d);
            }
        });
        form.render();
    });
 
    //监听提交
    form.on('submit(add)', function (data) {
      
        if (data.field.ParentID === "") { data.field.ParentID = "00000000-0000-0000-0000-000000000000"; }
        data.field.icon = "fa " + data.field.icon;
        console.log(data.field.icon);
        $.post("/Common/save", { tab:"RoleApp", data: JSON.stringify(data.field) }, function (resp) {
            resp = JSON.parse(resp);
            if (resp.Success) {
                layer.alert(resp.msg);
                window.location.reload();

            }
        })
        return false;
    });


    //开启全功能
    colorpicker.render({
        elem: '#_Color'
        , color: 'rgba(7, 155, 140, 1)'
        , format: 'rgb'
        , predefine: true
        , alpha: true
        , done: function (color) {
            $('#Color').val(color);
        }
        , change: function (color) {
            //给当前页面头部和左侧设置主题色
            // $('.header-demo,.layui-side .layui-nav').css('background-color', color);
        }
    });



    exports('Menu', {});

});
function a_del(row) {

    var msg = '确认删除【' + row.Title + '】吗？' ;
    layer.confirm(msg, { icon: 3, title: '删除' + row.Title }, function (index) {
        var lay = layer.msg('删除中...', { icon: 16, shade: 0.5, time: 20000000 });
        $.post("/api/Users/DelMenu", { ID: row.ID }, function (resp) {
            resp = JSON.parse(resp);
            if (resp.Success) {
                layer.alert(resp.msg);
                window.location.reload();

            }
        });
        layer.close(index);
  
   
    });
 
}
function a_edit(row) {
    if (row['icon'] !== null) {     iconPicker.checkIcon('icon', row['icon']);}

    layer.open({
        type: 1, title: "编辑菜单",
        area: ['650px', '490px'],
        fixed: false, //不固定
        maxmin: true,
        content: $("#addmenu"), success: function (layero) {
            form.val("addclass", row);
            form.render(); //更新全部
        }, cancel: function () {
            ////右上角关闭回调
            $("#addmenu").hide();

        }
    });
}
function a_add() {
    //iframe层-父子操作

    layer.open({

        title:"新增菜单",
        type: 1,
        area: ['650px', '490px'],
        fixed: false, //不固定
        maxmin: true,
        content: $("#addmenu")
    });
}