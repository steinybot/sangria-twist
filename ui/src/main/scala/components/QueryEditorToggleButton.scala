package components

import components.Attributes._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.StatelessComponent
import slinky.web.html._

@react class QueryEditorToggleButton extends StatelessComponent {

  case class Props(text: String, section: String, expanded: Boolean)

  override def render(): ReactElement = {
    a(
      className := "btn",
      data - "toggle" := "collapse",
      data - "target" := s"#${props.section}",
      ariaExpanded := props.expanded,
      ariaControls := props.section
    )(
      props.text
    )
  }
}
