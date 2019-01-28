/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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

package io.appium.espressoserver.lib.model

import androidx.test.espresso.action.*
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.JsonAdapter
import io.appium.espressoserver.lib.helpers.GsonParserHelpers
import java.lang.reflect.Type

@JsonAdapter(MobileClickActionParams.MobileClickActionParamsDeserializer::class)
data class MobileClickActionParams(
        val tapper : Tapper,
        val coordinatesProvider : CoordinatesProvider,
        val precisionDescriber : PrecisionDescriber,
        val inputDevice: Int,
        val buttonState: Int
) : AppiumParams() {


    class MobileClickActionParamsDeserializer : JsonDeserializer<MobileClickActionParams> {

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, paramType: Type?,
                                 paramJsonDeserializationContext: JsonDeserializationContext?): MobileClickActionParams {
            val jsonObject = json.asJsonObject

            val inputDevice = if (jsonObject.has("inputDevice"))
                    jsonObject.get("inputDevice").asInt
                else 0

            val buttonState = if (jsonObject.has("buttonState"))
                jsonObject.get("buttonState").asInt
                else 0

            val gsonParserHelpers = GsonParserHelpers();

            // Deserialize TAPPER as a tap enum
            val tapper = gsonParserHelpers.parseEnum<Tap>(
                    jsonObject,
                    "tapper",
                    "See https://developer.android.com/reference/android/support/test/espresso/action/Tap for list of valid tapper types"
            ) ?: Tap.SINGLE

            // Deserialize COORDINATES_PROVIDER as a general location enum
            val coordinatesProvider = gsonParserHelpers.parseEnum<GeneralLocation>(
                    jsonObject,
                    "coordinatesProvider",
                    "See https://developer.android.com/reference/android/support/test/espresso/action/GeneralLocation for list of valid coordinatesProvider types"
            ) ?: GeneralLocation.VISIBLE_CENTER

            // Deserialize PRECISION_DESCRIBER as a 'Press' enum
            val precisionDescriber = gsonParserHelpers.parseEnum<Press>(
                    jsonObject,
                    "precisionDescriber",
                    "See https://developer.android.com/reference/android/support/test/espresso/action/Press for list of valid precisionDescriber types"
            ) ?: Press.FINGER

            return MobileClickActionParams(tapper, coordinatesProvider, precisionDescriber, inputDevice, buttonState);
        }
    }

}