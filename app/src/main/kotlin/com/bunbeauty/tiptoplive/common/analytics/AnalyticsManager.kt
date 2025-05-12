package com.bunbeauty.tiptoplive.common.analytics

import android.util.Log
import com.bunbeauty.tiptoplive.common.util.Seconds
import com.bunbeauty.tiptoplive.common.util.toTimeString
import com.bunbeauty.tiptoplive.features.notification.NotificationMessage
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

private const val STREAM_STARTED_EVENT = "stream_started"
private const val USERNAME_PARAM = "username"
private const val VIEWER_COUNT_PARAM = "viewer_count"
private const val VIEWER_COUNT_EVENT = "viewer_count_"

private const val STREAM_STOPPED_EVENT = "stream_stopped"
private const val STREAM_FINISHED_EVENT = "stream_finished"
private const val STREAM_AUTO_FINISHED_EVENT = "stream_auto_finished"
private const val STREAM_DURATION_PARAM = "stream_duration"

private const val FEEDBACK_POSITIVE_EVENT = "feedback_positive"
private const val FEEDBACK_NEGATIVE_EVENT = "feedback_negative"
private const val FEEDBACK_DO_NOT_ASK_EVENT = "feedback_do_not_ask"

private const val OPEN_QUESTIONS_EVENT = "open_questions"
private const val SELECT_QUESTION_EVENT = "select_question"

private const val SHARE_EVENT = "share"
private const val RATE_US_CLICK_EVENT = "rate_us_click"

private const val PREMIUM_LATER_CLICK_EVENT = "premium_later_click"
private const val PREMIUM_CLICK_EVENT = "premium_click"
private const val PREMIUM_QUITE_EVENT = "premium_quite"
private const val CHECKOUT_CLICK_PREFIX = "checkout_click_"

private const val USED_DAYS_PREFIX = "used_day_"
private const val NEW_LEVEL_PREFIX = "new_level_"
private const val NOTIFICATION_CLICK = "notification_click_"

private const val BILLING_CONNECTION_SUCCESS_EVENT = "billing_connection_success"
private const val BILLING_CONNECTION_FAILED_EVENT = "billing_connection_failed_"
private const val BILLING_DISCONNECTION_EVENT = "billing_disconnection"
private const val PRODUCT_NOT_FOUND_EVENT = "product_not_found_"
private const val START_BILLING_FLOW_EVENT = "start_billing_flow_"
private const val FAIL_BILLING_FLOW_EVENT = "fail_billing_flow_"
private const val PURCHASE_PRODUCT_EVENT = "purchase_product_"
private const val PURCHASE_FAILED_EVENT = "purchase_failed_"
private const val ACKNOWLEDGE_PRODUCT_EVENT = "acknowledge_product_"
private const val ACKNOWLEDGEMENT_FAILED_EVENT = "acknowledgement_failed_"

private const val ANALYTICS_TAG = "analytics"

@Singleton
class AnalyticsManager @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics
) {

    fun trackStreamStart(username: String, viewerCount: Int) {
        trackEvent("$VIEWER_COUNT_EVENT$viewerCount")
        trackEvent(
            event = STREAM_STARTED_EVENT,
            params = mapOf(
                USERNAME_PARAM to username,
                VIEWER_COUNT_PARAM to viewerCount
            )
        )
    }

    fun trackStreamStop(duration: Seconds) {
        trackEvent(
            event = STREAM_STOPPED_EVENT,
            params = mapOf(
                STREAM_DURATION_PARAM to duration.toTimeString()
            )
        )
    }

    fun trackStreamFinish(duration: Seconds) {
        trackEvent(
            event = STREAM_FINISHED_EVENT,
            params = mapOf(
                STREAM_DURATION_PARAM to duration.toTimeString()
            )
        )
    }

    fun trackStreamAutoFinish() {
        trackEvent(event = STREAM_AUTO_FINISHED_EVENT)
    }

    fun trackPositiveFeedback() {
        trackEvent(event = FEEDBACK_POSITIVE_EVENT)
    }

    fun trackNegativeFeedback() {
        trackEvent(event = FEEDBACK_NEGATIVE_EVENT)
    }

    fun trackDoNotAsk() {
        trackEvent(event = FEEDBACK_DO_NOT_ASK_EVENT)
    }

    fun trackOpenQuestions() {
        trackEvent(event = OPEN_QUESTIONS_EVENT)
    }

    fun trackSelectQuestion() {
        trackEvent(event = SELECT_QUESTION_EVENT)
    }

    fun trackShareClick() {
        trackEvent(event = SHARE_EVENT)
    }

    fun trackRateUsClick() {
        trackEvent(event = RATE_US_CLICK_EVENT)
    }

    fun trackPremiumLaterClick() {
        trackEvent(event = PREMIUM_LATER_CLICK_EVENT)
    }

    fun trackPremiumClick() {
        trackEvent(event = PREMIUM_CLICK_EVENT)
    }

    fun trackPremiumQuite() {
        trackEvent(event = PREMIUM_QUITE_EVENT)
    }

    fun trackCheckoutClick(productId: String) {
        trackEvent(event = "$CHECKOUT_CLICK_PREFIX$productId")
    }

    fun trackBillingConnectionSuccess() {
        trackEvent(event = BILLING_CONNECTION_SUCCESS_EVENT)
    }

    fun trackBillingConnectionFailed(state: String) {
        trackEvent(event = "$BILLING_CONNECTION_FAILED_EVENT$state")
    }

    fun trackBillingDisconnection() {
        trackEvent(event = BILLING_DISCONNECTION_EVENT)
    }

    fun trackProductNotFound(productId: String) {
        trackEvent(event = "$PRODUCT_NOT_FOUND_EVENT$productId")
    }

    fun trackStartBillingFlow(productId: String) {
        trackEvent(event = "$START_BILLING_FLOW_EVENT$productId")
    }

    fun trackFailBillingFlow(productId: String) {
        trackEvent(event = "$FAIL_BILLING_FLOW_EVENT$productId")
    }

    fun trackPurchaseProduct(productId: String) {
        trackEvent(event = "$PURCHASE_PRODUCT_EVENT$productId")
    }

    fun trackPurchaseFailed(productId: String, reason: String) {
        trackEvent(event = "$PURCHASE_FAILED_EVENT${productId}_$reason")
    }

    fun trackAcknowledgeProduct(productId: String) {
        trackEvent(event = "$ACKNOWLEDGE_PRODUCT_EVENT$productId")
    }

    fun trackAcknowledgementFailed(productId: String, reason: String) {
        trackEvent(event = "$ACKNOWLEDGEMENT_FAILED_EVENT${productId}_$reason")
    }

    fun trackUsedDays(usedDayCount: Int) {
        val eventPostfix = when (usedDayCount) {
            in 1..7 -> usedDayCount.toString()
            else -> "8_and_more"
        }
        trackEvent(event = "$USED_DAYS_PREFIX$eventPostfix")
    }

    fun trackNewLevel(level: Int) {
        trackEvent(event = "$NEW_LEVEL_PREFIX$level")
    }

    fun trackNotificationClick(notificationMessage: NotificationMessage) {
        trackEvent(event = "$NOTIFICATION_CLICK${notificationMessage.name}")
    }

    fun trackError(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    private fun trackEvent(event: String, params: Map<String, Any> = emptyMap()) {
        firebaseAnalytics.logEvent(event) {
            params.forEach { (key, value) ->
                when (value) {
                    is String -> param(key, value)
                    is Long -> param(key, value)
                    is Int -> param(key, value.toLong())
                    else -> {
                        // Not supported
                    }
                }
            }
        }
        Log.d(ANALYTICS_TAG, event)
    }

}