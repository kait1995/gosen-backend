package com.gosen.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gosen.model.DeviceSetting;
import com.gosen.model.GraphData;
import com.gosen.repository.DeviceSettingRepository;
import com.gosen.repository.GraphDataRepository;

@RestController
public class DataRestController {
	
	private final DeviceSettingRepository deviceSettingRepo;
	private final GraphDataRepository graphDataRepo;
	
	DataRestController(DeviceSettingRepository deviceSettingRepo, GraphDataRepository graphData){
		this.deviceSettingRepo = deviceSettingRepo;
		this.graphDataRepo = graphData;
	}
	
	@PostMapping(value="/v1/item", consumes="application/json;charset=UTF-8")
	public ResponseEntity<Long> addItem(@RequestBody DeviceSetting deviceSetting) {
		DeviceSetting res = deviceSettingRepo.save(deviceSetting);
		return ResponseEntity.ok(res.getSettingId());
		/*
		if(deviceSettingRepo.existsByTitle(deviceSetting.getTitle())) {
			return ResponseEntity.badRequest().body("The title is already in use.");
		} else {
			DeviceSetting res = deviceSettingRepo.save(deviceSetting);
			return ResponseEntity.ok(res.getSettingId().toString());
		}
		*/
	}
	
	@PutMapping(value="/v1/item/{id}")
	public ResponseEntity<String> updateItem(
			@PathVariable("id") Long id,
			@RequestBody DeviceSetting deviceSetting
	){
		var res = deviceSettingRepo.findById(id);
		if(res.isPresent()) {
			res.get().setDeviceNumber(deviceSetting.getDeviceNumber());
			res.get().setTitle(deviceSetting.getTitle());
			deviceSettingRepo.save(res.get());
			return ResponseEntity.ok("Response ok");
		} else {
			return ResponseEntity.badRequest().body("");
		}
	}
	
	@DeleteMapping(value="/v1/item/{id}")
	public ResponseEntity<String> removeItem(@PathVariable("id") Long id){
		var deviceSetting = deviceSettingRepo.findById(id);
		if(deviceSetting.isPresent()) {
			deviceSettingRepo.deleteById(id);
			return ResponseEntity.ok("Response ok.");
		} else {
			return ResponseEntity.badRequest().body("The title was not found.");
		}
	}
	
	@GetMapping(value="/v1/item", produces="application/json;charset=UTF-8")
	public ResponseEntity<List<DeviceSetting>> getItems(){
		List<DeviceSetting> res = deviceSettingRepo.findAll();
		return ResponseEntity.ok(res);
	}
	
	@GetMapping(value="/v1/device", produces="application/json;charset=UTF-8")
	public ResponseEntity<List<GraphData>> getGraphData(){
		List<GraphData> res = graphDataRepo.findAll();
		return ResponseEntity.ok(res);
	}
	
}
