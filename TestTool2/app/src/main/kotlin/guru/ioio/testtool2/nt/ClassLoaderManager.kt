package guru.ioio.testtool2.nt

import android.app.Application
import android.util.Log

object ClassLoaderManager {
    @JvmStatic
    fun replaceSystemClassLoader(app: Application) {
        PatchClassLoaderUtils.patch(app)
    }

    @JvmStatic
    fun getClassLoader(old: ClassLoader) = CustomClassLoader(old)
}

class CustomClassLoader(old: ClassLoader) : ClassLoader(old) {

    override fun loadClass(name: String?): Class<*> {

        Log.i("CCL", "loadClass: $name")
        return super.loadClass(if (name == "android.view.View") "guru.ioio.testtool2.nt.CustomView" else name)
    }

}