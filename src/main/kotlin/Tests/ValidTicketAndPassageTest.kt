package Tests

import com.skidata.accesscontroller.integrationtest.controller.VenueIntegrationTestController
import com.skidata.accesscontroller.integrationtest.tools.IntegrationTest
import com.skidata.accesscontroller.integrationtest.tools.createBarcodeDataCarrier
import com.skidata.checkpoint.dt.CheckpointIdentity
import com.skidata.checkpoint.dt.PassageDirection
import io.kotest.matchers.shouldBe

class ValidTicketAndPassageTest(private val checkpointIdentity: CheckpointIdentity ) : IntegrationTest<BaseSentences> {
    override suspend fun specifyTest(venueController: VenueIntegrationTestController<BaseSentences>){
        venueController.withDevice(checkpointIdentity){
            `is device reachable` shouldBe true
            `barrier passability`(PassageDirection.DEFAULT) shouldBe false
            `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)
            val utId= "SD09.24.201300000001"
            `offer ticket`(createBarcodeDataCarrier(utId))
            `current display content should eventually contain`(DEFAULT_HSH_GRANT_ACCESS_DISPLAY_CONTENT)
            // this is to check if the barrier is passable at the moment
            `barrier passability should stay`(true)
            `pass barrier`(PassageType.SINGLE)
            // this is to check that the barrier will close afterward.
            `barrier passability should eventually be`(false)
            `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)
        }
    }
}