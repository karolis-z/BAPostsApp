package com.example.bapostsapp.data.local

import androidx.room.*
import com.example.bapostsapp.data.entities.*
import com.example.bapostsapp.data.mappers.toAddressEntity
import com.example.bapostsapp.data.mappers.toCompanyEntity
import com.example.bapostsapp.data.mappers.toUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserEntity(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddressEntity(address: AddressEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyEntity(company: CompanyEntity): Long

    @Query("DELETE FROM 'users'")
    suspend fun deleteAllUsers()

    /**
     * Adds a new [UserEntity] along with embedded objects [AddressEntity] and [CompanyEntity].
     * Takes in a [UserDto] because it holds the userId value which is needed for [AddressEntity]
     * and [CompanyEntity].
     */
    @Transaction
    suspend fun saveNewUser(userDto: UserDto) {
        insertUserEntity(user = userDto.toUserEntity())
        insertAddressEntity(address = userDto.address.toAddressEntity(userId = userDto.id))
        insertCompanyEntity(company = userDto.company.toCompanyEntity(userId = userDto.id))
    }

    @Query("SELECT * FROM 'users' ORDER BY id")
    fun getUsersFlow(): Flow<List<UserEntityFull>>

}