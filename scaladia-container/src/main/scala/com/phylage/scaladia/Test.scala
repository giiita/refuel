package com.phylage.scaladia

import com.phylage.scaladia.injector.AutoInject

trait IF

class Test extends IF with AutoInject[IF] {

}
