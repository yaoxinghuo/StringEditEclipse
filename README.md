String Edit plugin for Eclipse 1.0.1
=================

Multi-Line Popup String Editor plugin for eclipse

![](https://public.bn1303.livefilestore.com/y2ppD_dW6j1zpjCe4O7k4avJFwnV60doQY7BVpN00dWjpUwKhevgHv9zaSpL8kVhMQn146BlSuvuk9xXuTchA-6wPk0SQKCDxWimc5t2ie9tCg/eclipse_2013-12-07.png?psid=1)

When edit java source file in eclipse, meet string variable, ctrl+click(mac is cmd+click, or hit Ctrl/Cmd+Alt+M or right mouse click show context menu, select "String Edit"), will popup a multi-line string editor window, all the changes with this editor will sync to the main editor and escape the strings.
Can also copy the original string to clicpboard.
Have the option whether to escape the Chinese to unicode.

Offline install zip package can be downloaded from here: http://sdrv.ms/196Ykus or http://pan.baidu.com/s/1Aahwz

Xcode 下有个文本插件https://github.com/holtwick/HOStringSense-for-Xcode 对于要输入和编辑长文本的开发者非常好用，直接弹出一个 Popup 框，在里面就可以方便的编辑文本了，受到他的启发，同时也参考了https://github.com/kbss/StringUtils_plugin 这个项目，自己开发了一个 Eclipse 下的类似功能的插件，按照国际惯例，开源之: https://github.com/yaoxinghuo/StringEditEclipse

主要功能如图
1)在 java 代码的变量文本上，按住 Ctrl+鼠标单击(MAC 是 Cmd+单击)，或者按快捷键（Ctrl+Alt+M，mac是 Cmd+Alt+M），或者点右键，在右键菜单找到"String Edit"，程序会自动找出有文本的地方，进而判断是否弹出编辑器；

2)弹出如图的编辑器，编辑器中直接显示转义前的文本，方便阅读和编辑，在编辑器中的改动会实时修改并转义保存到变量中；

3)还可以复制文本，比直接在变量中复制的优点是不会复制成转义后的文本；

4)通过复选框Unicode String Format，可以选择是否将变量中的中文保存成 Unicode，如勾选后，就变成String sql = "insert into test(id, name ,age,sex)\n" +
"values(1,\"\u674E\u521A\",18,\"male\");"。

最后给出 离线zip 包安装地址：http://pan.baidu.com/s/1Aahwz 或者 http://sdrv.ms/196Ykus

