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

import com.apollographql.scalajs.ApolloBoostClient
import com.apollographql.scalajs.react.ApolloProvider
import org.scalajs.dom
import org.scalajs.dom.Element
import slinky.core.facade.{ReactElement, ReactInstance}
import slinky.web.ReactDOM

import org.querki.jquery._

trait ReactGraphQLApp {

  protected def rootElementID: String = "root"

  protected def renderApp(app: ReactElement, graphQLURI: String): ReactInstance = {
    val container = root().getOrElse(createRoot())
    val client = ApolloBoostClient(graphQLURI)
    val wrapped = ApolloProvider(client)(app)
    ReactDOM.render(wrapped, container)
  }

  private def root(): Option[Element] = {
    Option(dom.document.getElementById(rootElementID))
  }

  private def createRoot(): Element = {
    val elem = dom.document.createElement("div")
    elem.id = rootElementID
    val _ = dom.document.body.appendChild(elem)
    elem
  }
}
