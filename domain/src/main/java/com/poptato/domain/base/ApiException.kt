package com.poptato.domain.base

class ApiException(
    val code: String,
    message: String
): Exception(message)