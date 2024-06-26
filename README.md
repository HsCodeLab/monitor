# 不用改代码的上位机监控软件

#### 介绍
不用改代码的上位机监控软件，用于UPS,逆变器，充电桩等新能源设备（不限于）监控上位机。

#### 软件架构
在新能源设备通信中，你是否受够了适配N多种机型，匹配N多种协议,然后在代码中维护一大串if-else直到头昏脑胀？直到某一天if-else多到你自己都认不全？
如果是，这个软件正是你需要的.
软件设计思想：改配置不改代码.一旦开发完成，后期维护基本不用改代码.
那么是怎么做到的呢？JSON ！！！
什么是JSON? 可以理解为特殊格式的.txt文件.大佬们请自动忽略这一句.
页面和协议都是基于JSON配置文件，软件启动以后读取相关JSON配置，对应生成相应页面。通信时根据JSON协议配置读取和显示.
也就是说后期如果协议有修改或者增加，对应修改相关的JSON协议文件就好了.

#### 访问链接

Github: https://github.com/HsCodeLab/monitor

Gitee: https://gitee.com/watson_bi/monitor


#### 相关软件和技术

1.  开发工具：idea , JDK17, gradle
2.  逻辑处理：java , 特别的，其中处理JSON用的是Jackson类库.
3.  页面框架：javaFX.
4.  数据库：H2内嵌数据库.
5.  数据库框架: MyBatis.

#### 说明
1.  要生成新页面还需要form式的拖控件或者直接一个个用代码new出来吗？NO.直接复制并修改JSON文件即可.

2.  要适配新的协议还需要加if-else吗？NO.直接复制并修改JSON文件即可.

3.  要增加新的语言比如一开始只有中文现在要增加英语要改代码吗？No.直接复制并修改JSON文件即可.

4.  以上3问，效果请看演示视频.实现细节请看源码.

5.  为什么使用H2内嵌数据库? 一般情况下，单机应用H2数据库足矣.简单小巧方便，直接引入jar包依赖就行.当然因为数据库框架用的是MyBatis,如果要切换到其他数据库也很方便，改一些MyBatis的配置就行.
    
6.  一般情况下，上位机监控软件和新能源设备可以有多种通信方式.串口/USB/Modbus-RTC/Modbus-TCP/SNMP等等.在此版本中实现了串口/Modbus通信的逻辑，其他通信方式，各位同学可以自行搜索实现相应逻辑.

7.  在此版本中，只搞了一个简单的历史记录表，实现分页查询的功能，至于更多的表格设计和各种增删改方法，请根据自己的需要加以实现.

8.  为什么使用gradle? 一开始是使用maven,但是为了适配java模块化,改为gradle,因为gradle有一个自动将non-modular jar转为modular jar的插件badass-jlink-plugin.为什么需要模块化?传统的java项目需要带上整个jre,模块化项目只需要带必要模块的自定义jre,安装包体积大大缩小.

9.  适配Windows/Mac/Linux三种平台,但安装包只做了Windows平台的.其他平台可自行测试.

10. 因为页面是读取JSON生成的,假设生成一个按钮以后,怎么注册点击事件呢?比如生成了"打开串口"的按钮,怎么让该按钮注册有打开串口的功能?
    反射.代码中扫描JSON文件生成按钮,JSON中有一项"item_name"配置,当找到这一个按钮时,同时也会找带有这个"item_name"注解的方法,反射调用.
    说的有点抽象,具体可参考serial.json配置文件和HandlerSerial.java文件对照.其中扫描注解的代码部分在ComponentHandlerFactory类中,
    bindHandler函数(方法).
    总结一下:一句话就是,扫描JSON生成按钮对象,扫描注解找到函数,反射调用该函数,按钮点击就有对应功能了.其他组件类似.

11. 功能基本实现,但是代码有点繁琐,逻辑有点复杂,对新人不太友好.所以目前重点实现逻辑,页面的话有功能但是没有一开始设想的那么便利.
    比如如果要两个按钮,就需要两项配置,这就很重复了.像Vue里面就有v-for指令可以实现传一个数组就可以渲染多个组件.但是要搞成这样
    属实有点太复杂了,目前时间和精力有限.

12. 欢迎各种意见和建议.如果同时还有小伙伴想参与到本开源项目中来,比如优化了哪部分代码或者设计,或者想设计新的页面/主题, 
    欢迎来私信,一起加入到本开源项目中来.


#### 软件体验
    master目录下有两个压缩文件,msi.zip指的是安装包的压缩包,解压以后可以安装体验.
    另外一个monitor.zip,则是免安装的压缩包,解压可以直接体验.

#### 软件截图

![深色主题-英文-实时信息](https://foruda.gitee.com/images/1705050807994610384/a92b5bb2_8975418.png)
![浅色主题-中文-图表展示](https://foruda.gitee.com/images/1705050840613531256/5865f8ee_8975418.png)

#### 链接

    演示视频抖音链接: https://v.douyin.com/iLj4HPpS/ IVL:/ 12/12 m@D.uF

