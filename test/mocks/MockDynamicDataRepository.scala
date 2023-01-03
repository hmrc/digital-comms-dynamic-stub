/*
 * Copyright 2023 HM Revenue & Customs
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

package mocks

import base.BaseSpec
import models.DynamicDataModel
import org.mongodb.scala.result.{DeleteResult, InsertOneResult}
import org.scalamock.handlers.{CallHandler0, CallHandler1}
import org.scalamock.scalatest.MockFactory
import services.DynamicStubService

import scala.concurrent.Future

trait MockDynamicDataRepository extends BaseSpec with MockFactory {

  lazy val mockDataRepository: DynamicStubService = mock[DynamicStubService]

  def mockAddEntry(document: DynamicDataModel)
                  (response: Future[InsertOneResult]): CallHandler1[DynamicDataModel, Future[InsertOneResult]] =
    (mockDataRepository.insert(_: DynamicDataModel))
      .expects(document)
      .returning(response)

  def mockRemoveAll(response: DeleteResult): CallHandler0[Future[DeleteResult]] =
    (() => mockDataRepository.removeAll())
      .expects()
      .returning(Future.successful(response))

  def mockFind(response: List[DynamicDataModel]): CallHandler1[Seq[(String, String)], Future[Seq[DynamicDataModel]]] =
    (mockDataRepository.find(_: Seq[(String, String)]))
      .expects(*)
      .returning(Future.successful(response))
}
