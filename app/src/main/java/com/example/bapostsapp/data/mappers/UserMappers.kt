package com.example.bapostsapp.data.mappers

import com.example.bapostsapp.data.entities.UserDto
import com.example.bapostsapp.data.entities.UserEntity

fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        username = this.username,
        email = this.email,
        addressEntity = this.address.toAddressEntity(userId = this.id),
        phone = this.phone,
        website = this.website,
        companyEntity = this.company.toCompanyEntity(userId = this.id)
    )
}