package com.example.bapostsapp.data.entities

data class UserDto(
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val addressDto: AddressDto,
    val phone: String,
    val website: String,
    val companyDto: CompanyDto
)

data class CompanyDto(
    val name: String,
    val catchPhrase: String,
    val bs: String
)

data class AddressDto(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geoDto: GeoDto
)

data class GeoDto(
    val lat: String,
    val lng: String
)