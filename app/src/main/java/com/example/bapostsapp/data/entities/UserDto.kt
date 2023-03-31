package com.example.bapostsapp.data.entities

data class UserDto(
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val address: AddressDto,
    val phone: String,
    val website: String,
    val company: CompanyDto
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
    val geo: GeoDto
)

data class GeoDto(
    val lat: String,
    val lng: String
)