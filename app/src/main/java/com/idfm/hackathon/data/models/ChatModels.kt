package com.idfm.hackathon.data.models

data class ResponseMetadata(
    val usage: Usage,
    val system_fingerprint: String
)

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)

data class Kwargs(
    val lc_serializable: Boolean,
    val lc_kwargs: LcKwargs,
    val lc_namespace: List<String>,
    val content: String,
    val additional_kwargs: Map<String, Any>,
    val response_metadata: ResponseMetadata,
    val id: String,
    val tool_calls: List<Any>,
    val invalid_tool_calls: List<Any>,
    val usage_metadata: UsageMetadata
)

data class LcKwargs(
    val lc_serializable: Boolean,
    val lc_kwargs: LcKwargsContent
)

data class LcKwargsContent(
    val content: String,
    val tool_calls: List<Any>,
    val invalid_tool_calls: List<Any>,
    val additional_kwargs: Map<String, Any>,
    val response_metadata: ResponseMetadata,
    val id: String
)

data class UsageMetadata(
    val output_tokens: Int,
    val input_tokens: Int,
    val total_tokens: Int,
    val input_token_details: Map<String, Any>,
    val output_token_details: Map<String, Any>
)

data class Message(
    val lc: Int,
    val type: String,
    val id: List<String>,
    val kwargs: Kwargs
)

data class Values(
    val messages: List<Message>,
    val answer: String
)

data class JsonResponse(
    val node: String,
    val values: Values
)