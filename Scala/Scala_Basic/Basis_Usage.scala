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
    // Option[Int]

    val line = "xx"
    val lineAnnotation = line.map(l => s" line $l").getOrElse("")

  }

  def haha() = print("haha")
}
