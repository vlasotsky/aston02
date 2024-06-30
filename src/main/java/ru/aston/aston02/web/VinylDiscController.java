package ru.aston.aston02.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.aston.aston02.model.dto.VinylDiscDto;
import ru.aston.aston02.service.VinylDiscServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/v1/vinyl-collection/discs")
public class VinylDiscController {
    private final VinylDiscServiceImpl service;

    private final ObjectMapper objectMapper;

    public VinylDiscController(VinylDiscServiceImpl service) {
        this.service = service;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }


    @PostMapping
    public void save(@RequestBody VinylDiscDto discDto) {
        service.saveVinylDisc(discDto);
    }

    @GetMapping("/{discId}")
    public VinylDiscDto get(@PathVariable Long discId) {
        return service.getVinylDisc(discId);
    }

    @PostMapping("/{discId}")
    public void update(@PathVariable Long discId, @RequestBody VinylDiscDto discDto) {
        service.updateVinylDisc(discId, discDto);
    }

    @PostMapping("/{discId}")
    public void delete(@PathVariable Long discId) {
        service.deleteVinylDisc(discId);
    }

    @GetMapping
    public List<VinylDiscDto> getAll() {
        return service.getAllVinylDiscs();
    }
}
