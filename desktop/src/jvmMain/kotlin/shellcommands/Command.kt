package shellcommands

@JvmInline
value class Command(
    val commands: List<String>
) {
    fun getFullCommand(): String = commands.joinToString(" ")
}
