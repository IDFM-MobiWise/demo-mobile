package com.idfm.hackathon.data.repositories.sample

import com.idfm.hackathon.data.models.SampleDto
import com.idfm.hackathon.data.repositories.RepositoryResult
import kotlinx.coroutines.flow.Flow




fun interface SampleRepository {
    fun fetchData(): Flow<RepositoryResult<SampleDto>>
}