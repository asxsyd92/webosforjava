var $ = null;
layui.config({
    base: '/Common/layim/layui_exts/' //指定 winui 路径
    , version: '1.0.0-beta'
}).define(['layer', 'element', 'form', 'laydate', 'common', 'workflow'], function (exports) {
    // layui.use(['layer', 'element', 'form', 'laydate'], function () {
    layer = layui.layer, element = layui.element, form = layui.form, $ = layui.$, common = layui.common, workflow = layui.workflow;
    var key = common.getRequest().key;

    if (key !== null && key !== undefined) {
        workflow.openFlow1(key);
    }
    else {
        layer.msg("新建流程");
        workflow.addFlow();
    };

    exports('index', {});
});
//定义全局公共参数
//流程设置
function flowadd() {
    var link_options = '<option value=""></option><%=link_DBConnOptions%>'; //数据连接选项

    var defaultManager = window.sessionStorage["userid"];
        isAdd = common.getRequest().key == null ? true : false;
        flowID = common.getRequest().key == null ? "" : common.getRequest().key;

        $.post("/api/WorkFlow/GetTypeOptions", {}, function (resp) {
            var mysjon = JSON.parse(resp);
            console.log(mysjon);
            var options = "<option></option> ";
            var db = "<option></option> ";
            for (var i = 0; i < mysjon.db.length; i++) {
                options += "<option value='" + mysjon.db[i] + "'>" + mysjon.db[i] + "</option>"
            }
            for (dbi = 0; mysjon.data.length > dbi; dbi++) {
                db += "<option value='" + mysjon.data[dbi].ID + "'>" + mysjon.data[dbi].Title + "</option>"
            }
            document.getElementById("title_tables").innerHTML = options;
            document.getElementById("base_Type").innerHTML = db; form.render(); //更新全部
           
                var json = workflow.wf_json;
                //t_db(json.titleField.link);
                //t_table(json.titleField.table);
            if (json) {
                if (json.id === "") {
                    $("#base_ID").val("00000000-0000-0000-0000-000000000000");
                } else {
                    $("#base_ID").val(json.id);
                }
                    
                    $("#base_Name").val(json.name);
                    $("#base_Type").val(json.type); form.render();
                $("#base_Manager").val("u_" + window.localStorage["userid"]);
                    //new RoadUI.Member().setValue($("#base_Manager"));
                    $("#base_InstanceManager").val(defaultManager);
                    //new RoadUI.Member().setValue($("#base_InstanceManager"));
                    $("#base_RemoveCompleted").val(json.removeCompleted);
                    $("#base_Note").val(json.note);
                    $("#base_Debug").val(json.debug);
                    $("#base_DebugUsers").val(json.debugUsers);
                    //new RoadUI.Member().setValue($("#base_DebugUsers"));
                    $("#base_FlowType").val(json.flowType || "");
                    var databases = json.databases;
                    if (databases) {

                        for (var i = 0; i < databases.length; i++) {
                            t_table(databases[i].table);
                            $("#primaryKey").val(databases[i].primaryKey);

                        }
                    }
                    if (json.titleField) {
                        $("#title_dbconn").val(json.titleField.link);
                        $("#title_tables").val(json.titleField.table);
                        // $("#title_tables").html(getTables(json.titleField.link, json.titleField.table));
                        $("#title_title").val(json.titleField.field);
                    }
                }
          
            form.render(); //更新全部
        })


        function t_db() {
            var options = '<option value=""></option>';
            var fields = getTables();
            for (var i = 0; i < fields.length; i++) {
                options += "<option value='" + fields[i] + "'>" + fields[i] + "</option>"
            }

            document.getElementById("title_tables").innerHTML = options;
            if (workflow.wf_json.titleField.link != null) {
                $("#title_tables").val(workflow.wf_json.titleField.table);
            }
            form.render(); //更新全部
        }
        function t_table(data) {

            if (data.value == '') { return; }
            var options = '<option value=""></option>';
            var fields = workflow.getFields(data);
            for (var i = 0; i < fields.length; i++) {
                options += "<option value='" + fields[i] + "'>" + fields[i] + "</option>"
            }
            document.getElementById("primaryKey").innerHTML = options;
            document.getElementById("title_title").innerHTML = options;
            if (workflow.wf_json.length>0) {
                $("#primaryKey").val(workflow.wf_json.titleField.primaryKey);
            } if (workflow.wf_json.length>0) {
                $("#title_title").val(workflow.wf_json.titleField.link);
            }
            form.render(); //更新全部
        }
        form.on('select(title_db_change)', function (data) {
            t_db()

        });
        form.on('select(title_table_change)', function (data) {
            t_table(data.value);

        });
        function title_db_change(obj, table) {
            if (!obj || !obj.value) return;
            $("#title_tables").html(workflow.getTables(obj.value, table));

        }
        function title_table_change(obj, fields) {
            if (!obj || !obj.value) return;
            var conn = $("#title_dbconn").val();
            $("#title_title").html(workflow.getFields(conn, obj.value, fields));
        }





}
function flowsconfirm1() {
    //  $(but).prop("disabled", true);
    if (isAdd) {
        workflow. initwf();
    }
    var json = workflow.wf_json;
    json.id = $("#base_ID").val();
    json.name = $("#base_Name").val() || '';
    json.type = $("#base_Type").val() || '';
    json.manager = $("#base_Manager").val() || "u_" + window.localStorage["userid"];
    json.instanceManager = $("#base_InstanceManager").val() || '';
    json.removeCompleted = $("#base_RemoveCompleted").val() || '';
    json.debug = $("#base_Debug").val() || "0";
    json.debugUsers = $("#base_DebugUsers").val() || '';
    json.note = $("#base_Note").val() || '';
    json.flowType = $("#base_FlowType").val() || "";
    json.databases = [];
    json.databases.push({
        link: 'server=.;uid=sa;pwd=clf6421355920n9*;database=coreoa;Max Pool Size = 512;',
        linkName: '平台连接',//$("#link_db_" + index + " option[value='" + ($('#link_db_' + index).val() || '') + "']").text(),
        table: $("#title_tables").val(),// $('#link_table_' + index).val() || '',
        primaryKey: $('#primaryKey').val() || ''
    });
    json.titleField = {
        link: 'server=.;uid=sa;pwd=clf6421355920n9*;database=coreoa;Max Pool Size = 512;',
        table: $("#title_tables").val() || '',
        field: $("#title_title").val() || ''
    };
    if ($("#base_ID").val() === "00000000-0000-0000-0000-000000000000") {
        wf_id = "00000000-0000-0000-0000-000000000000";
    } else {
        wf_id = flowID;
    }
   
    workflow.initLinks_Tables_Fields(json.databases);
    layer.close(_addindex); $("#flowadd").hide();
    return false;
}


//步骤设置

var stepid = '';
function setflows() {
    stepid = stepSettingid;
    console.log(stepid);
    //初始化行为里面字段值选择项以及字段状态列表
    var fields = workflow.links_tables_fields;
    var tables = [];
    if (fields && fields.length > 0) {
        var trs = '';
        var valueFieldOptions = '<option value=""></option>';
        for (var i = 0; i < fields.length; i++) {
            valueFieldOptions += '<option value="' + fields[i].link + '.' + fields[i].table + '.' + fields[i].field + '">' + fields[i].linkName + '.' + fields[i].table + '.' + fields[i].field + (fields[i].fieldNote ? '(' + fields[i].fieldNote + ')' : '') + '</option>';
            trs += '<tr>';
            trs += '<td style="background:#ffffff; height:30px;">';
            trs += '<input type="hidden" value="' + i.toString() + '" id="data_check_index_' + i.toString() + '" />';
            trs += '<input type="hidden" value="' + fields[i].link + '.' + fields[i].table + '.' + fields[i].field + '" id="data_check_field_' + i.toString() + '" />';
            trs += fields[i].linkName + '</td>';
            trs += '<td style="background:#ffffff;">' + fields[i].table + '</td>';
            trs += '<td style="background:#ffffff;">' + fields[i].field + (fields[i].fieldNote ? '(' + fields[i].fieldNote + ')' : '') + '</td>';
            trs += '<td style="background:#ffffff;"><select class="myselect" id="data_check_status_' + i.toString() + '" style="width:60px;"><option value="0">编辑</option><option value="1">只读</option><option value="2">隐藏</option></select></td>';
            trs += '<td style="background:#ffffff;"><select class="myselect" id="data_check_check_' + i.toString() + '" style="width:100px;"><option value="0">不检查</option><option value="1">允许为空,非空时检查</option><option value="2">不允许为空,并检查</option></select></td>';
            trs += '</tr>';
            tables.push(fields[i].table);
        }
        $("#behavior_ValueField").html(valueFieldOptions);
        $("#data_table tbody").append(trs);
        //  new RoadUI.Select().init($(".myselect", $("#data_table tbody")));
    }
    //初始化所有表过滤下拉选择
    tables = tables;//.unique();
    var tablesoptions = '<option value=""></option>';
    for (var i = 0; i < tables.length; i++) {
        tablesoptions += '<option value="' + tables[i] + '">' + tables[i] + '</option>';
    }
    $("#data_alltable").html(tablesoptions).bind("change", function () {
        var value = $(this).val();
        var $trs = $("#data_table tbody tr");
        if (value.length == 0) {
            $trs.show();
            return;
        }

        for (var i = 0; i < $trs.size(); i++) {
            var $tds = $("td", $trs.eq(i));
            if ($tds.size() > 2 && $tds.eq(1).text() != value) {
                $trs.eq(i).hide();
            }
            else {
                $trs.eq(i).show();
            }
        }
    });


    var json = workflow.wf_json;
    var step;

    if (json && json.steps && json.steps.length > 0) {
        var stepOptions = '<option value=""></option>';//初始化行为里面的处理者步骤和退回步骤选择
        for (var i = 0; i < json.steps.length; i++) {
            if (json.steps[i].id == stepid) {
                alert(stepid);
                step = json.steps[i];
            }
            else {
                stepOptions += '<option value="' + json.steps[i].id + '">' + json.steps[i].name + '</option>';
            }
        }
        $("#behavior_HandlerStep").html(stepOptions);
        $("#behavior_BackStep").html(stepOptions);
    }

    initStep(step);

    //按钮排序
    //  new RoadUI.DragSort($("#button_Select div"));

};

function form_types_change(value) {
    $.ajax({
        url: top.rootdir + "/Platform/RoleApp/GetApps.ashx", data: { type: value }, async: false, type: "post", success: function (txt) {
            $("#form_forms").html('<option value=""></option>' + txt);
        }
    });
}

function form_add(formid, formtitle, formtype) {
    formid = formid || $("#form_forms").val();
    if (!formid) {
        alert("请选择要添加的表单!");
        return false;
    }
    else if ($("#form_list div ul[val='" + formid + "']").size() > 0) {
        alert("该表单已经添加了!");
        return false;
    }
    formtitle = formtitle || $("#form_forms option[value='" + formid + "']").text();
    formtype = formtype || $("#form_types option[value='" + $("#form_types").val() + "']").text();

    var $ul = $('<ul class="listulli" val="' + formid + '"><span>' + formtitle +
        '</span> - <span style="color:#999999;">' + formtype + '</span>' +
        '<span onclick="form_remove(this); return false;" style="padding-right:12px; margin-left:10px; height:18px; line-height:18px; cursor:pointer;' +
        ' background:url(../../Images/ico/cancel.gif) no-repeat left; padding-left:19px;"><a href="javascript:form_remove(this);return false;">删除</a></span></ul>');

    $("#form_list div").append($ul);

    new RoadUI.DragSort($("#form_list div"));
}
function form_remove(span) {
    //if (confirm("您真的要删除该表单吗?"))
    //{
    $(span).parent().remove();
    //new RoadUI.DragSort($("#form_list div"));
    //}
}

var $currentButton = null;
function button_click(ul) {
    $currentButton = $(ul);
    var $buttons = null;
    if ($currentButton.parent().parent().attr('id') == "button_List") {
        $buttons = $("#button_List div ul");
    }
    else if ($currentButton.parent().parent().attr('id') == "button_Select") {
        $buttons = $("#button_Select div ul");
    }
    $buttons.each(function () {
        $(this).removeClass().addClass("listulli");
    });
    $(ul).removeClass().addClass("listulli1");
    $("#button_Note1").text($(ul).attr("note"));
}

function button_dblclick(ul) {
    button_click(ul);
    button_add();
}

function button_add() {
    if ($currentButton == null) {
        alert("请选择要添加的按钮!"); return false;
    }
    if ($currentButton.parent().parent().attr('id') == "button_List") {
        if ($("#button_Select div ul[val='" + $currentButton.attr("val") + "']").size() > 0) {
            alert("当前按钮已经选择了!"); return false;
        }
        $("#button_Select div").append($currentButton.clone());
    }
    else if ($currentButton.parent().parent().attr('id') == "button_Select") {
        $currentButton.remove();
    }
    $currentButton = null;
    //  new RoadUI.DragSort($("#button_Select div"));
}
function button_remove() {
    if ($currentButton == null) {
        alert("请选择要删除的按钮!"); return false;
    }
    $currentButton.remove();
    //  new RoadUI.DragSort($("#button_Select div"));
}

function initStep(step) {
    layui.use(['layer', 'element', 'form', 'laydate'], function () {
        layer = layui.layer, element = layui.element, form = layui.form;
        if (!step) {
            $("#step_Name").val("新步骤");
            return;
        }

        $("#step_Name").val(step.name); $("#step_ID").val(step.id); form.render(); //更新全部
        if (step.opinionDisplay) $("#step_OpinionDisplay").val(step.opinionDisplay);
        if (step.expiredPrompt) $("#step_ExpiredPrompt").val(step.expiredPrompt);
        if (step.signatureType) $("#step_SignatureType").val(step.signatureType);
        if (step.workTime) $("#step_WorkTime").val(step.workTime);
        //if(step.limitTime) $("#step_LimitTime").val(step.limitTime);
        //if(step.otherTime) $("#step_OtherTime").val(step.otherTime);
        if (step.archives) $("#step_Archives").val(step.archives);
        if (step.note) $("#step_Note").val(step.note);

        if (step.behavior.flowType) $("#behavior_FlowType").val(step.behavior.flowType);
        if (step.behavior.runSelect) $("#behavior_RunSelect").val(step.behavior.runSelect);
        if (step.behavior.handlerType) $("#behavior_HandlerType").val(step.behavior.handlerType);
        if (step.behavior.selectRange) {
            $("#behavior_SelectRange").val(step.behavior.selectRange);
            //new RoadUI.Member().setValue($("#behavior_SelectRange"));
        }
        if (step.behavior.handlerStep) $("#behavior_HandlerStep").val(step.behavior.handlerStep);
        if (step.behavior.valueField) $("#behavior_ValueField").val(step.behavior.valueField);
        if (step.behavior.defaultHandler) {
            $("#behavior_DefaultHandler").val(step.behavior.defaultHandler);
            // new RoadUI.Member().setValue($("#behavior_DefaultHandler"));
        }
        if (step.behavior.hanlderModel) $("#behavior_HanlderModel").val(step.behavior.hanlderModel);
        if (step.behavior.backModel) $("#behavior_BackModel").val(step.behavior.backModel);
        if (step.behavior.backType) $("#behavior_BackType").val(step.behavior.backType);
        if (step.behavior.backStep) $("#behavior_BackStep").val(step.behavior.backStep);
        if (step.behavior.percentage) $("#behavior_Percentage").val(step.behavior.percentage);
        if (step.behavior.countersignature) $("#behavior_Countersignature").val(step.behavior.countersignature);
        if (step.behavior.countersignaturePercentage) $("#behavior_CountersignaturePercentage").val(step.behavior.countersignaturePercentage);
        if (step.behavior.copyFor) {
            $("#behavior_CopyFor").val(step.behavior.copyFor);
            // new RoadUI.Member().setValue($("#behavior_CopyFor"));
        }

        var forms = step.forms;
        if (forms && forms.length > 0) {
            for (var i = 0; i < forms.length; i++) {
                //form_add(forms[i].id,forms[i].name,forms[i].type);
                $('#form_types').val(forms[i].type);
                form_types_change(forms[i].type);
                $("#form_forms").val(forms[i].id);
            }
        }

        var buttons = step.buttons;
        if (buttons && buttons.length > 0) {
            for (var i = 0; i < buttons.length; i++) {
                var $ul = $("#button_List div ul[val='" + buttons[i].id + "']");
                if ($ul.size() > 0) {
                    $currentButton = $ul;
                    button_add();
                }
            }
        }

        initDataFiledStatus(step.fieldStatus);

        if (step.event) {
            $("#event_SubmitBefore").val(step.event.submitBefore);
            $("#event_SubmitAfter").val(step.event.submitAfter);
            $("#event_BackBefore").val(step.event.backBefore);
            $("#event_BackAfter").val(step.event.backAfter);
        }

    });
}

function initDataFiledStatus(fields)//初始化字段状态列表
{
    if (!fields || fields.length == 0) {
        return;
    }

    $("#data_table tbody tr").each(function () {
        var field = $("input[id^='data_check_field_']", $(this)).val();
        var status = "0";
        var check = "0";
        for (var i = 0; i < fields.length; i++) {
            if (fields[i].field == field) {
                status = fields[i].status;
                check = fields[i].check;
                break;
            }
        }
        $("select[id^='data_check_status_']", $(this)).val(status)
        $("select[id^='data_check_check_']", $(this)).val(check)
    });
}

function data_StateCng(value) {
    $("select:visible[id^='data_check_status_']", $("#data_table tbody")).val(value);
}

function data_CheckCng(value) {
    $("select:visible[id^='data_check_check_']", $("#data_table tbody")).val(value);
}

function setstepconfirm1() {
    var step = {};
    step.id = stepid;
    step.type = "normal";
    step.name = $("#step_Name").val() || "";
    step.opinionDisplay = $("#step_OpinionDisplay").val() || "";
    step.expiredPrompt = $("#step_ExpiredPrompt").val() || "";
    step.signatureType = $("#step_SignatureType").val() || "";
    step.workTime = $("#step_WorkTime").val() || "";
    step.limitTime = "" // $("#step_LimitTime").val() || "";
    step.otherTime = "" // $("#step_OtherTime").val() || "";
    step.archives = $("#step_Archives").val() || "";
    step.archivesParams = $("#step_ArchivesParams").val() || "";
    step.note = $("#step_Note").val() || "";
    //获取隐藏的坐标
    step.position = { x: parseInt($("#_x").val()), y: parseInt($("#_y").val()), width: parseInt($("#_width").val()), height: parseInt( $("#_height").val()) };
step.countersignature = $("#step_Countersignature_1").prop("checked") ? 1 : 0;

    step.behavior = {
        flowType: $("#behavior_FlowType").val() || "",
        runSelect: $("#behavior_RunSelect").val() || "",
        handlerType: $("#behavior_HandlerType").val() || "",
        selectRange: $("#behavior_SelectRange").val() || "",
        handlerStep: $("#behavior_HandlerStep").val() || "",
        valueField: $("#behavior_ValueField").val() || "",
        defaultHandler: $("#behavior_DefaultHandler").val() || "",
        hanlderModel: $("#behavior_HanlderModel").val() || "",
        backModel: $("#behavior_BackModel").val() || "",
        backType: $("#behavior_BackType").val() || "",
        backStep: $("#behavior_BackStep").val() || "",
        percentage: $("#behavior_Percentage").val() || "",
        countersignature: $("#behavior_Countersignature").val() || "0",
        countersignaturePercentage: $("#behavior_CountersignaturePercentage").val() || "",
        copyFor: $("#behavior_CopyFor").val() || ""
    };

    step.forms = [];
    //$("#form_list div ul").each(function(i){
    //    var $spans=$(this).children('span');
    //    step.forms.push({ id: $(this).attr("val"), name:$spans.eq(0).text(), type:$spans.eq(1).text(), srot:i });
    //});
    var form_type = $('#form_types').val() || "";
    var form_forms = $("#form_forms").val() || "";
    if (form_forms.length > 0 && form_type.length > 0) {
        step.forms.push({ id: form_forms, name: "", type: form_type, srot: 0 });
    }

    step.buttons = [];
    $("#button_Select div ul").each(function (i) {
        step.buttons.push({ id: $(this).attr("val"), sort: i });
    });

    step.fieldStatus = [];
    $("#data_table tbody input[type='hidden'][id^='data_check_index_']").each(function (i) {
        var index = $(this).val();
        var fields = $("#data_check_field_" + index).val();
        var status = $("#data_check_status_" + index).val();
        var check = $("#data_check_check_" + index).val();
        step.fieldStatus.push({ field: fields, status: status, check: check });
    });

    step.event = {
        submitBefore: $("#event_SubmitBefore").val() || "",
        submitAfter: $("#event_SubmitAfter").val() || "",
        backBefore: $("#event_BackBefore").val() || "",
        backAfter: $("#event_BackAfter").val() || ""
    };

    workflow.addStep1(step);
    workflow.setStepText(step.id, step.name);
    layer.close(_setflowindex)
    return false;
    //new RoadUI.Window().close();
}