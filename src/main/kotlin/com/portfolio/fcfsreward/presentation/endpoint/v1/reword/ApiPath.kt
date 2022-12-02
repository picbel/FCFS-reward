package com.portfolio.fcfsreward.presentation.endpoint.v1.reword

object ApiPath {
    private const val V1 = "/v1"
    const val REWORD_ID = "{rewordId}"
    const val HISTORY_DATE = "{date}"
    const val REWORD = "$V1/reword/$REWORD_ID"
    const val REWORD_SUPPLY_DATE = "$V1/reword/$REWORD_ID/history/$HISTORY_DATE"
}
