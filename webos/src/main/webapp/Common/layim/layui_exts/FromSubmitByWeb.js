/**

 @Name：FromSubmitByWeb.js by web layui 表单生成器v2.0
 @Author：爱上歆随懿恫|http://www.asxsyd92.com
 @Url：http://www.asxsyd92.com
 @License：MIT
*/
var $ = null;
layui.config({
    base: '/Common/layim/layui_exts/' //指定
    , version: '1.0.0-beta'
}).define(['table', 'jquery', 'transfer', 'form', 'common', 'colorpicker', 'iconPicker', 'xmSelect', 'Enumerable'], function (exports) {

    //全局定义是否为流程
    ISFLOW = false;
    var table = layui.table, form = layui.form, common = layui.common, transfer = layui.transfer, Enumerable = layui.Enumerable
        , layer = layui.layer, xmSelect = layui.xmSelect
        , laydate = layui.laydate; laytpl = layui.laytpl; layedit = layui.layedit; $ = layui.jquery, tepsname = "", colorpicker = layui.colorpicker, iconPicker = layui.iconPicker;
    var indexlays = top.layer.msg("正在初始化请稍等。。。", { icon: 16, shade: 0.5, time: 20000000 });
    //检查登录状态
    common.islogin();

    //预览全部只读
    if (common.getRequest().prevew !== null && common.getRequest().prevew !== undefined) {
        $(".submittools").hide();

        var m = $('input,select,textarea,button', $($("form")[0]));
        $.each(m, function (index, item) {
            $(item).prop("disabled", true);
        });


    }
    //初始化当前信息中的默认值
    var mmm = $('input', $($("form")[0]));
    $.each(mmm, function (index, item) {
        switch ($(item).val()) {
            case common.SysOperation._SYS_GETUSERNAME: $(item).val(window.localStorage["user"]); break;
            case common.SysOperation._SYS_GETUSERID: $(item).val(window.localStorage["userid"]); break;
            case common.SysOperation._SYS_DATETIME: $(item).val(common.dateFormat("yyyy-MM-dd", new Date())); break;
            case common.SysOperation._SYS_ORGID: $(item).val(window.localStorage["orid"]); break;
            case common.SysOperation._SYS_ORGNAME: $(item).val(window.localStorage["orname"] ); break;
        };
    });
    if (common.getRequest().flowid !== null && common.getRequest().flowid !== undefined) {
        //这是个流程按流程处理并修订ISFLOW
        ISFLOW = true;
        //初始化流程
        $.post("/api/WorkFlowTasks/FlowInit", {
            FlowID: common.getRequest().flowid, stepid: common.getRequest().stepid
        }, function (resp) {
            var teps = JSON.parse(resp);

            if (teps.data.length > 0) {
                $("#nextstepid").val(teps.data[0].ID);
                $("#_tsuers").text(teps.data[0].Name);
                tepsname = teps.data[0].Name;
            } if (teps.currentStep.length > 0) {
                $("#stepid").val(teps.currentStep.ID);
            }


        });
        //流程公共参数初始化
        query = new Object();
        query.flowid = common.getRequest().flowid;
        query.instanceid = common.getRequest().key;
        query.taskid = common.getRequest().taskid;
        query.stepid = common.getRequest().stepid; //$("#stepid").val();
        query.groupid = common.getRequest().groupid;
        query.Form_TitleField = $(".asxsyd92move").length > 0 ? $(".asxsyd92move")[0].innerText : "未命名的表单";
        query.titleField = $("#Title").length > 0 ? $(".asxsyd92move").val() : "未添加Title字段";
    } else {
        ISFLOW = false;
        //不是流程按信息发布出来没有审核
    }
    var _sendUser_ = null;
    var oslits = null;
    form.on('submit(_submit)', function (formdata) {
        //定义发送者

        //提交或者发布设置按流程出来
        if (ISFLOW === true) {
            //获取处理人

            if (window.sessionStorage["touserslits"] != null && window.sessionStorage["touserslits"] != undefined) {
                mydata = JSON.parse(window.sessionStorage["touserslits"]);

                oslits = Enumerable.From(mydata.data).GroupBy(x => x.Organize).Select(x => ({ name: x.Key(), value: x.source[0].OrganizeID, children: x.source })).ToArray();
                tosend(formdata);
            } else {
                common.get('/api/Users/GetUsersTreeAsync', { page: 1, limit: 200 }, function (data) {
                    window.sessionStorage["touserslits"] = data;
                    mydata = JSON.parse(data);
                    oslits = Enumerable.From(mydata.data).GroupBy(x => x.Organize).Select(x => ({ name: x.Key(), value: x.source[0].OrganizeID, children: x.source })).ToArray();
                    tosend(formdata);
                }, function () {
                    layer.alert("网络错误！");
                });


            }



        } else {
            //非流程处理步骤
            //直接发布信息
            var istask = common.getRequest().istask;
            if (istask !== null && istask !== undefined) {
                istask = true;
            } else { istask = false; }
            common.post("/api/Form/FormCommonTaskSave", { table: $("#_table").val(), data: JSON.stringify(formdata.field), istask: istask }, function (resp) {

                if (resp.Success) {
                    layer.close();
                    layer.alert(resp.msg, {
                        skin: 'layui-layer-lan'
                        , closeBtn: 0
                        , anim: 4 //动画类型
                    });

                    window.location.href = "/webos/Page/CommonTask/CommonTask.html";
                }
            });

        }


        return false;
    });

    function tosend(formdata) {
        if ($("#nextstepid").val() === "") {
            iss = "completed";
            var opts = {};
            opts.type = "completed";
            opts.steps = [];
            common.post("/api/Form/FormSave", { table: $("#_table").val(), data: JSON.stringify(formdata.field), query: JSON.stringify(query), params1: JSON.stringify(opts) }, function (resp) {

                if (resp.Success) {

                    layer.alert(resp.msg, {
                        skin: 'layui-layer-molv' //样式类名
                        , closeBtn: 0
                    }, function () {

                        var id = $(window.frameElement).attr('tab-id');
                        top.window.element.tabDelete('xbs_tab', id);
                    });

                } else {
                    layer.alert(resp.msg, {
                        skin: 'layui-layer-molv' //样式类名
                        , closeBtn: 0
                    }, function () {

                        var id = $(window.frameElement).attr('tab-id');
                        top.window.element.tabDelete('xbs_tab', id);
                    });
                }
            });
            return false;
        };
        var send = layer.open({
            type: 1,
            title: "请选择处理人" + tepsname, //
            area: ['50%', '50%'],
            shade: 0.7, btn: ['保存', '取消'],
            // shadeClose: true,
            content: $("#_sendUser")[0].innerHTML,
            btn1: function (index, layero) {
                var arr = _sendUser_.getValue();
                var u = [];
                $(arr).each(function (index, uss) {
                    //这里注意事项，用户这里加上u_，如果是部门这里加上w_
                    u.push("u_" + uss.ID);

                });
                var opts = {};
                opts.type = "submit";
                opts.steps = [];
                opts.steps.push({ id: $("#nextstepid").val(), member: u.join(",") });

                if (opts.steps.length == 0) {
                    layer.msg("没有选择要处理的步骤!");
                    return false;
                }
                if (arr.length == 0) {
                    layer.msg("请选择处理!");
                    return false;
                }
                layer.close(send);
                common.post("/api/Form/FormSave", { table: $("#_table").val(), data: JSON.stringify(formdata.field), query: JSON.stringify(query), params1: JSON.stringify(opts) }, function (resp) {
                    resp = JSON.parse(resp);
                    if (resp.Success) {

                        layer.alert(resp.msg, {
                            skin: 'layui-layer-molv' //样式类名
                            , closeBtn: 0
                        }, function () {

                            var id = $(window.frameElement).attr('tab-id');
                            top.window.element.tabDelete('xbs_tab', id);
                        });

                    } else {
                        layer.alert(resp.msg, {
                            skin: 'layui-layer-molv' //样式类名
                            , closeBtn: 0
                        }, function () {

                        });
                    }
                });

            },
            btn2: function (index, layero) {
                layer.close(index);
            }, success: function () {
                //渲染组织构架
                _sendUser_ = xmSelect.render({
                    el: '#_sendUser_', filterable: true, radio: true,
                    clickClose: true, data: oslits
                });
            }
        });
    }
    //初始化时间
    var asdatetime = $(".as-datetime");
    if (asdatetime.length > 0) {
        $.each(asdatetime, function (index, item, arr) {
            if (item.id !== null) {
                laydate.render({
                    elem: '#' + item.id
                });
            }

        });
    }

    var ashide = $(".ashide");
    //隐藏
    if (ashide.length > 0) {
        $.each(ashide, function (index, item, arr) {

            $(item).attr("style", "display: none;");


        });
    }
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
    //普通上传

    //百度上传

    //单选框

    //复选框

    form.render();

    var key = common.getRequest().key;
    if (key !== null && key !== "" && key !== undefined) {
        steps = [];
        steps.push({ TableName: $("#_table").val(), ColumnsName: "ID", ColumnType: key });
        $.get('/Tasks/GetFormData', { table: $("#_table").val(), where: JSON.stringify(steps) }, function (res) {
            var dat = JSON.parse(res);
            if (dat.length > 0) {
                //处理图标选择
                //初始化下拉框
                var asselect = $(".asselect");

                if (asselect.length > 0) {
                    $.each(asselect, function (index, item, arr) {
                        if (item.id !== null) {
                            var fd_types = $("#" + item.id).data("target");
                            if (common.getRequest().zhuanti !== null && common.getRequest().zhuanti !== undefined) {
                                $.get("/api/Form/GetDictionaryByID", { id: common.getRequest().zhuanti }, function (resp) {
                                    var mydata = JSON.parse(resp);
                                    var ree = new Array();
                                    ree = dicfor(mydata.data, ree);
                                    ree = Enumerable.From(ree).Distinct().ToArray();
                                    var datas = { //数据
                                        "title": "Layui常用模块"
                                        , "list": ree
                                    };
                                    var getTpl = _a_type.innerHTML,
                                        view = document.getElementById(item.id);
                                    laytpl(getTpl).render(datas, function (html) {
                                        view.innerHTML = html;
                                    });

                                    form.val("addinfos", dat[0]);
                                    form.render();
                                });
                            }
                            //这里只是网站所以从id中取
                            else {
                                if (fd_types !== null && fd_types !== "" && fd_types !== undefined) {
                                    $.get("/api/Form/GetDictionaryByID", { id: fd_types }, function (resp) {
                                        var mydata = JSON.parse(resp);
                                        var ree = new Array();
                                        ree = dicfor(mydata.data, ree);
                                        ree = Enumerable.From(ree).Distinct().ToArray();
                                        var datas = { //数据
                                            "title": "Layui常用模块"
                                            , "list": ree
                                        };
                                        var getTpl = _a_type.innerHTML,
                                            view = document.getElementById(item.id);
                                        laytpl(getTpl).render(datas, function (html) {
                                            view.innerHTML = html;
                                        });
                                        form.val("addinfos", dat[0]);
                                        form.render();
                                    });
                                }
                            }
                        }


                    });
                }

                form.val("addinfos", dat[0]);
                baidubjq();
                form.render();

                var _IconPicker = $(".as-IconPicker");
                if (_IconPicker.length > 0) {
                    try {
                        var iconame = _IconPicker[0].name;
                        $("#" + iconame)
                        /**
                         * 选中图标 （常用于更新时默认选中图标）
                         * @param filter lay-filter
                         * @param iconName 图标名称，自动识别fontClass/unicode
                         */
                        iconPicker.checkIcon(iconame, dat[0][iconame]);
                    } catch (e) {
                        console.log(e);
                    }

                }
                top.layer.close(indexlays);
            }

        });
    } else {
        baidubjq();
        top.layer.close(indexlays);
        //初始化下拉框
        var asselect = $(".asselect");

        if (asselect.length > 0) {
            $.each(asselect, function (index, item, arr) {
                if (item.id !== null) {
                    var fd_types = $("#" + item.id).data("target");
                    if (common.getRequest().zhuanti !== null && common.getRequest().zhuanti !== undefined) {
                        $.get("/api/Form/GetDictionaryByID", { id: common.getRequest().zhuanti }, function (resp) {
                            var mydata = JSON.parse(resp);
                            var ree = new Array();
                            ree = dicfor(mydata.data, ree);
                            ree = Enumerable.From(ree).Distinct().ToArray();
                            var datas = { //数据
                                "title": "Layui常用模块"
                                , "list": ree
                            };
                            var getTpl = _a_type.innerHTML,
                                view = document.getElementById(item.id);
                            laytpl(getTpl).render(datas, function (html) {
                                view.innerHTML = html;
                            });

                            form.render();
                        });
                    }
                    //这里只是网站所以从id中取
                    else {
                        if (fd_types !== null && fd_types !== "" && fd_types !== undefined) {
                            $.get("/api/Form/GetDictionaryByID", { id: fd_types }, function (resp) {
                                var mydata = JSON.parse(resp);
                                var ree = new Array();
                                ree = dicfor(mydata.data, ree);
                                ree = Enumerable.From(ree).Distinct().ToArray();
                                var datas = { //数据
                                    "title": "Layui常用模块"
                                    , "list": ree
                                };
                                var getTpl = _a_type.innerHTML,
                                    view = document.getElementById(item.id);
                                laytpl(getTpl).render(datas, function (html) {
                                    view.innerHTML = html;
                                });
                    
                                form.render();
                            });
                        }
                    }
                }


            });
        }

    }
    //获取编辑器列表并初始化百度编辑器
    function baidubjq() {
        //获取编辑器列表并初始化百度编辑器
        var baidu = $(".baiduEditor");
        if (baidu.length > 0) {

            $.each(baidu, function (index, item, arr) {
                var code = [];

                if (item.id !== null) {
                    if ($("#" + item.id).length > 0) {
                        if ($("#" + item.id)[0].dataset.target !== undefined && $("#" + item.id)[0].dataset.target !== null && $("#" + item.id)[0].dataset.target !== "") {
                            var tools = $("#" + item.id)[0].dataset.target.split(",");
                            $(tools).each(function (i, its) {
                                code.pop(its);
                            });

                        }
                    }
                    if (code.length > 0) {
                        ue = UE.getEditor(item.id, {
                            initialFrameWidth: "100%",//设置编辑器宽度%
                            initialFrameHeight: 250,//设置编辑器高度
                            scaleEnabled: false,//设置不自动调整高度
                            toolbars: code,
                            readonly: common.getRequest().prevew !== null && common.getRequest().prevew !== undefined && common.getRequest().prevew !== "" ? true : false
                            //scaleEnabled {Boolean} [默认值：false]//是否可以拉伸长高，(设置true开启时，自动长高失效)
                        });

                    } else {
                        ue = UE.getEditor(item.id, {
                            initialFrameWidth: "100%",//设置编辑器宽度%
                            initialFrameHeight: 250,//设置编辑器高度
                            scaleEnabled: false,//设置不自动调整高度
                            readonly: common.getRequest().prevew !== null && common.getRequest().prevew !== undefined && common.getRequest().prevew !== "" ? true : false
                            //scaleEnabled {Boolean} [默认值：false]//是否可以拉伸长高，(设置true开启时，自动长高失效)
                        });


                    }

                }


            })
        }
    }



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
        toolbars: [["insertimage", "attachment"]]
    });
    // 自定义按钮绑定触发多图上传和上传附件对话框事件

    $(".j_upload_img_btn").click(function () {
        // 监听多图上传和上传附件组件的插入动作
        uploadEditor.ready(function () {
            uploadEditor.addListener("beforeInsertImage", _beforeInsertImage);
            uploadEditor.addListener("afterUpfile", _afterUpfile);
        });

        var dialog = uploadEditor.getDialog("insertimage");
        dialog.title = '多图上传';
        dialog.render();
        dialog.open();
    });
    $(".j_upload_file_btn").click(function () {

        var dialog = uploadEditor.getDialog("attachment");
        dialog.title = '附件上传';
        dialog.render();
        dialog.open();
    });


    // 多图上传动作
    function _beforeInsertImage(t, result) {

        var imageHtml = [];
        $.each(result, function (index, item, arr) {
            imageHtml.push(item.src);
        });
        $(".as-BaiDuImg").val(imageHtml.join(","));
        var form = layui.form;
        form.render(); //更新全部

    }

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
    //选择组件只允许有一组

    var _IconPicker = $(".as-IconPicker");
    if (_IconPicker.length > 0) {

        iconPicker.render({
            // 选择器，推荐使用input
            elem: '.as-IconPicker',
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

            },
            // 渲染成功后的回调
            success: function (d) {

            }
        });
    }

    var _Colorpicker = $(".colorpicker-form");
    if (_Colorpicker.length > 0) {
        //开启全功能
        colorpicker.render({
            elem: '.colorpicker-form'
            , color: 'rgba(7, 155, 140, 1)'
            , format: 'rgb'
            , predefine: true
            , alpha: true
            , done: function (color) {
                $(".as-Colorpicker").val(color); //向隐藏域赋值
                layer.tips('给指定隐藏域设置了颜色值：' + color, this.elem);
            }
            , change: function (color) {
            }
        });
    }

    exports('FromSubmitByWeb', {});
});







