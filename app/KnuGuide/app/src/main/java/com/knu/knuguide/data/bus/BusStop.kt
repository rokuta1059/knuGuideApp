package com.knu.knuguide.data.bus

import com.knu.knuguide.data.KNUData
import java.io.Serializable

class BusStop : KNUData, Serializable {
    var busStopName: String
    var busStopDirection: String
    var nodeId: String
    var busNumber: Int
    var isFetched: Boolean = false

    var busList = mutableListOf<BusItem>()

    constructor(busStopName: String, busStopDirection: String, nodeId: String, busNumber: Int) {
        this.busStopName = busStopName
        this.busStopDirection = busStopDirection
        this.nodeId = nodeId
        this.busNumber = busNumber
    }

    override fun getRecyclerType(): Int = KNUData.Type.ITEM_BUS_STOP
}