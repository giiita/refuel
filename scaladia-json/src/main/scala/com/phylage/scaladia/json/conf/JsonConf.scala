package com.phylage.scaladia.json.conf

import com.phylage.scaladia.injector.RecoveredInject

class JsonConf(val unicodeSerialize: Boolean,
               val unicodeDeserialize: Boolean)

object DefaultJsonSetting extends JsonConf(false, false) with RecoveredInject[JsonConf]
