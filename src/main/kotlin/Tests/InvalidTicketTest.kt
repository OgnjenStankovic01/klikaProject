package Tests

import com.skidata.accesscontroller.integrationtest.controller.VenueIntegrationTestController
import com.skidata.accesscontroller.integrationtest.tools.IntegrationTest
import com.skidata.accesscontroller.integrationtest.tools.createBarcodeDataCarrier
import com.skidata.checkpoint.dt.CheckpointIdentity
import com.skidata.checkpoint.dt.PassageDirection
import io.kotest.matchers.shouldBe

class InvalidTicketTest(private val checkpointIdentity: CheckpointIdentity) : IntegrationTest<BaseSentences> {
    override suspend fun specifyTest(venueController: VenueIntegrationTestController<BaseSentences>) {
        venueController.withDevice(checkpointIdentity) {
            `barrier passability`(PassageDirection.DEFAULT) shouldBe false
            `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)
            val utId = "SD09.24.201300000004"
            `offer ticket`(createBarcodeDataCarrier(utId))
            `current display content should eventually contain`(DEFAULT_HSH_DENY_ACCESS_DISPLAY_CONTENT)
            `barrier passability should stay`(false)
            `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)

        }
    }
}
