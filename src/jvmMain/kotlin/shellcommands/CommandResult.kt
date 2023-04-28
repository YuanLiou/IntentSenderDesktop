package shellcommands

data class CommandResult(val exitCode: Int, val output: String, val errorMessage: String)
