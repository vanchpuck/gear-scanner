package org.izolotov.crawler.v4

import org.scalatest.flatspec.AnyFlatSpec
import shapeless.HList

class ShapelessTest extends AnyFlatSpec{
  import scala.reflect.runtime.universe._
  import shapeless.HList
  import shapeless.Generic
  case class A(a: Int)
  case class B(b: Int)

  val gen = Generic[A]
  case class MyClass[H <: HList](hs: H)

  object MyClass {
    import shapeless.Generic
    def apply[P <: Product, L <: HList](p: P)(implicit gen: Generic.Aux[P, L]) =
      new MyClass[L](gen.to(p))
  }

  it should ".." in {
    val p: Product with Serializable = A(2)
//    gen.to(p)
    println(MyClass(A(1)).hs)
//    val s: String = Seq(A(1), B(2)).iterator.next().prproductPrefix
//    println(s)
//    MyClass(1, "Hello", 12.6)
  }

}
