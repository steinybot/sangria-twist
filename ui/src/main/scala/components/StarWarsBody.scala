package components

import components.Attributes._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.StatelessComponent
import slinky.web.html._

@react class StarWarsBody extends StatelessComponent {

  type Props = Unit

  override def render(): ReactElement = {
    div(className := "container")(
      div(className := "row")(
        div(className := "col-sm-7 blog-main")(
          section(
            h2("GraphQL Query"),
            div(id := "queryEditor")
          ),
          QueryEditorToggleButton("Variables", "variablesSection", expanded = false),
          QueryEditorToggleButton("Config", "configSection", expanded = false),
          QueryEditorToggleButton("Examples", "exampleSection", expanded = false),
          QueryEditorButton("permalink")(
            span(className := "glyphicon glyphicon-link", ariaHidden := true, key := "x")
          ),
          QueryEditorButton("runButton")(
            span(className := "glyphicon glyphicon-play", ariaHidden := true, key := "x")
          ),
          StarWarsExamples(),
          StarWarsConfig(),
          CollapsibleSection("variablesSection", "Variables")(
            div(id := "variablesEditor")
          ),
          CollapsibleSection("errors", "Errors")(
            div(id := "responseError")
          ),
          CollapsibleSection("response", "Response")(
            div(id := "responseEditor")
          ),
        ),
        div(className := "col-sm-4 col-sm-offset-1")(
          StarWarsSchema()
        )
      )
    )
  }
}
