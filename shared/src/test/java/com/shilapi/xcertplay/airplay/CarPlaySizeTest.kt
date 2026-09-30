package com.shilapi.xcertplay.airplay

import org.junit.Assert.assertEquals
import org.junit.Test

class CarPlaySizeTest {
    @Test
    fun `presets map back to themselves`() {
        CarPlaySize.entries.forEach { assertEquals(it, CarPlaySize.fromWidthMillimeters(it.widthMillimeters)) }
    }

    @Test
    fun `older widths snap to the nearest preset`() {
        assertEquals(CarPlaySize.LARGE, CarPlaySize.fromWidthMillimeters(200))
        assertEquals(CarPlaySize.SMALLER, CarPlaySize.fromWidthMillimeters(400))
    }

    @Test
    fun `presets fit the reported width range`() {
        CarPlaySize.entries.forEach {
            assertEquals(it.widthMillimeters, AirPlayDisplaySettings.sanitizeWidthPhysicalMm(it.widthMillimeters))
        }
    }

    @Test
    fun `canvas presets map back to themselves and stay supported`() { // [改动:尺寸档位] 校验画布档位可反查，且必须是 CarPlayUiScale 支持的档位
        CarPlaySize.entries.forEach {
            assertEquals(it, CarPlaySize.fromUiScalePercent(it.uiScalePercent))
            assertEquals(it.uiScalePercent, CarPlayUiScale.sanitize(it.uiScalePercent))
        }
    }

    @Test
    fun `smaller sizes ask for a larger canvas`() { // [改动:尺寸档位] 校验“越小档位请求越大画布”，防止以后把方向改反
        assertEquals(CarPlaySize.LARGE, CarPlaySize.fromUiScalePercent(CarPlayUiScale.DEFAULT + 15))
        assertEquals(CarPlaySize.MEDIUM, CarPlaySize.fromUiScalePercent(CarPlayUiScale.DEFAULT))
        assertEquals(CarPlaySize.SMALL, CarPlaySize.fromUiScalePercent(85))
        assertEquals(CarPlaySize.SMALLER, CarPlaySize.fromUiScalePercent(75))
        assertEquals(
            "a smaller preset must never request a smaller canvas than a larger one",
            CarPlaySize.entries.sortedBy { it.uiScalePercent }.map { it.label },
            listOf("Smaller", "Small", "Medium", "Large"),
        )
    }
}
