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
package matter.controller.cluster.eventstructs

import java.util.Optional
import matter.controller.cluster.*
import matter.tlv.ContextSpecificTag
import matter.tlv.Tag
import matter.tlv.TlvReader
import matter.tlv.TlvWriter

class ThermostatClusterActivePresetChangeEvent(
  val previousPresetHandle: Optional<ByteArray>,
  val currentPresetHandle: ByteArray,
) {
  override fun toString(): String = buildString {
    append("ThermostatClusterActivePresetChangeEvent {\n")
    append("\tpreviousPresetHandle : $previousPresetHandle\n")
    append("\tcurrentPresetHandle : $currentPresetHandle\n")
    append("}\n")
  }

  fun toTlv(tlvTag: Tag, tlvWriter: TlvWriter) {
    tlvWriter.apply {
      startStructure(tlvTag)
      if (previousPresetHandle.isPresent) {
        val optpreviousPresetHandle = previousPresetHandle.get()
        put(ContextSpecificTag(TAG_PREVIOUS_PRESET_HANDLE), optpreviousPresetHandle)
      }
      put(ContextSpecificTag(TAG_CURRENT_PRESET_HANDLE), currentPresetHandle)
      endStructure()
    }
  }

  companion object {
    private const val TAG_PREVIOUS_PRESET_HANDLE = 0
    private const val TAG_CURRENT_PRESET_HANDLE = 1

    fun fromTlv(tlvTag: Tag, tlvReader: TlvReader): ThermostatClusterActivePresetChangeEvent {
      tlvReader.enterStructure(tlvTag)
      val previousPresetHandle =
        if (tlvReader.isNextTag(ContextSpecificTag(TAG_PREVIOUS_PRESET_HANDLE))) {
          Optional.of(tlvReader.getByteArray(ContextSpecificTag(TAG_PREVIOUS_PRESET_HANDLE)))
        } else {
          Optional.empty()
        }
      val currentPresetHandle =
        tlvReader.getByteArray(ContextSpecificTag(TAG_CURRENT_PRESET_HANDLE))

      tlvReader.exitContainer()

      return ThermostatClusterActivePresetChangeEvent(previousPresetHandle, currentPresetHandle)
    }
  }
}