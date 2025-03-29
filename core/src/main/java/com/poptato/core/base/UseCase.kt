package com.poptato.core.base

import kotlinx.coroutines.flow.Flow

abstract class UseCase<REQUEST, RESPONSE> {
    abstract suspend operator fun invoke(request: REQUEST): Flow<RESPONSE>
}