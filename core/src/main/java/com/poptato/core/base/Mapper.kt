package com.poptato.core.base

interface Mapper<RESPONSE, MODEL> {
    fun responseToModel(response: RESPONSE): MODEL
}