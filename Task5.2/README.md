# TensorFlow训练石头剪刀布数据集
本文将演示石头剪刀布图片库的神经网络训练过程。石头剪刀布数据集包含了不同的手势图片，来自不同的种族、年龄和性别。
首先下载石头剪刀布的训练集和测试集：


```python
!wget --no-check-certificate https://storage.googleapis.com/learning-datasets/rps.zip -O D:/Game/rps.zip

!wget --no-check-certificate https://storage.googleapis.com/learning-datasets/rps-test-set.zip -O D:/Game/rps-test-set.zip
```

下载后解压数据集
检测数据集的解压结果，打印相关信息。


```python
import os
rock_dir = os.path.join('D:/data/rps/rock')
paper_dir = os.path.join('D:/data/rps/paper')
scissors_dir = os.path.join('D:/data/rps/scissors')

print('total training rock images:', len(os.listdir(rock_dir)))
print('total training paper images:', len(os.listdir(paper_dir)))
print('total training scissors images:', len(os.listdir(scissors_dir)))

rock_files = os.listdir(rock_dir)
print(rock_files[:10])

paper_files = os.listdir(paper_dir)
print(paper_files[:10])

scissors_files = os.listdir(scissors_dir)
print(scissors_files[:10])

```

    total training rock images: 840
    total training paper images: 840
    total training scissors images: 840
    ['rock01-000.png', 'rock01-001.png', 'rock01-002.png', 'rock01-003.png', 'rock01-004.png', 'rock01-005.png', 'rock01-006.png', 'rock01-007.png', 'rock01-008.png', 'rock01-009.png']
    ['paper01-000.png', 'paper01-001.png', 'paper01-002.png', 'paper01-003.png', 'paper01-004.png', 'paper01-005.png', 'paper01-006.png', 'paper01-007.png', 'paper01-008.png', 'paper01-009.png']
    ['scissors01-000.png', 'scissors01-001.png', 'scissors01-002.png', 'scissors01-003.png', 'scissors01-004.png', 'scissors01-005.png', 'scissors01-006.png', 'scissors01-007.png', 'scissors01-008.png', 'scissors01-009.png']
    

各打印两张石头剪刀布训练集图片


```python
%matplotlib inline

import matplotlib.pyplot as plt
import matplotlib.image as mpimg

pic_index = 2

next_rock = [os.path.join(rock_dir, fname)
             for fname in rock_files[pic_index-2:pic_index]]
next_paper = [os.path.join(paper_dir, fname)
              for fname in paper_files[pic_index-2:pic_index]]
next_scissors = [os.path.join(scissors_dir, fname)
                 for fname in scissors_files[pic_index-2:pic_index]]

for i, img_path in enumerate(next_rock+next_paper+next_scissors):
    #print(img_path)
    img = mpimg.imread(img_path)
    plt.imshow(img)
    plt.axis('Off')
    plt.show()

```


    
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/output_5_0.png)    
    



    
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/output_5_1.png)    
    



    
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/output_5_2.png)    
    



    
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/output_5_3.png)    
    



    
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/output_5_4.png)    
    



    
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/output_5_5.png)    
    


调用TensorFlow的keras进行数据模型的训练和评估。Keras是开源人工神经网络库，TensorFlow集成了keras的调用接口，可以方便的使用。


```python
import tensorflow as tf
import keras_preprocessing
from keras_preprocessing import image
from keras_preprocessing.image import ImageDataGenerator

TRAINING_DIR = "D:/data/rps/"
training_datagen = ImageDataGenerator(
    rescale = 1./255,
    rotation_range=40,
    width_shift_range=0.2,
    height_shift_range=0.2,
    shear_range=0.2,
    zoom_range=0.2,
    horizontal_flip=True,
    fill_mode='nearest')

VALIDATION_DIR = "D:/data/rps-test-set/"
validation_datagen = ImageDataGenerator(rescale = 1./255)

train_generator = training_datagen.flow_from_directory(
    TRAINING_DIR,
    target_size=(150,150),
    class_mode='categorical',
    batch_size=126
)

validation_generator = validation_datagen.flow_from_directory(
    VALIDATION_DIR,
    target_size=(150,150),
    class_mode='categorical',
    batch_size=126
)

model = tf.keras.models.Sequential([
    # Note the input shape is the desired size of the image 150x150 with 3 bytes color
    # This is the first convolution
    tf.keras.layers.Conv2D(64, (3,3), activation='relu', input_shape=(150, 150, 3)),
    tf.keras.layers.MaxPooling2D(2, 2),
    # The second convolution
    tf.keras.layers.Conv2D(64, (3,3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2,2),
    # The third convolution
    tf.keras.layers.Conv2D(128, (3,3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2,2),
    # The fourth convolution
    tf.keras.layers.Conv2D(128, (3,3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2,2),
    # Flatten the results to feed into a DNN
    tf.keras.layers.Flatten(),
    tf.keras.layers.Dropout(0.5),
    # 512 neuron hidden layer
    tf.keras.layers.Dense(512, activation='relu'),
    tf.keras.layers.Dense(3, activation='softmax')
])


model.summary()

model.compile(loss = 'categorical_crossentropy', optimizer='rmsprop', metrics=['accuracy'])

history = model.fit(train_generator, epochs=25, steps_per_epoch=20, validation_data = validation_generator, verbose = 1, validation_steps=3)

model.save("rps.h5")
```

    Found 2520 images belonging to 3 classes.
    Found 372 images belonging to 3 classes.
    Model: "sequential"
    _________________________________________________________________
     Layer (type)                Output Shape              Param #   
    =================================================================
     conv2d (Conv2D)             (None, 148, 148, 64)      1792      
                                                                     
     max_pooling2d (MaxPooling2D  (None, 74, 74, 64)       0         
     )                                                               
                                                                     
     conv2d_1 (Conv2D)           (None, 72, 72, 64)        36928     
                                                                     
     max_pooling2d_1 (MaxPooling  (None, 36, 36, 64)       0         
     2D)                                                             
                                                                     
     conv2d_2 (Conv2D)           (None, 34, 34, 128)       73856     
                                                                     
     max_pooling2d_2 (MaxPooling  (None, 17, 17, 128)      0         
     2D)                                                             
                                                                     
     conv2d_3 (Conv2D)           (None, 15, 15, 128)       147584    
                                                                     
     max_pooling2d_3 (MaxPooling  (None, 7, 7, 128)        0         
     2D)                                                             
                                                                     
     flatten (Flatten)           (None, 6272)              0         
                                                                     
     dropout (Dropout)           (None, 6272)              0         
                                                                     
     dense (Dense)               (None, 512)               3211776   
                                                                     
     dense_1 (Dense)             (None, 3)                 1539      
                                                                     
    =================================================================
    Total params: 3,473,475
    Trainable params: 3,473,475
    Non-trainable params: 0
    _________________________________________________________________
    Epoch 1/25
    20/20 [==============================] - 55s 3s/step - loss: 1.4447 - accuracy: 0.3437 - val_loss: 1.0930 - val_accuracy: 0.3333
    Epoch 2/25
    20/20 [==============================] - 54s 3s/step - loss: 1.0989 - accuracy: 0.3964 - val_loss: 1.0913 - val_accuracy: 0.4005
    Epoch 3/25
    20/20 [==============================] - 52s 3s/step - loss: 1.1337 - accuracy: 0.4675 - val_loss: 0.9567 - val_accuracy: 0.5108
    Epoch 4/25
    20/20 [==============================] - 52s 3s/step - loss: 0.9545 - accuracy: 0.5607 - val_loss: 0.5878 - val_accuracy: 0.8548
    Epoch 5/25
    20/20 [==============================] - 55s 3s/step - loss: 0.7951 - accuracy: 0.6294 - val_loss: 0.7550 - val_accuracy: 0.5081
    Epoch 6/25
    20/20 [==============================] - 52s 3s/step - loss: 0.6672 - accuracy: 0.7091 - val_loss: 0.4019 - val_accuracy: 0.9677
    Epoch 7/25
    20/20 [==============================] - 53s 3s/step - loss: 0.5418 - accuracy: 0.7619 - val_loss: 0.1373 - val_accuracy: 1.0000
    Epoch 8/25
    20/20 [==============================] - 55s 3s/step - loss: 0.5243 - accuracy: 0.7984 - val_loss: 0.1697 - val_accuracy: 0.9570
    Epoch 9/25
    20/20 [==============================] - 55s 3s/step - loss: 0.4222 - accuracy: 0.8337 - val_loss: 0.0844 - val_accuracy: 0.9839
    Epoch 10/25
    20/20 [==============================] - 52s 3s/step - loss: 0.2684 - accuracy: 0.8917 - val_loss: 0.0780 - val_accuracy: 0.9758
    Epoch 11/25
    20/20 [==============================] - 52s 3s/step - loss: 0.3220 - accuracy: 0.8940 - val_loss: 0.2014 - val_accuracy: 0.8844
    Epoch 12/25
    20/20 [==============================] - 54s 3s/step - loss: 0.2884 - accuracy: 0.8869 - val_loss: 0.1960 - val_accuracy: 0.9328
    Epoch 13/25
    20/20 [==============================] - 53s 3s/step - loss: 0.2072 - accuracy: 0.9262 - val_loss: 0.0154 - val_accuracy: 1.0000
    Epoch 14/25
    20/20 [==============================] - 53s 3s/step - loss: 0.1697 - accuracy: 0.9361 - val_loss: 0.0382 - val_accuracy: 0.9946
    Epoch 15/25
    20/20 [==============================] - 54s 3s/step - loss: 0.1483 - accuracy: 0.9508 - val_loss: 0.0506 - val_accuracy: 1.0000
    Epoch 16/25
    20/20 [==============================] - 54s 3s/step - loss: 0.3356 - accuracy: 0.8988 - val_loss: 0.0424 - val_accuracy: 1.0000
    Epoch 17/25
    20/20 [==============================] - 52s 3s/step - loss: 0.1106 - accuracy: 0.9615 - val_loss: 0.0402 - val_accuracy: 1.0000
    Epoch 18/25
    20/20 [==============================] - 52s 3s/step - loss: 0.1199 - accuracy: 0.9623 - val_loss: 0.0100 - val_accuracy: 1.0000
    Epoch 19/25
    20/20 [==============================] - 53s 3s/step - loss: 0.1125 - accuracy: 0.9647 - val_loss: 0.0139 - val_accuracy: 1.0000
    Epoch 20/25
    20/20 [==============================] - 53s 3s/step - loss: 0.1183 - accuracy: 0.9556 - val_loss: 0.0159 - val_accuracy: 1.0000
    Epoch 21/25
    20/20 [==============================] - 53s 3s/step - loss: 0.0736 - accuracy: 0.9762 - val_loss: 0.0281 - val_accuracy: 1.0000
    Epoch 22/25
    20/20 [==============================] - 52s 3s/step - loss: 0.1367 - accuracy: 0.9484 - val_loss: 0.0712 - val_accuracy: 0.9731
    Epoch 23/25
    20/20 [==============================] - 51s 3s/step - loss: 0.0612 - accuracy: 0.9782 - val_loss: 0.0253 - val_accuracy: 0.9839
    Epoch 24/25
    20/20 [==============================] - 52s 3s/step - loss: 0.1083 - accuracy: 0.9619 - val_loss: 0.0430 - val_accuracy: 0.9785
    Epoch 25/25
    20/20 [==============================] - 52s 3s/step - loss: 0.1070 - accuracy: 0.9603 - val_loss: 0.0153 - val_accuracy: 1.0000
    

training_datagen是一个ImageDataGenerator对象，用于对训练数据进行数据增强操作。数据增强操作包括图像缩放、旋转、平移、剪切、缩放和水平翻转等。  

validation_datagen是一个ImageDataGenerator对象，用于对验证数据进行预处理操作。  

target_size参数设置了图像的目标大小为150x150像素，class_mode参数设置为"categorical"，表示进行多类别分类任务，batch_size参数设置为126，即每次生成126个样本。  

定义了一个卷积神经网络模型。使用了tf.keras.models.Sequential来创建一个顺序模型model，模型由多个层按顺序堆叠而成。  

1.第一个层是一个Conv2D卷积层，有64个过滤器，每个过滤器大小为3x3，使用ReLU激活函数，并指定输入形状为150x150的RGB图像（3表示颜色通道）。   
2.接着是一个MaxPooling2D池化层，池化窗口大小为2x2。   
3.然后又两个类似的卷积和池化层。   
4.接下来是一个Flatten层，用于将卷积层的输出扁平化为一维向量，以便输入到后续的全连接层。   
5.在Flatten层后面添加了一个Dropout层，用于随机丢弃部分神经元，以减少过拟合。   
6.接着是一个包含512个神经元的全连接层，使用ReLU激活函数。   
7.最后是一个包含3个神经元的全连接层，使用Softmax激活函数，用于进行三类别的分类预测。   

ImageDataGenerator是Keras中图像预处理的类，经过预处理使得后续的训练更加准确。

Sequential定义了序列化的神经网络，封装了神经网络的结构，有一组输入和一组输出。可以定义多个神经层，各层之间按照先后顺序堆叠，前一层的输出就是后一层的输入，通过多个层的堆叠，构建出神经网络。

神经网络两个常用的操作：卷积和池化。由于图片中可能包含干扰或者弱信息，使用卷积处理（此处的Conv2D函数）使得我们能够找到特定的局部图像特征（如边缘）。此处使用了3X3的滤波器（通常称为垂直索伯滤波器）。而池化（此处的MaxPooling2D）的作用是降低采样，因为卷积层输出中包含很多冗余信息。池化通过减小输入的大小降低输出值的数量。详细的信息可以参考知乎回答“如何理解卷积神经网络（CNN）中的卷积和池化？”。更多的卷积算法参考Github Convolution arithmetic。

Dense的操作即全连接层操作，本质就是由一个特征空间线性变换到另一个特征空间。Dense层的目的是将前面提取的特征，在dense经过非线性变化，提取这些特征之间的关联，最后映射到输出空间上。Dense这里作为输出层。

完成模型训练之后，我们绘制训练和验证结果的相关信息。



```python
import matplotlib.pyplot as plt
acc = history.history['accuracy']
val_acc = history.history['val_accuracy']
loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(len(acc))

plt.plot(epochs, acc, 'r', label='Training accuracy')
plt.plot(epochs, val_acc, 'b', label='Validation accuracy')
plt.title('Training and validation accuracy')
plt.legend(loc=0)
plt.figure()
plt.show()

```


    
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/output_9_0.png)    
    



    <Figure size 640x480 with 0 Axes>


利用生成了模型，我们可以运行实际中的例子，例如上传石头剪头布的图片进行推测，使用model.predict。这里不做展开，后续我们利用Tensorflow Lite进行Android APP开发时，可以很自然的利用手机自带的摄像头或者图片库进行图片输入。
