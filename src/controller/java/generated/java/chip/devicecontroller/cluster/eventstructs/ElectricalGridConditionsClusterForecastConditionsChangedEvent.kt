/*
 *
 *    Copyright (c) 2023 Project CHIP Authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package chip.devicecontroller.cluster.eventstructs

import chip.devicecontroller.cluster.*
import matter.tlv.AnonymousTag
import matter.tlv.ContextSpecificTag
import matter.tlv.Tag
import matter.tlv.TlvReader
import matter.tlv.TlvWriter

class ElectricalGridConditionsClusterForecastConditionsChangedEvent(
  val forecastConditions:
    List<
      chip.devicecontroller.cluster.structs.ElectricalGridConditionsClusterElectricalGridConditionsStruct
    >?
) {
  override fun toString(): String = buildString {
    append("ElectricalGridConditionsClusterForecastConditionsChangedEvent {\n")
    append("\tforecastConditions : $forecastConditions\n")
    append("}\n")
  }

  fun toTlv(tlvTag: Tag, tlvWriter: TlvWriter) {
    tlvWriter.apply {
      startStructure(tlvTag)
      if (forecastConditions != null) {
        startArray(ContextSpecificTag(TAG_FORECAST_CONDITIONS))
        for (item in forecastConditions.iterator()) {
          item.toTlv(AnonymousTag, this)
        }
        endArray()
      } else {
        putNull(ContextSpecificTag(TAG_FORECAST_CONDITIONS))
      }
      endStructure()
    }
  }

  companion object {
    private const val TAG_FORECAST_CONDITIONS = 0

    fun fromTlv(
      tlvTag: Tag,
      tlvReader: TlvReader,
    ): ElectricalGridConditionsClusterForecastConditionsChangedEvent {
      tlvReader.enterStructure(tlvTag)
      val forecastConditions =
        if (!tlvReader.isNull()) {
          buildList<
            chip.devicecontroller.cluster.structs.ElectricalGridConditionsClusterElectricalGridConditionsStruct
          > {
            tlvReader.enterArray(ContextSpecificTag(TAG_FORECAST_CONDITIONS))
            while (!tlvReader.isEndOfContainer()) {
              this.add(
                chip.devicecontroller.cluster.structs
                  .ElectricalGridConditionsClusterElectricalGridConditionsStruct
                  .fromTlv(AnonymousTag, tlvReader)
              )
            }
            tlvReader.exitContainer()
          }
        } else {
          tlvReader.getNull(ContextSpecificTag(TAG_FORECAST_CONDITIONS))
          null
        }

      tlvReader.exitContainer()

      return ElectricalGridConditionsClusterForecastConditionsChangedEvent(forecastConditions)
    }
  }
}
