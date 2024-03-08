package Tests

class PassThroughClosedBarrierTest(private val checkpointIdentity: CheckpointIdentity) : IntegrationTest<BaseSentences> {
    override suspend fun specifyTest(venueController: VenueIntegrationTestController<BaseSentences>) {
        venueController.withDevice(checkpointIdentity) {
            `barrier passability`(PassageDirection.DEFAULT) shouldBe false
            `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)
            `pass barrier`(PassageType.SINGLE)
            `barrier passability should stay`(false)
            `current display content should eventually contain`(DEFAULT_HSH_IDLE_DISPLAY_CONTENT)
        }
    }
}