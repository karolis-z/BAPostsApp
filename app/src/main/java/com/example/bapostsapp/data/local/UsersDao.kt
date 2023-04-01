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
     * Saves the list of [UserDto]s by converting each element to [UserEntity] and its related
     * objects [AddressEntity] and [CompanyEntity]. Takes in a [UserDto] because it holds the userId
     * value which is needed for [AddressEntity] and [CompanyEntity].
     */
    @Transaction
    suspend fun saveNewUsers(users: List<UserDto>) {
        users.forEach { user ->
            insertUserEntity(user = user.toUserEntity())
            insertAddressEntity(address = user.address.toAddressEntity(userId = user.id))
            insertCompanyEntity(company = user.company.toCompanyEntity(userId = user.id))
        }
    }

    @Query("SELECT * FROM 'users' ORDER BY id")
    fun getUsersFlow(): Flow<List<UserEntityFull>>

}