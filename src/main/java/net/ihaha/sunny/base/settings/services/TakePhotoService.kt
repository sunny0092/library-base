package net.ihaha.sunny.base.settings.services

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import net.ihaha.sunny.base.extention.getCameraPermission
import net.ihaha.sunny.base.extention.getStoragePermission
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.*


/**
 * Date: 17/03/2021.
 * @author SANG.
 * @version 1.0.0.
 */
@ExperimentalCoroutinesApi
@FlowPreview
class TakePhotoService constructor(val fragment: Fragment, val activity: Activity) {

    companion object {
        const val OPERATION_CAPTURE_PHOTO = 111
        const val OPERATION_CHOOSE_PHOTO = 222
    }


    fun openGallery() {
        fragment.getStoragePermission(
            onLocationGranted = {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                fragment.startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
            },
            onDenied = {
                Toast.makeText(activity, "Please enable storage permission", Toast.LENGTH_LONG)
                    .show()
            })
    }

    fun capturePhoto(provider: String) {
        try {
            fragment.getCameraPermission(
                onGranted = {
                    val capturedImage = File(
                        activity.applicationContext.externalCacheDir,
                        "${UUID.randomUUID()}.jpg"
                    )
                    capturedImage.createNewFile()
                    val imageURI = if (Build.VERSION.SDK_INT >= 24) {
                        activity.applicationContext.let {
                            FileProvider.getUriForFile(it, provider, capturedImage)
                        }
                    } else {
                        Uri.fromFile(capturedImage)
                    }
                    val intent = Intent("android.media.action.IMAGE_CAPTURE")
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI)
                    fragment.startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
                },
                onDenied = {
                    Toast.makeText(activity, "Please enable storage permission", Toast.LENGTH_LONG).show()
                })
        } catch (e: Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        }
    }

    suspend fun resultForImageCapture(fileName: String?, imageURI: Uri?): MultipartBody.Part? {
        return getImageAsMultiPart(fileName, imageURI!!)
    }

    private suspend fun getImageAsMultiPart(
        fileName: String?,
        imageURI: Uri
    ): MultipartBody.Part? {
        val file: File?
        when {
            imageURI.toString().contains("https://") -> {
                file = Glide.with(activity).asFile().load(imageURI).submit().get()
            }
            imageURI.toString().contains("content://") -> {
                file = File(activity.cacheDir, "${UUID.randomUUID()}.jpg")
                val inputStream = activity.contentResolver.openInputStream(imageURI)
                FileUtils.copyToFile(inputStream, file)
            }
            else -> return null
        }
        if (file?.exists() == true) {
            val compressedImageFile = Compressor.compress(activity, file) {
                resolution(1280, 720)
                quality(80)
                format(Bitmap.CompressFormat.JPEG)
                size(1_000_000) // 2 MB
            }
            val requestFile: RequestBody =
                compressedImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData(
                fileName!!,
                compressedImageFile.name.trim(),
                requestFile
            )
        } else {
            return null
        }
    }
}