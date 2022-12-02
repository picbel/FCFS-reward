package com.portfolio.fcfsreward.appconfig

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object FcfsLoggerFactory {
    inline fun <reified T> T.logger(): Logger {
        return LoggerFactory.getLogger(T::class.java)
    }
}