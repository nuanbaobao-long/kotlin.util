package com.rfid.handler.usehandler.view

import android.content.Context
import android.graphics.*
import com.alibaba.fastjson.util.TypeUtils.getClass
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.AccessControlContext

class GlideRoundTransform : BitmapTransformation {
    override fun getId(): String {
        return System.currentTimeMillis().toString()
    }

    override fun transform(pool: BitmapPool?, toTransform: Bitmap?, outWidth: Int, outHeight: Int): Bitmap {
        return roundCrop(pool, toTransform)!!
    }

    constructor(context: Context) : super(context)

    private fun roundCrop(pool: BitmapPool?, source: Bitmap?): Bitmap? {
        if (source == null) return null;

        var size = Math.min(source.getWidth(), source.getHeight())
        var x = (source.getWidth() - size) / 2
        var y = (source.getHeight() - size) / 2

        var squared = Bitmap.createBitmap(source, x, y, size, size)

        var result = pool?.get(size, size, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        var canvas = Canvas(result)
        var paint = Paint()
        paint.setShader(BitmapShader(squared, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR))
        paint.setAntiAlias(true)
        var r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        return result
    }
}