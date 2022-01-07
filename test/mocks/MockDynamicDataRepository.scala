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

package mocks

import base.BaseSpec
import models.DynamicDataModel
import org.scalamock.handlers.{CallHandler1, CallHandler2}
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Json.JsValueWrapper
import reactivemongo.api.commands.{UpdateWriteResult, WriteError, WriteResult}
import repositories.DynamicStubDataRepository

import scala.concurrent.{ExecutionContext, Future}

trait MockDynamicDataRepository extends BaseSpec with MockFactory {

  val successWriteResult: UpdateWriteResult = UpdateWriteResult(
    ok = true,
    n = 1,
    nModified = 1,
    upserted = Seq(),
    writeErrors = Seq(),
    None,
    None,
    None
  )
  val errorWriteResult: UpdateWriteResult = UpdateWriteResult(
    ok = false,
    n = 1,
    nModified = 1,
    upserted = Seq(),
    writeErrors = Seq(WriteError(1,1,"Error")),
    None,
    None,
    None
  )

  lazy val mockDataRepository: DynamicStubDataRepository = mock[DynamicStubDataRepository]

  def mockAddEntry(document: DynamicDataModel)
                  (response: WriteResult): CallHandler2[DynamicDataModel, ExecutionContext, Future[WriteResult]] =
    (mockDataRepository.insert(_: DynamicDataModel)(_: ExecutionContext))
      .expects(document, *)
      .returning(Future.successful(response))

  def mockRemoveAll()(response: WriteResult): CallHandler1[ExecutionContext, Future[WriteResult]] =
    (mockDataRepository.removeAll()(_: ExecutionContext))
      .expects(*)
      .returning(Future.successful(response))

  def mockFind(response: List[DynamicDataModel]): CallHandler2[(String, JsValueWrapper),
                                                               ExecutionContext, Future[List[DynamicDataModel]]] =
    (mockDataRepository.find(_: (String, JsValueWrapper))(_: ExecutionContext))
      .expects(*,*)
      .returning(Future.successful(response))
}
