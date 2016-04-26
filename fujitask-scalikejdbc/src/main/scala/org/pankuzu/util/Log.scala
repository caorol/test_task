package org.pankuzu.util

/**
  * Created by caorol on 2016/04/07.
  */
object Log {
  def log(message: String = ""): Unit = {
    def getClasses(stackTrace: Array[StackTraceElement]): Seq[String] = {
      stackTrace foreach print
      println
      var classs: Seq[String] = Nil

      for (th <- stackTrace) {
        val className = th.getClassName
        val packageName = className.substring(0, className.lastIndexOf('.'))
        if (className != "org.pankuzu.util.Class$" &&
          packageName != "sun.reflect" &&
          packageName.indexOf("org.scalatest") == -1 &&
          packageName.indexOf("java.lang") == -1 &&
          packageName.indexOf("scala.") == -1) {
          classs = classs :+ th.getClassName()
        }
      }
      classs
    }

    def formatClassName(className: String): String = {
      var _className = className
      def changeName(targetName: String): Unit ={
        var length = targetName.length
        if (targetName == "class") {
          length += 1
        }
        if (_className.substring(_className.length - length, _className.length) == "$" + targetName) {
          _className = _className.substring(0, _className.length - length) + "[trait]"
        } else {
          if (_className.substring(_className.length - 1, _className.length) == "$") {
            _className += "[object]"
          } else {
            _className += "[class]"
          }
        }
      }
      def deleteAnon: Unit = {
        val pos = _className.indexOf("$$")
        if (pos > -1) {
          _className = _className.substring(0, pos)
        }
      }

      deleteAnon
      changeName("class")

      _className
    }

    val stackTrace = Thread.currentThread().getStackTrace()(2)
    val nowClassName = formatClassName(stackTrace.getClassName.substring(stackTrace.getClassName.lastIndexOf('.') + 1, stackTrace.getClassName.length))
    val nowMethodName = stackTrace.getMethodName
    val line = stackTrace.getLineNumber
    var msg = ""
    if (message != "") {
      msg = " : " + message
    }
    println(s"$nowClassName($line): $nowMethodName$msg")

  }

}
