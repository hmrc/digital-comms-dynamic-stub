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

package controllers

import base.BaseSpec
import mocks.MockSecureMessageService
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.http.Status._
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.stubControllerComponents

import scala.concurrent.Future

class SecureMessageControllerSpec extends BaseSpec with MockSecureMessageService {

  implicit val cc: ControllerComponents = stubControllerComponents()

  lazy val controller = new SecureMessageController(mockSecureMessageService)

  "SecureMessageController.insert" when {

    "the request body is valid json" should {

      val testJson = Json.obj("test" -> "test")
      val testInvalidJson = "invalid"

      s"return Status CREATED ($CREATED) if data successfully added to stub" in {
        lazy val request = FakeRequest().withJsonBody(testJson).withHeaders(("Content-Type", "application/json"))
        lazy val result = controller.insert()(request)

        mockInsert(testJson)(Future.successful(true))
        status(result) shouldBe CREATED
      }

      "return Status InternalServerError (500) if unable to add data to the stub" in {
        lazy val request = FakeRequest().withJsonBody(testJson).withHeaders(("Content-Type", "application/json"))
        lazy val result = controller.insert()(request)

        mockInsert(testJson)(Future.successful(false))
        status(result) shouldBe INTERNAL_SERVER_ERROR
      }

      "return Status BadRequest (400) if request body is not json" in {
        lazy val request = FakeRequest().withTextBody(testInvalidJson).withHeaders(("Content-Type", "text/plain"))
        lazy val result = controller.insert()(request)

        status(result) shouldBe BAD_REQUEST
      }
    }
  }

  "SecureMessageController.remove" should {

    "return Status OK (200) on successful removal of all stubbed data" in {
      lazy val result = controller.remove()(request)

      mockRemoveAll()(Future.successful(true))

      status(result) shouldBe OK
    }

    "return Status InternalServerError (500) on unsuccessful removal of all stubbed data" in {
      lazy val result = controller.remove()(request)

      mockRemoveAll()(Future.successful(false))

      status(result) shouldBe INTERNAL_SERVER_ERROR
    }
  }

  "SecureMessageController.count" should {

    "return Status OK (200) on successful count of all stubbed data" in {
      lazy val result = controller.count()(request)

      mockCount()(Future.successful(1))

      status(result) shouldBe OK
    }
  }
}
