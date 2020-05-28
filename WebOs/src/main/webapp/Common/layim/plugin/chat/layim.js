//var chat = $.connection; var getList ;
layui.use('layim', function () {
    var layim = layui.layim;
    var $ = layui.jquery;
    $.ajaxSetup({
        headers: { "Authorization": "bearer " + window.sessionStorage["_token"] }
    });
    var url = '/systemHub' + "?access_token=" + window.sessionStorage["_token"]   //Hub地址
        , onlineCount = 0;  //在线人数
    //创建signalR连接
    const connection = new signalR.HubConnection(url);
    connection.on("receivePrivateMessage", (username, avatar, id, type, message) => {
        obj = {
            username: username
            , avatar: avatar
            , id: id
            , type: type
            , content: message
        };
        // alert(message);
        layim.getMessage(obj);
        //定义receivePrivateMessage客户端函数供服务端调用
    });
    connection.on('UserOffline', function (userName) {
        winui.window.msg( userName+"退出聊天室");
       // console.log('<p class="systemmsg"><span style=\"color:#0094ff;margin-right:5px;\">' + userName + '</span>退出聊天室</p>');
        //msg_end.scrollIntoView();
        onlineCount--;
        console.log('(' + onlineCount + '人在线)');
    });
    //演示自动回复
    var autoReplay = [
        '您好，我现在有事不在，一会再和您联系。',
        '你没发错吧？face[微笑] ',
        '洗澡中，请勿打扰，偷窥请购票，个体四十，团体八折，订票电话：一般人我不告诉他！face[哈哈] ',
        '你好，我是主人的美女秘书，有什么事就跟我说吧，等他回来我会转告他的。face[心] face[心] face[心] ',
        'face[威武] face[威武] face[威武] face[威武] ',
        '<（@￣︶￣@）>',
        '看便签加群啊',
        '你要和我说话？你真的要和我说话？你确定自己想说吗？你一定非说不可吗？那你说吧，这是自动回复。',
        'face[黑线]  你慢慢说，别急……',
        '(*^__^*) face[嘻嘻] ，是贤心吗？'
    ];

    //基础配置
    layim.config({
        //初始化接口
        // msgbox: layui.cache.dir + 'css/modules/layim/html/msgbox.html' //消息盒子页面地址，若不开启，剔除该项即可
        top: '20px' //主面板相对浏览器右侧距离
        , init: {
            //url: '/Extend/GetUserList'
            // url: getList
            url: "/api/Users/GetUsersList"
            , data: {}, headers: { "Authorization": "bearer " + window.sessionStorage["_token"] },
            type: 'get'
        }
        //查看群员接口
        , members: {
            url: 'Common/oa/js/json/getMembers.json'
            , data: {}, headers: { "Authorization": "bearer " + window.sessionStorage["_token"] }
        }

        , uploadImage: {
            url: '' //（返回的数据格式见下文）
            , type: '' //默认post
        }
        , uploadFile: {
            url: '' //（返回的数据格式见下文）
            , type: '' //默认post
        }

        //扩展工具栏
        , tool: [{
            alias: 'code'
            , title: '代码'
            , icon: '&#xe64e;'
        }, { alias: 'quick', title: '快捷回复', icon: '&#xe611;' }]

        //,brief: true //是否简约模式（若开启则不显示主面板）
        , offset: ['100px', '50px']
        , title: '爱上歆随懿恫OA' //自定义主面板最小化时的标题
        //   , right: '100px' //主面板相对浏览器右侧距离

        //,minRight: '90px' //聊天面板最小化时相对浏览器右侧距离
        , initSkin: '3.jpg' //1-5 设置初始背景
        //,skin: ['aaa.jpg'] //新增皮肤
        //,isfriend: false //是否开启好友
        //,isgroup: false //是否开启群组
        , min: true //是否始终最小化主面板，默认false
        , notice: true //是否开启桌面消息提醒，默认false
        //,voice: false //声音提醒，默认开启，声音文件为：default.wav

        , msgbox: 'Common/layim/dist/css/modules/layim/html/msgbox.html' //消息盒子页面地址，若不开启，剔除该项即可
        , find: 'Common/layim/dist/css/modules/layim/html/find.html' //发现页面地址，若不开启，剔除该项即可
        , chatLog: 'Common/layim/dist/css/modules/layim/html/chatLog.html' //聊天记录页面地址，若不开启，剔除该项即可
        , copyright: true
    });
    //监听在线状态的切换事件
    layim.on('online', function (status) {
        layer.msg(status);
    });

    //监听签名修改
    layim.on('sign', function (value) {
        layer.msg(value);
    });
    //监听自定义工具栏点击，以添加代码为例
    layim.on('tool(code)', function (insert) {
        layer.prompt({
            title: '插入代码'
            , formType: 2
            , shade: 0
        }, function (text, index) {
            layer.close(index);
            insert('[pre class=layui-code]' + text + '[/pre]'); //将内容插入到编辑器
        });
    });
    //监听快捷回复
    layim.on('tool(quick)', function (insert, send) {
        var quick_list = ['你是小笨蛋', '对啊对啊', '哈哈'], quick_html = '';
        layui.each(quick_list, function (index, item) {
            quick_html += '<li class="quick">' + item + '</li>';
        });//循环列表
        layer.open({
            title: '快捷回复'
            , content: '<ul>' + quick_html + '</ul>'
            , btn: 0
            , success: function (layero, index) {
                $(layero).find(".quick").on("click", function () {
                    insert($(this).text());//把内容保存到对话框
                    //send();//立即发送
                    layer.close(index); //关闭弹窗 
                });
            }
        });
    });
    //监听layim建立就绪
    layim.on('ready', function (res) {
     
        var connectLoading = layer.msg('连接聊天服务器中...', { icon: 16 });
       // 连接聊天服务器
        connection.start()
            .then(function () {
                //连接成功
                layer.close(connectLoading);
             //   $(self).addClass('online').attr('title', '在线');
                console.log("成功连接聊天室");

                // msg_end.scrollIntoView();
            })
            .catch(error => {
                //连接出错
                layer.close(connectLoading);
                if (error.statusCode === 401) {
                    layer.msg('登录后方可使用聊天室', { shift: 6, icon: 5 });
                } else {
                    console.error(error.message);
                    layer.msg('连接聊天服务器失败', { shift: 6, icon: 5 });
                }
            });
        layim.msgbox(5); //模拟消息盒子有新消息，实际使用时，一般是动态获得


    });
    //监听发送消息
    layim.on('sendMessage', function (data) {
        //发送消息
        //从客户端中调用在此调用之前在自定义Hub定义的SendMessage方法
        connection.invoke("SendPrivateMessage", data.to
            .id, data.mine.content).catch(err => console.log(err.toString()));;

    });
    //监听查看群员
    layim.on('members', function (data) {
        //console.log(data);
    });

    //监听聊天窗口的切换
    layim.on('chatChange', function (res) {
        var type = res.data.type;
        console.log(res.data.id)
        if (type === 'friend') {
            //模拟标注好友状态
            //layim.setChatStatus('<span style="color:#FF5722;">在线</span>');
        } else if (type === 'group') {
            //模拟系统消息
            layim.getMessage({
                system: true
                , id: res.data.id
                , type: "group"
                , content: '模拟群员' + (Math.random() * 100 | 0) + '加入群聊'
            });
        }
    });


    //面板外的操作
    var active = {
        chat: function () {
            //创造一个临时会话
            layim.chat({
                name: '小闲'
                , type: 'friend'
                , avatar: 'http://tva3.sinaimg.cn/crop.0.0.180.180.180/7f5f6861jw1e8qgp5bmzyj2050050aa8.jpg'
                , id: 1008612
            });
            layer.msg('也就是说，此人可以不在好友面板里');
        }
        , message: function () {
            //制造一个好友发过来的消息
            layim.getMessage({
                username: "贤心"
                , avatar: "http://tp1.sinaimg.cn/1571889140/180/40030060651/1"
                , id: "100001"
                , type: "friend"
                , content: "嗨，你好！欢迎体验LayIM。演示标记：" + new Date().getTime()
                , timestamp: new Date().getTime()
            });
        }
        , messageTemp: function () {
            //制造一个临时会话消息
            layim.getMessage({
                username: "小酱"
                , avatar: "http://tva1.sinaimg.cn/crop.7.0.736.736.50/bd986d61jw8f5x8bqtp00j20ku0kgabx.jpg"
                , id: "198909151014"
                , type: "friend"
                , content: "临时：" + new Date().getTime()
            });
        }
        , add: function () {
            //实际使用时数据由动态获得
            layim.add({
                type: 'friend'
                , username: '麻花疼'
                , avatar: 'http://tva4.sinaimg.cn/crop.18.0.171.171.180/903a16c6jw8esb5s0n5ruj205u064jr9.jpg'
                , submit: function (group, remark, index) {
                    layer.msg('好友申请已发送，请等待对方确认', {
                        icon: 1
                        , shade: 0.5
                    }, function () {
                        layer.close(index);
                    });

                    //通知对方
                    /*
                    $.post('/im-applyFriend/', {
                      uid: info.uid
                      ,from_group: group
                      ,remark: remark
                    }, function(res){
                      if(res.status != 0){
                        return layer.msg(res.msg);
                      }
                      layer.msg('好友申请已发送，请等待对方确认', {
                        icon: 1
                        ,shade: 0.5
                      }, function(){
                        layer.close(index);
                      });
                    });
                    */
                }
            });
        }
        , addqun: function () {
            layim.add({
                type: 'group'
                , username: 'LayIM会员群'
                , avatar: 'http://tva2.sinaimg.cn/crop.0.0.180.180.50/6ddfa27bjw1e8qgp5bmzyj2050050aa8.jpg'
                , submit: function (group, remark, index) {
                    layer.msg('申请已发送，请等待管理员确认', {
                        icon: 1
                        , shade: 0.5
                    }, function () {
                        layer.close(index);
                    });

                    //通知对方
                    /*
                    $.post('/im-applyGroup/', {
                      uid: info.uid
                      ,from_group: group
                      ,remark: remark
                    }, function(res){
                    
                    });
                    */
                }
            });
        }
        , addFriend: function () {
            var user = {
                type: 'friend'
                , id: 1234560
                , username: '李彦宏' //好友昵称，若申请加群，参数为：groupname
                , avatar: 'http://tva4.sinaimg.cn/crop.0.0.996.996.180/8b2b4e23jw8f14vkwwrmjj20ro0rpjsq.jpg' //头像
                , sign: '全球最大的中文搜索引擎'
            }
            layim.setFriendGroup({
                type: user.type
                , username: user.username
                , avatar: user.avatar
                , group: layim.cache().friend //获取好友列表数据
                , submit: function (group, index) {
                    //一般在此执行Ajax和WS，以通知对方已经同意申请
                    //……

                    //同意后，将好友追加到主面板
                    layim.addList({
                        type: user.type
                        , username: user.username
                        , avatar: user.avatar
                        , groupid: group //所在的分组id
                        , id: user.id //好友ID
                        , sign: user.sign //好友签名
                    });

                    layer.close(index);
                }
            });
        }
        , addGroup: function () {
            layer.msg('已成功把[Angular开发]添加到群组里', {
                icon: 1
            });
            //增加一个群组
            layim.addList({
                type: 'group'
                , avatar: "http://tva3.sinaimg.cn/crop.64.106.361.361.50/7181dbb3jw8evfbtem8edj20ci0dpq3a.jpg"
                , groupname: 'Angular开发'
                , id: "12333333"
                , members: 0
            });
        }
        , removeFriend: function () {
            layer.msg('已成功删除[凤姐]', {
                icon: 1
            });
            //删除一个好友
            layim.removeList({
                id: 121286
                , type: 'friend'
            });
        }
        , removeGroup: function () {
            layer.msg('已成功删除[前端群]', {
                icon: 1
            });
            //删除一个群组
            layim.removeList({
                id: 101
                , type: 'group'
            });
        }
        //置灰离线好友
        , setGray: function () {
            layim.setFriendStatus(168168, 'offline');

            layer.msg('已成功将好友[马小云]置灰', {
                icon: 1
            });
        }
        //取消好友置灰
        , unGray: function () {
            layim.setFriendStatus(168168, 'online');

            layer.msg('成功取消好友[马小云]置灰状态', {
                icon: 1
            });
        }
    };
    $('.site-demo-layim').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });



});