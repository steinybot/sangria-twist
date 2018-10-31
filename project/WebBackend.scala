import WebFrontend.Keys._
import com.typesafe.sbt.digest.Import._
import com.typesafe.sbt.gzip.Import._
import com.typesafe.sbt.web.Import._
import com.typesafe.sbt.web.PathMapping
import play.sbt.routes.RoutesKeys._
import rocks.muki.graphql.GraphQLSchemaPlugin
import sbt.Keys._
import sbt._
import sbt.internal.LoadedBuildUnit
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._
import scalajsbundler.sbtplugin.WebScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.WebScalaJSBundlerPlugin.autoImport._
import wartremover._
import webscalajs.WebScalaJS.autoImport._

object WebBackend extends AutoPlugin {

  trait Keys {

    // We shouldn't need this but scalaJSProjects has the wrong type
    val scalaJSProjectRefs = Def.settingKey[Seq[ProjectReference]]("Scala.js projects attached to the sbt-web project")
  }

  object Keys extends Keys

  import Keys._

  val autoImport: Keys = Keys

  override val requires: Plugins = WebScalaJSBundlerPlugin && GraphQLSchemaPlugin

  override val projectSettings: Seq[Def.Setting[_]] = Seq(
    Assets / pipelineStages ++= Seq(scalaJSPipeline),
    pipelineStages ++= Seq(digest, gzip),
    Compile / compile := (Compile / compile).dependsOn(scalaJSPipeline).value,
    wartremoverExcluded ++= (Compile / routes).value,
    wartremoverExcluded += sourceManaged.value / "sbt-sangria-codegen" / "SchemaGen.scala",
    scalaJSProjectRefs := frontendProjectsSetting.value,
    scalaJSProjects := scalaJSProjectsSetting.value,
    npmAssets ++= frontendNpmAssetsTask.value
  )

  private def frontendProjectsSetting = Def.setting {
    val units = loadedBuild.value.units
    val projects = allProjectRefs(units)
    for {
      projectRef <- projects
      project <- Project.getProject(projectRef, units).toList
      autoPlugin <- project.autoPlugins
      if autoPlugin == WebFrontend
    } yield {
      val _ = autoPlugin // Workaround for false-negative unused warning.
      projectRef
    }
  }

  private def scalaJSProjectsSetting = Def.setting {
    val units = loadedBuild.value.units
    val projects = scalaJSProjectRefs.value
    for {
      projectRef <- projects
      project <- projectForReference(projectRef, units).toList
    } yield Project(project.id, project.base)
  }

  private def projectForReference(ref: Reference, units: Map[URI, LoadedBuildUnit]): Option[ResolvedProject] = {
    ref match {
      case projectRef: ProjectRef => Project.getProject(projectRef, units)
      case _ => None
    }
  }

  private def allProjectRefs(units: Map[URI, LoadedBuildUnit]): Seq[ProjectRef] = {
    units.toSeq.flatMap {
      case (build, unit) => refs(build, unit.defined.values.toSeq)
    }
  }

  private def refs(build: URI, projects: Seq[ResolvedProject]): Seq[ProjectRef] = {
    projects.map { p =>
      ProjectRef(build, p.id)
    }
  }

  private def frontendNpmAssetsTask: Def.Initialize[Task[Seq[PathMapping]]] = {
    frontendProjectAssetsTask.flatMap { tasks =>
      tasks.fold(task(Nil)) { (previous, next) =>
        for {
          p <- previous
          n <- next
        } yield p ++ n
      }
    }
  }

  private def frontendProjectAssetsTask: Def.Initialize[Task[Seq[Task[Seq[PathMapping]]]]] = Def.task {
    val stateTask = state.taskValue
    val projectRefs = scalaJSProjectRefs.value
    projectRefs.map { project =>
      projectAssets(stateTask, Scope.ThisScope.in(project))
    }
  }

  private def projectAssets(stateTask: Task[State], scope: Scope) = {
    for {
      state <- stateTask
      extracted = Project.extract(state)
      assetDependencies <- task(extracted.get(scope / npmAssetDependencies))
      nodeInstallDir <- extracted.get(scope.in(Compile) / npmUpdate)
    } yield {
      val nodeModulesDir = nodeInstallDir / "node_modules"
      val assets = assetDependencies.foldLeft(PathFinder.empty) {
        case (pathFinder, asset) => pathFinder +++ asset.assets(nodeModulesDir / asset.name)
      }
      assets.pair(Path.relativeTo(nodeModulesDir))
    }
  }
}
