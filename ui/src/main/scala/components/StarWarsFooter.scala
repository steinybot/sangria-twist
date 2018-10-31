package components

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class StarWarsFooter extends StatelessComponent {

  case class Props(sangriaImageURL: String, graphIQLURL: String)

  override def render(): ReactElement = {
    footer(
      div(className := "container")(
        div(className := "blog-header")(
          p(
            """This is just a small demonstration. It really gets interesting when you start to play with the schema on
              |the server side. Fortunately it's pretty easy to do. """.stripMargin,
            a(href := "https://github.com/sangria-graphql/sangria-playground", target := "_blank")(
              "sangria-playground"
            ),
            """ is available on GitHub, and since it's a simple Play application, all it takes to start playground
              |locally and start playing with the schema is this:""".stripMargin
          ),
          pre(
            """$ git clone https://github.com/sangria-graphql/sangria-playground.git
              |$ cd sangria-playground
              |$ sbt run""".stripMargin
          ),
          p(
            "Now you are ready to point your browser to ",
            a(href := "http://localhost:9000", target := "_blank")("http://localhost:9000"),
            " to see the same page you are seeing here. The only prerequisites are ",
            a(href := "http://www.scala-sbt.org/download.html", target := "_blank")("SBT"),
            " and ",
            a(href := "http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html", target := "_blank")(
              "Java 8"
            ),
            ". Sangria playground is also available as a ",
            a(href := "https://www.typesafe.com/activator/template/sangria-playground", target := "_blank")(
              "Typesafe Activator Template"
            ),
            "."
          )
        )
      )
    )
  }
}
