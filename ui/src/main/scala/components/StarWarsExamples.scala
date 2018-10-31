package components

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.StatelessComponent
import slinky.web.html._

@react class StarWarsExamples extends StatelessComponent {

  type Props = Unit

  override def render(): ReactElement = {
    val links = Seq(
      "heroAndFriends" -> "Hero and his friends names",
      "fragmentsExample" -> "Use of fragments to query common human and droid fields",
      "humanWithVariable" -> "Human by ID using variable",
      "fullIntrospection" -> "Full introspection"
    ).map {
      case (id, text) => ExampleLink(id, text): ReactElement
    }
    val break: ReactElement = br()
    val separated = links.flatMap(link => Seq(link, break)).dropRight(1)
    CollapsibleSection("exampleSection", "Examples")(separated: _*)
  }
}
