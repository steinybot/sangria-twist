package components

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class ExampleLink extends StatelessComponent {

  case class Props(id: String, text: String)

  override def render(): ReactElement = {
    a(id := props.id, href := "#", className := "exampleLink")(props.text)
  }
}
