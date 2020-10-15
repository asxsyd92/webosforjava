layui.config({
    base: 'Common/layim/layui_exts/' //指定 winui 路径 //假设这是cookie.js所在的目录（本页面的相对路径）
}).extend({ //设定模块别名
    Vue: 'Vue'
    //  如果js是在根目录，也可以不用设定别名,因为我js的是在根目录，所以这句话其实也不用写也行。
    , VueRouter: 'VueRouter', httpVueLoader: 'httpVueLoader', common: 'common'
}); var $ = null;
layui.use(["element", "layer", "form", "util", "flow", "layedit", 'Vue', 'VueRouter', 'httpVueLoader', 'common', 'ClipboardJS'],
    function () {
        $ = layui.jquery, element = layui.element; var Vue = layui.Vue, VueRouter = layui.VueRouter, httpVueLoader = layui.httpVueLoader, common = layui.common, ClipboardJS = layui.ClipboardJS;

        Vue.component('pane', {
            template: `
	<div class="pane" v-show="show">		
		<slot></slot>
	</div>
	`,
            data: function () {
                return {
                    show: true
                }
            },
            //props为来自父组件的变量，实现父组件与子组件通信
            props: {
                //设置pane的标识
                name: {
                    type: String
                },
                //label是设置标题
                label: {
                    type: String,
                    default: ''
                }
            },
            methods: {
                updateNav: function () {
                    //$parent 父链，通知父组件（tabs）进行更新。
                    //说明：在业务中尽可能不要使用$parent来操作父组件，$parent适合标签页这样的独立组件
                    this.$parent.updateNav();
                }
            },
            //监听label
            watch: {
                label() {
                    this.updateNav();
                }
            },

            mounted() {
                //初始化tabs
                this.updateNav();
            }
        })
        Vue.component('tabs', {
            template: `
	<div class="tabs">
            <div class="tabs-bar">
                <!--标题页的标题 v-for遍历, :class 动态绑定class-->
                <div :class="tabCls(item)" v-for="(item,index) in navList" @click="handleChange(index)">
                    {{item.label}}
                </div>
            </div>
            <div class="tabs-content">
                <!--这里的slot就是嵌套的pane组件的内容-->
                <slot></slot>
            </div>
        </div>
	`,
            data: function () {
                return {
                    //将pane的标题保存到数组中
                    navList: [],
                    //保存父组件的value到currentValue变量中，以便在本地维护
                    currentValue: this.value
                }
            },
            props: {
                //接收父组件的value
                value: {
                    type: [String]
                }
            },
            methods: {
                //使用$children遍历子组件，得到所有的pane组件
                getTabs: function () {
                    return this.$children.filter(function (item) {
                        return item.$options.name === 'pane';
                    })
                },
                //更新tabs
                updateNav() {
                    this.navList = [];
                    var _this = this;
                    this.getTabs().forEach(function (pane, index) {
                        _this.navList.push({
                            label: pane.label,
                            name: pane.name || index
                        });
                        //如果没有设置name，默认设置为索引值
                        if (!pane.name) {
                            pane.name = index;
                        }
                        //设置第一个pane为当前显示的tab
                        if (index === 0) {
                            if (!_this.currentValue) {
                                _this.currentValue = pane.name || index;
                            }
                        }
                    });
                    this.updateStatus();
                },
                updateStatus() {
                    var tabs = this.getTabs();
                    var _this = this;
                    //显示当前选中的tab对应的pane组件，隐藏没有选中的
                    tabs.forEach(function (tab) {
                        return tab.show = tab.name === _this.currentValue;
                    })
                },
                tabCls: function (item) {
                    return [
                        'tabs-tab',
                        {
                            //为当前选中的tab加一个tabs-tab-active class
                            'tabs-tab-active': item.name === this.currentValue
                        }
                    ]
                },
                //点击tab标题触发
                handleChange: function (index) {
                    var nav = this.navList[index];
                    var name = nav.name;
                    //改变当前选中的tab，触发watch
                    this.currentValue = name;
                    //实现子组件与父组件通信
                    this.$emit('input', name);
                }
            },
            watch: {
                value: function (val) {
                    this.currentValue = val;
                },
                currentValue: function () {
                    //tab发生变化时，更新pane的显示状态
                    this.updateStatus();
                }
            }
        })

        const menus = [

            {
                show: true,
                ioc: "fa fa-book fa-spin",
                name: "我的作品",
                path: "/product",
                component: "/PortalSite/product.html"
            },
            {
                show: true,
                ioc: "fa fa-download fa-spin",
                name: "分享下载",
                path: "/soso",
                component: "/PortalSite/soso.html"
            },
            {
                show: true,
                ioc: "fa fa-shopping-cart fa-spin",
                name: "淘券券",
                path: "/taobao",
                component: "/PortalSite/taobao.html"
            },
            {
                show: true,
                ioc: "fa fa-grav fa-open",
                name: "开发者",
                path: "/open/:type",
                component: "/PortalSite/open.html"
            }, {
                show: true,
                ioc: "fa fa-free-code-camp fa-spin",
                name: "磁力链接",
                path: "/bt",
                component: "/PortalSite/bt.html"
            }, {
                show: false,
                ioc: "",
                name: "详情",
                path: "/newsDetail/:key",
                component: "/PortalSite/newsDetail.html"
            },
            // 重定向
            {
                show: false,
                path: '*',
                redirect: '/mian',
                component: "/PortalSite/mian.html"
            }
        ];
        let routes = []; let _muen = [];
        $(menus).each(function (index, menu) {

            //  for (let menu of menus) {
            if (menu.show) {
                _muen.push(menu);
            }
            Vue.component(menu.name, function (resolve) {
                // 这个特殊的 `require` 语法将会告诉 webpack
                // 自动将你的构建代码切割成多个包，这些包
                // 会通过 Ajax 请求加载
                require([menu.path], resolve);
            });
        

            routes.push({
                path: menu.path,
                component: httpVueLoader(menu.component)
            });


        });
        var app = new Vue({
            el: '#app',
            data: {
                //初始显示第几个
                menus: _muen,
                value: '1'

            }
           
    });
  


    });
Date.prototype.Format = function (n) {
    var t = n;
    return t = t.replace(/yyyy|YYYY/, this.getFullYear()),
        t = t.replace(/yy|YY/, this.getYear() % 100 > 9 ? (this.getYear() % 100).toString() : "0" + this.getYear() % 100),
        t = t.replace(/MM/, this.getMonth() + 1 > 9 ? (this.getMonth() + 1).toString() : "0" + (this.getMonth() + 1)),
        t = t.replace(/M/g, this.getMonth() + 1),
        t = t.replace(/w|W/g, ["日", "一", "二", "三", "四", "五", "六"][this.getDay()]),
        t = t.replace(/dd|DD/, this.getDate() > 9 ? this.getDate().toString() : "0" + this.getDate()),
        t = t.replace(/d|D/g, this.getDate()),
        t = t.replace(/hh|HH/, this.getHours() > 9 ? this.getHours().toString() : "0" + this.getHours()),
        t = t.replace(/h|H/g, this.getHours()),
        t = t.replace(/mm/, this.getMinutes() > 9 ? this.getMinutes().toString() : "0" + this.getMinutes()),
        t = t.replace(/m/g, this.getMinutes()),
        t = t.replace(/ss|SS/, this.getSeconds() > 9 ? this.getSeconds().toString() : "0" + this.getSeconds()),
        t.replace(/s|S/g, this.getSeconds())
};


function showTime() {
    try {
        var time = document.getElementById("datatime");
        var show_time = document.getElementById("show_time");

        var da = new Date();
        time.innerHTML = da.getFullYear() + "-" + (da.getMonth() + 1) + "-" + da.getDate();
        show_time.innerHTML = da.getHours() + ":" + da.getMinutes() + ":" + da.getSeconds() + ' 星期' + '日一二三四五六'.charAt(da.getDay());
    } catch (e) { console.log(e); }

}
function openmuen() {

    //$("#_on_off")[0].removeclass("fa-navicon");
    //$("#_on_off")[0].addclass("fa-times");
    $(".blog-mask").unbind("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend");
    $(".blog-nav-left").unbind("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend");
    $(".blog-mask").removeClass("maskOut");
    $(".blog-mask").addClass("maskIn");
    $(".blog-mask").removeClass("layui-hide");
    $(".blog-mask").addClass("layui-show");
    $(".blog-nav-left").removeClass("leftOut");
    $(".blog-nav-left").addClass("leftIn");
    $(".blog-nav-left").removeClass("layui-hide");
    $(".blog-nav-left").addClass("layui-show");

}

function closemuen() {

    //$("#_on_off")[0].removeClass("fa-times");
    //$("#_on_off")[0].addClass("fa-navicon");
    $(".blog-mask").on("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend",
        function () {

            $(".blog-mask").addClass("layui-hide")
        });
    $(".blog-nav-left").on("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend",
        function () {

            $(".blog-nav-left").addClass("layui-hide")
        });
    $(".blog-mask").removeClass("maskIn");
    $(".blog-mask").addClass("maskOut");
    $(".blog-mask").removeClass("layui-show");
    $(".blog-nav-left").removeClass("leftIn");
    $(".blog-nav-left").addClass("leftOut");
    $(".blog-nav-left").removeClass("layui-show")
}