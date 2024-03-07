package Tests.Tests

import Tests.BaseSentences
import Tests.DEFAULT_HSH_GRANT_ACCESS_DISPLAY_CONTENT
import Tests.DEFAULT_HSH_IDLE_DISPLAY_CONTENT
import com.skidata.accesscontroller.integrationtest.controller.VenueIntegrationTestController
import com.skidata.accesscontroller.integrationtest.tools.IntegrationTest
import com.skidata.accesscontroller.integrationtest.tools.createBarcodeDataCarrier
import com.skidata.checkpoint.dt.CheckpointIdentity
import com.skidata.checkpoint.dt.PassageDirection
import io.kotest.matchers.shouldBe

class wrongSidePass(private val checkpointIdentity: CheckpointIdentity) : IntegrationTest<BaseSentences> {
    override suspend fun specifyTest(venueController: VenueIntegrationTestController<BaseSentences>) {
        venueController.withDevice(checkpointIdentity) {
            `barrier passability`(PassageDirection.DEFAULT) shouldBe false
            `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)
            var utId= “SD09.24.201300000001”
            `offer ticket`(createBarcodeDataCarrier(utId))
            `current display content should eventually contain`(DEFAULT_HSH_GRANT_ACCESS_DISPLAY_CONTENT)
            `barrier passability should stay`(true)
            `pass barrier`(CheckpointSide.BACK,PassageType.SINGLE)
            // i dont know exactly how the device functions but i would gather you can't pass through
            // if you check the ticket from the opposite side
            `barrier passability should stay`(true)
            `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)
            `barrier passability should eventually be`(false)

        }
    }
}
