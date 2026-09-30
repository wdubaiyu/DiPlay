package com.shilapi.xcertplay.airplay

/**
 * The single user-facing CarPlay size.
 *
 * The size is driven by the canvas scale: iOS lays the CarPlay UI out for the negotiated canvas and
 * the host fits that canvas back to the same touch surface, so a larger canvas makes the icons and
 * text smaller. [widthMillimeters] is still declared to the phone and stays adjustable on the head
 * unit, so both knobs keep describing the same preset.
 */
enum class CarPlaySize(val label: String, val widthMillimeters: Int, val uiScalePercent: Int) { // [改动:尺寸档位] 新增 uiScalePercent：CarPlay 大小实际由画布缩放控制，毫米数对 iOS 无效
    LARGE("Large", 250, 115),
    MEDIUM("Medium", 300, 100),
    SMALL("Small", 350, 85),
    SMALLER("Smaller", 400, 75); // [改动:尺寸档位] 新增“更小”档：画布 75%（1920x1080 基准 → 2560x1440），比 Small 再小约 12%

    companion object {
        val DEFAULT = MEDIUM

        /** Maps any stored width, including values from older builds, to the nearest preset. */
        fun fromWidthMillimeters(millimeters: Int): CarPlaySize =
            entries.minBy { kotlin.math.abs(it.widthMillimeters - millimeters) }

        /** Maps any stored canvas scale, including values from older builds, to the nearest preset. */
        fun fromUiScalePercent(percent: Int): CarPlaySize = // [改动:尺寸档位] 新增：按画布档位反查档位，供手机端选择器回显
            entries.minBy { kotlin.math.abs(it.uiScalePercent - percent) }
    }
}
