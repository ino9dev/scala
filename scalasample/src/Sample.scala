import scala.actors.Actor
import scala.actors.Actor._

//objectを使うとシングルトンが出来る
class ClsSample {
  
  //関数定義１(Intを返却する関数f1)
  //戻り値は最後に記述した値を返却 or return句で指定
  def f1():Int = {0}
  //関数定義２（引数を複数取る関数）
  def f2(x:Int,y:Int):Int = {Math.sqrt(x^2+y^2).toInt} //norm
  //関数定義３（引数リストを複数取る関数）
  def f3(x:Int)(y:Int):Int = x+y

  //コンストラクタはここに書く
  //newされたとき実行される
  println("This Sample Object was instanced")
  val f4 = (x:Int,y:Int)=>{x+y}
  
  //curried && apply
  println("f4 has two args x:Int,y:Int. f4 is curried with first arg and curried method return f4(1)(_). f4(1).apply(1) return " +(f4.curried(1))(1))

  //applyするもよし省略するもよし
  println(s"apply は省略できる=>" + f4.curried(1).apply(1))
  println(s"apply は省略できる=>" + f4.curried(1)(1))
  
  //collectionとその演算たち
  //参照：http://qiita.com/f81@github/items/75c616a527cf5c039676
  var seq = Seq(1,2,3)
  println(s"Sequence（並び順が有りSequenced List）" + seq)
  println(s"Intersect（積集合）" + seq.intersect(Seq(2))) //(2)
  println(s"Union（和集合）" + seq.union(Seq(4))) //(1,2,3,4)
  println(s"Diff(差集合)" + seq.diff(Seq(1,2))) //(3)
  
  //高階関数あれこれ
  val list = (1 to 10).toList
  println(s"1..10を2倍したListを返す" + list.map(_*2))
  println(s"1..10をreduceする" + list.reduce((e1,e2)=>(e1+e2)))
  println(s"1..10をsumする" + list.sum)
  println(s"1..10をflattenする" + List(List(1,2,3),List(4,5,6),List(7,8,9),List(10)).flatten)
  println(s"1..10をflatMapする" + List(list,list).flatMap{x=>x :+ 11}) //List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
  println(s"1..10を2の倍数でfilterする" + list.filter(e=>(if(e%2!=0){true}else{false}))) //List(1,3,5,7,9)
  
  //畳み込み(1,2,3,4,5,6,7,8,9,10) => ((((0)+1)+2)+3)
  println(s"1..10をfoldRightする" + (1 to 3).toList.foldRight(0)((e1,e2)=>(e1+e2)))
  //副作用あり
  println(s"1..10をforeachでprintする" + (1 to 10).toList.foreach(print(_)))
  println(s"1..10をforeachでprintする" + (1 to 10).toList.foreach(print))
  
  //タプル
  println(s"タプル"+(1,2).getClass())

  
}
object Sample{

  //main関数の呼び出し
  def main(args:Array[String]) : Unit = {
    
    var concreatedSample = new ClsSample
    
    //可変変数
    var name = "Sample"
    
    //不変変数
    val staicname = "StaticName"
    
    //error
    //staticname = "OtherStaticName"
    
    //innner関数定義
    def sum(x:List[Int]):Int = {return 0}
    
    //lambda（無名関数）
    val lambdaf = (e:Int)=>(e*2)

    //多相関数
    //２種類ある
    //パラメトリック多相 ... 構造に作用し、なかの値の型を問わない
    //アドホック多相 ... 型クラスによる多相性、異なる型の間で共通したインターフェースでの異なる振る舞いを定義済みの型に対して拡張する
    //ref : http://eed3si9n.com/learning-scalaz/ja/polymorphism.html
    //ref : https://skami.iocikun.jp/computer/haskell/web_lecture/for_programmer/polymorphic.html
    //ref : http://chopl.in/post/2012/11/06/introduction-to-typeclass-with-scala/

    def parametric[K](list:List[K])=(list.head)
    println(s"Intの要素を持つListの一つ目を取り出す" + parametric(List(1,2,3))) //1
    println(s"Stringの要素を持つListの一つ目を取り出す" + parametric(List("A","B","C")))
    
    trait Add[A] { def add(e1:A,e2:A):A } //関数定義のみ
    class AdhocStringAdd extends Add[String]{ def add(a:String,b:String):String=(a+b)} //Add関数定義を継承したAdhocStringAdd
    def adhocadd[A: Add](a1:A,a2:A):A = implicitly[Add[A]].add(a1,a2)
    
    //def sum(xs : List[Int]):Int = xs.foldLeft(0){_+_}
    //def ap[A,B](fa: => F[A])(f: => F[A => B]): F[B]
    
    //列挙する
    var l = for(i <- 1 to 5) yield i
    println(s"yielded" + l)


    /*
    var cat = new CatSample
    println(cat.id[String]("this is id"))
    var a = List(1 to 10)
    println(a)
    
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
            case (i,j)=>println("Mew-!("+i +","+j +")");
            case (i)=>println("Mew-!" + i +"");
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

class DataBean[T](_name:T){
  var name = _name
  def getName():T ={this.name}
  def setName(name:T):Unit = this.name = name
}

object Monoid {
  def mappend(a: Int, b: Int): Int = a + b
  def mzero: Int = 0
}