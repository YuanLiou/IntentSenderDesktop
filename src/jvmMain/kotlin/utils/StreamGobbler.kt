package utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class StreamGobbler(
    private val inputStream: InputStream,
    private val messages: (String) -> Unit
) : Runnable {

    override fun run() {
        BufferedReader(InputStreamReader(inputStream)).lines()
            .forEach {
                messages(it)
            }
    }
}
