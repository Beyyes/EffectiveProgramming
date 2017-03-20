package basis

/**
  * Created by beyyes on 17/3/13.
  */
private[basis] object Basis_Usage {
  def main(args: Array[String]): Unit = {

    // Switch Usage
    //    val i = 4
    //    i match {
    //      case 1 | 3 => print("haha")
    //      case _ => print("default")
    //    }


    // Option Usage
    // Scala Option(选项)类型用来表示一个值是可选的（有值或无值)。
    // Option[T] 是一个类型为 T 的可选值的容器： 如果值存在， Option[T] 就是一个 Some[T] ，如果不存在， Option[T] 就是对象 None 。
    val myMap: Map[String, String] = Map("key1" -> "value")
    val value1: Option[String] = myMap.get("key1")
    val value2: Option[String] = myMap.get("key2")
    println(value1) // Some("value1")
    println(value2) // None
    // 你可以使用 getOrElse() 方法来获取元组中存在的元素或者使用其默认的值，实例如下：
    val a:Option[Int] = Some(5)
    val b:Option[Int] = None
    println("a.getOrElse(0): " + a.getOrElse(0) )
    println("b.getOrElse(10): " + b.getOrElse(0) )
    val lineAnnotation = a.map(l => s" line $l").getOrElse("") //getOrElse使用默认值或存在值
    println(lineAnnotation)
    
    
    // Other Usage
  }

  def haha() = print("haha")
}
