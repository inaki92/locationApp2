package com.inaki.locationapp

class MyCoordinate {

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    companion object {
        private var instance: MyCoordinate? = null

        fun getInstance(): MyCoordinate {
            instance?.let {
                return it
            } ?: kotlin.run {
                instance = MyCoordinate()
                return instance as MyCoordinate
            }
        }
    }
}