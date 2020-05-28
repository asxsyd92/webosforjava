/**

 @Name：FormDesign.js by web layui 表单设计器v2.0
 @Author：爱上歆随懿恫|http://www.asxsyd92.com
 @Url：http://www.asxsyd92.com
 @License：MIT
*/
var $ = null;
layui.define(['jquery', 'layer', 'form', 'common', 'element', 'laytpl', 'tree', 'util'], function (exports) {

    $ = layui.jquery, laytpl = layui.laytpl, common = layui.common, tree = layui.tree, util = layui.util;
    common.islogin();
    if (window.sessionStorage["_flowalllist"] !== null && window.sessionStorage["_flowalllist"] !== undefined) {

        var hso = "<option value= '00000000-0000-0000-0000-000000000000' >全部</option>";
        var data = JSON.parse(window.sessionStorage["_flowalllist"]);
        $.each(data, function (i, item) {
            hso += "<option value='" + item.ID + "'>" + item.Name + "</option>";

        });

        $("#liucheng").html(hso);
        if (common.getRequest().flowid !== undefined && common.getRequest().flowid !== null) {
            $("#liucheng").html(common.getRequest().flowid);
        }
     
    }
    else {
        common.get("/api/WorkFlow/GetFlowAllList", {}, function (resp) {
            var ks = JSON.parse(resp);
            window.sessionStorage["_flowalllist"] = JSON.stringify( ks.data);
            var hso = "<option value= '00000000-0000-0000-0000-000000000000' >全部</option>";
            $.each(ks.data, function (i, item) {
                hso += "<option value='" + item.ID + "'>" + item.Name + "</option>";

            });

            $("#liucheng").html(hso);
            if (common.getRequest().flowid !== undefined && common.getRequest().flowid !== null) {
                $("#liucheng").html(common.getRequest().flowid);
            }
          
        });
    }
  
    form = layui.form; layer = layui.layer, common = layui.common, element = layui.element;
    var utils = {
        SingleClick: function (_this) {
            //渲染属性html
            if ($("#_a_SingleClick").length > 0) {
                this.HtmlRender($("#_a_SingleClick")[0]);
            }
            var thats = $(_this);
            console.log(thats);
            var key = thats.find('.layui-form-label').eq(0);
            var value = thats.find('.layui-input').eq(0);
            if (thats[0].className.indexOf("layui-col-md6") >= 0) {
                $("#fd_ys").val("layui-col-md6");
            } else {
                $("#fd_ys").val("layui-col-md12");
            }
            $("#fd_type").val(value[0].type),
                $("#fd_id").val(value[0].id),

                $("#fd_title").val(key[0].innerText),
                $("#fd_value").val(value[0].value),
                fd_required = value[0].getAttribute('lay-verify');
            //验证
            if (fd_required === null || fd_required === "") { $("input:radio[name=fd_required][value='否']").attr('checked', 'true'); }
            else { $("input:radio[name=fd_required][value='是']").attr('checked', 'true'); }
            //只读
            fd_disabled = value[0].getAttribute('disabled');
            if (fd_disabled === null || fd_disabled === "") { $("input:radio[name=fd_disabled][value='否']").attr('checked', 'true'); }
            else { $("input:radio[name=fd_disabled][value='是']").attr('checked', 'true'); }
            //隐藏
            fd_hide = $(".ashide");
            if (fd_hide.length === 0) { $("input:radio[name=fd_hide][value='否']").attr('checked', 'true'); }
            else { $("input:radio[name=fd_hide][value='是']").attr('checked', 'true'); }
            form.render();
            //监听提交
            form.on('submit(xiugai)', function (data) {
                var fd_type = $("#fd_type").val(),
                    fd_id = $("#fd_id").val(),
                    fd_title = $("#fd_title").val(),
                    fd_required = $('input:radio[name="fd_required"]:checked').val(),
                    fd_value = $("#fd_value").val(),
                    fd_disabled = $('input:radio[name="fd_disabled"]:checked').val(),
                    fd_hide = $('input:radio[name="fd_hide"]:checked').val();
                if (fd_id === null || fd_id === "") {
                    layer.tips('ID不能为空', '#fd_id');
                    return false;
                } if (fd_title === null || fd_title === "") {
                    layer.tips('title不能为空', '#fd_title');
                    return false;
                }
                if ($.trim(fd_id).length > 0 && $.trim(fd_title).length > 0) {
                    try {
                        key.data('title', fd_title).html(fd_title);
                        value[0].id = fd_id,
                            value[0].name = fd_id,
                            value[0].type = fd_type,
                            value[0].placeholder = "请输入" + fd_title,
                          //  value[0].value = fd_value;
                        $(value).attr("value", fd_value);
                        if (fd_required === "是") { $(value).attr("lay-verify", "required"); } else { $(value).removeAttr("lay-verify"); }
                        if (fd_disabled === "是") { $(value).attr("disabled", "disabled"); } else { $(value).removeAttr("disabled"); }
                        if (fd_hide === "是") {
                            $(thats).attr("class", $("#fd_ys").val()+" ashide as_a_singleclick");
                        } else { $(thats).attr("class", $("#fd_ys").val()+" as_a_singleclick"); }
                        layer.msg("修改完成", { icon: 1, shade: 0, time: 5000 });
                    } catch (e) {
                        // layer.close(index);
                        layer.msg("修改失败" + e);
                        console.log(e);
                    }
                } else { layer.alert("ID是一个必不可少的参数" + e, { icon: 1 }); }
                return false;
            });
            form.on('submit(delete)', function (data) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
                return false;
            });


        },
        //编辑框的处理方法
        DoubleClick: function (_this) {
            var thats = $(_this);
            top.layer.alert("请查看百度编辑器工具栏，当前默认全部显示工具栏上的所有的功能按钮和下拉框！");
            if ($("#_a_DoubleClick").length > 0) {
                this.HtmlRender($("#_a_DoubleClick")[0]);
            }
            var key = thats.find('.layui-form-label').eq(0);
            var value = thats.find('.layui-textarea').eq(0);

            $("#fd_id").val(value[0].id);
            $("#fd_title").val(key[0].innerText);

            if (value[0].dataset.target === "" || value[0].dataset.target === null) {
                var tools = 'fullscreen, source, |, undo, redo, |,'
                    + ' bold, italic, underline, fontborder, strikethrough, superscript, subscript, removeformat, formatmatch, autotypeset, blockquote, pasteplain, |, forecolor, backcolor, insertorderedlist, insertunorderedlist, selectall, cleardoc, |, '
                    + '  rowspacingtop, rowspacingbottom, lineheight, |,'
                    + '  customstyle, paragraph, fontfamily, fontsize, |,'
                    + ' directionalityltr, directionalityrtl, indent, |,'
                    + ' justifyleft, justifycenter, justifyright, justifyjustify, |, touppercase, tolowercase, |,'
                    + ' link, unlink, anchor, |, imagenone, imageleft, imageright, imagecenter, |,'
                    + ' simpleupload, insertimage, emotion, scrawl, insertvideo, music, attachment, map, gmap, insertframe, insertcode, webapp, pagebreak, template, background, |,'
                    + ' horizontal, date, time, spechars, snapscreen, wordimage, |,'
                    + 'inserttable, deletetable, insertparagraphbeforetable, insertrow, deleterow, insertcol, deletecol, mergecells, mergeright, mergedown, splittocells, splittorows, splittocols, charts, |,'
                    + ' print, preview, searchreplace, help, drafts';
                $("#fd_value").val(tools);
            } else {
                $("#fd_value").val(value[0].dataset.target);
            }

            form.on('submit(xiugai)', function (data) {
                var fd_id = $("#fd_id").val(),
                    fd_title = $("#fd_title").val(),

                    fd_value = $("#fd_value").val();
                if (fd_id === null || fd_id === "") {
                    layer.tips('ID不能为空', '#fd_id');
                    return false;
                } if (fd_title === null || fd_title === "") {
                    layer.tips('title不能为空', '#fd_title');
                    return false;
                }
                if ($.trim(fd_id).length > 0 && $.trim(fd_title).length > 0) {
                    try {
                        key.data('title', fd_title).html(fd_title);
                        value[0].id = fd_id,
                            value[0].name = fd_id,
                            value[0].placeholder = "请输入" + fd_title,
                            $(value).attr("data-target", $("#fd_value").val());

                    } catch (e) {
                        layer.close(index);
                        layer.msg("修改失败" + e);
                        console.log(e);
                    }

                    return false;
                }
            });
            form.on('submit(delete)', function (data) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
                return false;
            });


        },
        //添加html
        AddHtml: function (_this) {
            //var that = $(_this);
            //console.log(this);
            //var target = null;
            //target = that.data('target');
            ////标签只能有一个
            //if (target.indexOf("_asxsfromname") > 0) {
            //    //判断标签是否已经有了
            //    if ($("#_asxsfromname").length > 0) {
            //        layer.msg("你已经添加了请务重复添加");
            //    } else {
            //        $('._two').append(target);
            //    }
            //}
            //else if (target.indexOf("_asxstisp") > 0) {
            //    //判断标签是否已经有了
            //    if ($("#_asxstisp").length > 0) {
            //        layer.msg("你已经添加了请务重复添加");
            //    } else {
            //        $('._two').append(target);
            //    }
            //} else { $('._two').append(target); }
            //$('.asxsyd92move').arrangeable();
            //form.render(); //更新全部
            //$('.layui-form-item').arrangeable();
            //if ($("#" + _this).length <= 0) {
            //    layer.alert("ID不能为空");
            //    return false;
            //}
            //var getTpl = $("#" + _this)[0].innerHTML;
            //$('._two').append(getTpl);
            //$('.asxsyd92move').arrangeable();
            //form.render(); //更新全部
            //layer.msg("添加成功！");
            utils.Listening(_this);
        },
        //下拉
        DropDown: function (_this) {
            //  top.layer.alert("数据字典请填写Code值，否则请填写下拉选项值用|分隔请不要换行，静态值必须有|分隔，否则视为字典项！");
            var thats = $(_this);
            //渲染属性html
            if ($("#_a_DropDown").length > 0) {
                this.HtmlRender($("#_a_DropDown")[0]);
            }
            var key = thats.find('.layui-form-label').eq(0);
            var value = thats.find("select").eq(0);
     
            if (value[0].dataset.target === "" || value[0].dataset.target === null) {
               
                $("#fd_value").val($(value).html());
            } else {
                $("#fd_value").val(value[0].dataset.target);
            }


            $("#fd_id").val(value[0].id),
                $("#fd_title").val(key[0].innerText);
            var classname = thats[0].className.replace("layui-col-md6", $("#fd_ys").val());
            classname= classname.replace("layui-col-md12", $("#fd_ys").val());
            $(thats).attr("class", + classname);
            form.on('submit(formdic)', function (data) {
                var INDEXOPEN = layer.open({
                    type: 1, title: "编辑菜单",
                    area: ['650px', '490px'],
                    fixed: false, //不固定
                    maxmin: true,
                    content: $("#_a_settree")[0].innerHTML, success: function (layero) {
                        var lay = layer.msg('获取中...', { icon: 16, shade: 0.5, time: 20000000 });
                        $.get("/api/Users/GetByCode", { code: "newstype" }, function (resp) {
                            var my = JSON.parse(resp);  //权限设置
                            //开启复选框
                            console.log(my);
                            tree.render({
                                elem: '#settree'
                                , data: my.data
                                , id: '_settree'
                                , showCheckbox: true
                            });
                            //var _ids = [];

                            //$(my.s_data).each(function (i, it) {
                            //    _ids.push(it.MenuID);
                            //});

                            //console.log(_ids);
                            //tree.setChecked('_settree', _ids); //批量勾选 id 为 2、3 的节点
                            layer.close(lay);
                        });
                        form.render(); //更新全部
                    }, cancel: function () {
                        ////右上角关闭回调


                    }
                });
                //按钮事件
                util.event('lay-demo', {
                    getChecked: function (othis) {
                        var checkedData = tree.getChecked('_settree'); //获取选中节点的数据
                        if (checkedData.length > 1) {
                            layer.alert("只能选择一个");
                            return false;
                        }
                        if (checkedData.length !== 0) {
                            $("#fd_value").val(checkedData[0].ID);
                        }

                        layer.close(INDEXOPEN);
                    }

                });
                return false;
            });


            form.on('submit(xiugai)', function (data) {


                var fd_id = $("#fd_id").val(),
                    fd_title = $("#fd_title").val(),
                    fd_value = $("#fd_value").val();

                if (fd_id === null || fd_id === "") {
                    layer.tips('ID不能为空', '#fd_id');
                    return false;
                } if (fd_title === null || fd_title === "") {
                    layer.tips('title不能为空', '#fd_title');
                    return false;
                }

                try {
                    key.data('title', fd_title).html(fd_title);
                 
              
                    if ($("#fd_value").val().length > 0) {
                        if ($("#fd_value").val().indexOf('value=') > 0) {
                            $(value).html($("#fd_value").val());
                        } else {
                            $(value).attr("data-target", $("#fd_value").val());
                        }
                       
                    } 
                    value[0].id = fd_id,
                  value[0].name = fd_id;
              
         
                    layer.msg("修改成功");

                } catch (e) {

                    layer.msg("修改失败" + e);
                    console.log(e);
                }
                return false;
            });
            form.on('submit(delete)', function (data) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
                return false;
            });

        },
        RadioDown: function (_this) {
            var thats = $(_this);
            //渲染属性html
            if ($("#_a_RadioDown").length > 0) {
                this.HtmlRender($("#_a_RadioDown")[0]);
            }

            var key = thats.find('.layui-form-label').eq(0);
            var value = thats.find(".asRadio");// thats.find('.layui-input').eq(0);
            if (value.length > 0) {

                var rhml = "";
                $.each(value.find("input"), function (index, item, arr) {
                    rhml += item.outerHTML + "\n";
                });
                $("#fd_value").val(rhml);
            } else {
                value = thats.find(".asCheckBox");
                if (value.length > 0) {

                    var rhmls = "";
                    $.each(value.find("input"), function (index, item, arr) {
                        rhmls += item.outerHTML + "\n";
                    });
                    $("#fd_value").val(rhmls);}
            }


            $("#fd_title").val(key[0].innerText);
            if (thats[0].className.indexOf("layui-col-md6") >= 0) {
                $("#fd_ys").val("layui-col-md6");
            } else {
                $("#fd_ys").val("layui-col-md12");
            }
            form.on('submit(xiugai)', function (data) {
                var fd_title = $("#fd_title").val(),
                    fd_value = $("#fd_value").val();
                key.data('title', fd_title).html(fd_title);
          
                $(value).html(fd_value);
                var classname = thats[0].className.replace("layui-col-md6", $("#fd_ys").val());
                classname = classname.replace("layui-col-md12", $("#fd_ys").val());
                $(thats).attr("class", + classname);
                form.render();

                layer.msg("修改成功");
                return false;
            });
            form.on('submit(delete)', function (data) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
                return false;
            });

        },
        CheckBox: function (_this) {
            var thats = $(_this);
            //渲染属性html
            if ($("#_a_RadioDown").length > 0) {
                this.HtmlRender($("#_a_RadioDown")[0]);
            }

            var key = thats.find('.layui-form-label').eq(0);
            var value = thats.find(".asCheckBox");// thats.find('.layui-input').eq(0);
            if (value.length > 0) {

                var rhml = "";
                $.each(value.find("input"), function (index, item, arr) {
                    rhml += item.outerHTML + "\n";
                });
                $("#fd_value").val(rhml);
            }


            $("#fd_title").val(key[0].innerText);
            if (thats[0].className.indexOf("layui-col-md6") >= 0) {
                $("#fd_ys").val("layui-col-md6");
            } else {
                $("#fd_ys").val("layui-col-md12");
            }
            form.on('submit(xiugai)', function (data) {
                var fd_title = $("#fd_title").val(),
                    fd_value = $("#fd_value").val();
                key.data('title', fd_title).html(fd_title);
                $(value).html(fd_value);
                var classname = thats[0].className.replace("layui-col-md6", $("#fd_ys").val());
                classname = classname.replace("layui-col-md12", $("#fd_ys").val());
                $(thats).attr("class", + classname);
                form.render();
                layer.msg("修改成功");
                return false;
            });
            form.on('submit(delete)', function (data) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
                return false;
            });

        },

        AddAssembly: function (_id) {
            utils.Listening(_id);


        },
        SpanClick: function (_this) {
            if ($("#_a_SpanClick").length > 0) {
                this.HtmlRender($("#_a_SpanClick")[0]);
            }
            var that = $(_this);
            console.log(that);
            $("#fd_value").val(that[0].innerHTML);
            form.on('submit(xiugai)', function (data) {
                fd_value = $("#fd_value").val();
                that[0].innerHTML = fd_value;
                return false;
            });
            form.on('submit(delete)', function (data) {
                if (confirm('确定要删除吗？')) {
                    console.log(that);
                    var parent = that;
                    parent.remove();
                }
                return false;
            });
            form.render(); element.render(); //更新全部
        },
        C_AND_I: function (_this) {
            //渲染属性html
            if ($("#_a_C_AND_I").length > 0) {
                this.HtmlRender($("#_a_C_AND_I")[0]);
            }
            var thats = $(_this);
            console.log(thats);
            var key = thats.find('.layui-form-label').eq(0);
            var value = thats.find('.layui-input').eq(0);

            $("#fd_id").val(value[0].id),

                $("#fd_title").val(key[0].innerText);
            fd_required = value[0].getAttribute('lay-verify');

            form.render();
            //监听提交
            form.on('submit(xiugai)', function (data) {
                var fd_id = $("#fd_id").val();
                fd_title = $("#fd_title").val();

                if (fd_id === null || fd_id === "") {
                    layer.tips('ID不能为空', '#fd_id');
                    return false;
                } if (fd_title === null || fd_title === "") {
                    layer.tips('title不能为空', '#fd_title');
                    return false;
                }
                key.data('title', fd_title).html(fd_title);
                value[0].id = fd_id;
                value[0].name = fd_id;
                layer.msg("操作成功！");
                return false;
            });
            form.on('submit(delete)', function (data) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
                return false;
            });

        },
        YLrun: function () {
            if ($("#_fmname").val().length <= 0) {
                layer.msg("请填写名称");
                return false;
            }
            //添加表名
            if ($("#_table") !== null) {
                var tab = "<input lay-verify='required' name='_table' id='_table' value='" + $("#_fmname").val() + "' lay-filter='column' class='layui-input' style='display: none;' />";
                $(".layui-row").append(tab);
                form.render(); //更新全部
            }//$(".layui-form").html()
            var formhtml = $(".layui-form").html();
            formhtml = formhtml.replaceAll("FormDesign.", '').replaceAll('SingleClick', '').replaceAll("this", '').replaceAll('onclick=', '')
                .replaceAll("DoubleClick", '').replaceAll("DropDown", '').replaceAll("RadioDown", '').replaceAll("CheckBox", '').replaceAll('BaiduUpload', '')
                .replaceAll("SpanClick", "");
            $.post("/api/FormDesign/FormDesignYL", { html: formhtml }, function (data) {
                console.log(data);
                top.window.layer.open({
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['100%', '100%'], //宽高
                    //  content: "/" + data
                    content: data
                });
            }).error(function (e) {
                console.log(e);
            })
        },
        Listening: function (_id) {
            switch (_id) {
                case "_a_icon":
                    if ($(".as-IconPicker").length > 0) {
                        layer.msg("控件已经在表单中啦！");
                        return false;
                    }

                    utils.ListeningAddHtml(_id);
                    break;

                case "_a_color":
                    if ($(".as-Colorpicker").length > 0) {
                        layer.msg("控件已经在表单中啦！");
                        return false;
                    }

                    utils.ListeningAddHtml(_id);
                    break;
                case "_a_baiduimg": if ($(".as-BaiDuImg").length > 0) {
                    layer.msg("控件已经在表单中啦！");
                    return false;
                } utils.ListeningAddHtml(_id); break;
                case "_a_baidufile": if ($(".as-BaiDuFile").length > 0) {
                    layer.msg("控件已经在表单中啦！");
                    return false;
                } utils.ListeningAddHtml(_id); break;
                default: utils.ListeningAddHtml(_id);
            }


        },
        ListeningAddHtml: function (_id) {
            if ($("#" + _id).length <= 0) {
                layer.alert("ID不能为空");
                return false;
            }
            var getTpl = $("#" + _id)[0].innerHTML;
            $('._two').append(getTpl);
            $('.asxsyd92move').arrangeable();
            //注册监听事件
            form.render(); //更新全部
            utils.ListeningButton();

            layer.msg("添加成功！");

        }
        ,
        ListeningButton: function () {

            $(".as_a_icon").click(function (a_this) {
                utils.C_AND_I(a_this.currentTarget);
            });
            $(".as_a_spanclick").click(function (a_this) {
                utils.SpanClick(a_this.currentTarget);
            });
            $(".as_a_dropdown").click(function (a_this) {
                utils.DropDown(a_this.currentTarget);
            });

            $(".as_a_singleclick").click(function (a_this) {
                utils.SingleClick(a_this.currentTarget);
            });
            $(".as_a_doubleclick").click(function (a_this) {
                utils.DoubleClick(a_this.currentTarget);
            });
            $(".as_a_radiodown").click(function (a_this) {
                utils.RadioDown(a_this.currentTarget);
            });
        }
        ,
        jiazha: function () {

            if ($("#_fmname").val().length) {
                this.table($("#_fmname").val());
            } else {
                layer.msg("请填写表名称");
            }
        },
        deleteitem: function () {
            if (confirm('确定要删除吗？')) {
                var that = $(this);
                var parent = that.parent();
                parent.remove();
                if ($('.customerlist .customeritem').length === 0) $('.tips').show();
            }

        },
        faburun: function () {

            if ($("#_fmname").val().length <= 0) {
                layer.msg("请填写存储表");
                return false;
            }
            if ($("#_rormname").val().length <= 0) {
                layer.msg("路径名称不能为空并且不能与现有名称冲突");
                return false;
            }
            //添加表名
            if ($("#_table") !== null) {
                var tab = "<input lay-verify='required' name='_table' id='_table' value='" + $("#_fmname").val() + "' lay-filter='column' class='layui-input' style='display: none;' />";
                $(".layui-row").append(tab);
                form.render(); //更新全部
            }
            var formhtml = $(".asxsud92form").html();
            formhtml = formhtml.replaceAll("FormDesign.", '').replaceAll('SingleClick', '').replaceAll("this", '').replaceAll('onclick=', '')
                .replaceAll("DoubleClick", '').replaceAll("DropDown", '').replaceAll("RadioDown", '').replaceAll("CheckBox", '').replaceAll('BaiduUpload', '')
                .replaceAll("SpanClick", "");
            var obj = new Object(); obj.DesignHtml = $(".asxsud92form").html(); obj.RunHtml = formhtml;
            obj.Title = $(".asxsyd92move").length > 0 ? $(".asxsyd92move")[0].innerText : "未命名的表单";
            obj.DateTime = common.SysOperation._SYS_DATETIME; obj.UserID = common.SysOperation._SYS_GETUSERID; obj.Tab = $("#_fmname").val(); obj.Url = "_sys_url"; obj.ID = "_sys_id";
            $.post("/api/FormDesign/FormDesignHtml", { html: formhtml, name: $("#_rormname").val(), title: obj.Title, type: true, data: JSON.stringify(obj), flow: $("#liucheng").val() }, function (data) {
                console.log(data);
                top.window.layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['100%', '100%'], //宽高
                    //  content: "/" + data
                    content: data
                });
            });
        },

        table: function (key) {
            $('.draggable-element').arrangeable();
            console.log(key);
            if (key !== null) {

                $.get('/api/Form/FormTable?table=' + key, {}, function (res) {
                    //

                    var dat = JSON.parse(res);
                    if (dat.msg === "-1") {
                        var target = "";
                        $('._two').append(target);
                        var fs = false;
                        console.log(dat.data);
                        //生成头
                        $('._two').append("<div class='asxsyd92move as_a_spanclick' style='text-align: center;' id='_asxsfromname' ><fieldset class='layui-elem-field layui-field-title' style='margin-top: 30px;'><legend >表单名称</legend></fieldset> </div>");
                        $.each(dat.data, function (i, item) {
                            if (item.ColumnType.toLowerCase() === "uniqueidentifier".toLowerCase()) {
                                target = "<div  class='layui-col-md6  asxsyd92move as_a_singleclick' ><div class='layui-form-item'><label class='layui-form-label'>" + item.Description + "</label><div class='layui-input-block'><input lay-verify='required' value='00000000-0000-0000-0000-000000000000' placeholder='请输入" + item.Description + "' name='" + item.ColumnsName + "' id='" + item.ColumnsName + "' lay-filter='column' class='layui-input'/></div></div>";
                            } else if (item.ColumnType.toLowerCase() === "nvarchar(MAX)".toLowerCase()) {
                                target = "<div  class='layui-form-item layui-form-text as_a_doubleclick'><label class='layui-form-label'>" + item.Description + "</label><div class='layui-input-block'><textarea placeholder='请输入" + item.Description + "' name='" + item.ColumnsName + "' id='" + item.ColumnsName + "' class='layui-textarea baiduEditor'></textarea></div> </div>";
                            }
                            else if (item.ColumnType.toLowerCase() === "datetime(8)".toLowerCase()) {
                                target = "<div  class='layui-col-md6 asxsyd92move as_a_singleclick' ><div class='layui-form-item'><label class='layui-form-label'>" + item.Description + "</label><div class='layui-input-block'><input lay-verify='required' placeholder='请输入" + item.Description + "' name='" + item.ColumnsName + "' id='" + item.ColumnsName + "' lay-filter='column' class='layui-input as-datetime'/></div></div>";

                            }
                            else {
                                target = "<div  class='layui-col-md6 asxsyd92move as_a_singleclick' ><div class='layui-form-item'><label class='layui-form-label'>" + item.Description + "</label><div class='layui-input-block'><input lay-verify='required' placeholder='请输入" + item.Description + "' name='" + item.ColumnsName + "' id='" + item.ColumnsName + "' lay-filter='column' class='layui-input'/></div></div>";
                            }
                            $('._two').append(target);
                            if (!fs) {
                                $("#_fmname").val(item.TableName);
                                $("#_fmname").attr("disabled", "disabled");
                                $("#_rormname").val(item.TableName);
                                fs = true;
                            }
                         

                        });
                        //生成尾部
                        $('._two').append("<div class='asxsyd92move as_a_spanclick' id='_asxstisp'><div  class='layui-form-item'><blockquote  class='layui-elem-quote' style='text-align: left;color:red'>这里是填写注意事项说明：<br />1、这里是描述；<br />2、说明。</blockquote><div/></div>");

                        $('.asxsyd92move').arrangeable();
                        form.render(); //更新全部
                        utils.ListeningButton();
                    } else {
                        $(".asxsud92form").html(dat.data.DesignHtml);
                        $('.asxsyd92move').arrangeable();
                        $("#_rormname").val(dat.data.Url.split("Debug/")[1].replace(".html", ""));
                        //设置自读项
                        $("#_fmname").attr("disabled", "disabled");
                        form.render(); //更新全部 
                        utils.ListeningButton();
                    }

                });
            }
            return false;
        },

        HtmlRender: function (id) {
            var getTpl = id.innerHTML,
                view = $('#_html_contens');
            view.html(getTpl);
            //laytpl(getTpl).render(null, function (html) {
            //    view.html(getTpl);
            //});
            form.render();

        }

    }
    if (common.getRequest().tabid !== null && common.getRequest().tabid !== undefined) {
        utils.table(common.getRequest().tabid); $("#_fmname").val(common.getRequest().tabid);
    }
    String.prototype.replaceAll = function (text, newtext) {
        var retext = new RegExp(text, "g");
        return this.replace(retext, newtext);
    }
    //冲突检测
    $("#_rormname").blur(function () {
        var _this = $(this);
        var Name = _this.val();
        if (Name === "" || Name === null || Name === undefined) {
            return false;
        }
        common.get("/api/FormDesign/GetCheckName", { Name: Name}, function (resp) {
            var ks = JSON.parse(resp);
            if (ks.Success) {
                layer.msg(ks.msg);
                $('#_rormname').val(Name);
            } else {
                layer.alert(ks.msg);
                $('#_rormname').val("");
            }
           
        });
        //给两个文本框赋值    
        $('#_rormname').val("");

    });
    exports('FormDesignByWeb', utils);

});

