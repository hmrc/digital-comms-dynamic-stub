/*
 * Copyright 2020 HM Revenue & Customs
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

package services

import javax.inject.Inject
import play.api.libs.json.JsValue
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.DefaultDB
import repositories.EmailRepository
import uk.gov.hmrc.mongo.MongoConnector

import scala.concurrent.{ExecutionContext, Future}

class EmailService @Inject()(reactiveMongoComponent: ReactiveMongoComponent) {

  lazy val mongoConnector: MongoConnector = reactiveMongoComponent.mongoConnector
  implicit lazy val db: () => DefaultDB = mongoConnector.db

  private[services] lazy val repository: EmailRepository = new EmailRepository()

  def insert(data: JsValue)(implicit ec: ExecutionContext): Future[Boolean] =
    repository.insert(data).map(_.ok)

  def count()(implicit ec: ExecutionContext): Future[Int] =
    repository.count

  def removeAll()(implicit ec: ExecutionContext): Future[Boolean] =
    repository.removeAll().map(_.ok)
}
