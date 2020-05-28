layui.config({
    base: 'Common/layim/layui_exts/' //指定 winui 路径
    , version: '1.0.0-beta'
}).define(['layer', 'element', 'jquery', 'Vue', 'form','common'], function (exports) {

    layer = layui.layer;
    element = layui.element, form = layui.form, common = layui.common;
    $ = layui.jquery;
    Vue = layui.Vue;
    common.islogin();
    _menus = new Vue({
        el: '#userapp',
        data: {
            menus: [],
            users:[]
        },
        methods: {
            getmenu() {
                m = this;
                $.post('/api/Users/GetAppList', {}, function (data) {
                    var mydata = JSON.parse(data);
                    console.log(mydata);
                    m.menus = mydata.data;
                
                });
            },
            add_tab(_this) {
                console.log(_this);
                xadmin.add_tab(_this.Title,"/Webos"+ _this.tag);
            }, getuser() {
                var us = new Array();
                us.id = window.localStorage["userid"];
                us.name = window.localStorage["user"];
                us.orid = window.localStorage["orid"];
                us.orname = window.localStorage["orname"];
                us.picture = window.localStorage["picture"];
                    this.users = us;
            }, Color: function (_this) {
                console.log(_this);
                if (_this === null || _this === undefined) {
                    return "red";
                } else return _this;

            }
        }
        ,
        mounted() {
    
            this.getmenu();
            this.getuser();
            setTimeout(function () {
                element.init(); 
                init();
                form.render();//没有写这个，操作后没有效果
            }, 500);
        }
    });

    function init() {
        try {
            setInterval(function () {
                var time = document.getElementById("datatime");
                var show_time = document.getElementById("show_time");

                var da = new Date();
                time.innerHTML = da.getFullYear() + "-" + (da.getMonth() + 1) + "-" + da.getDate();
                show_time.innerHTML = da.getHours() + ":" + da.getMinutes() + ":" + da.getSeconds() + ' 星期' + '日一二三四五六'.charAt(da.getDay());

            }, 1000);
     } catch (e) { console.log(e); }
        element.on('nav(me)', function (elem) {
            //if (elem.text() == '亚南') {
            var parent = elem.parent();
            var p = parent.find("dl");
            if (p[0].className !== "layui-nav-child layui-anim layui-anim-upbit layui-show") {
                parent.find("dl").attr('class', 'layui-nav-child layui-anim layui-anim-upbit layui-show');
            } else {
                parent.find("dl").attr('class', 'layui-nav-child ');
            }
            $('.layui-show').on('click', 'dd', function (event) {
                var parent = elem.parent();
                var p = parent.find("dl");
                parent.find("dl").attr('class', 'layui-nav-child ');
            })

        });
        
        element.on('nav(mes)', function (elem) {
            //if (elem.text() == '亚南') {
            var parent = elem.parent();
            var p = parent.find("dl");
            if (p[0].className !== "layui-nav-child layui-anim layui-anim-upbit layui-show") {
                parent.find("dl").attr('class', 'layui-nav-child layui-anim layui-anim-upbit layui-show');
            } else {
                parent.find("dl").attr('class', 'layui-nav-child ');
            }
            $('.layui-show').on('click', 'dd', function (event) {
                var parent = elem.parent();
                var p = parent.find("dl");
                parent.find("dl").attr('class', 'layui-nav-child ');
            })
        });
        // 打开页面初始
        xadmin.init();

        //关闭tab清除记忆
        element.on('tabDelete(xbs_tab)', function (data) {
            var id = $(this).parent().attr('lay-id');
            xadmin.del_data(id);
        });
        //左侧菜单
        $('.left-nav #nav').on('click', 'li', function (event) {

            if ($(this).parent().attr('id') == 'nav') {
                xadmin.set_cate_data({ key: 'f1', value: $('.left-nav #nav li').index($(this)) })
                xadmin.set_cate_data({ key: 'f2', value: null })
                xadmin.set_cate_data({ key: 'f3', value: null })
            }

            if ($(this).parent().parent().parent().attr('id') == 'nav') {
                xadmin.set_cate_data({ key: 'f2', value: $('.left-nav #nav li').index($(this)) })
                xadmin.set_cate_data({ key: 'f3', value: null })
            }

            if ($(this).parent().parent().parent().parent().parent().attr('id') == 'nav') {
                xadmin.set_cate_data({ key: 'f3', value: $('.left-nav #nav li').index($(this)) })
            }



            if ($('.left-nav').css('width') == '60px') {
                $('.left-nav').animate({ width: '220px' }, 100);
                $('.page-content').animate({ left: '220px' }, 100);
                $('.left-nav i').css('font-size', '14px');
                $('.left-nav cite,.left-nav .nav_right').show();
            }

            if ($(window).width() < 768) {
                $('.page-content-bg').show();
            }

            $('.left-nav').find('a').removeClass('active');
            $(this).children('a').addClass('active');
            if ($(this).children('.sub-menu').length) {
                if ($(this).hasClass('open')) {
                    $(this).removeClass('open');
                    $(this).find('.nav_right').html('&#xe697;');
                    $(this).children('.sub-menu').stop(true, true).slideUp();
                    $(this).siblings().children('.sub-menu').slideUp();
                } else {
                    $(this).addClass('open');
                    $(this).children('a').find('.nav_right').html('&#xe6a6;');
                    $(this).children('.sub-menu').stop(true, true).slideDown();
                    $(this).siblings().children('.sub-menu').stop(true, true).slideUp();
                    $(this).siblings().find('.nav_right').html('&#xe697;');
                    $(this).siblings().removeClass('open');
                }
            }
            event.stopPropagation();
        })
        var left_tips_index = null;
        $('.left-nav #nav').on('mouseenter', '.left-nav-li', function (event) {
            if ($('.left-nav').css('width') != '220px') {
                var tips = $(this).attr('lay-tips');
                left_tips_index = layer.tips(tips, $(this));
            }
        })

        $('.left-nav #nav').on('mouseout', '.left-nav-li', function (event) {
            layer.close(left_tips_index);
        })
        // 隐藏左侧
        $('.container .left_open i').click(function (event) {
            if ($('.left-nav').css('width') == '220px') {
                $('.left-nav .open').click();
                $('.left-nav i').css('font-size', '18px');
                $('.left-nav').animate({ width: '60px' }, 100);
                $('.left-nav cite,.left-nav .nav_right').hide();
                $('.page-content').animate({ left: '60px' }, 100);
                $('.page-content-bg').hide();
            } else {
                $('.left-nav').animate({ width: '220px' }, 100);
                $('.page-content').animate({ left: '220px' }, 100);
                $('.left-nav i').css('font-size', '14px');
                $('.left-nav cite,.left-nav .nav_right').show();
                if ($(window).width() < 768) {
                    $('.page-content-bg').show();
                }
            }

        });

        $('.page-content-bg').click(function (event) {
            $('.left-nav .open').click();
            $('.left-nav i').css('font-size', '18px');
            $('.left-nav').animate({ width: '60px' }, 100);
            $('.left-nav cite,.left-nav .nav_right').hide();
            $('.page-content').animate({ left: '60px' }, 100);
            $(this).hide();
        });

        $(".layui-tab-title").on('contextmenu', 'li', function (event) {
            var tab_left = $(this).position().left;
            var tab_width = $(this).width();
            var left = $(this).position().top;
            var this_index = $(this).attr('lay-id');
            $('#tab_right').css({ 'left': tab_left + tab_width / 2 }).show().attr('lay-id', this_index);
            $('#tab_show').show();
            return false;
        });
        
        $('#tab_right').on('click', 'dd', function (event) {
            var data_type = $(this).attr('data-type');
            var lay_id = $(this).parents('#tab_right').attr('lay-id');
            if (data_type == 'this') {
                $('.layui-tab-title li[lay-id=' + lay_id + ']').find('.layui-tab-close').click();
            } else if (data_type == 'other') {
                $('.layui-tab-title li').eq(0).find('.layui-tab-close').remove();
                $('.layui-tab-title li[lay-id!=' + lay_id + ']').find('.layui-tab-close').click();
            } else if (data_type == 'all') {
                $('.layui-tab-title li[lay-id]').find('.layui-tab-close').click();
            }
            $('#tab_right').hide();
            $('#tab_show').hide();
        })


        $('.page-content,#tab_show,.container,.left-nav').click(function (event) {
            $('#tab_right').hide();
            $('#tab_show').hide();
        });

        // 页面加载完要做的
        xadmin.end();
    }

     exports('xadmin', xadmin);

});