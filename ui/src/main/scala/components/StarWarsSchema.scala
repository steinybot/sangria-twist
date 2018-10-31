package components

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class StarWarsSchema extends StatelessComponent {

  type Props = Unit

  override def render(): ReactElement = {
    div(
      h2("Schema"),
      div(id := "schema")(
        pre()
      )
    )
  }
}
