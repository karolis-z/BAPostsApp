package com.example.bapostsapp.data.mappers

import com.example.bapostsapp.data.entities.AddressDto
import com.example.bapostsapp.data.entities.AddressEntity

fun AddressDto.toAddressEntity(userId: Long): AddressEntity {
    return AddressEntity(
        userId = userId,
        street = this.street,
        suite = this.suite,
        city = this.city,
        zipcode = this.zipcode,
        /* Choosing to have null as longitude and latitude in case the api provides a string value
        * that could not be converted to a Double value. If ever needed, the lat and lng values
        * could be checked for null. */
        lat = this.geo.lat.toDoubleOrNull(),
        lng = this.geo.lng.toDoubleOrNull()
    )
}