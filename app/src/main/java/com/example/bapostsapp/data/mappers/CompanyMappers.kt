package com.example.bapostsapp.data.mappers

import com.example.bapostsapp.data.entities.CompanyDto
import com.example.bapostsapp.data.entities.CompanyEntity

fun CompanyDto.toCompanyEntity(userId: Long): CompanyEntity {
    return CompanyEntity(
        userId = userId,
        name = this.name,
        catchPhrase = this.catchPhrase,
        bs = this.bs
    )
}