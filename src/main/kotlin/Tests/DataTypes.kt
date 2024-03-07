
package Tests

import com.skidata.accesscontroller.datatypes.AcColor
import com.skidata.accesscontroller.integrationtest.tools.DisplayContentValues

const val STANDARD_IDLE_TEXT = "Integration Area A"

val DEFAULT_HSH_IDLE_DISPLAY_CONTENT = DisplayContentValues(
    displayText = STANDARD_IDLE_TEXT,
    image = "idle",
    statusColor = AcColor.GREEN,
    actionColor = AcColor.GREEN
)

val DEFAULT_HSH_DENY_ACCESS_DISPLAY_CONTENT = DisplayContentValues(
    displayText = "ACCESS DENIED",
    image = "deny",
    statusColor = AcColor.RED,
    actionColor = AcColor.RED
)

val DEFAULT_HSH_GRANT_ACCESS_DISPLAY_CONTENT = DisplayContentValues(
    displayText = "WELCOME",
    image = "grant",
    statusColor = AcColor.GREEN,
    actionColor = AcColor.GREEN
)
