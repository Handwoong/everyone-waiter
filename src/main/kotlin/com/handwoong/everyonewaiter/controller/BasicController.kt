package com.handwoong.everyonewaiter.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class BasicController {

    @GetMapping("/")
    fun indexPage() = "index"

}
