/**
  * Created by beyyes on 17/2/19.
  */

// scala object与class的区别是什么？
// 只有object才能运行main函数.

object Gao {



  // 函数定义
  def test(p : Int) : Int = p + 1 ;
  // 函数定义 带{}形式
  def complexCalc(i : Int) : Int = {
      println("complex")
      return i * 2
  }

  // 传递匿名函数，将其保存为不变量
  // 带=>的皆为匿名函数?
  val addOne = (i: Int) => i + 1;

  //部分应用_，一个没有全名的神奇通配符
  def add1(m : Int , n : Int, p : Int) : Int = {
      m + n + p
  }
  val add2 = add1(_ : Int, 2, _ : Int)
  // println(add2(5, 9))  // ， 上面这个_的用法大概懂了

  // what is 柯里化?  接收一个参数，返回一个参数，便于讨论?
  def multiply(m: Int)(n: Int): Int = m * n
  multiply(2)(3)
  val timesTwo = multiply(2) _
  add2.curried
  // println(add2(3, 6))

  // 可变长参数，类比java; args: String*

  // scala main函数
  def main(args : Array[String]) = {
      // println(add2(5));

      // val addOne = (i : Int) : Int => i + 2;

      // 测试类Calculator
      val calc = new Calculator
      print(calc.add(8, 9))
  }
}
