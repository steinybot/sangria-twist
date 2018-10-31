package components

import components.Attributes._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.StatelessComponent
import slinky.web.html._

@react class QueryEditorButton extends StatelessComponent {

  case class Props(id: String, children: ReactElement*)

  override def render(): ReactElement = {
    a(
      className := "btn",
      data - "toggle" := "collapse",
      ariaExpanded := false
    )(
      props.children
    )
  }
}
