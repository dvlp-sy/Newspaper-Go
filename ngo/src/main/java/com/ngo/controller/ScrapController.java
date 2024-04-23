package com.ngo.controller;

import com.ngo.common.ApiResponse;
import com.ngo.dto.MemoDto;
import com.ngo.dto.MemoGetDto;
import com.ngo.dto.ScrapDto;
import com.ngo.dto.ScrapListDto;
import com.ngo.service.ScrapService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScrapController
{
    private final ScrapService scrapService;

    public ScrapController(ScrapService scrapService) { this.scrapService = scrapService; }

    /**
     * 스크랩 관리
     */

    @GetMapping("/user/{userId}/scrap")
    public ApiResponse<ScrapListDto> getAllScraps(@PathVariable("userId") Long userId)
    {
        return scrapService.getAllScraps(userId);
    }

    @PostMapping("/user/{userId}/scrap")
    public ApiResponse<ScrapDto> postScrap(@PathVariable("userId") Long userId, @RequestBody ScrapDto scrapDto)
    {
        return scrapService.postScrap(userId, scrapDto);
    }

    @DeleteMapping("/user/{userId}/scrap/{scrapId}")
    public ApiResponse<ScrapDto> deleteScrap(@PathVariable("userId") Long userId, @PathVariable("scrapId") Long scrapId)
    {
        return scrapService.deleteScrap(userId, scrapId);
    }

    /**
     * 메모 관리
     */

    @PostMapping("/user/{userId}/scrap/{scrapId}/memo")
    public ApiResponse<MemoGetDto> postMemo(@PathVariable("userId") Long userId, @PathVariable("scrapId") Long scrapId, @RequestBody MemoDto memoDto)
    {
        return scrapService.postMemo(userId, scrapId, memoDto);
    }
}
