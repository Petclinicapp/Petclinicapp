package bootcamp.petclinic.controller;

import bootcamp.petclinic.dto.visit.VisitRequestDTO;
import bootcamp.petclinic.dto.visit.VisitResponseDTO;
import bootcamp.petclinic.service.VisitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/add")
    public ResponseEntity<VisitResponseDTO> createVisit(@RequestBody VisitRequestDTO visitRequestDTO) {
        VisitResponseDTO visitResponseDTO = visitService.createVisit(visitRequestDTO);
        return ResponseEntity.ok(visitResponseDTO);
    }

    @GetMapping("/{visitId}")
    public ResponseEntity<VisitResponseDTO> getVisit(@PathVariable String visitId) {
        return visitService.getVisitById(visitId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{visitId}")
    public ResponseEntity<VisitResponseDTO> updateVisit(
            @PathVariable String visitId,
            @RequestBody VisitResponseDTO visitUpdateDTO) {

        return visitService.updateVisit(visitId, visitUpdateDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
