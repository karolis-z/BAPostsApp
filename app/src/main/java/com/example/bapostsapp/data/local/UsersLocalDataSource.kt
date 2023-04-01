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
     * Saves a new user in the local database. This will overwrite a previously stored user with the
     * same [UserDto.id].
     */
    suspend fun saveNewUser(user: UserDto)

    /**
     * Deletes all users and its related objects from the local database.
     */
    suspend fun deleteAllUsers()

    /**
     * Returns a [Flow] containing a list of [UserEntityFull] objects from the local database.
     */
    fun getPosts(): Flow<List<UserEntityFull>>

}

class UsersLocalDataSourceImpl @Inject constructor(
    private val usersDao: UsersDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UsersLocalDataSource {

    override suspend fun saveNewUser(user: UserDto) = withContext(dispatcher) {
        usersDao.saveNewUser(userDto = user)
    }

    override suspend fun deleteAllUsers() = withContext(dispatcher) {
        usersDao.deleteAllUsers()
    }

    override fun getPosts(): Flow<List<UserEntityFull>> = usersDao.getUsersFlow()
}