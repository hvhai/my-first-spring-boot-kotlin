package com.codehunter.myfirstspringbootkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*

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

@Table("MESSAGES")
data class Message(@Id val id: String?, val text: String)

interface MessageRepository : CrudRepository<Message, String>

@Service
class MessageService(val messageRepository: MessageRepository) {
    fun findMessages(): List<Message> = messageRepository.findAll().toList()

    fun save(message: Message) = messageRepository.save(message)

//    fun findById(id: String): List<Message> =
//        if (messageRepository.findById(id).isPresent) listOf(messageRepository.findById(id).get()) else emptyList()
    fun findById(id: String): List<Message> = messageRepository.findById(id).toList()

    fun <T : Any> Optional<out T>.toList(): List<T> =
        if (isPresent) listOf(get()) else emptyList()
}