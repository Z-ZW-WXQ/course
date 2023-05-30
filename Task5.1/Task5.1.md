# 1.预备工作
## 1.1 创建tensorflow虚拟环境并激活
【图片1】  
## 1.2 在虚拟环境中下载tflite-model-maker
打开Anaconda控制台，输入pip install tflite-model-maker  
【图片2】  
这里有我的conda安装内容，包括pip install tflite-model-maker  
【图片3】  
## 1.3 下载完成，测试tensorflow以及tflite-model-maker模块是否能成功导入



```python
import tensorflow as tf
print(tf.__version__)
import os
import numpy as np
import tensorflow as tf
assert tf.__version__.startswith('2')
from tflite_model_maker import model_spec
from tflite_model_maker import image_classifier
from tflite_model_maker.config import ExportFormat
from tflite_model_maker.config import QuantizationConfig
from tflite_model_maker.image_classifier import DataLoader
import matplotlib.pyplot as plt
```

    2.9.3
    

出现ImportError: cannot import name 'array_record_module' from 'array_record.python'
千万不要重装或升级array_record_module！！！
问题我在github上找到了解决方法，将tensorflow-datasets由4.9.0降级为4.8.3，问题得以解决。
【图片4】
警告可忽略
# 2.模型训练
## 2.1 获取数据
本实验先从较小的数据集开始训练


```python
image_path = tf.keras.utils.get_file(
      'flower_photos.tgz',
      'https://storage.googleapis.com/download.tensorflow.org/example_images/flower_photos.tgz',
      extract=True)
image_path = os.path.join(os.path.dirname(image_path), 'flower_photos')
image_path
```




    'C:\\Users\\26698\\.keras\\datasets\\flower_photos'



这里从storage.googleapis.com中下载了本实验所需要的数据集。image_path可以定制，默认是在用户目录的.keras\datasets中。
## 2.2 运行示例
一共需4步完成。
第一步：加载数据集，并将数据集分为训练数据和测试数据。


```python
data = DataLoader.from_folder(image_path)
train_data, test_data = data.split(0.9)
```

    INFO:tensorflow:Load image with size: 3670, num_label: 5, labels: daisy, dandelion, roses, sunflowers, tulips.
    

第二步：训练Tensorflow模型


```python
model = image_classifier.create(train_data)
```

    INFO:tensorflow:Retraining the models...
    Model: "sequential"
    _________________________________________________________________
     Layer (type)                Output Shape              Param #   
    =================================================================
     hub_keras_layer_v1v2 (HubKe  (None, 1280)             3413024   
     rasLayerV1V2)                                                   
                                                                     
     dropout (Dropout)           (None, 1280)              0         
                                                                     
     dense (Dense)               (None, 5)                 6405      
                                                                     
    =================================================================
    Total params: 3,419,429
    Trainable params: 6,405
    Non-trainable params: 3,413,024
    _________________________________________________________________
    None
    Epoch 1/5
    

    D:\anaconda3\envs\tf\lib\site-packages\keras\optimizers\optimizer_v2\gradient_descent.py:108: UserWarning: The `lr` argument is deprecated, use `learning_rate` instead.
      super(SGD, self).__init__(name, **kwargs)
    

    103/103 [==============================] - 78s 718ms/step - loss: 0.8502 - accuracy: 0.7764
    Epoch 2/5
    103/103 [==============================] - 51s 491ms/step - loss: 0.6520 - accuracy: 0.8953
    Epoch 3/5
    103/103 [==============================] - 45s 435ms/step - loss: 0.6199 - accuracy: 0.9117
    Epoch 4/5
    103/103 [==============================] - 46s 441ms/step - loss: 0.5982 - accuracy: 0.9287
    Epoch 5/5
    103/103 [==============================] - 54s 524ms/step - loss: 0.5872 - accuracy: 0.9339
    

第三步：评估模型


```python
loss, accuracy = model.evaluate(test_data)
```

    12/12 [==============================] - 13s 579ms/step - loss: 0.6006 - accuracy: 0.9264
    

第四步，导出Tensorflow Lite模型


```python
model.export(export_dir='.')
```

    INFO:tensorflow:Assets written to: C:\Users\26698\AppData\Local\Temp\tmp02mr24a7\assets
    

    INFO:tensorflow:Assets written to: C:\Users\26698\AppData\Local\Temp\tmp02mr24a7\assets
    D:\anaconda3\envs\tf\lib\site-packages\tensorflow\lite\python\convert.py:766: UserWarning: Statistics for quantized inputs were expected, but not specified; continuing anyway.
      warnings.warn("Statistics for quantized inputs were expected, but not "
    

    INFO:tensorflow:Label file is inside the TFLite model with metadata.
    

    INFO:tensorflow:Label file is inside the TFLite model with metadata.
    

    INFO:tensorflow:Saving labels in C:\Users\26698\AppData\Local\Temp\tmprhwiwisj\labels.txt
    

    INFO:tensorflow:Saving labels in C:\Users\26698\AppData\Local\Temp\tmprhwiwisj\labels.txt
    

    INFO:tensorflow:TensorFlow Lite model exported successfully: .\model.tflite
    

    INFO:tensorflow:TensorFlow Lite model exported successfully: .\model.tflite
    

这里导出的Tensorflow Lite模型包含了元数据(metadata),其能够提供标准的模型描述。导出的模型存放在Jupyter Notebook当前的工作目录中。


```python
current_dir = os.getcwd()
print(current_dir)
```

    D:\apache-maven-3.8.5\maven-repo\pythontest1\.idea\软件实践
    

【图片5】
