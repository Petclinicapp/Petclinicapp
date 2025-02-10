package bootcamp.petclinic.controller;

import bootcamp.petclinic.dto.visit.VisitRequestDTO;
import bootcamp.petclinic.dto.visit.VisitResponseDTO;
import bootcamp.petclinic.service.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/add")
    public ResponseEntity<VisitRequestDTO> createVisit(@RequestBody VisitResponseDTO visitResponseDTO) {
        VisitRequestDTO visitRequestDTO = visitService.createVisit(visitResponseDTO);
        return ResponseEntity.ok(visitRequestDTO);
    }

    @GetMapping("/{visitId}")
    public ResponseEntity<VisitRequestDTO> getVisit(@PathVariable String visitId) {
        return visitService.getVisitById(visitId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VisitRequestDTO>> getVisitsByUserId(@PathVariable String userId) {
        List<VisitRequestDTO> visits = visitService.getVisitsByUserId(userId);
        return visits.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(visits);
    }

    @PatchMapping("/{visitId}")
    public ResponseEntity<String> updateVisit(@PathVariable String visitId, @Valid @RequestBody VisitRequestDTO visitRequestDTO) {
        return visitService.updateVisit(visitId, visitRequestDTO)
                .map(visit -> ResponseEntity.ok("Visit updated successfully!"))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{visitId}")
    public ResponseEntity<String> deleteVisit(@PathVariable String visitId) {
        visitService.deleteVisit(visitId);
        return ResponseEntity.ok("Visit deleted successfully!");
    }
}
