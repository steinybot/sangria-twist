import sbt.Keys._
import sbt.{Def, _}

import scala.sys.process._

object ApolloGraphQL extends AutoPlugin {

  final case class CodegenFailed(exitCode: Int) extends Exception(
    s"Non-zero exit code ($exitCode) returned when generating GraphQL types."
  )

  trait Keys {

    val graphQLApolloCLI = Def.taskKey[File]("Path to the apollo CLI")
    val graphQLSources = Def.taskKey[Seq[File]]("Source files containing the GraphQL queries")
    val graphQLSchema = Def.settingKey[File]("Path to the GraphQL schema file")
    val graphQLTypesNamespace = Def.settingKey[String]("Namespace for the generated GraphQL types")
    val graphQLGenerateTypes = Def.taskKey[Seq[File]]("Generates the static types for your GraphQL queries")
  }

  object Keys extends Keys

  import Keys._

  val autoImport: Keys = Keys

  override val projectSettings: Seq[Def.Setting[_]] = {
    Seq(resolvers += "Apollo Bintray" at "https://dl.bintray.com/apollographql/maven/") ++
      inConfig(Compile)(graphQLSettings)
  }

  private def graphQLSettings = Seq(
    graphQLTypesNamespace := name.value,
    sourceDirectory in graphQLGenerateTypes := sourceDirectory.value / "graphql",
    sourceManaged in graphQLGenerateTypes := sourceManaged.value / "graphql",
    graphQLSources := graphQLSourcesTask.value,
    graphQLSchema := baseDirectory.value / "schema.json",
    graphQLGenerateTypes := generateGraphQLTypesTask.value,
    sourceGenerators += graphQLGenerateTypes
  )

  private def graphQLSourcesTask = Def.task {
    val sourceDir = (sourceManaged in graphQLGenerateTypes).value
    (sourceDir ** "*.graphql").get
  }

  private def generateGraphQLTypesTask = Def.task {
    val apollo = graphQLApolloCLI.value.getPath
    val sourceDir = (sourceDirectory in graphQLGenerateTypes).value.getAbsolutePath
    val schema = graphQLSchema.value.getAbsolutePath
    val namespace = graphQLTypesNamespace.value
    val outputDir = (sourceManaged in graphQLGenerateTypes).value
    outputDir.mkdirs()
    val output = (outputDir / "graphql.scala").getAbsoluteFile
    val command = Seq(
      apollo,
      "codegen:generate",
      // TODO: handle globs properly. This needs to stay in sync with graphQLSources.
      s"--queries=$sourceDir/**/*.graphql",
      s"--schema=$schema",
      s"--namespace=$namespace",
      output.getPath
    )
    val workingDirectory = (baseDirectory in graphQLGenerateTypes).value
    val log = state.value.log
    val exitCode = Process(command, Some(workingDirectory)) ! log
    if (exitCode != 0) throw CodegenFailed(exitCode)
    Seq(output)
  }
}
