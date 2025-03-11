package com.poptato.domain.model.enums

enum class UserDeleteType(val reasons: String) {
    NOT_USED_OFTEN("NOT_USED_OFTEN"),
    MISSING_FEATURES("MISSING_FEATURES"),
    TOO_COMPLEX("TOO_COMPLEX")
}