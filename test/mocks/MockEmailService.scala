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
import models.EmailRequestModel
import org.mongodb.scala.result.{DeleteResult, InsertOneResult}
import org.scalamock.handlers.{CallHandler0, CallHandler1}
import org.scalamock.scalatest.MockFactory
import services.EmailService

import scala.concurrent.Future

trait MockEmailService extends BaseSpec with MockFactory {

  lazy val mockEmailService: EmailService = mock[EmailService]

  def mockInsert(data: EmailRequestModel)
                (response: InsertOneResult): CallHandler1[EmailRequestModel, Future[InsertOneResult]] =
    (mockEmailService.insert(_: EmailRequestModel))
      .expects(data)
      .returning(Future.successful(response))

  def mockRemoveAll(response: DeleteResult): CallHandler0[Future[DeleteResult]] =
    (() => mockEmailService.removeAll())
      .expects()
      .returning(Future.successful(response))

  def mockCount(response: Long): CallHandler0[Future[Long]] =
    (() => mockEmailService.count())
      .expects()
      .returning(Future.successful(response))
}
