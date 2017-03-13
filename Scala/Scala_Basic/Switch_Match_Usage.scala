// match用法，比java switch简洁的多

object Switch_Usage {
  def main(args: Array[String]): Unit = {
    val i = 4;
    i match {
      case 1 | 3 => print("haha")
      case _ => print("default")
    }
  }
}
