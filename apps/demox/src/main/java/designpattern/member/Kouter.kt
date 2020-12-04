package designpattern.member

class KOuter {

    class KNestedStatic(){
        fun foo() = 2
    }

    inner class KNestedNonStatic(){
        fun foo() = 2
    }

}