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

import controllers.{EnvironmentController, StarWarsController}
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import play.filters.cors.CORSComponents
import router.Routes

class StarWarsComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
    with controllers.AssetsComponents
    with CORSComponents
    with HttpFiltersComponents {

  private lazy val environmentController = new EnvironmentController(controllerComponents)

  private lazy val starWarsController = new StarWarsController(controllerComponents, actorSystem, configuration)

  override lazy val httpFilters: Seq[EssentialFilter] = corsFilter +: super.httpFilters

  lazy val router: Router = new Routes(httpErrorHandler, starWarsController, assets, environmentController)
}
