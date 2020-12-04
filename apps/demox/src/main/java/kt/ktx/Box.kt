package kt.ktx

class Box<T>(t: T) {
    var value = t
}

interface Source<out T>{
    fun nextT():T
}

interface Comparable<in T>{
    operator fun compareTo(other:T):Int
}


fun demo(strs:List<String>){
    val objects:List<Any> = strs
}

fun <T> copy(from:Array<out T>, to:Array<T>){

}

fun <T> fill(dest:Array<in T>, item:T){

}

val box: Box<Int> = Box(1)