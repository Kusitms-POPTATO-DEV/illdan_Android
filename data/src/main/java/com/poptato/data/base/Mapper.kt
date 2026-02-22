package com.poptato.data.base

interface Mapper<RESPONSE, MODEL> {
    fun responseToModel(response: RESPONSE?): MODEL
}