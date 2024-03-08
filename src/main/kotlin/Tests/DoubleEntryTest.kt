package Tests

class DoubleEntryTest(private val checkpointIdentity: CheckpointIdentity) : IntegrationTest<BaseSentences> {
    override suspend fun specifyTest(venueController: VenueIntegrationTestController<BaseSentences>) {
        venueController.withDevice(checkpointIdentity) {
            // ok so the `is device reachable` method doesn't seem to have any body, just a boolean return
            // maybe if it had body it would require a device?
            // if so, this test could be broken.
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

            // double entry, try to enter with the same ticket
            `offer ticket`(createBarcodeDataCarrier(utId))
            `current display content should eventually contain`(DEFAULT_HSH_DENY_ACCESS_DISPLAY_CONTENT)
            `barrier passability should stay`(false)
            `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)
            // should not be allowed, the test should fail.
        }
    }

}