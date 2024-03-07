package Tests

class reachTest(private val checkpointIdentity: CheckpointIdentity) : IntegrationTest<BaseSentences> {
    override suspend fun specifyTest(venueController: VenueIntegrationTestController<BaseSentences>) {
        venueController.withDevice(checkpointIdentity) {
            // ok so the `is device reachable` method doesn't seem to have any body, just a boolean return
            // maybe if it had body it would require a device?
            // if so, this test could be broken.
            if(`is device reachable`() == true){
                `barrier passability`(PassageDirection.DEFAULT) shouldBe false
                `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)
                var utId= “SD09.24.201300000001”
                `offer ticket`(createBarcodeDataCarrier(utId))
                `current display content should eventually contain`(DEFAULT_HSH_GRANT_ACCESS_DISPLAY_CONTENT)
                `barrier passability should stay`(true);
                `pass barrier`(PassageType.SINGLE)
                `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)
                `barrier passability should eventually be`(false)
            }
            else{
                System.out.println("Gate not reachable.")
            }
        }
    }

}