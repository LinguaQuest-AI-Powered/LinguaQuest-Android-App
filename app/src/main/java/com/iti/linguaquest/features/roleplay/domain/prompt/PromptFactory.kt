package com.iti.linguaquest.features.roleplay.domain.prompt

object PromptFactory {

    fun createBossEvaluationPrompt(
        transcriptText: String,
        taskObjective: String,
        nativeLanguage: String
    ): String {
        return """
            You are a roleplay evaluator. 
            The following transcript contains the dialogue of a roleplay session between the User and the AI Boss.
            
            Evaluate if the user achieved this objective: "$taskObjective"
            
            Evaluate the user's 'fluency_score' (0-100) based on their grammar, vocabulary, and conversational flow as shown in the transcript.
            Write a 'feedback_message' (in $nativeLanguage) summarizing how they handled the scenario.
            
            IMPORTANT: The user's input transcript is generated via an automated speech-to-text system. Because the user is utilizing an open microphone, background noise or moments of silence are occasionally hallucinated by the STT engine into unrelated foreign languages (e.g., Hindi, Chinese, Welsh) or random character strings. 

            You must strictly ignore any sudden, out-of-context language shifts or bizarre character artifacts in the transcript. Do NOT treat these as the user speaking the wrong language, do NOT mention them in your feedback, and absolutely do NOT let them negatively impact the user's `fluency_score`, `accuracy_score`, or overall task evaluation. Grade the user solely on the coherent portions of their intended target language.
            
            Return ONLY a valid JSON object matching this schema exactly:
            {
              "task_completed": boolean,
              "fluency_score": integer,
              "feedback_message": "string"
            }
            
            Transcript Logs:
            $transcriptText
        """.trimIndent()
    }

    fun createLiveSessionPrompt(bossName: String, roleDescription: String, objective: String, targetLanguage: String): String {
        return """
            You are $bossName. 
            Role: $roleDescription
            
            The user is attempting to: "$objective".
            Stay completely in character. Keep your responses concise and natural for spoken audio. 
            You MUST speak ONLY in $targetLanguage. Do not use any other language.
            Do not break character under any circumstances.
        """.trimIndent()
    }
}
