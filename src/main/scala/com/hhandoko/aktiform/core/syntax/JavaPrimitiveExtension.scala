package com.hhandoko.aktiform.core.syntax

object JavaPrimitiveExtension {

  /** Extension methods to convert `Option[Int]` to nullable `Integer`.
    *
    * Standard library's `orNull` will not compile, as the compiler would not
    * be able to infer the type properly (Null <:< Int).
    *
    * @param opt Int if has value.
    */
  implicit class OptionIntToJavaIntegerSyntax(opt: Option[Int]) {
    import java.lang.{Integer => JInt}
    def orIntNull: JInt =
      if (opt.isDefined) opt.get
      else null
  }
}
