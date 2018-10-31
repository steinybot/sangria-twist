import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

trait Dependencies {

  object Versions {

    val bootstrapFacade = "2.3.3"
    val jqueryFacade = "1.2"
    val macroParadise = "3.0.0-M11"
    val reactApolloScalaJS = "0.4.3"
    val sangria = "1.4.2"
    val sangriaPlayJson = "1.0.5"
    val scala = "2.12.7"
    val slinky = "0.5.0"
    val webpack = "4.20.2"
    val webpackCLI = "3.1.2"
    val webpackDevServer = "3.1.8"
  }

  object ScalaJS {
    
    val resolvers = Seq(
      "Apollo Bintray" at "https://dl.bintray.com/apollographql/maven/" 
    )

    val bootstrapFacade = Def.setting("com.github.karasiq" %%% "scalajs-bootstrap" % Versions.bootstrapFacade)
    val jqueryFacade = Def.setting("org.querki" %%% "jquery-facade" % Versions.jqueryFacade)
    val reactApolloScalaJS = Def.setting("com.apollographql" %%% "apollo-scalajs-react" % Versions.reactApolloScalaJS)
    val slinkyHot = Def.setting("me.shadaj" %%% "slinky-hot" % Versions.slinky)
    val slinkyWeb = Def.setting("me.shadaj" %%% "slinky-web" % Versions.slinky)

    val dependencies = Def.setting {
      Seq(
        bootstrapFacade.value,
        jqueryFacade.value,
        reactApolloScalaJS.value,
        slinkyHot.value,
        slinkyWeb.value
      )
    }
  }

  val macroParadise = "org.scalameta" % "paradise" % Versions.macroParadise cross CrossVersion.full
  val sangria = "org.sangria-graphql" %% "sangria" % Versions.sangria
  val sangriaPlayJson = "org.sangria-graphql" %% "sangria-play-json" % Versions.sangriaPlayJson

  object Npm {

    object Versions {
      val aceCodeEditor = "1.2.3"
      val apollo = "1.9.2"
      val apolloBoost = "0.1.16"
      val apolloClient = "2.4.2"
      val autoprefixer = "9.1.5"
      val bootstrap = "3.3.7"
      val cssLoader = "1.0.0"
      val colorguard = "1.2.1"
      val doiuse = "4.2.0"
      val fileLoader = "2.0.0"
      val graphQL = "0.13.2"
      val graphQLTag = "2.9.2"
      val jquery = "3.2.1"
      val miniCSSExtractPlugin = "0.4.4"
      val nodeSass = "4.9.3"
      val optimizeCSSAssetsWebpackPlugin = "5.0.1"
      val postCSSLoader = "3.0.0"
      val postCSSSorting = "4.0.0"
      val react = "16.5.2"
      val reactApollo = "2.1.11"
      val reactProxy = "1.1.8"
      val sassLoader = "7.1.0"
      val stylelint = "9.6.0"
      val stylelintConfigStandard = "18.2.0"
      val uglifyJSWebpackPlugin = "2.0.1"
      val webpackMerge = "4.1.4"
    }

    sealed trait NpmDependency {
      def name: String
      def version: String
    }

    object NpmDependency {
      implicit def toTuple(npmDependency: NpmDependency): (String, String) = npmDependency.name -> npmDependency.version
    }

    case class CommonJSModule(name: String, version: String) extends NpmDependency
    case class Assets(name: String, version: String, assets: File => PathFinder = _.allPaths) extends NpmDependency

    val aceCodeEditor = CommonJSModule("ace-code-editor", Versions.aceCodeEditor)
    val apollo = CommonJSModule("apollo", Versions.apollo)
    val apolloBoost = CommonJSModule("apollo-boost", Versions.apolloBoost)
    val apolloClient = CommonJSModule("apollo-client", Versions.apolloClient)
    val autoprefixer = CommonJSModule("autoprefixer", Versions.autoprefixer)
    val boostrap = CommonJSModule("bootstrap", Versions.bootstrap)
    val colorguard = CommonJSModule("colorguard", Versions.colorguard)
    val cssLoader = CommonJSModule("css-loader", Versions.cssLoader)
    val doiuse = CommonJSModule("doiuse", Versions.doiuse)
    val fileLoader = CommonJSModule("file-loader", Versions.fileLoader)
    val graphQL = CommonJSModule("graphql", Versions.graphQL)
    val graphQLTag = CommonJSModule("graphql-tag", Versions.graphQLTag)
    val jquery = CommonJSModule("jquery", Versions.jquery)
    val miniCSSExtractPlugin = CommonJSModule("mini-css-extract-plugin", Versions.miniCSSExtractPlugin)
    val nodeSass = CommonJSModule("node-sass", Versions.nodeSass)
    val optimizeCSSAssetsWebpackPlugin = CommonJSModule("optimize-css-assets-webpack-plugin", Versions.optimizeCSSAssetsWebpackPlugin)
    val postCSSLoader = CommonJSModule("postcss-loader", Versions.postCSSLoader)
    val postCSSSorting = CommonJSModule("postcss-sorting", Versions.postCSSSorting)
    val react = CommonJSModule("react", Versions.react)
    val reactApollo = CommonJSModule("react-apollo", Versions.reactApollo)
    val reactDom = CommonJSModule("react-dom", Versions.react)
    val reactProxy = CommonJSModule("react-proxy", Versions.reactProxy)
    val sassLoader = CommonJSModule("sass-loader", Versions.sassLoader)
    val stylelint = CommonJSModule("stylelint", Versions.stylelint)
    val stylelintConfigStandard = CommonJSModule("stylelint-config-standard", Versions.stylelintConfigStandard)
    val uglifyJSWebpackPlugin = CommonJSModule("uglifyjs-webpack-plugin", Versions.uglifyJSWebpackPlugin)
    val webpackMerge = CommonJSModule("webpack-merge", Versions.webpackMerge)

    val apolloClientCommonJSModules = Seq(
      apolloBoost,
      apolloClient,
      reactApollo,
      graphQL,
      graphQLTag
    )

    val commonJSModules = Seq(
      aceCodeEditor,
      boostrap,
      jquery,
      react,
      reactDom,
      reactProxy
    )

    val webpackPlugins = Seq(
      autoprefixer,
      colorguard,
      cssLoader,
      doiuse,
      fileLoader,
      miniCSSExtractPlugin,
      nodeSass,
      optimizeCSSAssetsWebpackPlugin,
      postCSSLoader,
      postCSSSorting,
      sassLoader,
      stylelint,
      stylelintConfigStandard,
      uglifyJSWebpackPlugin,
      webpackMerge
    )
  }

  val npm: Npm.type = Npm
}

object Dependencies extends Dependencies
