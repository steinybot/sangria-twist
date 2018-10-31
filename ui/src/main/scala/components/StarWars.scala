package components

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html.{className, div}

@react class StarWars extends StatelessComponent {

  case class Props(sangriaImageURL: String, graphIQLURL: String)

  override def render(): ReactElement = {
    div(className := "App")(
      StarWarsHeader(sangriaImageURL = props.sangriaImageURL, graphIQLURL = props.graphIQLURL),
      StarWarsBody(),
      StarWarsFooter()
    )
  }
}
