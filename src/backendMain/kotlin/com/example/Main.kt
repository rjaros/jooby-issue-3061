package com.example

import io.jooby.hikari.HikariModule
import io.jooby.kt.require
import io.jooby.kt.runApp
import javax.sql.DataSource

fun main(args: Array<String>) {
    runApp(args) {
        install(HikariModule("db"))
        onStarted {
            val db = require(DataSource::class, "db")
        }
    }
}
