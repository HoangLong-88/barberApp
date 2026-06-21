package com.example.barberapp.Helps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        // 1. Đọc góc xoay từ thẻ Exif của ảnh
        var rotationDegrees = 0f
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val exif = ExifInterface(inputStream)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            rotationDegrees = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        }

        // 2. Decode lấy Bitmap gốc
        val originalBitmap = context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        } ?: return null

        // 3. Xoay Bitmap gốc cho đứng thẳng lại (nếu cần)
        val rotatedBitmap = if (rotationDegrees != 0f) {
            val matrix = Matrix()
            matrix.postRotate(rotationDegrees)
            Bitmap.createBitmap(
                originalBitmap, 0, 0,
                originalBitmap.width, originalBitmap.height,
                matrix, true
            )
        } else {
            originalBitmap
        }

        // 4. Thu nhỏ ảnh xuống 200px (Dùng rotatedBitmap thay vì originalBitmap)
        val maxSize = 200
        val width = rotatedBitmap.width
        val height = rotatedBitmap.height
        val finalBitmap = if (width > maxSize || height > maxSize) {
            val aspectRatio = width.toFloat() / height.toFloat()
            val newWidth = if (width > height) maxSize else (maxSize * aspectRatio).toInt()
            val newHeight = if (height > width) maxSize else (maxSize / aspectRatio).toInt()
            Bitmap.createScaledBitmap(rotatedBitmap, newWidth, newHeight, true)
        } else {
            rotatedBitmap
        }

        // 5. Nén và chuyển đổi sang Base64
        val outputStream = ByteArrayOutputStream()
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        val byteArray = outputStream.toByteArray()

        Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
fun decodeBase64ToBitmap(base64Str: String?): ImageBitmap? {
    if (base64Str.isNullOrEmpty() || base64Str.startsWith("http")) return null
    return try {
        val imageBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
    } catch (e: Exception) {
        null
    }
}