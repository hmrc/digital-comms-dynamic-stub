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

package controllers

import base.BaseSpec
import mocks.MockEmailService
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{defaultAwaitTimeout, status}

class EmailControllerSpec extends BaseSpec with MockEmailService {

  lazy val controller = new EmailController(mockEmailService)

  "EmailController.insert" when {

    "the request body is valid json" should {

      val testInvalidJson = "invalid"

      s"return Status ACCEPTED ($ACCEPTED) if data successfully added to stub" in {
        lazy val request = FakeRequest().withBody(Json.toJson(emailModel)).withHeaders(("Content-Type", "application/json"))
        lazy val result = controller.insert()(request)

        mockInsert(emailModel)(successWriteResult)
        status(result) shouldBe ACCEPTED
      }

      "return Status InternalServerError (500) if unable to add data to the stub" in {
        lazy val request = FakeRequest().withBody(Json.toJson(emailModel)).withHeaders(("Content-Type", "application/json"))
        lazy val result = controller.insert()(request)

        mockInsert(emailModel)(errorWriteResult)
        status(result) shouldBe INTERNAL_SERVER_ERROR
      }

      "return Status UnsupportedMediaType (415) if request body is not json" in {
        lazy val request = FakeRequest().withBody(testInvalidJson).withHeaders(("Content-Type", "text/plain"))
        lazy val result = controller.insert()(request)

        status(result) shouldBe UNSUPPORTED_MEDIA_TYPE
      }
    }
  }

  "EmailController.remove" should {

    "return Status OK (200) on successful removal of all stubbed data" in {
      lazy val result = controller.remove()(request)

      mockRemoveAll(successDeleteResult)
      status(result) shouldBe OK
    }

    "return Status InternalServerError (500) on unsuccessful removal of all stubbed data" in {
      lazy val result = controller.remove()(request)

      mockRemoveAll(errorDeleteResult)
      status(result) shouldBe INTERNAL_SERVER_ERROR
    }
  }

  "EmailController.count" should {

    "return Status OK (200) on successful count of all stubbed data" in {
      lazy val result = controller.count()(request)

      mockCount(1)
      status(result) shouldBe OK
    }
  }
}
