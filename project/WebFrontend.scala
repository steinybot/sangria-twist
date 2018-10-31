import ApolloGraphQL.Keys._
import Dependencies._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt.{Def, _}
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._
import webscalajs.ScalaJSWeb

object WebFrontend extends AutoPlugin {

  trait Keys {

    val npmAssetDependencies = settingKey[Seq[Npm.Assets]]("NPM asset dependencies (assets that your program uses)")
  }

  object Keys extends Keys

  import Keys._

  val autoImport: Keys = Keys

  override val requires: Plugins = ScalaJSBundlerPlugin && ScalaJSPlugin && ScalaJSWeb

  val ignoreWartremoverOptions = Set(
    "-P:wartremover:traverser:org.wartremover.warts.AsInstanceOf",
    "-P:wartremover:traverser:org.wartremover.warts.NonUnitStatements",
    "-P:wartremover:traverser:org.wartremover.warts.Null"
  )

  val stylesheetFilter = "*.sass" || "*.scss" || "*.css"

  override val projectSettings: Seq[Def.Setting[_]] = Seq(
    resolvers ++= ScalaJS.resolvers,
    libraryDependencies ++= ScalaJS.dependencies.value,
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    scalacOptions := scalacOptions.value.filterNot(ignoreWartremoverOptions.contains),
    scalaJSUseMainModuleInitializer := true,
    addCompilerPlugin(macroParadise),
    useYarn := true,
    npmAssetDependencies := Seq(),
    graphQLApolloCLI := (Compile / npmInstall).value / "node_modules" / ".bin" / "apollo",
    webpackBundlingMode := BundlingMode.LibraryOnly(),
    webpack / version := Versions.webpack,
    webpackCliVersion := Versions.webpackCLI,
    startWebpackDevServer / version := Versions.webpackDevServer,
    webpackConfigFile in fastOptJS := Some(baseDirectory.value / "dev.webpack.config.js"),
    webpackConfigFile in fullOptJS := Some(baseDirectory.value / "prod.webpack.config.js"),
    webpackMonitoredDirectories += (Compile / resourceDirectory).value / "stylesheets",
    includeFilter in webpackMonitoredFiles := stylesheetFilter
  ) ++ inConfig(Compile)(bundlerCompileSettings)

  def bundlerCompileSettings: Seq[Def.Setting[_]] = Seq(
    npmDependencies ++= npm.commonJSModules,
    npmDependencies ++= npmAssetDependencies.value,
    npmDependencies ++= npm.apolloClientCommonJSModules,
    npmDevDependencies += npm.apollo,
    npmDevDependencies ++= npm.webpackPlugins
  )
}
