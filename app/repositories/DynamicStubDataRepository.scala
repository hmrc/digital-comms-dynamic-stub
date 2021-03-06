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

package repositories

import javax.inject.Inject
import models.DynamicDataModel
import play.api.libs.json.Json.JsValueWrapper
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.DefaultDB
import reactivemongo.api.commands.WriteResult
import uk.gov.hmrc.mongo.MongoConnector

import scala.concurrent.{ExecutionContext, Future}

class DynamicStubDataRepository @Inject()(reactiveMongoComponent: ReactiveMongoComponent) {

  lazy val mongoConnector: MongoConnector = reactiveMongoComponent.mongoConnector
  implicit lazy val db: () => DefaultDB = mongoConnector.db

  private[repositories] lazy val repository: DynamicStubRepository = new DynamicStubRepository()

  def find(query: (String, JsValueWrapper)*)(implicit ec: ExecutionContext): Future[List[DynamicDataModel]] =
    repository.find(query:_*)

  def insert(data: DynamicDataModel)(implicit ec: ExecutionContext): Future[WriteResult] = repository.insert(data)

  def removeAll()(implicit ec: ExecutionContext): Future[WriteResult] = repository.removeAll()

}
