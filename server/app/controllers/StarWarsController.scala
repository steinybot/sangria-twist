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

package controllers

import akka.actor.ActorSystem
import models.{CharacterRepo, StarWarsSchemaDefinition}
import play.api.Configuration
import play.api.libs.json._
import play.api.mvc._
import sangria.execution._
import sangria.execution.deferred.DeferredResolver
import sangria.marshalling.playJson._
import sangria.parser.{QueryParser, SyntaxError}
import sangria.renderer.SchemaRenderer

import scala.concurrent.Future
import scala.util.{Failure, Success}

class StarWarsController(components: ControllerComponents, system: ActorSystem, config: Configuration)
  extends AbstractController(components) {

  import system.dispatcher

  private final val DefaultPort = 9000

  private final val MaxQueryDepth = 15

  private final val QueryComplexityThreshold = 4000

  private val defaultGraphQLPort = {
    config.getOptional[Int]("http.port").getOrElse(DefaultPort)
  }

  private val defaultGraphQLUrl = {
    config.getOptional[String]("defaultGraphQLUrl")
      .getOrElse(s"http://localhost:$defaultGraphQLPort/graphql")
  }

  def index: Action[AnyContent] = Action {
    Ok(views.html.index(defaultGraphQLUrl))
  }

  def index2: Action[AnyContent] = Action {
    Ok(views.html.index2())
  }

  def graphiql: Action[AnyContent] = Action {
    Ok(views.html.graphiql())
  }

  def graphql(query: String, variables: Option[String], operation: Option[String]): Action[AnyContent] =
    Action.async(executeQuery(query, variables map parseVariables, operation))

  def graphqlBody: Action[JsValue] = Action.async(parse.json) { request =>
    val query = (request.body \ "query").as[String]
    val operation = (request.body \ "operationName").asOpt[String]

    val variables = (request.body \ "variables").toOption.flatMap {
      case JsString(vars) => Some(parseVariables(vars))
      case obj: JsObject => Some(obj)
      case _ => None
    }

    executeQuery(query, variables, operation)
  }

  private def parseVariables(variables: String) =
    if (variables.trim == "" || variables.trim == "null") Json.obj() else Json.parse(variables).as[JsObject]

  private def executeQuery(query: String, variables: Option[JsObject], operation: Option[String]) =
    QueryParser.parse(query) match {

      // query parsed successfully, time to execute it!
      case Success(queryAst) =>
        Executor.execute(
          StarWarsSchemaDefinition.StarWarsSchema, queryAst, new CharacterRepo,
          operationName = operation,
          variables = variables getOrElse Json.obj(),
          deferredResolver = DeferredResolver.fetchers(StarWarsSchemaDefinition.characters),
          exceptionHandler = exceptionHandler,
          queryReducers = List(
            QueryReducer.rejectMaxDepth[CharacterRepo](MaxQueryDepth),
            QueryReducer.rejectComplexQueries[CharacterRepo](QueryComplexityThreshold, (_, _) => TooComplexQueryError)
          )
        )
          .map(Ok(_))
          .recover {
            case error: QueryAnalysisError => BadRequest(error.resolveError)
            case error: ErrorWithResolver => InternalServerError(error.resolveError)
          }

      // can't parse GraphQL query, return error
      case Failure(error: SyntaxError) =>
        Future.successful(BadRequest(Json.obj(
          "syntaxError" -> error.getMessage,
          "locations" -> Json.arr(Json.obj(
            "line" -> error.originalError.position.line,
            "column" -> error.originalError.position.column
          ))
        )))

      case Failure(error) => Future.failed(error)
    }

  def renderSchema: Action[AnyContent] = Action {
    Ok(SchemaRenderer.renderSchema(StarWarsSchemaDefinition.StarWarsSchema))
  }

  lazy val exceptionHandler = ExceptionHandler {
    case (_, error@TooComplexQueryError) => HandledException(error.getMessage)
    case (_, error@MaxQueryDepthReachedError(_)) => HandledException(error.getMessage)
  }

  case object TooComplexQueryError extends Exception("Query is too expensive.")

}
