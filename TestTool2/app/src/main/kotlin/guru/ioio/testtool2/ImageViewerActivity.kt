package guru.ioio.testtool2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_image_viewer.*

class ImageViewerActivity : Activity() {
    companion object {
        fun launch(context: Context, uri: Uri) {
            val intent = Intent(context, ImageViewerActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = uri
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)


        intent?.data?.let { it ->
            contentResolver.openFileDescriptor(it, "r")?.let { pfd ->
                BitmapFactory.decodeFileDescriptor(pfd.fileDescriptor)?.let {
                    image.setImageBitmap(it)
                }
            }

        }
    }
}