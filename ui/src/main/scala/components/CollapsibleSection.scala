package components

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class CollapsibleSection extends StatelessComponent {

  case class Props(id: String, header: String, children: ReactElement*)

  override def render(): ReactElement = {
    val sectionElements = h2(props.header) +: props.children
    section(id := props.id, className := "collapse")(sectionElements: _*)
  }
}
