package com.poptato.data.mapper

import com.poptato.data.base.Mapper

object AuthLogOutResponseMapper: Mapper<String, Unit> {

    override fun responseToModel(response: String?) {}
}