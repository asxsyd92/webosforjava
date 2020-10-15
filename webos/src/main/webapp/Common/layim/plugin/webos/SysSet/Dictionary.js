layui.config({
    base: '/Common/layim/layui_exts/' //指定 插件 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'form', 'layer', 'laytpl', 'laydate','tree', 'util', 'common', 'treetable'], function (exports) {
    form = layui.form, layer = layui.layer; var treeTable = layui.treetable, laytpl = layui.laytpl, common = layui.common; $ = layui.$;
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
        }
    ];
    var lay = layer.msg('正在获取请稍等...', { icon: 16, shade: 0.5, time: 20000000 });
    $.get("/api/Users/GetDictionary", {}, function (resp) {
        layer.close(lay);
        resp = JSON.parse(resp);
        var arr = new Object();
        arr.ParentID = "00000000-0000-0000-0000-000000000000";
        arr.Title = "根目录"; arr.name = "根目录";
        var ree = new Array();
        ree.push(arr);
        ree= dicfor(resp.data,ree);
        var datas = { //数据
            "title": "Layui常用模块"
            , "list": ree
        };
        var getTpl = QXS.innerHTML,
            view = document.getElementById('ParentID');
        laytpl(getTpl).render(datas, function (html) {
            view.innerHTML = html;
        }); form.render();
        var tree1 = layui.treetable({
            elem: '#tree-table', //传入元素选择器
            checkbox: true,
            nodes: resp.data,
            layout: layout
        });
    });

    function dicfor(idata, ree) {
        $(idata).each(function (i, it) {
            ree.push(it);
          $(it.children).each(function (i, its) {
            ree.push(its);
              if (its.children.length > 0) {
                  ree.push(its);
                  dicfor(its.children, ree);
            }
        });

          });
        return ree;
    }
    //监听提交
    form.on('submit(add)', function (data) {
        var lay = layer.msg('保存中...', { icon: 16, shade: 0.5, time: 20000000 });
        $.post("/Common/save", { tab: "Dictionary", data: JSON.stringify(data.field) }, function (resp) {
            resp = JSON.parse(resp); layer.close(lay);
            if (resp.Success) {
                layer.alert(resp.msg);
                window.location.reload();

            }
        })
        return false;
    });

    //监听提交
    form.on('submit(del)', function (data) {
        console.log(data);
        $.post("/api/Users/DicDel", { key: data.field.ID }, function (resp) {
            resp = JSON.parse(resp);
            if (resp.Success) {
                layer.alert(resp.msg);
                window.location.reload();

            }
        })
        return false;
    });
    form.render();
    exports('Dictionary', {});
});
function a_del(row) {

    var msg = '删除字典会对表单造成不可预知的后果！确认删除【' + row.Title + '】吗？';
    layer.confirm(msg, { icon: 3, title: '删除' + row.Title }, function (index) {
        var lay = layer.msg('删除中...', { icon: 16, shade: 0.5, time: 20000000 });
        $.post("/api/Users/DicDel", { key: row.ID}, function (resp) {
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

    layer.open({
        type: 1, title: "编辑菜单",
        area: ['650px', '490px'],
        fixed: false, //不固定
        maxmin: true,
        content: $("#adddictionary"), success: function (layero) {
            form.val("addclass", row);
            form.render(); //更新全部
        }, cancel: function () {
            ////右上角关闭回调
            $("#adddictionary").hide();

        }
    });
}
function a_add() {
    //iframe层-父子操作

    layer.open({

        title: "新增字典项",
        type: 1,
        area: ['650px', '490px'],
        fixed: false, //不固定
        maxmin: true,
        content: $("#adddictionary"), success: function (layero) {
    
            form.render(); //更新全部
        }, cancel: function () {
            ////右上角关闭回调
            $("#adddictionary").hide();

        }
    });
}