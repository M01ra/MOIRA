package MakeUs.Moira.domain.projectApply;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectApplyRepo extends JpaRepository<ProjectApply, Long> {
    List<ProjectApply> findAllByApplicant_Id(Long applicantId);
    int countByApplicant_Id(Long applicantId);
}
