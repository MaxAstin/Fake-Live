package com.bunbeauty.tiptoplive.shared.data

object OpenAiConstants {

    // Defaults
    const val DEFAULT_MODEL = "gpt-4o-mini"
    const val DEFAULT_TEMPERATURE = 1
    const val DEFAULT_MAX_TOKENS = 2048
    const val DEFAULT_TOP_P = 1
    const val DEFAULT_FREQUENCY_PENALTY = 0
    const val DEFAULT_PRESENCE_PENALTY = 0

    // Types
    const val TEXT_TYPE = "text"
    const val IMAGE_URL_TYPE = "image_url"
    const val JSON_TYPE = "json_schema"
    const val OBJECT_TYPE = "object"
    const val ARRAY_TYPE = "array"
    const val STRING_TYPE = "string"

    // Roles
    const val ROLE_SYSTEM = "system"
    const val ROLE_USER = "user"

}