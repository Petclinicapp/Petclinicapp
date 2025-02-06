package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.VisitRequestDTO;
import bootcamp.petclinic.dto.VisitResponseDTO;
import bootcamp.petclinic.model.Visit;
import bootcamp.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VisitService {

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public VisitResponseDTO createVisit(VisitRequestDTO visitRequestDTO) {
        Visit visit = new Visit();
        visit.setVisitId(UUID.randomUUID().toString());
        visit.setPetId(visitRequestDTO.getPetId());
        visit.setVisitDateTime(visitRequestDTO.getVisitDateTime());
        visit.setReason(visitRequestDTO.getReason());

        visitRepository.save(visit);

        return new VisitResponseDTO(visit.getVisitId(), visit.getPetId(), visit.getVisitDateTime(), visit.getReason(), null, null, null, null);
    }

    public Optional<VisitResponseDTO> getVisitById(String visitId) {
        return visitRepository.findById(visitId)
                .map(visit -> new VisitResponseDTO(visit.getVisitId(), visit.getPetId(), visit.getVisitDateTime(), visit.getReason(), visit.getExaminationResults(), visit.getPerformedTests(), visit.getDiagnosis(), visit.getPrescribedTreatment()));
    }
}