package components

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class StarWarsHeader extends StatelessComponent {

  case class Props(sangriaImageURL: String, graphIQLURL: String)

  override def render(): ReactElement = {
    header(
      div(className := "container")(
        div(className := "blog-header")(
          img(src := props.sangriaImageURL)(),
          p(
            "This is an example of a ",
            a(href := "https://facebook.github.io/graphql", target := "_blank")(
              "GraphQL"
            ),
            " server written with ",
            a(href := "https://www.playframework.com/", target := "_blank")(
              "Play framework"
            ),
            " and ",
            a(href := "http://sangria-graphql.org/", target := "_blank")(
              "Sangria"
            ),
            ". It has the extra twist in that the UI has been written with ",
            a(href := "https://www.scala-js.org/", target := "_blank")(
              "Scala.js"
            ),
            ", ",
            a(href := "https://slinky.shadaj.me/", target := "_blank")(
              "Slinky"
            ),
            " and ",
            a(href := "https://github.com/apollographql/apollo-scalajs", target := "_blank")(
              "Apollo Scala.js"
            ),
            """ as the GraphQL client. It also serves as a playground.
              |On the right hand side you can see a textual representation of the GraphQL schema which is implemented
              |on the server and that you can query here.
              |On the left hand side you can execute a GraphQL query and see the results of its execution.
              |If you prefer, you can also use """.stripMargin,
            a(href := props.graphIQLURL)(
              "GraphiQL"
            ),
            "."
          )
        )
      )
    )
  }
}
