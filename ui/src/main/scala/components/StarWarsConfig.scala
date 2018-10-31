package components

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement

@react class StarWarsConfig extends StatelessComponent {

  type Props = Unit

  override def render(): ReactElement = {
    CollapsibleSection("configSection", "Config")(
      StarWarsConfigItem("graphqlUrl", "GraphQL Endpoint URL"),
      StarWarsConfigItem("operation", "Operation")
    )
  }
}
