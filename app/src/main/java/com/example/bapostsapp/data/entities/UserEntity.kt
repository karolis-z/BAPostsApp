package com.example.bapostsapp.data.entities

import androidx.room.*

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val addressEntity: AddressEntity,
    val phone: String,
    val website: String,
    val companyEntity: CompanyEntity
)

@Entity(
    primaryKeys = ["id", "userId"],
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class CompanyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val catchPhrase: String,
    val bs: String
)

@Entity(
    primaryKeys = ["id", "userId"],
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    /* Made a choice to simply include latitude and longitude values within the AddressEntity
    * instead of having one additional table in the database. These values will be converted back
    * to a Geo object through mappers. */
    val lat: Double?,
    val lng: Double?
)

data class UserEntityFull(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
        entity = CompanyEntity::class
    )
    val company: CompanyEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
        entity = AddressEntity::class
    )
    val address: AddressEntity,
)