package dev.dividebynull.teabot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TeaBotApplication

fun main(args: Array<String>) {
    runApplication<TeaBotApplication>(*args)
}
