package com.poptato.data.mapper

import com.poptato.data.base.Mapper

object UnitResponseMapper: Mapper<Unit, Unit> {
    override fun responseToModel(response: Unit?) {}
}