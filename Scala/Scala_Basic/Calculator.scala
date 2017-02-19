/**
  * Created by beyyes on 17/2/19.
  */

// 构造函数只能写在类后面吗，如果有多个构造函数怎么办

class Calculator (brand: String) {
    /**
      * A constructor.
      */
    val color: String = if (brand == "TI") {
        "blue"
    } else if (brand == "HP") {
        "black"
    } else {
        "white"
    }

    // 默认只能有一个构造函数
    def this(x : String) {
        this(x + 1)
    }

    def add(m: Int, n: Int): Int = m + n
}

/**
  * 什么时候使用特质与抽象类
  * 特质类似于java的接口
  *
    优先使用特质。一个类扩展多个特质是很方便的，但却只能扩展一个抽象类。
    如果你需要构造函数参数，使用抽象类。因为抽象类可以定义带参数的构造函数，而特质不行。例如，你不能说trait t(i: Int) {}，参数i是非法的。
**/

trait Car {
    val brand: String
}

trait Shiny {
    val shineRefraction: Int
}

class BMW extends Car with Shiny {
    val brand = "BMW"
    val shineRefraction = 12
}

// 类型
trait Cache[K, V] {
    def get(key: K): V
    def put(key: K, value: V)
    def delete(key: K)
}