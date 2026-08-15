package com.iti.linguaquest.core.ai.client

enum class GatewayModel(val modelId: String) {
    DEEPSEEK_V3_2("deepseek.v3.2"),
    LLAMA_3_3_70B("us.meta.llama3-3-70b-instruct-v1:0"),
    VOXTRAL_24B("mistral.voxtral-small-24b-2507"),
    GPT_OSS_20B("openai.gpt-oss-20b-1:0"),
    GPT_OSS_120B("openai.gpt-oss-120b-1:0"),
    QWEN3_VL("qwen.qwen3-vl-235b-a22b"),
    GPT_OSS_SAFEGUARD_120B("openai.gpt-oss-safeguard-120b"),
    GPT_OSS_SAFEGUARD_20B("openai.gpt-oss-safeguard-20b")
}
