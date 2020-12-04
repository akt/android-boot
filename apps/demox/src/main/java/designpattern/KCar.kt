package designpattern


data class KCar(
    val id: Int = 0,
    val brand: String = "BMW",
    val color: String = "black",
    val size: String,
    val modelNO: String,
    val price: Float
)

val car: KCar
    get() = KCar(1, "ccc", "", "", "", 1.2f)