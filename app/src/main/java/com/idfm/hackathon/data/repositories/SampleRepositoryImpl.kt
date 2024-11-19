package com.idfm.hackathon.data.repositories

import com.idfm.hackathon.data.models.SampleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
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

    override fun fetchData(): Flow<Result<SampleDto>> = flow {
        try {
            val response = sampleService.fetchData()
            emit(Result.success(response))
        } catch (e: HttpException) {
            emit(Result.failure<SampleDto>(e))
        } catch (e: IOException) {
            emit(Result.failure<SampleDto>(e))
        }
    }

}
