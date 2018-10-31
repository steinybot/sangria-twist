/*
 * Copyright 2018 BotTech
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import components.StarWars
import slinky.hot

import scala.scalajs.{LinkingInfo, js}
import scala.scalajs.js.undefined
import scala.scalajs.js.annotation.{JSExportTopLevel, JSImport}
import scala.scalajs.js.Dynamic.global

@JSImport("Stylesheets/main.scss", JSImport.Default)
@js.native
object Style extends js.Object {}

@JSImport("bootstrap", JSImport.Default)
@js.native
object Bootstrap extends js.Object {}

@JSImport("jquery", JSImport.Default)
@js.native
object JQuery extends js.Object {}

object StarWarsApp extends ReactGraphQLApp {

  // Prevent these from being eliminated as dead code.
  Style
  Bootstrap
  JQuery

  @JSExportTopLevel("entrypoint.main")
  def main(args: Array[String]): Unit = {
    if (LinkingInfo.developmentMode) {
      hot.initialize()
    }
    val graphQLURL = global.environment.controllers.StarWarsController.graphql("", undefined, undefined).toString
    val graphIQLURL = global.environment.controllers.StarWarsController.graphiql().url.toString
    val sangriaImageURL = global.environment.controllers.Assets.versioned("images/sip-of-sangria-1.svg").url.toString
    val props = StarWars.Props(sangriaImageURL, graphIQLURL)
    val app = StarWars(props)
    val _  = renderApp(app, graphQLURL)
  }
}
