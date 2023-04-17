package com.codehunter.myfirstspringbootkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@SpringBootApplication
class MyFirstSpringBootKotlinApplication

fun main(args: Array<String>) {
    runApplication<MyFirstSpringBootKotlinApplication>(*args)
}

@RestController
class MessageController(val messageService: MessageService) {
    @GetMapping("/")
    fun index(@RequestParam("name") name: String) = "Hello, $name!"

    @GetMapping("/all")
    fun index(): List<Message> = messageService.findMessages()

    @PostMapping("/")
    fun post(@RequestBody message: Message) = messageService.save(message)

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: String) = messageService.findById(id)

}

data class Message(val id: String?, val text: String)


@Service
class MessageService(val db: JdbcTemplate) {
    fun findMessages(): List<Message> = db.query("select * from messages") { response, _ ->
        Message(response.getString("id"), response.getString("text"))
    }

    fun save(message: Message) {
        val id = message.id ?: UUID.randomUUID().toString()
        db.update(
            "insert into messages values (?, ?)", id, message.text
        )
    }

    fun findById(id: String): List<Message> = db.query(
        "select * from messages where id = ?",
        { response, _ ->
            Message(response.getString("id"), response.getString("text"))
        },
        id
    )
}