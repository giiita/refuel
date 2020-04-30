package refuel.runtime

import refuel.Config

private[runtime] class PackagePathEntries(val moduleSymbolPath: Set[String], val classSymbolPath: Set[String]) {
  def union(that: PackagePathEntries): PackagePathEntries = {
    new PackagePathEntries(
      moduleSymbolPath ++ that.moduleSymbolPath,
      classSymbolPath ++ that.classSymbolPath
    )
  }

  def rounding(v: String): PackagePathEntries = {
    new PackagePathEntries(
      moduleSymbolPath.map(_.diff(v).slashToDot),
      classSymbolPath.map(_.diff(v).slashToDot)
    )
  }

  def doFinalize: PackagePathEntries = new PackagePathEntries(
    moduleSymbolPath.map(_.split("[\\$]?\\.class").head.replaceAll("\\$", ".")).collect {
      case r if !PackagePathEntries.unloadPackages.exists(r.startsWith) => r
    },
    classSymbolPath.map(_.split("[\\$]?\\.class").head.replaceAll("\\$", ".")).collect {
      case r if !PackagePathEntries.unloadPackages.exists(r.startsWith) => r
    }
  )
}

private[runtime] object PackagePathEntries {

  lazy val unloadPackages: Seq[String] = Config.blackList.map(_.value)

  def empty = new PackagePathEntries(Set.empty, Set.empty)
}
