package com.example.batuhan.project.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.batuhan.project.dto.CreateBlockDto;
import com.example.batuhan.project.entity.Block;
import com.example.batuhan.project.locale.MyLocaleResolver;
import com.example.batuhan.project.service.BlockService;

@RestController
public class BlockController {
	@Autowired
	BlockService blockService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private MyLocaleResolver myLocaleResolver;
	
	@PostMapping(value = "/admin/block/createBlock" , produces = "text/plain;charset=UTF-8")
	public String createBlock(HttpServletRequest request, @RequestBody CreateBlockDto block) {
		return messageSource.getMessage(blockService.createBlock(block), null, myLocaleResolver.resolveLocale(request));
	}
	
	
	@GetMapping(value="/block/deleteBlock" , produces = "text/plain;charset=UTF-8")
	public String deleteBlock(HttpServletRequest request, @RequestParam String blockName) {
		return messageSource.getMessage(blockService.deleteBlock(blockName.toUpperCase()), null, myLocaleResolver.resolveLocale(request));
	}
	@GetMapping(value = "/block/getBlocks")
	public List<Block> getBlocks() {
		return blockService.getBlocks();
	}
	@GetMapping(value="/block/getBlock")
	public Optional<Block> getBlock(@RequestParam String blockName) {
		return blockService.getBlock(blockName.toUpperCase());
	}
}
