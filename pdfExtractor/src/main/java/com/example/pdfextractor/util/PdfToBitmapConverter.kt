package com.example.pdfextractor.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object PdfToBitmapConverter {
    suspend fun convert(context: Context,uri: Uri):Result<List<Bitmap>> = withContext(Dispatchers.IO){
        try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri,"r")
                ?: return@withContext Result.failure(Exception("Failed to open file descriptor"))
            parcelFileDescriptor.use{ pdf->
                val pdfRender  = PdfRenderer(pdf)
                val pageCount = pdfRender.pageCount
                val bitmapList = mutableListOf<Bitmap>()

                for (pageIndex in 0 until pageCount){
                    val eachPage  = pdfRender.openPage(pageIndex)

                    val bitmap = Bitmap.createBitmap(eachPage.width,eachPage.height, Bitmap.Config.ARGB_8888)
                    eachPage.render(bitmap,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    bitmapList.add(bitmap)
                    eachPage.close()
                }
                pdfRender.close()
                Result.success(bitmapList)
            }
        }
        catch (e:IOException){
            Result.failure(e)
        }
        catch (e: SecurityException){
            Result.failure(e)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }
}