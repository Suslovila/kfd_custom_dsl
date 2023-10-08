import java.lang.Exception
import kotlin.math.max
import kotlin.reflect.KClass

object table {
    operator fun invoke(init: TableHandler.() -> Unit) : TableHandler {
        val tableHandler = TableHandler()
        tableHandler.init()
        return tableHandler
    }

}

class TableHandler {

    val mainColumns = mutableListOf<Column<Any>>()
    val rows = HashSet<Row>()

    fun <T : Any> column(name: String, cl: KClass<T>) {
        if(mainColumns.any {it.name == name}) throw Exception("Column with name $name already exists!")
        mainColumns += Column(name, name.length, cl.javaObjectType)
    }

    fun row(init: Row.() -> Unit) {
        rows.add(Row(this).also(init))
    }

    fun render() {

        fun printObj(value : Any, cellSize : Int) {
            if(value != Unit) with(value.toString()) { print("| " + this + " ".repeat((cellSize + 1 - this.length))) }
            else print("| " + " ".repeat(cellSize + 1))
        }

        mainColumns.forEach {
            printObj(it.name, it.length)
        }
            println(" |")

            rows.forEach{
                for(i in 0 until it.values.size){
                    printObj(it.values[i], mainColumns[i].length)
                }
                println(" |")
            }
    }
}
class Column <T : Any> (val name : String, var length : Int, val type : Class <out T>)

class Row(val table : TableHandler) {

    val values = MutableList<Any>(table.mainColumns.size){}

    fun cell(columnName : String, value : Any){
        val connectedColumn = table.mainColumns.singleOrNull{mainColumn -> mainColumn.name == columnName} ?: throw Exception("no such column with name $columnName!")
        if(!connectedColumn.type.isAssignableFrom(value.javaClass)) throw Exception("Wrong object type: attempt to add ${value.javaClass.name} into a column ${connectedColumn.name} which contains ${connectedColumn.type.name}")
        values[table.mainColumns.indexOf(connectedColumn)] = value
        connectedColumn.length = max(value.toString().length, connectedColumn.length)
    }
}