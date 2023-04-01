package com.example.bapostsapp.data.local

import com.example.bapostsapp.core.di.IoDispatcher
import com.example.bapostsapp.data.entities.UserDto
import com.example.bapostsapp.data.entities.UserEntityFull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UsersLocalDataSource {

    /**
     * Saves a list of users in the local database. This will overwrite all previously stored users
     * with the same [UserDto.id].
     */
    suspend fun saveNewUsers(users: List<UserDto>)

    /**
     * Deletes all users and its related objects from the local database.
     */
    suspend fun deleteAllUsers()

    /**
     * Returns a [Flow] containing a list of [UserEntityFull] objects from the local database.
     */
    fun getUsers(): Flow<List<UserEntityFull>>

}

class UsersLocalDataSourceImpl @Inject constructor(
    private val usersDao: UsersDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UsersLocalDataSource {

    override suspend fun saveNewUsers(users: List<UserDto>) = withContext(dispatcher) {
        usersDao.saveNewUsers(users = users)
    }

    override suspend fun deleteAllUsers() = withContext(dispatcher) {
        usersDao.deleteAllUsers()
    }

    override fun getUsers(): Flow<List<UserEntityFull>> = usersDao.getUsersFlow()
}