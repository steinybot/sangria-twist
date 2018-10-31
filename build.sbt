import play.sbt.routes.RoutesKeys

lazy val root = (project in file("."))
  .settings(name := "sangria-twist")
  .aggregate(
    ui,
    server
  )

lazy val server = (project in file("server"))
  .enablePlugins(
    PlayScala,
    WebBackend
  )
  .settings(
    libraryDependencies ++= Seq(
      sangria,
      sangriaPlayJson
    ),
    TwirlKeys.templateImports := Nil,
    RoutesKeys.routesImport := Seq("controllers.Assets.Asset"),
    graphqlSchemaSnippet := "models.StarWarsSchemaDefinition.StarWarsSchema"
  )

lazy val ui = (project in file("ui"))
  .enablePlugins(
    WebFrontend
  )
  .settings(
    graphQLTypesNamespace := "graphql"
  )
