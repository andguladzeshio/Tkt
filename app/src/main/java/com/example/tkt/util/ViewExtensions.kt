package com.example.tkt.util

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.core.view.doOnLayout
import com.example.tkt.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

fun ImageView.setQR(content: String, secondaryColorRes: Int = R.color.background_secondary) = doOnLayout {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height)
    val width = bitMatrix.width
    val height = bitMatrix.height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    val secondaryColor = context.getColor(secondaryColorRes)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else secondaryColor)
        }
    }
    setImageBitmap(bitmap)
}

