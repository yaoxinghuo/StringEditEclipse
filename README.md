StringEditEclipse
=================

Multi Line Popup String Edit plugin for eclipse

![](https://public.bn1.livefilestore.com/y2pLKunr0q-S_eHZZZ1s4aKqEcLX5cGa42dzYs1YFwiZetep8VPlWmGGFoFt1lk3BAetw0re_UoyK8jtjkJb3TPFRSAT5jQidsZKaka72GrFkk/2013-12-06_15-25-21.png?psid=1)

When edit java source file in eclipse, meet string variable, ctrl+click(mac is cmd+click, or hit Ctrl+Alt+M or right mouse click show context menu, select "String Edit"), will popup a multi string editor, all the change with this editor will sync to the main editor and escape the strings.
Can also copy the original string to clicpboard.
Have the option whether to escape the Chinese to unicode.

Offline zip package can be downloaded from here: http://pan.baidu.com/s/1vMJxD

Xcode 下有个文本插件https://github.com/holtwick/HOStringSense-for-Xcode 对于要输入和编辑长文本的开发者非常好用，直接弹出一个 Popup 框，在里面就可以方便的编辑文本了，受到他的启发，同时也参考了https://github.com/kbss/StringUtils_plugin这个项目，自己开发了一个 Eclipse 下的类似功能的插件，按照国际惯例，开源之: https://github.com/yaoxinghuo/StringEditEclipse

主要功能如图：
1)在 java 代码的变量文本上，按住 Ctrl+鼠标单击，或者按快捷键（Ctrl+Alt+M），或者点右键，在右键菜单找到String Edit，程序会自动找出有文本的地方，进而判断是否弹出编辑器；
2)弹出如图的编辑器，在编辑器中的改动会实时修改到变量中；
3)还可以复制文本，比直接在变量中复制的优点是，他不会复制成转意后的文本；
4)通过复选框Unicode String Format，可以选择是否将变量中的中文保存成 Unicode，如勾选后，就变成String sql = "insert into test(id, name ,age,sex)\n" +
"values(1,\"\u674E\u521A\",18,\"male\");"。

最后给出 离线zip 包安装地址：http://pan.baidu.com/s/1vMJxD

