package shellcommands

@JvmInline
value class Command(val commands: List<String>) {
    fun getFullCommand(): String {
        return commands.joinToString(" ")
    }
}
