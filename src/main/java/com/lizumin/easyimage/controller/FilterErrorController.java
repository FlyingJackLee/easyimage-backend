package com.lizumin.easyimage.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 1:18 am
 * 4
 */
@RestController
public class FilterErrorController {
      @RequestMapping("/api/error/throw")
      public void rethrow(HttpServletRequest request) throws Exception {
          throw ((Exception) request.getAttribute("filter.error"));
      }
}
