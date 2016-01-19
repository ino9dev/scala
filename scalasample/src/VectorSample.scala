object VectorSample {

  def main(args:Array[String]):Unit = {
    var v1 = Vector(Vector(1,1,1),Vector(2,2,2),Vector(3,3,3))
    println(
        v1.map(x=>x.reduce((e1,e2)=>(e1+e2)))
    )
    println(List(1,2,3).map(e=>e*2))
    println(List.fill(5)("A"))
    println(List.fill(5)(1))
    println((1 to 100).foldRight(0)((e1,e2)=>e1+e2))

    //1,1,2,3,5,8,13,21
    println(fib(5))
    //println(fib(10))
  }
  
  //fib(1) == 1
  //fib(2) == 1
  //fib(n) = fib(n-1)+fib(n-2)
  
  //num = 5
  //fib(1)+fib(2)+fib(3)+fib(4)
  //fib(0) = 1
  //fib(1) = 1
  //fib(2) = fib(1)+fib(0)
  //fib(3) = fib(2)+fib(1)
  
  def fib(num:Int):Int = {
    //return (0 to num).toList.reduce((e1,e2)=>(e1+e2))
    var fibnacci = (0 to num-1).toList.map(nthfib(_))
    println(fibnacci)
    return fibnacci.sum[Int]
  }
  
  def nthfib(num:Int):Int = num match{
    case 0|1 => 1
    case _ => nthfib(num-1) + nthfib(num-2)
  }
}