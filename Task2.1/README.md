# 实验2.1
## 一.实验目的
构建第一个Kotlin应用
## 二.实验环境
操作系统：Windows 10;Andorid stdio
## 三.实验步骤和结果
### 1.准备环境
#### 1.1选择Basic Activity
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/211.png)  
#### 1.2创建虚拟设备
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/212.png)  
#### 1.3运行First Fragment
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/213.png)  
### 2.探索界面布局编辑器
#### 2.1查看具体的布局设计界面
每个界面由一个Fragment组成，初始界面显示的FirstFragment，双
击fragment_first.xml可以查看具体的布局设计界面
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/214.png)  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/215.png)  
#### 2.2对文本内容进行修改
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/216.png)  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/217.png)  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/218.png)  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/219.png)  
#### 2.3对文本颜色，包括大小颜色等的修改
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2110.png)  
修改示例：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2111.png)  
#### 2.4重新运行代码，效果如下：
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2112.png)  
### 3.添加按钮和约束
#### 3.1添加按钮，并修改文本内容  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2113.png)  
为新增的两个按钮添加约束，固定相对位置：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2114.png)  
重新运行效果如下：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2115.png)
#### 3.2更改背景颜色
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2116.png)  
这里发现可以将原来的@string/Hello_My_Kotlin，直接改为其他内容，不需要修改键值对  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2117.png)  
修改效果如下：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2118.png)  
### 4.添加组件并完成交互代码
#### 4.1设置代码自动补充  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2119.png)  
#### 4.2为按钮添加点击事件（弹出消息框）  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2120.png)   
#### 4.3设置COUNT的点击事件，随机生成一个数，作为RANDOM功能的范围  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2121.png)  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2122.png)  
#### 4.4补充SecondFragment
添加文本框：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2123.png)  
修改文本框内容样式：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2124.png)  
### 5.应用程序使用Navigation机制：  
#### 5.设置Navigation环境
Navigation机制用来导航两个页面FirstFragment和SecondFragment，向两个界面添加若干组件，并添加事件代码完成指定功能
```` 
dependencies {
  val nav_version = "2.5.3"
  // Kotlin
  implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
  implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
}
````
但是当我输入这段代码时出现，var 报错，改为def后能正常下载依赖  
<br>在gradle.app中添加插件：  
`id 'androidx.navigation.safeargs.kotlin'`
<br>在gradle,project中添加插件：  
`id 'androidx.navigation.safeargs.kotlin' version '2.5.3' apply false'`
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2125.png)  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2126.png)  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2127.png)  
#### 5.2创建导航图
①在“Project”窗口中，右键点击 res 目录，然后依次选择 New > Android Resource File。此时系统会显示 New Resource File 对话框。  
②在 File name 字段中输入名称，例如“nav_graph”。  
③从 Resource type 下拉列表中选择 Navigation，然后点击 OK。  
当您添加首个导航图时，Android Studio 会在 res 目录内创建一个 navigation 资源目录。该目录包含您的导航图资源文件（例如 nav_graph.xml）。  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2128.png)  
#### 5.3Navigation Editor
添加图表后，Android Studio 会在 Navigation Editor 中打开该图表。在 Navigation Editor 中，您可以直观地修改导航图，或直接修改底层 XML。    
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2129.png)  
①Attributes：显示导航图中当前所选项的属性。  
②Graph Editor：包含导航图的视觉表示形式。您可以在 Design 视图和 Text 视图中的底层 XML 表示形式之间切换。  
③Component tree：查看布局的层次结构。  
#### 5.4创建导航动作
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2130.png)  
#### 5.5FirstFragment添加代码，向SecondFragment发数据
在activity_main.xml中添加以下代码，用于在容器中动态添加和替换 Fragment:  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2131.png)  
将获取FirstFragment当前TextView中显示的数字并传输至SecondFragment。  
修改RANDOM点击事件，实现将随机数字传到SecondFragment。  
修改FirstFragment.kt中代码，修改后如下：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2132.png)  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2133.png)  
SecondFragment.kt中加入代码：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2134.png)  
我的头提示栏为：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2135.png)  
这里在 strings.xml 中，需要定义一个新的字符串资源，具体代码如下  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2136.png)  
这样才能替换%d为COUNT设置的值  
给PREVIOUS按钮设置点击事件，返回FIrstFragment  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2137.png)  
最终效果如下：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2138.png)
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2139.png)








