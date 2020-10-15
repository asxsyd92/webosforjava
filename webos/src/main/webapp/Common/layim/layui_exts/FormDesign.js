/**

 @Name：FormDesign layui路由扩展v0.01
 @Author：爱上歆随懿恫|http://www.asxsyd92.com
 @Url：http://www.asxsyd92.com
 @License：MIT
*/
var $ = null;
layui.define(['jquery', 'layer', 'form','common'], function (exports) {

    $ = layui.jquery;
    form = layui.form; layer = layui.layer, common = layui.common;
        var util = {
            SingleClick :function(_this) {
        var thats = $(_this);
        console.log(thats);
        var key = thats.find('.layui-form-label').eq(0);
        var value = thats.find('.layui-input').eq(0);

        layer.open({
            type: 0,
            title: '字段属性编辑',
            skin: 'layui-layer-rim',
            area: ['750px', 'auto'],
            content: ' <div class="row" style=" margin-left:7px; margin-top:10px;">'
            + '<div class="col-sm-12">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 标题:</span>'
            + '<input id="fd_title" type="text" class="layui-input" placeholder="请输入显示标题">'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> Name  :</span>'
            + '<input id="fd_id" type="text" class="layui-input" placeholder="请再唯一Name">'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 值  :</span>'
            + '<input id="fd_value" type="text" class="layui-input" placeholder="请再输入默认值">'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon">类型:</span>'
            + '<select id="fd_type" lay-verify="" style="height: 34px;width:100%; padding: 6px 12px; font-size: 14px; line-height: 1.42857143;color: #555;background-color: #fff; background-image: none;border: 1px solid #ccc; border-radius: 4px;" ><option>button</option><option>checkbox</option><option>color</option><option>date</option><option>datetime</option><option>datetime-local</option><option>email</option> <option>hidden</option><option>image</option><option>password</option><option>number</option><option>tel</option><option>text</option></select>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 必填:</span>'
            + '<input type="radio" name="fd_required" id="fd_required" value="是" title="是"><label for="fd_required">是</label><input type="radio" name="fd_required" id="fd_required" value="否" title="否"><label for="fd_required">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 编辑:</span>'
            + '<input type="radio" name="fd_disabled" id="fd_disabled" value="是" title="是"><label for="fd_disabled" checked>是</label><input type="radio" name="fd_disabled" id="fd_disabled" value="否" title="否"><label for="fd_disabled">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 隐藏:</span>'
            + '<input type="radio" name="fd_hide" id="fd_hide" value="是" title="是"><label for="fd_hide" checked>是</label><input type="radio" name="fd_hide" id="fd_hide" value="否" title="否"><label for="fd_hide">否</label>'
            + '</div>'
            + '</div>'
            + '</div>'
            ,
            btn: ['保存', '取消', '删除'],
            success: function (layero, index) {
                $("#fd_type").val(value[0].type),
                    $("#fd_id").val(value[0].id),
                    $("#fd_title").val(key[0].innerText),
                    $("#fd_value").val(value[0].value),
                    fd_required = value[0].getAttribute('lay-verify');
                if (fd_required === null || fd_required === "") { $("input:radio[name=fd_required][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_required][value='是']").attr('checked', 'true'); }

                fd_disabled = value[0].getAttribute('disabled');
                if (fd_disabled === null || fd_disabled === "") { $("input:radio[name=fd_disabled][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_disabled][value='是']").attr('checked', 'true'); }

                fd_hide = $(".ashide");
                if (fd_hide.length === 0) { $("input:radio[name=fd_hide][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_hide][value='是']").attr('checked', 'true'); }

            },
            btn1: function (index, layero) {
                var fd_type = $("#fd_type").val(),
                    fd_id = $("#fd_id").val(),
                    fd_title = $("#fd_title").val(),
                    fd_required = $('input:radio[name="fd_required"]:checked').val(),
                    fd_value = $("#fd_value").val(),
                    fd_disabled = $('input:radio[name="fd_disabled"]:checked').val(),
                    fd_hide = $('input:radio[name="fd_hide"]:checked').val();
                if ($.trim(fd_id).length > 0 && $.trim(fd_title).length > 0) {
                    try {
                        key.data('title', fd_title).html(fd_title);

                        switch (value[0].tagName.toLowerCase()) {
                            case "input":
                                value[0].id = fd_id, value[0].name = fd_id, value[0].type = fd_type, value[0].placeholder = "请输入" + fd_title, value[0].value = fd_value;
                                if (fd_required === "是") { $(value).attr("lay-verify", "required"); } else { $(value).removeAttr("lay-verify"); }
                                if (fd_disabled === "是") { $(value).attr("disabled", "disabled"); } else { $(value).removeAttr("disabled"); }
                                if (fd_hide === "是") {

                                    $(thats).attr("class", "layui-col-md6 ashide");
                                } else { $(thats).attr("class", "layui-col-md6"); }
                                break;

                        }
                        layer.close(index);
                        layer.msg("修改成功");


                    } catch (e) {
                        layer.close(index);
                        layer.msg("修改失败" + e);
                        console.log(e);
                    }


                } else { layer.msg("设置未成功，标题和唯一Name标识为必填项！"); }
            },
            btn2: function (index, layero) {
                layer.close(index);
            },
            btn3: function (index, layero) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
            }

        });

    },
    //编辑框的处理方法
             DoubleClick:function(_this) {
        var thats = $(_this);

        var key = thats.find('.layui-form-label').eq(0);
        var value = thats.find('.layui-textarea').eq(0);

        layer.open({
            id: 1,
            type: 0,
            title: '字段属性编辑',
            skin: 'layui-layer-rim',
            area: ['750px', 'auto'],
            //
            content: ' <div class="row" style="width: 700px;  margin-left:7px; margin-top:10px;">'
            + '<div class="col-sm-12">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 标题:</span>'
            + '<input id="fd_title" type="text" class="layui-input" placeholder="请输入显示标题">'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> Name  :</span>'
            + '<input id="fd_id" type="text" class="layui-input" placeholder="请再唯一Name">'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 值  :</span>'
            + '<input id="fd_value" type="text" class="layui-input" placeholder="请再输入默认值">'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon">类型:</span>'
            + '<select id="fd_type" lay-verify="" style="height: 34px;width:100%; padding: 6px 12px; font-size: 14px; line-height: 1.42857143;color: #555;background-color: #fff; background-image: none;border: 1px solid #ccc; border-radius: 4px;" ><option>button</option><option>checkbox</option><option>color</option><option>date</option><option>datetime</option><option>datetime-local</option><option>email</option> <option>hidden</option><option>image</option><option>password</option><option>number</option><option>tel</option><option>text</option></select>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 必填:</span>'
            + '<input type="radio" name="fd_required" id="fd_required" value="是" title="是"><label for="fd_required">是</label><input type="radio" name="fd_required" id="fd_required" value="否" title="否"><label for="fd_required">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 编辑:</span>'
            + '<input type="radio" name="fd_disabled" id="fd_disabled" value="是" title="是"><label for="fd_disabled" checked>是</label><input type="radio" name="fd_disabled" id="fd_disabled" value="否" title="否"><label for="fd_disabled">否</label>'
            + '</div>'
            + '</div>'
            + '</div>'
            ,
            btn: ['保存', '取消', '删除'],
            success: function (layero, index) {
                $("#fd_type").val(value[0].type), $("#fd_id").val(value[0].id), $("#fd_title").val(key[0].innerText), $("#fd_value").val(value[0].value),
                    fd_required = value[0].getAttribute('lay-verify');
                if (fd_required === null || fd_required === "") { $("input:radio[name=fd_required][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_required][value='是']").attr('checked', 'true'); }
                fd_disabled = value[0].getAttribute('disabled');
                if (fd_disabled === null || fd_disabled === "") { $("input:radio[name=fd_disabled][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_disabled][value='是']").attr('checked', 'true'); }
                fd_hide = $(".ashide");
                if (fd_hide.length === 0) { $("input:radio[name=fd_hide][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_hide][value='是']").attr('checked', 'true'); }
            },
            btn1: function (index, layero) {
                var fd_type = $("#fd_type").val(),
                    fd_id = $("#fd_id").val(),
                    fd_title = $("#fd_title").val(),
                    fd_required = $('input:radio[name="fd_required"]:checked').val(),
                    fd_value = $("#fd_value").val(),
                    fd_disabled = $('input:radio[name="fd_disabled"]:checked').val(),
                    fd_hide = $('input:radio[name="fd_hide"]:checked').val();
                if ($.trim(fd_id).length > 0 && $.trim(fd_title).length > 0) {
                    try {
                        key.data('title', fd_title).html(fd_title);

                        switch (value[0].tagName.toLowerCase()) {
                            case "textarea": value[0].id = fd_id, value[0].name = fd_id, value[0].type = fd_type, value[0].placeholder = "请输入" + fd_title, value[0].value = fd_value;
                                if (fd_required === "是") { $(value).attr("lay-verify", "required"); } else { $(value).removeAttr("lay-verify"); }
                                if (fd_disabled === "是") { $(value).attr("disabled", "disabled"); } else { $(value).removeAttr("disabled"); }
                                if (fd_hide === "是") {
                                    $(thats).attr("class", "layui-col-md6 ashide");
                                } else { $(thats).attr("class", "layui-col-md6"); }
                                break;

                        }
                        layer.close(index);
                        layer.msg("修改成功");

                    } catch (e) {
                        layer.close(index);
                        layer.msg("修改失败" + e);
                        console.log(e);
                    }


                } else { layer.msg("设置未成功，标题和唯一Name标识为必填项！"); }
            },
            btn2: function (index, layero) {
                layer.close(index);
            },
            btn3: function (index, layero) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
            }

        });

    },
             AddHtml: function (_this) {
        var that = $(_this);
        var target = null;
        target = that.data('target');
        console.log(target);
        $('._two').append(target);
        $('.layui-col-md6').arrangeable();
        form.render(); //更新全部
        $('.layui-form-item').arrangeable();
    },
             DropDown: function (_this) {
        var thats = $(_this);
        console.log(thats);
        var key = thats.find('.layui-form-label').eq(0);
        var value = thats.find("select").eq(0);// thats.find('.layui-input').eq(0);
        //if (value.length === 0) {
        //    value = thats.find('.layui-textarea').eq(0);
        //}
        layer.open({
            type: 0,
            title: '字段属性编辑',
            skin: 'layui-layer-rim',
            area: ['750px', 'auto'],
            content: ' <div class="row" style=" margin-left:7px; margin-top:10px;">'
            + '<div class="col-sm-12">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 标题:</span>'
            + '<input id="fd_title" type="text" class="layui-input" placeholder="请输入显示标题">'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> Name  :</span>'
            + '<input id="fd_id" type="text" class="layui-input" placeholder="请再唯一Name">'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 字典:填写数据字典中的ID guid</span>'
            + '<input id="fd_value" type="text" class="layui-input" placeholder="请再输入默认值">'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon">值:填写下拉值</span>'
            + '<textarea id="fd_type" type="text" class="layui-textarea" placeholder="请再输入"/>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 必填:</span>'
            + '<input type="radio" name="fd_required" id="fd_required" value="是" title="是"><label for="fd_required">是</label><input type="radio" name="fd_required" id="fd_required" value="否" title="否"><label for="fd_required">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 编辑:</span>'
            + '<input type="radio" name="fd_disabled" id="fd_disabled" value="是" title="是"><label for="fd_disabled" checked>是</label><input type="radio" name="fd_disabled" id="fd_disabled" value="否" title="否"><label for="fd_disabled">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 隐藏:</span>'
            + '<input type="radio" name="fd_hide" id="fd_hide" value="是" title="是"><label for="fd_hide" checked>是</label><input type="radio" name="fd_hide" id="fd_hide" value="否" title="否"><label for="fd_hide">否</label>'
            + '</div>'
            + '</div>'
            + '</div>'
            ,
            btn: ['保存', '取消', '删除'],
            success: function (layero, index) {
                if (value[0].dataset !== null) {
                    try { $("#fd_value").val(value[0].dataset.target); }
                    catch (ex) { console.log(ex); }

                }

                $("#fd_type").val($(value).html());
                $("#fd_id").val(value[0].id), $("#fd_title").val(key[0].innerText),
                    fd_required = value[0].getAttribute('lay-verify');
                if (fd_required === null || fd_required === "") { $("input:radio[name=fd_required][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_required][value='是']").attr('checked', 'true'); }
                fd_disabled = value[0].getAttribute('disabled');
                if (fd_disabled === null || fd_disabled === "") { $("input:radio[name=fd_disabled][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_disabled][value='是']").attr('checked', 'true'); }

                fd_hide = $(".ashide");
                if (fd_hide.length === 0) { $("input:radio[name=fd_hide][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_hide][value='是']").attr('checked', 'true'); }


            },
            btn1: function (index, layero) {
                console.log(thats);
                var fd_types = $("#fd_type").data("target");

                var fd_type = $("#fd_type").val(),
                    fd_id = $("#fd_id").val(),
                    fd_title = $("#fd_title").val(),
                    fd_required = $('input:radio[name="fd_required"]:checked').val(),
                    fd_value = $("#fd_value").val(),
                    fd_disabled = $('input:radio[name="fd_disabled"]:checked').val(),
                    fd_hide = $('input:radio[name="fd_hide"]:checked').val();
                if ($.trim(fd_id).length > 0 && $.trim(fd_title).length > 0) {
                    try {
                        key.data('title', fd_title).html(fd_title);
                        switch (value[0].tagName.toLowerCase()) {
                            case "select":

                                if ($("#fd_value").val().length > 0) {
                                    $(value).attr("data-target", $("#fd_value").val());
                                } else {
                                    console.log(fd_type);
                                    value.html(fd_type); $(value).attr("data-target", "");
                                }

                                value[0].id = fd_id, value[0].name = fd_id, value[0].placeholder = "请输入" + fd_title;
                                if (fd_required === "是") { $(value).attr("lay-verify", "required"); } else { $(value).removeAttr("lay-verify"); }
                                if (fd_disabled === "是") { $(value).attr("disabled", "disabled"); } else { $(value).removeAttr("disabled"); }
                                if (fd_hide === "是") {
                                    $(thats).attr("class", "layui-col-md6 ashide");
                                } else { $(thats).attr("class", "layui-col-md6"); }

                                break;

                        }
                        layer.close(index);
                        layer.msg("修改成功");

                    } catch (e) {
                        layer.close(index);
                        layer.msg("修改失败" + e);
                        console.log(e);
                    }


                } else { layer.msg("设置未成功，标题和唯一Name标识为必填项！"); }
            },
            btn2: function (index, layero) {
                layer.close(index);
            },
            btn3: function (index, layero) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
            }

        });
    },
     RadioDown: function (_this) {
        var thats = $(_this);
        console.log(thats);
        var key = thats.find('.layui-form-label').eq(0);
        var value = thats.find("input");// thats.find('.layui-input').eq(0);
        layer.open({
            type: 0,
            title: '字段属性编辑',
            skin: 'layui-layer-rim',
            area: ['750px', 'auto'],
            content: ' <div class="row" style=" margin-left:7px; margin-top:10px;">'
            + '<div class="col-sm-12">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 标题:</span>'
            + '<input id="fd_title" type="text" class="layui-input" placeholder="请输入显示标题">'
            + '</div>'
            + '</div>'

            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon">值:填写单选值，Name对于数据库中字段</span>'
            + '<textarea id="fd_type" type="text" class="layui-textarea" placeholder="请再输入"/>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 必填:</span>'
            + '<input type="radio" name="fd_required" id="fd_required" value="是" title="是"><label for="fd_required">是</label><input type="radio" name="fd_required" id="fd_required" value="否" title="否"><label for="fd_required">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 编辑:</span>'
            + '<input type="radio" name="fd_disabled" id="fd_disabled" value="是" title="是"><label for="fd_disabled" checked>是</label><input type="radio" name="fd_disabled" id="fd_disabled" value="否" title="否"><label for="fd_disabled">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 隐藏:</span>'
            + '<input type="radio" name="fd_hide" id="fd_hide" value="是" title="是"><label for="fd_hide" checked>是</label><input type="radio" name="fd_hide" id="fd_hide" value="否" title="否"><label for="fd_hide">否</label>'
            + '</div>'
            + '</div>'
            + '</div>'
            ,
            btn: ['保存', '取消', '删除'],
            success: function (layero, index) {
                //if (value[0].dataset !== null) {
                //    try { $("#fd_value").val(value[0].dataset.target); }
                //    catch (ex) { console.log(ex); }

                //}
                if (value.length > 0) {
                    var rhml = "";
                    $.each(value, function (index, item, arr) {
                        rhml += item.outerHTML + "\n";
                    });
                    $("#fd_type").val(rhml);
                }

                $("#fd_id").val(value[0].id), $("#fd_title").val(key[0].innerText),
                    fd_required = value[0].getAttribute('lay-verify');
                if (fd_required === null || fd_required === "") { $("input:radio[name=fd_required][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_required][value='是']").attr('checked', 'true'); }
                fd_disabled = value[0].getAttribute('disabled');
                if (fd_disabled === null || fd_disabled === "") { $("input:radio[name=fd_disabled][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_disabled][value='是']").attr('checked', 'true'); }

                fd_hide = $(".ashide");
                if (fd_hide.length === 0) { $("input:radio[name=fd_hide][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_hide][value='是']").attr('checked', 'true'); }


            },
            btn1: function (index, layero) {
                console.log(thats);
                //var fd_types = $("#fd_type").data("target");

                var fd_type = $("#fd_type").val(),
                    fd_id = $("#fd_id").val(),
                    fd_title = $("#fd_title").val(),
                    fd_required = $('input:radio[name="fd_required"]:checked').val(),
                    fd_value = $("#fd_value").val(),
                    fd_disabled = $('input:radio[name="fd_disabled"]:checked').val(),
                    fd_hide = $('input:radio[name="fd_hide"]:checked').val();
                if ($.trim(fd_title).length > 0) {
                    try {
                        key.data('title', fd_title).html(fd_title);
                        switch (value[0].tagName.toLowerCase()) {
                            case "input":
                                $(".asRadio").html(fd_type);
                                if (fd_required === "是") { $(value).attr("lay-verify", "required"); } else { $(value).removeAttr("lay-verify"); }
                                if (fd_disabled === "是") { $(value).attr("disabled", "disabled"); } else { $(value).removeAttr("disabled"); }
                                if (fd_hide === "是") {
                                    $(thats).attr("class", "layui-col-md6 ashide");
                                } else { $(thats).attr("class", "layui-col-md6"); }
                                layui.use(['form'], function () {
                                    form = layui.form; form.render();
                                });
                                break;

                        }
                        layer.close(index);
                        layer.msg("修改成功");

                    } catch (e) {
                        layer.close(index);
                        layer.msg("修改失败" + e);
                        console.log(e);
                    }


                } else { layer.msg("设置未成功，标题和唯一Name标识为必填项！"); }
            },
            btn2: function (index, layero) {
                layer.close(index);
            },
            btn3: function (index, layero) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
            }

        });
    },
     CheckBox: function (_this) {
        var thats = $(_this);
        console.log(thats);
        var key = thats.find('.layui-form-label').eq(0);
        var value = thats.find("input");// thats.find('.layui-input').eq(0);
        layer.open({
            type: 0,
            title: '字段属性编辑',
            skin: 'layui-layer-rim',
            area: ['750px', 'auto'],
            content: ' <div class="row" style=" margin-left:7px; margin-top:10px;">'
            + '<div class="col-sm-12">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 标题:</span>'
            + '<input id="fd_title" type="text" class="layui-input" placeholder="请输入显示标题">'
            + '</div>'
            + '</div>'

            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon">值:填写单选值，Name对于数据库中字段</span>'
            + '<textarea id="fd_type" type="text" class="layui-textarea" placeholder="请再输入"/>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 必填:</span>'
            + '<input type="radio" name="fd_required" id="fd_required" value="是" title="是"><label for="fd_required">是</label><input type="radio" name="fd_required" id="fd_required" value="否" title="否"><label for="fd_required">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 编辑:</span>'
            + '<input type="radio" name="fd_disabled" id="fd_disabled" value="是" title="是"><label for="fd_disabled" checked>是</label><input type="radio" name="fd_disabled" id="fd_disabled" value="否" title="否"><label for="fd_disabled">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 隐藏:</span>'
            + '<input type="radio" name="fd_hide" id="fd_hide" value="是" title="是"><label for="fd_hide" checked>是</label><input type="radio" name="fd_hide" id="fd_hide" value="否" title="否"><label for="fd_hide">否</label>'
            + '</div>'
            + '</div>'
            + '</div>'
            ,
            btn: ['保存', '取消', '删除'],
            success: function (layero, index) {
                //if (value[0].dataset !== null) {
                //    try { $("#fd_value").val(value[0].dataset.target); }
                //    catch (ex) { console.log(ex); }

                //}
                if (value.length > 0) {
                    var rhml = "";
                    $.each(value, function (index, item, arr) {
                        rhml += item.outerHTML + "\n";
                    });
                    $("#fd_type").val(rhml);
                }

                $("#fd_id").val(value[0].id), $("#fd_title").val(key[0].innerText),
                    fd_required = value[0].getAttribute('lay-verify');
                if (fd_required === null || fd_required === "") { $("input:radio[name=fd_required][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_required][value='是']").attr('checked', 'true'); }
                fd_disabled = value[0].getAttribute('disabled');
                if (fd_disabled === null || fd_disabled === "") { $("input:radio[name=fd_disabled][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_disabled][value='是']").attr('checked', 'true'); }

                fd_hide = $(".ashide");
                if (fd_hide.length === 0) { $("input:radio[name=fd_hide][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_hide][value='是']").attr('checked', 'true'); }


            },
            btn1: function (index, layero) {
                console.log(thats);
                //var fd_types = $("#fd_type").data("target");

                var fd_type = $("#fd_type").val(),
                    fd_id = $("#fd_id").val(),
                    fd_title = $("#fd_title").val(),
                    fd_required = $('input:radio[name="fd_required"]:checked').val(),
                    fd_value = $("#fd_value").val(),
                    fd_disabled = $('input:radio[name="fd_disabled"]:checked').val(),
                    fd_hide = $('input:radio[name="fd_hide"]:checked').val();
                if ($.trim(fd_title).length > 0) {
                    try {
                        key.data('title', fd_title).html(fd_title);
                        switch (value[0].tagName.toLowerCase()) {
                            case "input":
                                $(".asCheckBox").html(fd_type);
                                if (fd_required === "是") { $(value).attr("lay-verify", "required"); } else { $(value).removeAttr("lay-verify"); }
                                if (fd_disabled === "是") { $(value).attr("disabled", "disabled"); } else { $(value).removeAttr("disabled"); }
                                if (fd_hide === "是") {
                                    $(thats).attr("class", "layui-col-md6 ashide");
                                } else { $(thats).attr("class", "layui-col-md6"); }
                                layui.use(['form'], function () {
                                    form = layui.form; form.render();
                                });
                                break;

                        }
                        layer.close(index);
                        layer.msg("修改成功");

                    } catch (e) {
                        layer.close(index);
                        layer.msg("修改失败" + e);
                        console.log(e);
                    }


                } else { layer.msg("设置未成功，标题和唯一Name标识为必填项！"); }
            },
            btn2: function (index, layero) {
                layer.close(index);
            },
            btn3: function (index, layero) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
            }

        });
    },

     BaiduUpload: function (_this) {
        var thats = $(_this);
        console.log(thats);
        var key = thats.find('.layui-form-label').eq(0);
        var value = thats.find("input");// thats.find('.layui-input').eq(0);
        layer.open({
            type: 0,
            title: '字段属性编辑',
            skin: 'layui-layer-rim',
            area: ['750px', 'auto'],
            content: ' <div class="row" style=" margin-left:7px; margin-top:10px;">'
            + '<div class="col-sm-12">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 标题:</span>'
            + '<input id="fd_title" type="text" class="layui-input" placeholder="请输入显示标题">'
            + '</div>'
            + '</div>'

            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon">值:填写单选值，Name对于数据库中字段,多个上传请确保name和id不重复</span>'
            + '<textarea id="fd_type" type="text" class="layui-textarea" placeholder="请再输入"/>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 必填:</span>'
            + '<input type="radio" name="fd_required" id="fd_required" value="是" title="是"><label for="fd_required">是</label><input type="radio" name="fd_required" id="fd_required" value="否" title="否"><label for="fd_required">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 编辑:</span>'
            + '<input type="radio" name="fd_disabled" id="fd_disabled" value="是" title="是"><label for="fd_disabled" checked>是</label><input type="radio" name="fd_disabled" id="fd_disabled" value="否" title="否"><label for="fd_disabled">否</label>'
            + '</div>'
            + '</div>'
            + '<div class="col-sm-12" style="margin-top: 10px">'
            + '<div class="input-group">'
            + '<span class="input-group-addon"> 隐藏:</span>'
            + '<input type="radio" name="fd_hide" id="fd_hide" value="是" title="是"><label for="fd_hide" checked>是</label><input type="radio" name="fd_hide" id="fd_hide" value="否" title="否"><label for="fd_hide">否</label>'
            + '</div>'
            + '</div>'
            + '</div>'
            ,
            btn: ['保存', '取消', '删除'],
            success: function (layero, index) {

                $("#fd_type").val($(".baiduupload").html());
                //if (value.length > 0) {
                //    var rhml = "";
                //    $.each(value, function (index, item, arr) {
                //        rhml += item.outerHTML + "\n";
                //    });

                //}

                $("#fd_id").val(value[0].id), $("#fd_title").val(key[0].innerText),
                    fd_required = value[0].getAttribute('lay-verify');
                if (fd_required === null || fd_required === "") { $("input:radio[name=fd_required][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_required][value='是']").attr('checked', 'true'); }
                fd_disabled = value[0].getAttribute('disabled');
                if (fd_disabled === null || fd_disabled === "") { $("input:radio[name=fd_disabled][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_disabled][value='是']").attr('checked', 'true'); }

                fd_hide = $(".ashide");
                if (fd_hide.length === 0) { $("input:radio[name=fd_hide][value='否']").attr('checked', 'true'); }
                else { $("input:radio[name=fd_hide][value='是']").attr('checked', 'true'); }


            },
            btn1: function (index, layero) {
                console.log(thats);
                //var fd_types = $("#fd_type").data("target");

                var fd_type = $("#fd_type").val(),
                    fd_id = $("#fd_id").val(),
                    fd_title = $("#fd_title").val(),
                    fd_required = $('input:radio[name="fd_required"]:checked').val(),
                    fd_value = $("#fd_value").val(),
                    fd_disabled = $('input:radio[name="fd_disabled"]:checked').val(),
                    fd_hide = $('input:radio[name="fd_hide"]:checked').val();
                if ($.trim(fd_title).length > 0) {
                    try {
                        key.data('title', fd_title).html(fd_title);
                        switch (value[0].tagName.toLowerCase()) {
                            case "input":
                                $(".baiduupload").html(fd_type);
                                if (fd_required === "是") { $(value).attr("lay-verify", "required"); } else { $(value).removeAttr("lay-verify"); }
                                if (fd_disabled === "是") { $(value).attr("disabled", "disabled"); } else { $(value).removeAttr("disabled"); }
                                if (fd_hide === "是") {
                                    $(thats).attr("class", "layui-col-md6 ashide");
                                } else { $(thats).attr("class", "layui-col-md6"); }
                                layui.use(['form'], function () {
                                    form = layui.form; form.render();
                                });
                                break;

                        }
                        layer.close(index);
                        layer.msg("修改成功");

                    } catch (e) {
                        layer.close(index);
                        layer.msg("修改失败" + e);
                        console.log(e);
                    }


                } else { layer.msg("设置未成功，标题和唯一Name标识为必填项！"); }
            },
            btn2: function (index, layero) {
                layer.close(index);
            },
            btn3: function (index, layero) {
                if (confirm('确定要删除吗？')) {
                    console.log(thats);
                    var parent = thats;
                    parent.remove();
                }
            }

        });
     },
     yulan: function(){
        if ($("#_fmname").val().length <= 0) {
            layer.msg("请填写名称");
            return false;
        }
        //添加表名
        if ($("#_table") !== null) {
            var tab = "<input lay-verify='required' name='_table' id='_table' value='" + $("#_fmname").val() + "' lay-filter='column' class='layui-input' style='display: none;' />";
            $(".layui-row").append(tab);
            form.render(); //更新全部
        }

        //页面层
        $.ajax({

            type: "Post",
            url: "/api/FormDesign/FormDesignHtml",
            //  headers: {"Authorization": "bearer " + window.sessionStorage["token"] },
            data: { html: $(".layui-form").html(), name: $("#_fmname").val(), type: true },
            //dataType: "text/html; charset=utf-8",
            success: function (data) {
                layer.open({
                    type: 2,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['100%', '100%'], //宽高
                    content: "/" + data
                });
                // setForm("forms", data.data);
            },
            error: function (txt) {
                layer.alert("网络异常错误，请稍后再试", { title: "温馨提示" });

            }
        });

    },

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
     table: function (key) {
         $('.draggable-element').arrangeable();
         console.log(key);
         if (key !== null) {

             $.get('/api/Form/FormTable?table=' + key, {}, function (res) {
                 //
                
                 var dat = JSON.parse(res);
                 var target = "";
                 $('._two').append(target);
                 var fs = false;
                 console.log(dat.data);
                 $.each(dat.data, function (i, item) {
                     if (item.ColumnType.toLowerCase() === "uniqueidentifier".toLowerCase()) {
                         target = "<div onclick='FormDesign.SingleClick(this)' class='layui-col-md6' ><label class='layui-form-label'>" + item.Description + "</label><div class='layui-input-block'><input lay-verify='required' value='00000000-0000-0000-0000-000000000000' placeholder='请输入" + item.Description + "' name='" + item.ColumnsName + "' id='" + item.ColumnsName + "' lay-filter='column' class='layui-input'/></div>";
                     } else if (item.ColumnType.toLowerCase() === "nvarchar(MAX)".toLowerCase()) {
                         target = "<div onclick='FormDesign.DoubleClick(this)' class='layui-form-item layui-form-text'><label class='layui-form-label'>" + item.Description + "</label><div class='layui-input-block'><textarea placeholder='请输入" + item.Description + "' name='" + item.ColumnsName + "' id='" + item.ColumnsName + "' class='layui-textarea baiduEditor'></textarea></div> </div>";
                     }
                     else if (item.ColumnType.toLowerCase() === "datetime(8)".toLowerCase()) {
                         target = "<div onclick='FormDesign.SingleClick(this)' class='layui-col-md6' ><label class='layui-form-label'>" + item.Description + "</label><div class='layui-input-block'><input lay-verify='required' placeholder='请输入" + item.Description + "' name='" + item.ColumnsName + "' id='" + item.ColumnsName + "' lay-filter='column' class='layui-input as-datetime'/></div>";

                     }
                     else {
                         target = "<div onclick='FormDesign.SingleClick(this)' class='layui-col-md6' ><label class='layui-form-label'>" + item.Description + "</label><div class='layui-input-block'><input lay-verify='required' placeholder='请输入" + item.Description + "' name='" + item.ColumnsName + "' id='" + item.ColumnsName + "' lay-filter='column' class='layui-input'/></div>";
                     }
                     $('._two').append(target);
                     if (!fs) {
                         $("#_fmname").val(item.TableName); fs = true;
                     }

                 });
                 $('.layui-col-md6').arrangeable();
                 form.render(); //更新全部
             });
         }
         return false;
     }
    }
    exports('FormDesign', util);

});