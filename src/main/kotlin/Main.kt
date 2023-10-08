fun main(args: Array<String>) {

    table {
        column("ID", Int::class)
        column("Name", String::class)
        column("Age", Int::class)

        row {
            cell("ID", 1)
            cell("Name", "Alice")
            cell("Age", 25)
        }
        row {
            cell("ID", 2)
            cell("Name", "Bob")
            cell("Age", 30)
        }
    }.render()
}



