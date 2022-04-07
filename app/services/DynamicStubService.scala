/*
 * Copyright 2022 HM Revenue & Customs
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

import models.DynamicDataModel
import org.mongodb.scala.model.Filters.{and, empty, equal}
import org.mongodb.scala.result.{DeleteResult, InsertOneResult}
import repositories.DynamicStubRepository
import uk.gov.hmrc.mongo.MongoComponent

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DynamicStubService @Inject()(mongoComponent: MongoComponent)(implicit ec: ExecutionContext) {

  private[services] lazy val repository: DynamicStubRepository = new DynamicStubRepository(mongoComponent)

  def find(query: Seq[(String, String)]): Future[Seq[DynamicDataModel]] = {
    val terms = query.map(q => equal(q._1, q._2))
    repository.collection.find(and(terms:_*)).toFuture()
  }

  def insert(data: DynamicDataModel): Future[InsertOneResult] =
    repository.collection.insertOne(data).toFuture()

  def removeAll(): Future[DeleteResult] =
    repository.collection.deleteMany(empty()).toFuture()
}
