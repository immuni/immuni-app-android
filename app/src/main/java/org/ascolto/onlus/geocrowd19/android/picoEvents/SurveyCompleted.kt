package org.ascolto.onlus.geocrowd19.android.picoEvents

import com.bendingspoons.pico.model.UserAction
import okhttp3.internal.EMPTY_BYTE_ARRAY
import org.ascolto.onlus.geocrowd19.android.models.survey.*


/**
 * surveyVersion: The version of the survey that has been completed
 * profileId: The id of the profile that has completed the survey
 * answers: A dictionary where the key is the question_id and the value is an array of answers. Note that in case of a single index (radio) an array should be sent anyway; This index can either be an index (integer) or an array of indices.
 * triageProfile: The triage profile calculated at the end of the survey. In case of error, and therefore empty triage profile, we can use _empty_
 * previousUserHealthState: The health state of the user before the survey
 * userHealthState: The health state of the user after the survey
 */
class SurveyCompleted(
    userId: String,
    surveyVersion: String,
    answers: SurveyAnswers,
    triageProfile: TriageProfileId?,
    previousUserHealthState: UserHealthState,
    userHealthState: UserHealthState
) {
    val userAction = UserAction(
        "survey_completed",
        mapOf(
            "survey_version" to surveyVersion,
            "profile_id" to userId,
            "answers" to answers.mapValues {
                it.value.map { answer ->
                    when (answer) {
                        is SimpleAnswer -> answer.index
                        is CompositeAnswer -> answer.componentIndexes
                    }
                }
            },
            "calculated_triage_profile" to (triageProfile ?: EMPTY_TRIAGE_PROFILE),
            "initial_user_states" to previousUserHealthState,
            "final_user_states" to userHealthState
        )
    )

    companion object {
        const val EMPTY_TRIAGE_PROFILE = "__empty__"
    }
}
