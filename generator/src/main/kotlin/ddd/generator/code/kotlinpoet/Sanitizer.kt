package app.ddd.generator.code.kotlinpoet

import java.io.*

class Sanitizer(private val out: Writer) : Appendable, Closeable {
    private val builder = StringBuilder()

    override fun append(csq: CharSequence): Appendable {
        csq.forEach { append(it) }
        return this
    }

    override fun append(csq: CharSequence, start: Int, end: Int) = append(csq.subSequence(start, end))

    override fun append(c: Char): Appendable {
        builder.append(c)
        if (c == '\n') {
            var line = builder.toString()
            if (!line.startsWith("import kotlin.")) {
                line = line
                    .replace("public ", "")
                    .replace(": Unit", "")
                out.write(line)
            }
            builder.clear()
        }
        return this
    }

    override fun close() = out.close()
}
