import scala.actors.Actor
import scala.actors.Actor._

class DataBean[T](_name:T){
  var name = _name
  def getName():T ={this.name}
  def setName(name:T):Unit = this.name = name
}

object Monoid {
  def mappend(a: Int, b: Int): Int = a + b
  def mzero: Int = 0
}

object Sample{
  var name = "Sample"
  
  def sum(xs : List[Int]):Int = xs.foldLeft(0){_+_}
  //def ap[A,B](fa: => F[A])(f: => F[A => B]): F[B]
  def main(args:Array[String]) : Unit = {
    
    println(List(1,2,3,4,5).sum)
    /*
    var variable = 0
    val variable_final = 0
    var variable_final2 = new DataBean("name")
    
    variable_final2.name = "hoge"
    variable = 1
    
    */
    //列挙する
    var l = for(i <- 1 to 5) yield i
    println(l)
    println((1 to 10).filter((i)=>i % 2 == 0))

    /*
    var cat = new CatSample
    println(cat.id[String]("this is id"))
    var a = List(1 to 10)
    println(a)
    
    //関数定義
    var f1 = (a:Int)=>a+1
    
    println(f1(2))

    //function
    //lambdax.xx (x)=>xx
    var b = (bb:String)=>bb
    println(b("a"))
    
    //本来の指定,多相関数
    def ids[HOGE](a:HOGE)=((a:HOGE)=>(a:HOGE))
    //略記
    def id[HOGE]: HOGE=> HOGE = a=>a
    
    def ff[HOGE]: (HOGE=>HOGE) = (a=>a)
    
    //def define
    //functionname
    //(arg:Type,arg:Type)
    //:ReturnType
    //=function
    def f2(x:Int):Int= x+1
    def f2_1(x:Int)=x+1
    def f2_2(x:Int,y:Int):Int = x+y
    //curry化の方法
    var f2_2_applied = f2_2(1,_:Int)
    
    //再帰的関数
    def fibo(n:Int): BigInt = if(n==0||n==1) 1 else fibo(n-1) + fibo(n-2)
    
    (1 to 30).foreach((i)=>print(fibo(i)+" "))
    (1 to 10).foreach(println)
    
    def p[X](s:X):X=s
    
    print(p("print"))
    
    /*
    println(f2_2_applied)
    println("f2_2_applied(2)=" + f2_2_applied(2))
    
    println("f2(2)=" + f2(2))
    println("f2_1(2)=" + f2_1(2))
    
    println(ff(1))
    
    println(id(1,2,3,4))
    */
    
    /*
    var i = 1
    println(f1(i))
    println(this.name)
    println(this.+("hoge"))
    var list = List(1,2,3,4)
    var list2= 1 to 10
    var list3= Range(1,10)
    println(list.reduce((e1,e2)=>e1+e2))
    println(list2.map(_+1))
    
    var cat = new Cat() with Mew { override def mew():String = {"mmmew!"}}
    println(cat.mew)
    var cat2 = new Cat() with Mew
    println(cat2.mew)
    */
    
    var actor = new CatActor()
    actor.start
    
    //[!]  sync messages
    //[?!] async messages
    
    //-> taple
    //→ equals tapple
    (1 to 2).foreach{
      i => actor ! (i,i*i)
    }
    */
    var mew = new Cat()
    println(mew.mewadd)
  }
  
  def f1(x:Integer)={
    x+1
  }
}

trait Mew { 
  def mew() : String = {
    "mew-."
  }
}

trait Bow {
  def bow() : String = {
    "bow -"
  }
}

class Cat extends Mew with Bow{
  var t = "nora"
  def mewadd():Unit = {
    println(this.t + ":「" + super.mew + "」" + "and" + " " + super.bow)
  }
}

class CatActor extends Actor {
    def act() = {
        loop {
          react {
            case (i,j)=>println("Hello CatActor" + i +","+j +"");
            case (i)=>println("Hello CatActor" + i +"");
          }
        }
    }
}

class CatSample {
  //idという関数
  //ジェネリクスで型指定
  //
  def id[HOGE]: HOGE=> HOGE = a => a
}

//圏の定義
class Category {
  //恒等射
  //Aという型を持つ、idという関数
  
  def id[A]: A => A = a => a
  def compose[A, B, C](g: B => C, f: A => B): A => C = g compose f // This is Function.compose, not a recursive call!
}

//class CategorySpec extends Specification with ScalaCheck {
//  import Category._
//
//  "A Category" should {
//
//    val f = (i: Int) => i.toString
//    val g = (s: String) => s.length
//    val h = (i: Int) => i * i
//
//    "satisfy associativity" in {
//      Prop forAll { (i: Int) =>
//        compose(h, compose(g, f))(i) == compose(compose(h, g), f)(i)
//      } must pass
//    }
//
//    "satisfy identity" in {
//      Prop forAll { (i: Int) =>
//        compose(f, id[Int])(i) mustEqual compose(id[String], f)(i)
//      } must pass
//    }
//  }
//}