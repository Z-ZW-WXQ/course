# 实验2.2
## 一.实验目的
构建CameraX应用
## 二.实验环境
操作系统：Windows 10;Andorid stdio
## 三.实验步骤和结果
### 1.创建项目
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/221.png)  
### 2.准备环境
#### 2.1添加 Gradle 依赖项
打开 CameraXApp.app 模块的 build.gradle 文件，并添加 CameraX 依赖项   
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/222.png)   
#### 2.2Codelab 使用 ViewBinding，在 android 代码块的末尾，紧跟在 buildTypes 之后，添加以下代码：   
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/223.png)   
出现提示时，点击 Sync Now，我们就可以在应用中使用 CameraX 了   
### 3.创建 Codelab 布局
#### 3.1打开位于 res/layout/activity_main.xml 的 activity_main 布局文件，并将其替换为以下代码。  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/224.png)   
#### 3.2使用以下代码更新 res/values/strings.xml 文件
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/225.png)   
### 4.设置 MainActivity.kt  
````
import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.example.cameraxapp.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.PermissionChecker
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale

typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {
   private lateinit var viewBinding: ActivityMainBinding

   private var imageCapture: ImageCapture? = null

   private var videoCapture: VideoCapture<Recorder>? = null
   private var recording: Recording? = null

   private lateinit var cameraExecutor: ExecutorService

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       viewBinding = ActivityMainBinding.inflate(layoutInflater)
       setContentView(viewBinding.root)

       // Request camera permissions
       if (allPermissionsGranted()) {
           startCamera()
       } else {
           ActivityCompat.requestPermissions(
               this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
       }

       // Set up the listeners for take photo and video capture buttons
       viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
       viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

       cameraExecutor = Executors.newSingleThreadExecutor()
   }

   private fun takePhoto() {}

   private fun captureVideo() {}

   private fun startCamera() {}

   private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
       ContextCompat.checkSelfPermission(
           baseContext, it) == PackageManager.PERMISSION_GRANTED
   }

   override fun onDestroy() {
       super.onDestroy()
       cameraExecutor.shutdown()
   }

   companion object {
       private const val TAG = "CameraXApp"
       private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
       private const val REQUEST_CODE_PERMISSIONS = 10
       private val REQUIRED_PERMISSIONS =
           mutableListOf (
                          Manifest.permission.CAMERA,
               Manifest.permission.RECORD_AUDIO
           ).apply {
               if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                   add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
               }
           }.toTypedArray()
   }

````
### 5.请求必要权限
#### 5.1打开 AndroidManifest.xml，然后将以下代码行添加到 application 标记之前：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/226.png)   
添加代码到 MainActivity.kt. 中，用于授予相机权限：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/227.png)   
这里需要添加：  
`@SuppressLint("MissingSuperCall")`
否则系统报错
### 6.实现 Preview 用例
#### 6.1在startCamera()添加  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/228.png)   
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/229.png)   
此时，可以看到CameraX app界面预览，如下：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2210.png)   
### 7.实现 ImageCapture 用例
#### 7.1添加代码到 takePhoto() 方法中：
````
private fun takePhoto() {
    // Get a stable reference of the modifiable image capture use case
    val imageCapture = imageCapture ?: return

    // Create time stamped name and MediaStore entry.
    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }

    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues)
        .build()

    // Set up image capture listener, which is triggered after photo has
    // been taken
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(this),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun
                    onImageSaved(output: ImageCapture.OutputFileResults){
                val msg = "Photo capture succeeded: ${output.savedUri}"
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)
            }
        }
    )
}
````
#### 7.2需要在startCamera()中添加imagaCapture的权限：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2211.png)   
#### 7.3重新运行应用，然后按 Take Photo。屏幕上应该会显示一个消息框，我们会在日志中看到一条消息：      
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2212.png)     
新拍摄的照片会保存到 MediaStore 中，我们可以使用任何 MediaStore 应用查看这些照片  
### 8.实现 ImageAnalysis 用例
#### 8.1将此分析器添加为 MainActivity.kt 中的内部类。     
分析器会记录图像的平均亮度，如需创建分析器，我们会替换实现 ImageAnalysis.Analyzer 接口的类中的 analyze 函数。      
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2213.png)    
#### 8.2在 startCamera() 方法中，将此代码添加到 imageCapture 代码下。  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2214.png)    
#### 8.3提供相机权限，更新 cameraProvider 上的 bindToLifecycle() 调用，以包含 imageAnalyzer。  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2215.png)   
#### 8.4立即运行应用！它会大约每秒在 logcat 中生成一个类似于下面的消息。  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2216.png)   
### 9.实现 VideoCapture 用例
#### 9.1添加 captureVideo() 方法：该方法可以控制 VideoCapture 用例的启动和停止   
````
/ Implements VideoCapture use case, including start and stop capturing.
private fun captureVideo() {
    val videoCapture = this.videoCapture ?: return

    viewBinding.videoCaptureButton.isEnabled = false

    val curRecording = recording
    if (curRecording != null) {
        // Stop the current recording session.
        curRecording.stop()
        recording = null
        return
    }

    // create and start a new recording session
    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
        }
    }

    val mediaStoreOutputOptions = MediaStoreOutputOptions
        .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        .setContentValues(contentValues)
        .build()
    recording = videoCapture.output
        .prepareRecording(this, mediaStoreOutputOptions)
        .apply {
            if (PermissionChecker.checkSelfPermission(this@MainActivity,
                    Manifest.permission.RECORD_AUDIO) ==
                PermissionChecker.PERMISSION_GRANTED)
            {
                withAudioEnabled()
            }
        }
        .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
            when(recordEvent) {
                is VideoRecordEvent.Start -> {
                    viewBinding.videoCaptureButton.apply {
                        text = getString(R.string.stop_capture)
                        isEnabled = true
                    }
                }
                is VideoRecordEvent.Finalize -> {
                    if (!recordEvent.hasError()) {
                        val msg = "Video capture succeeded: " +
                                "${recordEvent.outputResults.outputUri}"
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT)
                            .show()
                        Log.d(TAG, msg)
                    } else {
                        recording?.close()
                        recording = null
                        Log.e(TAG, "Video capture ends with error: " +
                                "${recordEvent.error}")
                    }
                    viewBinding.videoCaptureButton.apply {
                        text = getString(R.string.start_capture)
                        isEnabled = true
                    }
                }
            }
        }
}
````
#### 9.2在 startCamera() 中，将以下代码放置在 preview 创建行之后。这将创建 VideoCapture 用例。  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2217.png)   
#### 9.3（可选）同样在 startCamera() 中，通过删除或注释掉以下代码来停用 imageCapture 和 imageAnalyzer 用例：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2218.png)  
#### 9.4将 Preview + VideoCapture 用例绑定到生命周期相机。仍在 startCamera() 内，将 cameraProvider.bindToLifecycle() 调用替换为以下代码：  
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2219.png)   
#### 9.5运行并录制一些剪辑：
•	按“START CAPTURE”按钮。请注意，图片说明会变为“STOP CAPTURE”。
•	录制几秒钟或几分钟的视频。
•	按“STOP CAPTURE”按钮（和 start capture 按钮是同一个按钮）。
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2220.png)   
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2221.png)   
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2222.png)  
### 10.（可选）将 VideoCapture 与其他用例结合使用
前面的 VideoCapture 步骤演示了 Preview 和 VideoCapture 的组合，所有设备都支持此组合（如设备功能表中所述）。  
在这一步中，我们会将 ImageCapture 用例添加到现有的 VideoCapture + Preview 组合中，以演示 Preview + ImageCapture + VideoCapture。  
对于我的版本来说：
![image](https://github.com/Z-ZW-WXQ/course/blob/master/img/2223.png)    
支持Preview + VideoCapture + ImageCapture和Preview + ImageCapture+ ImageAnalysis
