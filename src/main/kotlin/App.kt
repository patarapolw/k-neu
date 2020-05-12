package k_neu

import com.google.gson.Gson
import org.eclipse.swt.SWT
import org.eclipse.swt.browser.Browser
import org.eclipse.swt.browser.TitleListener
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Paths
import kotlin.concurrent.thread


data class Config (
    val title: String,
    var url: String,
    val server: List<String>?,
    val maximized: Boolean?
)

fun main () {
    var rootDirFile = File(Config::class.java.protectionDomain.codeSource.location.toURI())
    var configFile = Paths.get(rootDirFile.toString(), "config.json").toFile()
    if (!configFile.exists()) {
        configFile = File(Config::class.java.classLoader.getResource("config.json")?.toURI()
            ?: throw Error("config.json is not found."))
        rootDirFile = configFile.parentFile
    }

    val gson = Gson()
    val config = gson.fromJson(configFile.readText(), Config::class.java)

    val display = Display()
    val shell = Shell(display)
    shell.maximized = config.maximized ?: false
    shell.text = config.title

    val layout = FillLayout()

    val browser = Browser(shell, SWT.FILL)
    browser.addTitleListener { TitleListener {
        shell.text = it.title
    } }

    shell.layout = layout
    shell.open()

    if (config.server != null) {
        val pb = ProcessBuilder(config.server)
        pb.directory(rootDirFile)

        val serverProcess = pb.start()
        shell.addDisposeListener {
            serverProcess.destroy()
        }
    }

    config.url = config.url.replace(Regex("\\$\\{([A-Za-z0-9]+)}")) {
        System.getenv(it.groups[1]?.value ?: "") ?: ""
    }

    val url = try {
        URL(config.url)
    } catch (e: MalformedURLException) {
        Paths.get(rootDirFile.toString(), config.url).toUri().toURL()
    }

    thread(isDaemon = true) {
        while (!checkUrl(url)) {
            Thread.sleep(1000)
        }
        Display.getDefault().asyncExec {
            browser.url = url.toString()
        }
    }

    while (!shell.isDisposed) {
        if (!display.readAndDispatch()) {
            display.sleep();
        }
    }
}

fun checkUrl (url: URL): Boolean {
    var connection: HttpURLConnection? = null
    try {
        connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        return listOf(200, 301, 404).contains(connection.responseCode)
    } catch (e: IOException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    } finally {
        connection?.disconnect()
    }

    return false
}