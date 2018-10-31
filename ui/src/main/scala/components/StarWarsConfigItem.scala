package components

import components.Attributes._
import slinky.core.{AttrPair, StatelessComponent}
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react class StarWarsConfigItem extends StatelessComponent {

  case class Props(id: String, placeholder: String)

  override def render(): ReactElement = {
    val addonId = "basic-addon1"
    val attributes: Seq[AttrPair[input.tagType]] = Seq(
      `type` := "text",
      className := "form-control",
      placeholder := props.placeholder,
      ariaDescribedBy := addonId
    )
    div(className := "input-group")(
      span(className := "input-group-addon", id := addonId)(
        span(className := "glyphicon glyphicon-globe", ariaHidden := true)
      ),
      input(id := props.id, attributes: _*)
    )
  }
}
