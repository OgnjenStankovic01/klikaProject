package Tests

import com.skidata.accesscontroller.integrationtest.tools.DisplayContentValues
import com.skidata.accesscontroller.tools.CheckpointLoggerFactory
import com.skidata.checkpoint.dt.CheckpointIdentity
import com.skidata.checkpoint.dt.CheckpointSide
import com.skidata.checkpoint.dt.Color
import com.skidata.checkpoint.dt.ConsumerType
import com.skidata.checkpoint.dt.DisplayContent
import com.skidata.checkpoint.dt.PassageDirection
import com.skidata.checkpoint.dt.PassageType
import com.skidata.checkpoint.dt.TicketTechnology
import com.skidata.checkpoint.dt.datacarrier.DataCarrier
import io.kotest.assertions.nondeterministic.ContinuallyConfiguration
import io.kotest.assertions.nondeterministic.EventuallyConfiguration
import io.kotest.assertions.nondeterministic.continuallyConfig
import io.kotest.assertions.nondeterministic.eventuallyConfig
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

private const val defaultWaitDuration: Long = 10000
private const val defaultRetryInterval: Long = 1000
private val defaultEventuallyConfiguration = eventuallyConfig {
    duration = defaultWaitDuration.milliseconds
    interval = defaultRetryInterval.milliseconds
}
private val defaultContinuallyConfiguration: ContinuallyConfiguration<Boolean> = continuallyConfig {
    duration = 5.seconds
    interval = defaultRetryInterval.milliseconds
}

interface BaseSentences {
    val loggerFactory: CheckpointLoggerFactory
    val checkpointIdentity: CheckpointIdentity

    /**
     * Checks the availability of the device by the integration test
     * @return true, if the integration test can communicate with the device and the device is ready to take orders
     */
    fun `is device reachable`(): Boolean

    /**
     * Get the display content currently shown on the device
     */
    suspend fun `current display content`(
        physicalCheckpointSide: CheckpointSide = CheckpointSide.FRONT,
        consumerType: ConsumerType = ConsumerType.CUSTOMER
    ): DisplayContent

    /**
     * This method checks over a certain duration periodically (provided through [eventuallyConfiguration])
     * if the actual display content matches the parameters given to this method.
     * If all expected values match the actual display content then the method returns successful.
     * If not, the method waits for an interval time provided by [eventuallyConfiguration] and then tries again,
     * until either all expected values match or the duration limit by [eventuallyConfiguration] is reached, in which case the method throws an AssertionError.
     * @param displayText The expected display text. If null, then the display text will not be checked.
     * @param image The expected image provided as string. If null, then the image will not be checked.
     * @param statusColor The expected status color of the display. If null, then the status color will not be checked.
     * @param actionColor The expected action color of the display. If null, then the action color will not be checked.
     * @param physicalCheckpointSide The side of the display.
     * @param consumerType The consumer type of the display.
     * @param eventuallyConfiguration The duration of the [eventuallyConfiguration] gives the time how long the method tries
     */
    suspend fun `current display content should eventually contain`(
        displayText: String? = null,
        image: String? = null,
        statusColor: Color? = null,
        actionColor: Color? = null,
        physicalCheckpointSide: CheckpointSide = CheckpointSide.FRONT,
        consumerType: ConsumerType = ConsumerType.CUSTOMER,
        eventuallyConfiguration: EventuallyConfiguration = defaultEventuallyConfiguration
    )

    /**
     * Overloaded version of the method above
     */
    suspend fun `current display content should eventually contain`(
        displayContentValues: DisplayContentValues,
        physicalCheckpointSide: CheckpointSide = CheckpointSide.FRONT,
        consumerType: ConsumerType = ConsumerType.CUSTOMER,
        eventuallyConfiguration: EventuallyConfiguration = defaultEventuallyConfiguration
    ) {
        `current display content should eventually contain`(
            displayContentValues.displayText,
            displayContentValues.image,
            displayContentValues.statusColor?.color,
            displayContentValues.actionColor?.color,
            physicalCheckpointSide,
            consumerType,
            eventuallyConfiguration
        )
    }

    /**
     * Offer a ticket to the ticket reader unit
     */
    suspend fun `offer ticket`(
        dataCarrier: DataCarrier,
        physicalCheckpointSide: CheckpointSide = CheckpointSide.FRONT
    )

    /**
     * Simulate an entry attempt through the barrier
     */
    suspend fun `pass barrier`(
        physicalCheckpointSide: CheckpointSide = CheckpointSide.FRONT,
        type: PassageType = PassageType.SINGLE,
        timeout: Long = defaultWaitDuration
    )

    /**
     * Checks if the reader is eventually activated/deactivated
     * @param checkpointSide The side from which the person wants to enter
     * @return true if the barrier can be passed from side [checkpointSide]
     */
    suspend fun `reading should eventually be`(
        reading: Boolean,
        readingTechnology: TicketTechnology,
        checkpointSide: CheckpointSide = CheckpointSide.FRONT,
        eventuallyConfiguration: EventuallyConfiguration = defaultEventuallyConfiguration
    )

    /**
     * Checks if the barrier is currently in a position that a person can enter or not
     * @param passageDirection The side from which the person wants to enter
     * @return true if the barrier can be passed from side [passageDirection]
     */
    suspend fun `barrier passability`(
        passageDirection: PassageDirection = PassageDirection.DEFAULT
    ): Boolean

    /**
     * Checks if the barrier is eventually in a position that a person can enter
     * @param passageDirection The side from which the person wants to enter
     * @return true if the barrier can be passed from side [passageDirection]
     */
    suspend fun `barrier passability should eventually be`(
        passible: Boolean,
        passageDirection: PassageDirection = PassageDirection.DEFAULT,
        eventuallyConfiguration: EventuallyConfiguration = defaultEventuallyConfiguration
    )

    /**
     * Checks if barrier stay in a certain passable mode (either stays closed or stays passable) for a certain amount of time
     */
    suspend fun `barrier passability should stay`(
        passible: Boolean,
        passageDirection: PassageDirection = PassageDirection.DEFAULT,
        continuallyConfiguration: ContinuallyConfiguration<Boolean> = defaultContinuallyConfiguration
    )
}
