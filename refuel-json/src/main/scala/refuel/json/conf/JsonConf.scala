package refuel.json.conf

import refuel.injector.RecoveredInject

class JsonConf(val unicodeSerialize: Boolean,
               val unicodeDeserialize: Boolean)

object DefaultJsonSetting extends JsonConf(false, false) with RecoveredInject[JsonConf]
