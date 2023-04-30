package com.handwoong.everyonewaiter.controller;

import com.handwoong.everyonewaiter.config.security.SecurityUtils;
import com.handwoong.everyonewaiter.dto.MemberDto;
import com.handwoong.everyonewaiter.dto.StoreDto;
import com.handwoong.everyonewaiter.service.MemberService;
import com.handwoong.everyonewaiter.service.StoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/members/stores")
@RequiredArgsConstructor
public class StoreController {

    private final MemberService memberService;

    private final StoreService storeService;

    @GetMapping
    public String storePage(Model model) {
        String username = SecurityUtils.getUsername();

        MemberDto.ResponseDto memberDto = memberService.findMemberByUsername(username);
        List<StoreDto.ResponseDto> storeList = storeService.findStoreList(username);

        model.addAttribute("member", memberDto);
        model.addAttribute("storeList", storeList);
        return "stores/index";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        String username = SecurityUtils.getUsername();

        MemberDto.ResponseDto memberDto = memberService.findMemberByUsername(username);

        model.addAttribute("member", memberDto);
        model.addAttribute("storeDto", new StoreDto.RequestDto());
        return "stores/register";
    }

    @PostMapping("/register")
    public String register(@Validated StoreDto.RequestDto storeReqDto) {
        String username = SecurityUtils.getUsername();

        storeService.register(username, storeReqDto);
        return "redirect:/members/stores";
    }

    @GetMapping("/edit/{storeId}")
    public String editPage(@PathVariable Long storeId, Model model) {
        String username = SecurityUtils.getUsername();

        MemberDto.ResponseDto memberDto = memberService.findMemberByUsername(username);
        StoreDto.ResponseDto storeDto = storeService.findStore(username, storeId);

        model.addAttribute("member", memberDto);
        model.addAttribute("storeDto", storeDto);
        return "stores/edit";
    }

    @PutMapping("/edit/{storeId}")
    public String edit(@PathVariable Long storeId, @Validated StoreDto.RequestDto storeReqDto) {
        String username = SecurityUtils.getUsername();

        storeService.update(username, storeId, storeReqDto);

        return "redirect:/members/stores";
    }

    @GetMapping("/delete/{storeId}")
    public String delete(@PathVariable Long storeId) {
        String username = SecurityUtils.getUsername();

        storeService.delete(username, storeId);

        return "redirect:/members/stores";
    }
}
