/**

 @Name：workflow layui路由扩展v0.01
 @Author：爱上歆随懿恫|http://www.asxsyd92.com
 @Url：http://www.asxsyd92.com
 @License：MIT
*/
layui.config({
    base: '/Common/layim/layui_exts/' //指定 winui 路径
    , version: '1.0.0-beta'
}).define(['jquery', 'Raphael','common'], function (exports) {
    var $ = layui.$, common = layui.common, Raphael = layui.Raphael;
    common.islogin();
    //随着节点位置的改变动态改变箭头
    Raphael.fn.drawArr = function (obj) {
        if (!obj || !obj.obj1) {
            return;
        }

        if (!obj.obj2) {
            var point1 = workflow.getStartEnd(obj.obj1, obj.obj2);
            var path2 = workflow.getArr(point1.start.x, point1.start.y, workflow.mouseX, workflow.mouseY, 7);
            for (var i = 0; i < workflow.tempArrPath.length; i++) {
                workflow.tempArrPath[i].arrPath.remove();
            }
            workflow.tempArrPath = [];
            obj.arrPath = this.path(path2);
            obj.arrPath.attr({ "stroke-width": 1.7, "stroke": workflow.wf_connColor, "fill": workflow.wf_connColor });
            workflow.tempArrPath.push(obj);
            return;
        }

        var point = workflow.getStartEnd(obj.obj1, obj.obj2);
        var path1 = workflow.getArr(point.start.x, point.start.y, point.end.x, point.end.y, 7);
        try {
            if (obj.arrPath) {
                obj.arrPath.attr({ path: path1 });
            }
            else {
                obj.arrPath = this.path(path1);
                obj.arrPath.attr({ "stroke-width": 1.7, "stroke": workflow.wf_connColor, "fill": workflow.wf_connColor, "x": point.start.x, "y": point.start.y, "x1": point.end.x, "y1": point.end.y });
                if (workflow.wf_designer) {
                    obj.arrPath.click(workflow.connClick);
                    obj.arrPath.dblclick(workflow.connSetting);
                    obj.arrPath.id = obj.id;
                    obj.arrPath.fromid = obj.obj1.id;
                    obj.arrPath.toid = obj.obj2.id;
                }
            }
        } catch (e) { console.log(e) }
        return obj;
    };
    //删除数组中元素
    Array.prototype.remove = function (n) {
        if (isNaN(n) || n > this.length) { return false; }
        this.splice(n, 1);
    }

    workflow = {
        tempArrPath: [], //临时连线
        mouseX: 0,
        mouseY: 0,
        wf_id: "",//当前流程ID
        links_tables_fields: [],//当前流程的所有连接所有表和字段
        wf_json: [],
        wf_r: null, //画板对象
        wf_steps: [], //步骤数组
        wf_texts: [], //文本数组
        wf_conns: [], //连线数组
        wf_option: "", //当前操作
        wf_focusObj: null, //当前焦点对象
        wf_width: 108, //步骤宽度
        wf_height: 50, //步骤高度
        wf_rect: 8, //圆角大小
        wf_designer: true, //是否是设计模式(查看流程图时不帮定双击事件）
        wf_connColor: "#898a89", //连线的常规颜色
        wf_nodeBorderColor: "#587aa9", //节点边框颜色
        wf_noteColor: "#efeff0", //节点填充颜色
        wf_stepDefaultName: "新步骤",//默认步骤名称
        //添加步骤
        addStep: function (x, y, text, id, addToJSON, type1, bordercolor, bgcolor) {
            var guid = workflow.getGuid();
            var xy = workflow.getNewXY();
            x = x || xy.x;
            y = y || xy.y;
            text = text || workflow.wf_stepDefaultName;
            id = id || guid;
            var rect = workflow.wf_r.rect(x, y, workflow.wf_width, workflow.wf_height, workflow.wf_rect);
            rect.attr({ "fill": bgcolor || workflow.wf_noteColor, "stroke": bordercolor || workflow.wf_nodeBorderColor, "stroke-width": 1.4, "cursor": "default" });
            rect.id = id;
            rect.type1 = type1 ? type1 : "normal";
            rect.drag(workflow.move, workflow.dragger, workflow.up);
            if (workflow.wf_designer) {
                rect.click(workflow.click);
                if ("normal" == rect.type1) {
                    rect.dblclick(workflow.stepSetting);
                }
                else if ("subflow" == rect.type1) {
                    rect.dblclick(workflow.subflowSetting);
                }
            }
            workflow.wf_steps.push(rect);

            var text2 = text.length > 8 ? text.substr(0, 7) + "..." : text;
            var text1 = workflow.wf_r.text(x + 52, y + 25, text2);
            text1.attr({ "font-size": "12px" });
            if (text.length > 8) text1.attr({ "title": text });
            text1.id = "text_" + id;
            text1.type1 = "text";
            workflow.wf_texts.push(text1);

            if (addToJSON == undefined || addToJSON == null) addToJSON = true;
            if (addToJSON) {
                var step = {};
                step.id = guid;
                step.type = type1 ? type1 : "normal";
                step.name = text;
                step.position = { x: x, y: y, width: workflow.wf_width, height: workflow.wf_height };
                if (rect.type1 === "normal") {
                    step.opinionDisplay = "";
                    step.expiredPrompt = "";
                    step.signatureType = "";
                    step.workTime = "";
                    step.limitTime = "";
                    step.otherTime = "";
                    step.archives = "";
                    step.archivesParams = "";
                    step.note = "";
                    step.behavior = {
                        flowType: "",
                        runSelect: "",
                        handlerType: "",
                        selectRange: "",
                        handlerStep: "",
                        valueField: "",
                        defaultHandler: "",
                        hanlderModel: "",
                        backModel: "",
                        backStep: "",
                        backType: "",
                        percentage: ""
                    };
                    step.forms = [];
                    step.buttons = [];
                    step.fieldStatus = [];
                    step.event = {
                        submitBefore: "",
                        submitAfter: "",
                        backBefore: "",
                        backAfter: ""
                    };
                }
                else if (rect.type1 === "subflow") {
                    step.flowid = "";
                    step.handler = "";
                    step.strategy = 0;
                }
                workflow.addStep1(step);
            }
        }
        ,//添加子流程节点
        addSubFlow: function () {
            workflow.addStep(null, null, "子流程步骤", null, null, "subflow", null, null)
        },//复制当前选中步骤
        copyStep: function () {
            if (workflow.wf_focusObj === null || !workflow.isStepObj(workflow.wf_focusObj)) {
                layer.alert('请选择要复制的步骤', {
                    title: '温馨提示'


                });
                return;
                //  alert("请选择要复制的步骤");
              //  return;
            }
            var json = null;
            var text = "";
            var id = workflow.getGuid();
            if (workflow.wf_json && workflow.wf_json.steps) {
                for (var i = 0; i < workflow.wf_json.steps.length; i++) {
                    if (workflow.wf_json.steps[i].id == workflow.wf_focusObj.id) {
                        json = workflow.wf_json.steps[i];
                        json.id = id;
                        text = json.name;
                        json.name = text;
                        break;
                    }
                }
            }
            workflow.addStep(undefined, undefined, text, id, false);
        },//设置步骤文本
        setStepText: function (id, txt) {
            var stepText = workflow.wf_r.getById("text_" + id);
            if (stepText != null) {
                if (txt.length > 8) {
                    stepText.attr({ "title": txt });
                    txt = txt.substr(0, 7) + "...";
                }
                stepText.attr({ "text": txt });
            }
        },
        setLineText: function (id, txt) {
            var line;
            for (var i = 0; i < workflow.wf_conns.length; i++) {
                if (workflow.wf_conns[i].id == id) {
                    line = workflow.wf_conns[i];
                    break;
                }
            }
            if (!line) {
                return;
            }
            var bbox = line.arrPath.getBBox();
            var txt_x = (bbox.x + bbox.x2) / 2;
            var txt_y = (bbox.y + bbox.y2) / 2;

            var lineText = workflow.wf_r.getById("line_" + id);
            if (lineText != null) {
                if (!txt) {
                    lineText.remove();
                }
                else {
                    lineText.attr("x", txt_x);
                    lineText.attr("y", txt_y);
                    lineText.attr("text", txt || "");
                }
                return;
            }

            if (txt) {
                var textObj = workflow.wf_r.text(txt_x, txt_y, txt);
                textObj.type1 = "line";
                textObj.id = "line_" + id;
                workflow.wf_texts.push(textObj);
            }
            //line.arrPath.attr("title", txt);
        },
        //删除当前焦点及其附属对象
        removeObj: function () {
            if (workflow.wf_focusObj === null) {
                layer.alert('请选择要删除的对象', {
                    title: '温馨提示'


                });
                return false;
                //alert("请选择要删除的对象!");
            }
            //else if (!confirm('您真的要删除选定对象吗?'))
            //{
            //    return false;
            //}
            var iis = layer.confirm('您真的要删除选定对象吗', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                if (workflow.isStepObj(workflow.wf_focusObj))//如果选中的是步骤
                {
                    if (workflow.wf_focusObj.id) {
                        for (var i = 0; i < workflow.wf_texts.length; i++) {
                            if (workflow.wf_texts[i].id == "text_" + workflow.wf_focusObj.id) {
                                workflow.wf_texts.remove(i);
                                var text = workflow.wf_r.getById("text_" + workflow.wf_focusObj.id);
                                if (text) text.remove();
                            }
                        }
                    }
                    var deleteConnIndex = new Array();
                    for (var j = 0; j < workflow.wf_conns.length; j++) {

                        if (workflow.wf_conns[j].arrPath && (workflow.wf_conns[j].obj1.id == workflow.wf_focusObj.id || workflow.wf_conns[j].obj2.id == workflow.wf_focusObj.id)) {
                            workflow.deleteLine(workflow.wf_conns[j].id, workflow.wf_conns[j].arrPath.id);
                            deleteConnIndex.push(j);
                            workflow.wf_conns[j].arrPath.remove();
                        }
                    }
                    for (var m = deleteConnIndex.length; m--;) {
                        workflow.wf_conns.remove(deleteConnIndex[m]);
                    }
                    deleteConnIndex = new Array();

                    for (var k = 0; k < workflow.wf_steps.length; k++) {
                        if (workflow.wf_steps[k].id == workflow.wf_focusObj.id) {
                            workflow.wf_steps.remove(k);
                            workflow.deleteStep(workflow.wf_focusObj.id);
                        }
                    }
                    workflow.wf_focusObj.remove();
                    layer.close(iis);
                }
                else//如果选中的是线
                {
                    for (var j = 0; j < workflow.wf_conns.length; j++) {
                        if (workflow.wf_conns[j].arrPath && workflow.wf_conns[j].arrPath.id == workflow.wf_focusObj.id) {
                            workflow.deleteLine(workflow.wf_conns[j].id, workflow.wf_conns[j].arrPath.id);
                            workflow.wf_conns.remove(j);
                        }
                    }
                    workflow.wf_focusObj.remove();
                    layer.close(iis);
                }
            }, function () {
                layer.close(iis);
                return false;
            });

        },//得到新步骤的XY
        getNewXY: function () {
            var x = 10, y = 50;
            if (workflow.wf_steps.length > 0) {
                var step = workflow.wf_steps[workflow.wf_steps.length - 1];
                x = parseInt(step.attr("x")) + 170;
                y = parseInt(step.attr("y"));
                if (x > workflow.wf_r.width - workflow.wf_width) {
                    x = 10;
                    y = y + 100;
                }

                if (y > workflow.wf_r.height - workflow.wf_height) {
                    y = workflow.wf_r.height - workflow.wf_height;
                }
            }
            return { x: x, y: y };
        },//添加连线
        addConn: function () {
            if (!workflow.wf_focusObj || !workflow.isStepObj(workflow.wf_focusObj)) {
                layer.alert('请选择要连接的步骤', {
                    title: '温馨提示'


                }); return false;
                // alert("请选择要连接的步骤!"); return false;
            }
            workflow.wf_option = "line";
            document.body.onmousemove = workflow.mouseMove;
            document.body.onmousedown = function () {
                for (var i = 0; i < workflow.tempArrPath.length; i++) {
                    workflow.tempArrPath[i].arrPath.remove();
                }
                workflow.tempArrPath = [];
                document.body.onmousemove = null;
            };
        }, mouseMove: function (ev) {
            ev = ev || window.event;
            var mousePos = workflow.mouseCoords(ev);
            workflow.mouseX = mousePos.x;
            workflow.mouseY = mousePos.y;
            var obj = { obj1: workflow.wf_focusObj, obj2: null };
            workflow.wf_r.drawArr(obj);
        }, mouseCoords: function (ev) {
            if (ev.pageX || ev.pageY) {
                return { x: ev.pageX, y: ev.pageY };
            }
            return {
                x: ev.clientX + document.body.scrollLeft - document.body.clientLeft,
                y: ev.clientY + document.body.scrollTop - document.body.clientTop
            };
        },
        //连接对象
        connObj: function (obj, addToJSON, title) {
            if (addToJSON == undefined || addToJSON == null) addToJSON = true;
            if (workflow.isLine(obj)) {
                var newline = workflow.wf_r.drawArr(obj);
                workflow.wf_conns.push(newline);
                if (addToJSON) {
                    var line = {};
                    line.id = obj.id;
                    line.text = title || "";
                    line.from = obj.obj1.id;
                    line.to = obj.obj2.id;
                    line.customMethod = "";
                    line.sql = "";
                    line.noaccordMsg = "";
                    workflow.addLine(line);
                }
                else {
                    workflow.setLineText(obj.id, title);
                }
            }
        },//单击事件执行相关操作
        click: function () {
            var o = this;
            switch (workflow.wf_option) {
                case "line":
                    var obj = { id: workflow.getGuid(), obj1: workflow.wf_focusObj, obj2: o };
                    workflow.connObj(obj);
                    break;
                default:
                    workflow.changeStyle(o);
                    break;
            }
            workflow.wf_option = "";
            workflow.wf_focusObj = this;
        },//连线单击事件
        connClick: function () {
            for (var i = 0; i < workflow.wf_conns.length; i++) {
                if (workflow.wf_conns[i].arrPath === this) {

                    workflow.wf_conns[i].arrPath.attr({ "stroke": "#db0f14", "fill": "#db0f14" });
                }
                else {
                    workflow.wf_conns[i].arrPath.attr({ "stroke": workflow.wf_connColor, "fill": workflow.wf_connColor });
                }
            }
            for (var i = 0; i < workflow.wf_steps.length; i++) {
                workflow.wf_steps[i].attr("fill", "#efeff0");
                workflow.wf_steps[i].attr("stroke", "#23508e");
            }

            workflow.wf_focusObj = this;
        },//判断一个节点与另一个节点之间是否可以连线
        isLine: function (obj) {
            if (!obj || !obj.obj1 || !obj.obj2) {
                return false;
            }
            if (obj.obj1 === obj.obj2) {
                return false;
            }
            if (!workflow.isStepObj(obj.obj1) || !workflow.isStepObj(obj.obj2)) {
                return false;
            }
            for (var i = 0; i < workflow.wf_conns.length; i++) {
                if (obj.obj1 === obj.obj2 || (workflow.wf_conns[i].obj1 === obj.obj1 && workflow.wf_conns[i].obj2 === obj.obj2)) {
                    return false;
                }
            }
            return true;
        },//判断一个对象是否是步骤对象
        isStepObj: function (obj) {
            return obj && obj.type1 && (obj.type1.toString() == "normal" || obj.type1.toString() == "subflow");
        },//得到XML DOM
        getXmlDoc: function () {
            var xmlDoc = RoadUI.Core.getXmlDoc();
            xmlDoc.async = false;
            return xmlDoc
        },//得到GUID
        getGuid: function () {
            return Raphael.createUUID().toLowerCase();
        },//改变节点样式
        changeStyle: function (obj) {
            if (!obj) {
                return;
            }
            for (var i = 0; i < workflow.wf_steps.length; i++) {
                if (workflow.wf_steps[i].id == obj.id) {
                    workflow.wf_steps[i].attr("fill", workflow.wf_noteColor);
                    workflow.wf_steps[i].attr("stroke", "#cc0000");
                }
                else {
                    workflow.wf_steps[i].attr("fill", workflow.wf_noteColor);
                    workflow.wf_steps[i].attr("stroke", workflow.wf_nodeBorderColor);
                }
            }

            for (var i = 0; i < workflow.wf_conns.length; i++) {
                if (workflow.wf_conns[i].arrPath) {
                    workflow.wf_conns[i].arrPath.attr({ "stroke": workflow.wf_connColor, "fill": workflow.wf_connColor });
                }
            }
            //obj.animate({ }, 500);
        },//拖动节点开始时的事件
        dragger: function () {
            this.ox = this.attr("x");
            this.oy = this.attr("y");
            workflow.changeStyle(this);
        },
        //拖动事件
        move: function (dx, dy) {
            var x = this.ox + dx;
            var y = this.oy + dy;

            if (x < 0) {
                x = 0;
            }
            else if (x > workflow.wf_r.width - workflow.wf_width) {
                x = workflow.wf_r.width - workflow.wf_width;
            }

            if (y < 0) {
                y = 0;
            }
            else if (y > workflow.wf_r.height - workflow.wf_height) {
                y = workflow.wf_r.height - workflow.wf_height;
            }
            this.attr("x", x);
            this.attr("y", y);
            if (this.id) {
                var text = workflow.wf_r.getById('text_' + this.id);
                if (text) {
                    text.attr("x", x + 52);
                    text.attr("y", y + 25);
                }
            }
            for (var j = workflow.wf_conns.length; j--;) {
                if (workflow.wf_conns[j].obj1.id == this.id || workflow.wf_conns[j].obj2.id == this.id) {
                    for (var n = 0; n < workflow.wf_json.lines.length; n++) {
                        if (workflow.wf_json.lines[n].id == workflow.wf_conns[j].arrPath.id) {
                            workflow.setLineText(workflow.wf_json.lines[n].id, workflow.wf_json.lines[n].text);
                            break;
                        }
                    }
                    workflow.wf_r.drawArr(workflow.wf_conns[j]);
                }
            }
            workflow.wf_r.safari();
        },//拖动结束后的事件
        up: function () {
            workflow.changeStyle(this);
            //记录移动后的位置
            if (workflow.isStepObj(this)) {
                var bbox = this.getBBox();
                if (bbox) {
                    var steps = workflow.wf_json.steps;
                    if (steps && steps.length > 0) {
                        for (var i = 0; i < steps.length; i++) {
                            if (steps[i].id == this.id) {
                                steps[i].position = { "x": bbox.x, "y": bbox.y, "width": bbox.width, "height": bbox.height };
                                break;
                            }
                        }
                    }
                }
            }
        }, getStartEnd: function (obj1, obj2) {
            var bb1 = obj1 ? obj1.getBBox() : null;
            var bb2 = obj2 ? obj2.getBBox() : null;
            var p = [
                { x: bb1.x + bb1.width / 2, y: bb1.y - 1 },
                { x: bb1.x + bb1.width / 2, y: bb1.y + bb1.height + 1 },
                { x: bb1.x - 1, y: bb1.y + bb1.height / 2 },
                { x: bb1.x + bb1.width + 1, y: bb1.y + bb1.height / 2 },
                bb2 ? { x: bb2.x + bb2.width / 2, y: bb2.y - 1 } : {},
                bb2 ? { x: bb2.x + bb2.width / 2, y: bb2.y + bb2.height + 1 } : {},
                bb2 ? { x: bb2.x - 1, y: bb2.y + bb2.height / 2 } : {},
                bb2 ? { x: bb2.x + bb2.width + 1, y: bb2.y + bb2.height / 2 } : {}
            ];
            var d = {}, dis = [];
            for (var i = 0; i < 4; i++) {
                for (var j = 4; j < 8; j++) {
                    var dx = Math.abs(p[i].x - p[j].x),
                        dy = Math.abs(p[i].y - p[j].y);
                    if (
                        (i == j - 4) ||
                        (((i != 3 && j != 6) || p[i].x < p[j].x) &&
                            ((i != 2 && j != 7) || p[i].x > p[j].x) &&
                            ((i != 0 && j != 5) || p[i].y > p[j].y) &&
                            ((i != 1 && j != 4) || p[i].y < p[j].y))
                    ) {
                        dis.push(dx + dy);
                        d[dis[dis.length - 1]] = [i, j];
                    }
                }
            }
            if (dis.length == 0) {
                var res = [0, 4];
            } else {
                res = d[Math.min.apply(Math, dis)];
            }
            var result = {};
            result.start = {};
            result.end = {};
            result.start.x = p[res[0]].x;
            result.start.y = p[res[0]].y;
            result.end.x = p[res[1]].x;
            result.end.y = p[res[1]].y;
            return result;
        },

        getArr: function (x1, y1, x2, y2, size) {
            var angle = Raphael.angle(x1, y1, x2, y2);
            var a45 = Raphael.rad(angle - 28);
            var a45m = Raphael.rad(angle + 28);
            var x2a = x2 + Math.cos(a45) * size;
            var y2a = y2 + Math.sin(a45) * size;
            var x2b = x2 + Math.cos(a45m) * size;
            var y2b = y2 + Math.sin(a45m) * size;
            return ["M", x1, y1, "L", x2, y2, "M", x2, y2, "L", x2b, y2b, "L", x2a, y2a, "z"].join(",");
        },
        initwf: function () {
            workflow.wf_json = {};
            workflow.wf_steps = new Array();
            workflow.wf_texts = new Array();
            workflow.wf_conns = new Array();
            workflow.wf_r.clear();
        }, removeArray: function (array, n) {
            if (isNaN(n) || n > array.length) { return false; }
            array.splice(n, 1);
        },//得到一连接的所有表
        getTables: function () {
            var tables = [];
            $.ajax({
                url: "/api/WorkFlow/GetTable_SqlServer", dataType: "json", async: false, cache: false, success: function (json) {
                    for (var i = 0; i < json.length; i++) {
                        tables.push(json[i]);
                    }
                }
            });
            return tables;
        },//得到一个表所有字段
        getFields: function (table) {
            var fields = [];
            $.ajax({
                url: "/api/WorkFlow/GetFields?table=" + table, dataType: "json", async: false, cache: false, success: function (json) {
                    //console.log(json);
                    //for (var i = 0; i < json.length; i++)
                    //{
                    //    fields.push(json[i]);
                    //}
                    fields = json;
                }
            });
            return fields;
        },//载入当前数据连接的所有表和字段
        initLinks_Tables_Fields: function (databases) {
            if (!databases || databases.length == 0) {
                return;
            }
            workflow.links_tables_fields = [];
            for (var i = 0; i < databases.length; i++) {
                var fields = workflow.getFields(databases[i].link, databases[i].table);
                for (var k = 0; k < fields.length; k++) {
                    links_tables_fields.push({ link: databases[i].link, linkName: databases[i].linkName, table: databases[i].table, field: fields[k].name, fieldNote: fields[k].note });
                }
            }
        },
        //添加步骤
        addStep1: function (step) {
            if (!step) return;
            if (!workflow.wf_json.steps) workflow.wf_json.steps = [];
            var isup = false;
            for (var i = 0; i < workflow.wf_json.steps.length; i++) {
                if (workflow.wf_json.steps[i].id == step.id) {
                    workflow.wf_json.steps[i] = step;
                    isup = true;
                }
            }
            if (!isup) {
                workflow.wf_json.steps.push(step);
            }
        },//添加线
        addLine: function (line) {
            if (!line || !line.from || !line.to) return;
            if (!workflow.wf_json.lines) workflow.wf_json.lines = [];
            var isup = false;
            for (var i = 0; i < workflow.wf_json.lines.length; i++) {
                if (workflow.wf_json.lines[i].id == line.id) {
                    workflow.wf_json.lines[i] = line;
                    isup = true;
                }
            }
            if (!isup) {
                workflow.wf_json.lines.push(line);
            }
            workflow.setLineText(line.id, line.text);
        },//根据当前JSON重载入流程
        reloadFlow: function (json) {
            if (!json || !json.id || $.trim(json.id) == "") return false;
            workflow.wf_json = json;
            workflow.wf_id = workflow.wf_json.id;
            workflow.wf_r.clear();
            workflow.wf_steps = [];
            workflow.wf_conns = [];
            workflow.wf_texts = [];
            var steps = workflow.wf_json.steps;
            if (steps && steps.length > 0) {
                for (var i = 0; i < steps.length; i++) {
           
                        workflow.addStep(steps[i].position.x, steps[i].position.y, steps[i].name, steps[i].id, false, steps[i].type);

            
               }
            }
            var lines = workflow.wf_json.lines;
            if (lines && lines.length > 0) {
                for (var i = 0; i < lines.length; i++) {
                    workflow.connObj({ id: lines[i].id, obj1: workflow.wf_r.getById(lines[i].from), obj2: workflow.wf_r.getById(lines[i].to) }, false, lines[i].text);
                }
            }

            //初始化数据连接
            workflow.initLinks_Tables_Fields(workflow.wf_json.databases);
        },//从json中删除步骤
        deleteStep: function (stepid) {
            var steps = workflow.wf_json.steps;
            if (steps && steps.length > 0) {
                for (var i = 0; i < steps.length; i++) {
                    if (steps[i].id == stepid) {
                        workflow.removeArray(steps, i);
                    }
                }
            }
        },//从json中删除线
        deleteLine: function (lineid, textid) {
            var lines = workflow.wf_json.lines;
            if (lines && lines.length > 0) {
                for (var i = 0; i < lines.length; i++) {
                    if (lines[i].id == lineid) {
                        workflow.removeArray(lines, i);
                    }
                }
            }
            if (textid) {
                if (workflow.wf_texts && workflow.wf_texts.length > 0) {
                    for (var i = 0; i < workflow.wf_texts.length; i++) {

                        if (workflow.wf_texts[i].id == "line_" + textid) {
                            workflow.wf_texts[i].remove();
                        }
                    }
                }
            }
        },
        //步骤属性设置
        stepSetting: function () {
            stepSettingid = this.id;
            console.log(this);
            var bbox = this.getBBox();
            //var url = top.rootdir + "/Platform/WorkFlowDesigner/Set_Step.aspx?appid=" + appid + "&id=" + this.id + "&x=" + bbox.x + "&y=" + bbox.y + "&width=" + bbox.width + "&height=" + bbox.height;
            //dialog.open({ title: "步骤参数设置", width: 700, height: 400, url: url, openerid: iframeid, resize: false });
            _setflowindex = layer.open({
                type: 1, shade: 0.7, title: '步骤参数设置',
                area: ['80%', '80%'],
                content: $('#setstep')[0].innerHTML //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                , success: function (layero) {
                   //设置隐藏的坐标
                    $("#_x").val(bbox.x);
                    $("#_y").val(bbox.y);
                    $("#_width").val(bbox.width);
                    $("#_height").val(bbox.height);
                    setflows();

                }, cancel: function () {
                    //右上角关闭回调
                   // $("#setstep").hide();
                    //return false 开启该代码可禁止点击该按钮关闭
                }
            });
        },//子流程设置
        subflowSetting: function () {
            var bbox = this.getBBox();
            var url = top.rootdir + "/Platform/WorkFlowDesigner/Set_SubFlow.aspx?id=" + this.id + "&x=" + bbox.x + "&y=" + bbox.y + "&width=" + bbox.width + "&height=" + bbox.height;
            dialog.open({ title: "子流程步骤参数设置", width: 700, height: 400, url: url, resize: false });
        },
        //流转条件设置
        connSetting: function () {
            var url = top.rootdir + "/Platform/WorkFlowDesigner/Set_Line.aspx?appid=" + appid + "&id=" + this.id + "&from=" + this.fromid + "&to=" + this.toid;
            dialog.open({ title: "流转条件设置", width: 700, height: 400, url: url, openerid: iframeid, resize: false });
        },
        //流程属性设置
        flowAttrSetting: function (isAdd) {
            _addindex = layer.open({
                type: 1, shade: 0.7, title: '流程属性设置',
                area: ['80%', '80%'],
                content: $('#flowadd')[0].innerHTML //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                , success: function (layero) {
                    flowadd();

                }, cancel: function () {
                    //右上角关闭回调
                    $("#flowadd").hide();
                    //return false 开启该代码可禁止点击该按钮关闭
                }
            });

        }, openFlow1: function (id) {
            var json = $.ajax({
                url: "/api/WorkFlow/GetJSON?flowid=" + id, async: false, cache: false, dataType: "json", success: function (json) {
                    workflow.reloadFlow(json);
                }
            });
        },//保存流程
        saveFlow: function (op) {
            if (!workflow.wf_json) {
                layer.alert('未设置流程', {
                    title: '温馨提示'


                }); return false;
                // alert("未设置流程!"); 
            }
            else if (!workflow.wf_json.id || $.trim(workflow.wf_json.id) == "") {
                layer.alert('请先新建或打开流程', {
                    title: '温馨提示'


                }); return false;
                //  alert("请先新建或打开流程!"); return false;
            }
            else if (!workflow.wf_json.name || $.trim(workflow.wf_json.name) == "") {
                layer.alert('流程名称不能为空', {
                    title: '温馨提示'


                }); return false;
                // alert("流程名称不能为空!"); return false;
            }
            //if (op == "delete" && !confirm("您真的要删除该流程吗?"))
            //{
            //    return;
            //}
            if (op == "delete") {
                var iis = layer.confirm('您真的要删除该流程吗', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    var title = "";
                    if (op == "save") title = "保存流程";
                    else if (op == "install") title = "安装流程";
                    else if (op == "uninstall") title = "卸载流程";
                    else if (op == "delete") title = "删除流程";
                    // var url = "flowid=" + wf_json.id + "&op=" + (op || "save");
                    var json = JSON.stringify(workflow.wf_json);
                    $.ajax({
                        url: "/api/WorkFlow/Save" + op, type: "post", async: true, dataType: "text", data: { json: json }, success: function (txt) {
                            if (1 == txt) {
                                alert("删除成功!");
                            }
                            else {
                                alert(txt);
                            }

                        }, error: function (obj) { layer.alert(obj.responseText); }
                    });
                    layer.close(iis);
                }, function () {
                    layer.close(iis);
                    return false;
                });
            } else {
                var json = JSON.stringify(workflow.wf_json);
                $.ajax({
                    url: "/api/WorkFlow/Save", type: "post", async: true, dataType: "text", data: { json: json, op: op }, success: function (txt) {
                        if (1 === txt) {
                            layer.alert("保存成功!");
                            window.location.href = "WorkFlowList.html";
                        }
                        else {
                            layer.alert(txt);
                        }
                        //  window.setTimeout('new RoadUI.Window().close();', 1);
                    }, error: function (obj) { layui.alert(obj.responseText); }
                });
            }

        }
        ,
        //新建流程
        addFlow: function () {
            workflow.flowAttrSetting(1);
        },//另存为
        saveAs: function () {
            if (!workflow.wf_json || !workflow.wf_json.id || $.trim(workflow.wf_json.id) == "") {
                layer.alert('请先新建或打开一个流程', {
                    title: '温馨提示'


                }); return false;
                // alert("请先新建或打开一个流程!"); return false;
            }
            var url = top.rootdir + "/Platform/WorkFlowDesigner/SaveAs.aspx?appid=" + appid + "&flowid=" + workflow.wf_json.id;
            dialog.open({ title: "流程另存为", width: 600, height: 230, url: url, openerid: iframeid, resize: false });
        }
    }
    workflow.wf_r = Raphael("flowdiv", $(window).width(), $(window).height() - 28);
    workflow. wf_r.customAttributes.type1 = function () { };
    workflow. wf_r.customAttributes.fromid = function () { };
    workflow. wf_r.customAttributes.toid = function () { };



    exports('workflow', workflow);

});



