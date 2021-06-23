/*
 * Copyright 2021 HM Revenue & Customs
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
import mocks.MockDynamicDataRepository
import models.DynamicDataModel
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.mvc.Http.Status

class SetupDataControllerSpec extends BaseSpec with MockDynamicDataRepository {

  lazy val controller = new SetupDataController(mockDataRepository)

  "SetupDataController.addData" when {

    "the request method is 'GET' or 'POST'" should {

      val model: DynamicDataModel = DynamicDataModel(
        _id = "1234",
        method = "GET",
        response = Some(Json.parse("{}")),
        status = Status.OK
      )

      "return Status OK (200) if data successfully added to stub" in {
        lazy val request = FakeRequest().withBody(Json.toJson(model)).withHeaders(("Content-Type", "application/json"))
        lazy val result = controller.addData(request)

        mockAddEntry(model)(successWriteResult)
        status(result) shouldBe Status.OK
      }

      "return Status InternalServerError (500) if unable to add data to the stub" in {
        lazy val request = FakeRequest().withBody(Json.toJson(model)).withHeaders(("Content-Type", "application/json"))
        lazy val result = controller.addData(request)

        mockAddEntry(model)(errorWriteResult)
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "the request method is not 'GET' or 'POST'" should {

      val model: DynamicDataModel = DynamicDataModel(
        _id = "1234",
        method = "BLOB",
        response = Some(Json.parse("{}")),
        status = Status.OK)

      "return Status BadRequest (400)" in {
        lazy val request = FakeRequest().withBody(Json.toJson(model)).withHeaders(("Content-Type", "application/json"))
        lazy val result = controller.addData(request)

        status(result) shouldBe Status.BAD_REQUEST
      }
    }
  }

  "SetupDataController.removeAllData" should {

    "return Status OK (200) on successful removal of all stubbed data" in {
      lazy val request = FakeRequest()
      lazy val result = controller.removeAll()(request)

      mockRemoveAll()(successWriteResult)

      status(result) shouldBe Status.OK
    }

    "return Status InternalServerError (500) on successful removal of all stubbed data" in {
      lazy val request = FakeRequest()
      lazy val result = controller.removeAll()(request)

      mockRemoveAll()(errorWriteResult)

      status(result) shouldBe Status.INTERNAL_SERVER_ERROR
    }
  }
}
