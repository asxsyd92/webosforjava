var $ = null;
layui.config({
    base: '/Common/layim/layui_exts/' //指定 winui 路径
    , version: '1.0.0-beta'
}).define(['table', 'jquery','transfer', 'winui', 'form', 'common'], function (exports) {
    //全局定义是否为流程
    ISFLOW = false;
    var table = layui.table, form = layui.form, common = layui.common, transfer = layui.transfer
        , layer = layui.layer
        , laydate = layui.laydate; laytpl = layui.laytpl; layedit = layui.layedit; $ = layui.jquery, tepsname="";
    //初始化表单
        $.ajaxSetup({
            headers: { "Authorization": "bearer " + window.sessionStorage["_token"] }
        });
    if (common.getRequest().flowid !== null || common.getRequest().flowid !== undefined) {
        //这是个流程按流程处理并修订ISFLOW
        ISFLOW = true;
        //初始化流程
        $.post("/api/WorkFlowTasks/FlowInit", {
            FlowID: common.getRequest().flowid, stepid: common.getRequest().stepid
        }, function (resp) {
            var teps = JSON.parse(resp);
            console.log(teps);
            if (teps.data.length > 0) {
                $("#nextstepid").val(teps.data[0].ID);
                $("#_tsuers").text(teps.data[0].Name); tepsname = teps.data[0].Name;
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
    } else {
        //不是流程按信息发布出来没有审核
    }


    form.on('submit(_submit)', function (formdata) {
        //提交或者发布设置按流程出来
        if (ISFLOW === true) {
            //获取处理人
            $.get('/api/Users/GetUser', { page: 1, limit: 200 }, function (data) {
                mydata = JSON.parse(data);
                console.log(mydata);
                transfer.render({
                    elem: '#_sendUser'
                    , data: mydata.data
                    , id: '_sendUserdata' //定义索引
                    , parseData: function (mydata) {
                        return {
                            "value": 'u_' + mydata.ID //数据值
                            , "title": mydata.Name //数据标题
                            , "disabled": false  //是否禁用
                            , "checked": false//是否选中
                        };
                    }
                });
                //没有下一步骤直接完成任务
                if ($("#nextstepid").val() === "") {
                    iss = "completed";
                    var opts = {};
                    opts.type = "completed";
                    opts.steps = [];

                    $.post("/api/Form/FormSave", { table: $("#_table").val(), data: JSON.stringify(formdata.field), query: JSON.stringify(query), params1: JSON.stringify(opts) }, function (resp) {
                        resp = JSON.parse(resp);
                        if (resp.Success) {
                            var _frame = common.getRequest().frame;
                            if (_frame !== null) {
                                // console.log(_frames);

                                var fis = top.window.document.getElementById(_frame);
                                if (fis !== null) {
                                    var ks = $(fis);
                                    ks.context.contentWindow.window.location.reload();
                                    var keys = common.getRequest().key;
                                    if (keys !== null) {
                                        top.winui.window.close(keys);
                                    }

                                }

                            }
                        }
                    });
                    return false;
                };
                layer.open({
                    type: 1,
                    title: "请选择处理人" + tepsname,
                    area: ['auto', '80%'],
                    shade: 0.8, btn: ['保存', '取消'],
                    // shadeClose: true,
                    content: $("#_sendUser"),
                    btn1: function (index, layero) {
                        var getData = transfer.getData('_sendUserdata');
                        var arr = new Array();
                        $(getData).each(function (index, us) {
                            arr[index] = us.value;
                        });

                        var opts = {};
                        opts.type = "submit";
                        opts.steps = [];
                        opts.steps.push({ id: $("#nextstepid").val(), member: arr.join(",") });

                        if (opts.steps.length == 0) {
                            layer.msg("没有选择要处理的步骤!");
                            return false;
                        }
                        if (arr.length == 0) {
                            layer.msg("请选择出来人!");
                            return false;
                        }
                        $.post("/api/Form/FormSave", { table: $("#_table").val(), data: JSON.stringify(formdata.field), query: JSON.stringify(query), params1: JSON.stringify(opts) }, function (resp) {
                            resp = JSON.parse(resp);
                            if (resp.Success) {
                                var _frame = common.getRequest().frame;
                                if (_frame !== null) {
                                    // console.log(_frames);

                                    var fis = top.window.document.getElementById(_frame);
                                    if (fis !== null) {
                                        var ks = $(fis);
                                        ks.context.contentWindow.window.location.reload();
                                        var keys = common.getRequest().key;
                                        if (keys !== null) {
                                            top.winui.window.close(keys);
                                        }

                                    }

                                }
                            }
                        });

                    },
                    btn2: function (index, layero) {
                        layer.close(index);
                    }
                });

            }).error(function (er) { layer.alert("网络异常错误，请稍后再试", { title: "温馨提示" }); })



        } else {
            //非流程处理步骤
            //直接发布信息
            $.post("/api/Form/FormCommonTaskSave", { table: $("#_table").val(), data: JSON.stringify(formdata.field) }, function (resp) {
                resp = JSON.parse(resp);
                if (resp.Success) {
                    var _frame = common.getRequest().frame;
                    if (_frame !== null) {
                        // console.log(_frames);

                        var fis = top.window.document.getElementById(_frame);
                        if (fis !== null) {
                            var ks = $(fis);
                            ks.context.contentWindow.window.location.reload();
                            var keys = common.getRequest().key;
                            if (keys !== null) {
                                top.winui.window.close(keys);
                            }

                        }

                    }
                }
            });

        }
        

            return false;
        });
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
    //初始化下拉框
    var asselect = $(".asselect");

    if (asselect.length > 0) {
        $.each(asselect, function (index, item, arr) {
            if (item.id !== null) {
                var fd_types = $("#" + item.id).data("target");
                if (fd_types !== null && fd_types !== "") {
                    $.get("/api/Form/GetDictionaryByID", { id: fd_types }, function (resp) {
                        var mydata = JSON.parse(resp);
                        var datas = { //数据
                            "title": "Layui常用模块"
                            , "list": mydata.data
                        };
                        var getTpl = _a_type.innerHTML,
                            view = document.getElementById(item.id);
                        laytpl(getTpl).render(datas, function (html) {
                            view.innerHTML = html;
                        }); form.render();
                    });
                }
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
    //普通上传

    //百度上传

    //单选框

    //复选框

    form.render();

    var key = common.getRequest().key;
    if (key !== null && key !== "" && key !== undefined) {
        console.log(key);


        steps = [];
        steps.push({ TableName: $("#_table").val(), ColumnsName: "ID", ColumnType: key });
        $.get('/api/Form/GetFormData', { table: $("#_table").val(), where: JSON.stringify(steps) }, function (res) {
            var dat = JSON.parse(res);
            if (dat.length > 0) {
                form.val("addinfos", dat[0]);
                baidubjq();
            }

        });
    } else {
        baidubjq();
    }
    //获取编辑器列表并初始化百度编辑器
    function baidubjq() {
        //获取编辑器列表并初始化百度编辑器
        var baidu = $(".baiduEditor");
        if (baidu.length > 0) {

            $.each(baidu, function (index, item, arr) {
                //code
                console.log(item);
                if (item.id !== null) {
                    ue = UE.getEditor(item.id, {
                        initialFrameWidth: "100%",//设置编辑器宽度%
                        initialFrameHeight: 250,//设置编辑器高度
                        scaleEnabled: true//设置不自动调整高度
                        //scaleEnabled {Boolean} [默认值：false]//是否可以拉伸长高，(设置true开启时，自动长高失效)
                    });
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

        alert("图片上传");
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


    //document.getElementById('j_upload_file_btn').onclick = function () {
    //    var dialog = uploadEditor.getDialog("attachment");
    //    dialog.title = '附件上传';
    //    dialog.render();
    //    dialog.open();
    //};

    // 多图上传动作
    function _beforeInsertImage(t, result) {
        console.log(t);
        var imageHtml = '';
        for (var i in result) {
            $(".asup-value").val(result[i].src);
            $(".as-imgs")[0].src = result[i].src;

            break;
        }

        var form = layui.form;
        form.render(); //更新全部

    }

    //// 附件上传
    function _afterUpfile(t, result) {
        alert();
        var fileHtml = '';
        for (var i in result) {
            fileHtml += result[i].url + ","

        }
        // document.getElementById("src").value = fileHtml;
        console.log(fileHtml);
        $("#source").val(fileHtml);
        var form = layui.form;
        form.render(); //更新全部


        // document.getElementById('upload_file_wrap').innerHTML = fileHtml;
    }
    exports('FromSubmit', {});
});







