# 1.模块环境构建
## 1.1打开Android Studio，选择“Open an Existing Project”，运行TFLClassify-main, 项目包含两个module：finish 和 start，finish模块是已经完成的项目，start则是本项目实践的模块。
构建模块并编译代码   
允许应用获取手机摄像头的权限，得到下述效果图，界面利用随机数表示虚拟的识别结果。   
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/401.png)    
# 2.向应用中添加TensorFlow Lite
## 2.1选择"start"模块，选择File，然后New>Other>TensorFlow Lite Mode  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/402.png)  
## 2.2选择已经下载的自定义的训练模型。
本教程模型训练任务以后完成，这里选择finish模块中ml文件下的FlowerModel.tflite。   
最终TensorFlow Lite模型被成功导入，并生成摘要信息。   
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/403.png)    
# 3.检查代码中的TODO项  
本项目初始代码中包括了若干的TODO项，以导航项目中未完成之处。为了方便起见，首先查看TODO列表视图，View>Tool Windows>TODO     
默认情况下了列出项目所有的TODO项，进一步按照模块分组（Group By）   
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/404.png)    
# 4.添加代码重新运行APP
## 4.1定位“start”模块MainActivity.kt文件的TODO 1，添加初始化训练模型的代码
```
// TODO 1: Add class variable TensorFlow Lite Model
  private val flowerModel = FlowerModel.newInstance(ctx)
```
## 4.2在CameraX的analyze方法内部，需要将摄像头的输入ImageProxy转化为Bitmap对象，并进一步转化为TensorImage 对象  
```
// TODO 2: Convert Image to Bitmap then to TensorImage
  val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))
```
## 4.3对图像进行处理并生成结果，主要包含下述操作：
按照属性score对识别结果按照概率从高到低排序    
列出最高k种可能的结果，k的结果由常量MAX_RESULT_DISPLAY定义  
```
// TODO 3: Process the image using the trained model, sort and pick out the top results
  val outputs = flowerModel.process(tfImage)
      .probabilityAsCategoryList.apply {
          sortByDescending { it.score } // Sort with highest confidence first
      }.take(MAX_RESULT_DISPLAY) // take the top results
```
## 4.4将识别的结果加入数据对象Recognition 中，包含label和score两个元素。后续将用于RecyclerView的数据显示
```
  // TODO 4: Converting the top probability items into a list of recognitions
  for (output in outputs) {
      items.add(Recognition(output.label, output.score))
  }
```
## 4.5将原先用于虚拟显示识别结果的代码注释掉或者删除
```
// START - Placeholder code at the start of the codelab. Comment this block of code out.
for (i in 0..MAX_RESULT_DISPLAY-1){
    items.add(Recognition("Fake label $i", Random.nextFloat()))
}
// END - Placeholder code at the start of the codelab. Comment this block of code out.
```
# 5.以物理设备重新运行start模块
# 6.最终运行效果
真机运行结果：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/406.jpg)    
虚拟设备运行结果：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/405.png)    
