package refuel.domination

case class PriorityCtx[T](priority: InjectionPriority, f: InjectionPriority => T)
