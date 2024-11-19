package com.idfm.hackathon.data.repositories.sample

import com.idfm.hackathon.data.models.SampleDto
import com.idfm.hackathon.data.repositories.RepositoryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class SampleRepositoryImpl(private val sampleService: SampleService) : SampleRepository {
//    override fun state(): StateFlow<SampleRepositoryState> {
//        TODO("Not yet implemented")
//    }
//
//    override fun get() {
//        TODO("Not yet implemented")
//    }

    override fun fetchData(): Flow<RepositoryResult<SampleDto>> = flow {

        emit(RepositoryResult.Loading())

        try {
            val response = sampleService.fetchData()
            emit(RepositoryResult.Success(response))
        } catch (e: HttpException) {
            emit(RepositoryResult.Error(e))
        } catch (e: IOException) {
            emit(RepositoryResult.Error(e))
        }
    }

}
